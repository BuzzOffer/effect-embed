from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    PROJECT_NAME: str = "Testing Module"
    API_V1_STR: str = "/api/v1"

    REDIS_URL: str = "redis://localhost:6379/0"

    MIN_SECURITY_ENTROPY: float = 7.5
    MAX_ALLOWABLE_BER: float = 0.05

    DEFAULT_KEY: str = "c2c2050eea72eb2b04c568d8685ec9cecda27d5c23d0f14373aa7084c4055d5b"

    class Config:
        env_file = ".env"

settings = Settings()