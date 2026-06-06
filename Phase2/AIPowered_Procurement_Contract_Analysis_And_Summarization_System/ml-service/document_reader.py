import fitz
import pytesseract
from docx import Document
from pdf2image import convert_from_path


def extract_text(file_path: str) -> str:
    """Extract text from PDF or DOCX. OCR fallback for scanned PDFs."""
    lower_path = file_path.lower()

    if lower_path.endswith(".docx"):
        doc = Document(file_path)
        return "\n".join(p.text for p in doc.paragraphs if p.text.strip())

    if lower_path.endswith(".pdf"):
        text = ""
        with fitz.open(file_path) as pdf:
            for page in pdf:
                text += page.get_text()

        if len(text.strip()) > 50:
            return text

        ocr_text = ""
        for img in convert_from_path(file_path):
            ocr_text += pytesseract.image_to_string(img)
        return ocr_text

    raise ValueError("Only PDF and DOCX files are supported")
