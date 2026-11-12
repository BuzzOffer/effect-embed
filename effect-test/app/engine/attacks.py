import io
import numpy as np
import cv2
from PIL import Image

def apply_jpeg_compression(image_bytes: bytes, quality: int) -> bytes:
    img = Image.open(io.BytesIO(image_bytes))
    output_buffer = io.BytesIO()

    img.save(output_buffer, format="JPEG", quality=quality)
    return output_buffer.getvalue()

def apply_gaussian_noise(image_bytes: bytes, mean=0, stddev=25) -> bytes:
    np_arr = np.frombuffer(image_bytes, np.uint8)

    img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
    if img is None:
        raise ValueError("Could not decode image bytes")

    noise = np.zeros(img.shape, np.int16)
    cv2.randn(noise, mean, stddev)
    noisy_img = cv2.add(img, noise, dtype=cv2.CV_8U)

    success, encoded_img = cv2.imencode('.jpg', noisy_img)
    return encoded_img.tobytes()