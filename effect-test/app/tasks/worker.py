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
def run_testing_pipeline(self, stego_bytes: bytes, expected_payload: str, effect_type: str):
    # np_arr = np.frombuffer(stego_bytes, np.uint8)
    # stego_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR_RGB)
    decoder = get_decoder(effect_type)

    expected_bytes = expected_payload.encode('utf-8')

    #Robustness testing
    robustness_results = []
    # img_png = Image.open(io.BytesIO(stego_bytes))
    # if img_png.mode in ("RGBA", "P"):
    #     img_png = img_png.convert("RGB")
    # extracted_bytes_png = decoder.decode(np.array(img_png))
    # decrypted_bytes_png = decrypt(extracted_bytes_png)

    # ber = calculate_ber(expected_bytes, decrypted_bytes_png)

    # robustness_results.append({
    #     "attack": f"PNG_CONTROL",
    #     "ber": ber,
    #     "expected_payload": expected_payload,
    #     "decoded_payload": decrypted_bytes_png.decode('utf-8'),
    #     "success": expected_bytes == decrypted_bytes_png
    # })

    for quality in [90, 75, 50, 25]:
        compressed_bytes = apply_jpeg_compression(stego_bytes, quality)

        decompressed_img = Image.open(io.BytesIO(compressed_bytes))
        img_np = np.array(decompressed_img)
        extracted_bytes = decoder.decode(img_np)
        decrypted_bytes = decrypt(extracted_bytes)

        ber = calculate_ber(expected_bytes, decrypted_bytes)

        robustness_results.append({
            "attack": f"JPEG_{quality}",
            "ber": ber,
            "decoded_payload": decrypted_bytes.decode('utf-8'),
            "success": expected_bytes == decrypted_bytes
        })

    return {
        "expected_payload": expected_payload,
        "robustness_score": sum([1 for r in robustness_results if r["success"]]),
        "robustness": robustness_results
    }

        