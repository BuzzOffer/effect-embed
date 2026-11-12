from fastapi import APIRouter, UploadFile, File, Form

router = APIRouter()

@router.post("/test")
async def start_test(
    stego_image: UploadFile = File(...),
    expected_payload: str = Form(...),
):
    stego_bytes = await stego_image.read()

    