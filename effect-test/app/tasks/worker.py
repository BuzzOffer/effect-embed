from typing import Any

import numpy as np
import cv2
import io
from celery import shared_task
from PIL import Image

from app.engine.attacks import apply_jpeg_compression, apply_gaussian_noise
from app.engine.robustness import calculate_ber
from app.engine.decoder import get_decoder
from app.engine.decrypt import decrypt

@shared_task(bind=True, name="run_testing_pipeline")
def run_testing_pipeline(self, stego_bytes: bytes, expected_payload: str, effect_type: str, selected_attacks: dict):
    # np_arr = np.frombuffer(stego_bytes, np.uint8)
    # stego_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR_RGB)
    decoder = get_decoder(effect_type)

    expected_bytes = expected_payload.encode('utf-8')

    original_png = selected_attacks.get("original_png", True)
    jpeg_qualities = selected_attacks.get("jpeg_qualities", [])
    gaussian_noise_enabled = selected_attacks.get("gaussian_noise", False)

    #Robustness testing
    robustness_results = []
    if original_png:
        img_png = Image.open(io.BytesIO(stego_bytes))
        if img_png.mode in ("RGBA", "P"):
            img_png = img_png.convert("RGB")
        extracted_bytes_png = decoder.decode(np.array(img_png))
        decrypted_bytes_png = decrypt(extracted_bytes_png)
    
        ber = calculate_ber(expected_bytes, decrypted_bytes_png)
    
        robustness_results.append({
            "attack": f"PNG_ORIGINAL",
            "details": f"The original image without distortions",
            "ber": ber,
            "expected_payload": expected_payload,
            "decoded_payload": decrypted_bytes_png.decode('utf-8'),
            "success": expected_bytes == decrypted_bytes_png
        })

    for quality in jpeg_qualities:
        compressed_bytes = apply_jpeg_compression(stego_bytes, quality)

        decompressed_img = Image.open(io.BytesIO(compressed_bytes))
        img_np = np.array(decompressed_img)
        extracted_bytes = decoder.decode(img_np)
        decrypted_bytes = decrypt(extracted_bytes)

        ber = calculate_ber(expected_bytes, decrypted_bytes)

        robustness_results.append({
            "attack": f"JPEG_{quality}",
            "details": f"JPEG compression applied with a quality of {quality}.",
            "ber": ber,
            "decoded_payload": decrypted_bytes.decode('utf-8', errors="replace"),
            "success": expected_bytes == decrypted_bytes
        })

    if gaussian_noise_enabled:
        gaussian_mean = selected_attacks.get("gaussian_mean", 0)
        gaussian_stddev = selected_attacks.get("gaussian_stddev", 25)

        gaussian_noisy_bytes = apply_gaussian_noise(stego_bytes, gaussian_mean, gaussian_stddev)
        gaussian_img = Image.open(io.BytesIO(gaussian_noisy_bytes))
        gaussian_np_arr = np.array(gaussian_img)
        extracted_gaussian = decoder.decode(gaussian_np_arr)
        decrypted_gaussian = decrypt(extracted_gaussian)
    
        ber_gaussian = calculate_ber(expected_bytes, decrypted_gaussian)
    
        robustness_results.append({
            "attack": f"GAUSSIAN",
            "details": f"Gaussian noise applied with a mean of {gaussian_mean} and a standard deviation of {gaussian_stddev}",
            "ber": ber_gaussian,
            "decoded_payload": decrypted_gaussian.decode('utf-8', errors="replace"),
            "success": expected_bytes == decrypted_gaussian
        })

    return {
        "expected_payload": expected_payload,
        "robustness_score": sum([1 for r in robustness_results if r["success"]]),
        "robustness": robustness_results
    }

        