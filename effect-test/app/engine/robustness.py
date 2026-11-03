import numpy as np

def calculate_ber(expected_bytes: bytes, extracted_bytes: bytes) -> float:
    if len(expected_bytes) != len(extracted_bytes):
        return 1.0
    
    expected_arr = np.frombuffer(expected_bytes, dtype=np.uint8)
    extracted_arr = np.frombuffer(extracted_bytes, dtype=np.uint8)

    expected_bits = np.unpackbits(expected_arr)
    extracted_bits = np.unpackbits(extracted_arr)

    mismatches = np.count_nonzero(expected_bits != extracted_bits) #counts number of bits that do not match
    
    return float(mismatches / len(expected_bits))