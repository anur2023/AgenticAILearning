from fastapi import FastAPI, HTTPException

from pipeline import analyze_news
from schemas import AnalyzeRequest, RiskAnalysisResult

app = FastAPI(title="Commodity Trade Risk Signal ML Service")


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/analyze", response_model=RiskAnalysisResult)
def analyze(body: AnalyzeRequest):
    result = analyze_news(body.text)
    if result.get("error"):
        raise HTTPException(status_code=400, detail=result["error"])
    return result
