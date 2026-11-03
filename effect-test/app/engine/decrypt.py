from cryptography.hazmat.primitives.ciphers.aead import AESGCM
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives import hashes
import os
import dotenv

HEADER_NO_PASSKEY = 0
HEADER_WITH_PASSKEY = 1
IV_LENGTH = 12
SALT_LENGTH = 16
TAG_LENGTH = 128
KEY_LENGTH = 256
ITERATIONS = 10000

def decrypt(encrypted_payload: bytearray) -> bytes:
    if len(encrypted_payload) < 1 + IV_LENGTH:
        return bytes(encrypted_payload)

    header = encrypted_payload[0]
    iv2 = encrypted_payload[1 : 1+IV_LENGTH]
    app_ciphertext = encrypted_payload[1+IV_LENGTH : ]

    #Get the app key
    key = os.environ["DEFAULT_KEY"]
    app_secret_key = bytes.fromhex(key)

    #Decrypt app layer
    aesgcm_app = AESGCM(app_secret_key)
    inner_data = aesgcm_app.decrypt(nonce=iv2, data=app_ciphertext, associated_data=None)

    if header == HEADER_NO_PASSKEY:
        return inner_data
    else:
        raise ValueError(f"Unknown header byte: {header}")