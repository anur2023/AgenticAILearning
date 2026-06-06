from typing import Optional

from pydantic import BaseModel


class ContractAnalysisResult(BaseModel):
    supplier: Optional[str] = None
    product: Optional[str] = None
    quantity: Optional[str] = None
    price: Optional[str] = None
    delivery_date: Optional[str] = None
    payment_terms: Optional[str] = None
    summary: Optional[str] = None


class TextAnalysisRequest(BaseModel):
    text: str
