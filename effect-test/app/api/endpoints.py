from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from celery.result import AsyncResult

from app.core.celery_app import celery_app
from app.tasks.worker import run_testing_pipeline

router = APIRouter()

@router.post("/test")
async def start_test(
    stego_image: UploadFile = File(...),
    expected_payload: str = Form(...),
    effect_type: str = Form(...)
):
    if stego_image.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(status_code=400, detail="Invalid stego image format.")
    
    stego_bytes = await stego_image.read()

    task = run_testing_pipeline.delay(
        stego_bytes,
        expected_payload,
        effect_type
    )

    return {"task_id": task.id, "status": "Task enqueued"}

@router.get("/test/status/{task_id}")
async def get_task_status(task_id: str):
    task = AsyncResult(task_id, app=celery_app)

    match task.state:
        case 'PENDING':
            return {"state": task.state, "status": "Waiting in queue..."}
        case 'FAILURE':
            return {"state": task.state, "error": str(task.info)}
        case 'SUCCESS':
            return {"state": task.state, "result": task.result}
        case _:
            return {"state": task.state}

    