import numpy as np
import cv2
from celery import shared_task

from app.engine.attacks import apply_jpeg_compression, apply_gaussian_noise
from app.engine.robustness import calculate_ber

@shared_task(bind=True, name="run_testing_pipeline")
def run_testing_pipeline(self, stego_bytes: bytes, expected_payload: str):
    np_arr = np.frombuffer(stego_bytes, np.uint8)
    stego_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR_RGB)

    expected_bytes = expected_payload.encode('utf-8')

    #Robustness testing
    robustness_results = []

    for quality in [90, 75, 50, 25]:
        compressed_bytes = apply_jpeg_compression(stego_bytes, quality)

        