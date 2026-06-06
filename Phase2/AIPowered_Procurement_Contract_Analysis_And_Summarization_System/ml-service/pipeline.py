import os
import tempfile
from typing import BinaryIO

from cleaner import clean_text
from document_reader import extract_text
from extractor import extract_contract_fields, extract_entities, merge_extractions
from schemas import ContractAnalysisResult
from summarizer import generate_summary


def analyze_text(raw_text: str) -> ContractAnalysisResult:
    cleaned = clean_text(raw_text)
    regex_data = extract_contract_fields(cleaned)
    ner_data = extract_entities(cleaned)
    fields = merge_extractions(regex_data, ner_data)
    summary = generate_summary(cleaned)
    return ContractAnalysisResult(**fields, summary=summary)


def analyze_contract_file(file_path: str) -> ContractAnalysisResult:
    raw_text = extract_text(file_path)
    return analyze_text(raw_text)


def analyze_upload(filename: str, file_obj: BinaryIO) -> ContractAnalysisResult:
    suffix = os.path.splitext(filename)[1]
    with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as tmp:
        tmp.write(file_obj.read())
        tmp_path = tmp.name
    try:
        return analyze_contract_file(tmp_path)
    finally:
        os.unlink(tmp_path)
