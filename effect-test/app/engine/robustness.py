import numpy as np

def calculate_ber(expected_bits: np.ndarray, extracted_bits: np.ndarray) -> float:
    if len(expected_bits) != len(extracted_bits):
        return 1.0
    mismatches = np.count_nonzero(expected_bits != extracted_bits) #counts number of bits that do not match
    return float(mismatches / len(expected_bits))