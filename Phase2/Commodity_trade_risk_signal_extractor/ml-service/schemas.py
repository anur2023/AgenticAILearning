from typing import Dict, List, Optional

from pydantic import BaseModel, Field


class EntitySpan(BaseModel):
    name: str
    start: int
    end: int


class Entities(BaseModel):
    countries: List[EntitySpan] = Field(default_factory=list)
    commodities: List[EntitySpan] = Field(default_factory=list)
    organizations: List[EntitySpan] = Field(default_factory=list)
    dates: List[EntitySpan] = Field(default_factory=list)


class AnalyzeRequest(BaseModel):
    text: str = Field(..., min_length=1)


class RiskAnalysisResult(BaseModel):
    cleaned_text: Optional[str] = None
    entities: Optional[Entities] = None
    risk_type: Optional[str] = None
    risk_score: Optional[float] = None
    severity: Optional[str] = None
    all_scores: Optional[Dict[str, float]] = None
    sentiment: Optional[str] = None
    sentiment_score: Optional[float] = None
    summary: Optional[str] = None
    error: Optional[str] = None
