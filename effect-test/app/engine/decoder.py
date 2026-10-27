import cv2
import numpy as np
from abc import ABC, abstractmethod

class BaseDecoder(ABC):
    @abstractmethod
    def decode(self, img_np: np.ndarray) -> bytearray:
        pass

class GlitchDecoder(BaseDecoder):
    def decode(self, img_np: np.ndarray) -> bytearray:
        threshold = 100
        height, width, channels = img_np.shape
        total_pixels = width * height

        modified_cols = []
        modified_px = []

        extracted_bytes = bytearray()
        current_byte = 0
        bit_offset = 0

        for y in range(height):
            for x in range(width):
                current_pixel = img_np[y, x]
                red, green, blue = current_pixel
                pixel_index = x + (y * width)

                if modified_px[pixel_index]:
                    continue

                avg_px_value = (red + green + blue) / 3
                if avg_px_value >= threshold:
                    modified_px[pixel_index] = True
                    x = pixel_index % width
                    if not modified_cols[x]:
                        count = 0
                        vertical_index = y + 1
                        while y < height:
                            next_px = img_np[vertical_index, x]
                            if (next_px == current_pixel).all():
                                count += 1
                                modified_px[x + (vertical_index * width)] = True
                                vertical_index += 1
                            else:
                                break
                        bit_extracted = -1
                        if count == 101:
                            bit_extracted = 0
                        elif count == 130:
                            bit_extracted = 1
                        if bit_extracted != -1:
                            current_byte = current_byte | (bit_extracted << bit_offset)
                            bit_offset += 1

                            if bit_offset == 8:
                                extracted_bytes.insert(0, current_byte)
                                bit_offset = 0
                                current_byte = 0
                        modified_cols[x] = True
        return extracted_bytes
