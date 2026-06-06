from fastapi import FastAPI, File, HTTPException, UploadFile

from pipeline import analyze_text, analyze_upload
from schemas import ContractAnalysisResult, TextAnalysisRequest

app = FastAPI(title="Procurement Contract ML Service")


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/analyze/file", response_model=ContractAnalysisResult)
async def analyze_file(file: UploadFile = File(...)):
    if not file.filename or not file.filename.lower().endswith((".pdf", ".docx")):
        raise HTTPException(status_code=400, detail="Only PDF and DOCX supported")
    return analyze_upload(file.filename, file.file)


@app.post("/analyze/text", response_model=ContractAnalysisResult)
def analyze_raw_text(body: TextAnalysisRequest):
    return analyze_text(body.text)
