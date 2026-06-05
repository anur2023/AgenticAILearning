from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

from predictor import classifier

app = FastAPI(title="Smart Mail Classification API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class EmailRequest(BaseModel):
    email_text: str = Field(..., min_length=1)


class PredictionResponse(BaseModel):
    category: str
    confidence: float
    processed_text: str


@app.on_event("startup")
def startup_event():
    classifier.load()


@app.get("/health")
def health():
    return {"status": "ok"}


@app.get("/categories")
def categories():
    if not classifier.categories:
        classifier.load()
    return {"categories": classifier.categories}


@app.post("/predict", response_model=PredictionResponse)
def predict(request: EmailRequest):
    result = classifier.predict(request.email_text)
    return result
