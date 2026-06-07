# Commodity Trade Risk Signal Extractor

Full-stack NLP application that analyzes commodity news and extracts trade risk signals — country, commodity, risk type, severity, and sentiment.

## Architecture

```
React (5173) → Spring Boot (8083) → Python FastAPI (8000) → NLP Models
                                      ↓
                              data/analyses.json (last 3 records)
```

## Prerequisites

- Java 8+
- Node.js 18+
- Python 3.10+

## Setup

### 1. ML Service (Python)

```bash
cd ml-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
python -m spacy download en_core_web_sm
```

First run downloads BART and DistilBERT models (~1.5 GB). This can take several minutes.

### 2. Backend (Spring Boot)

No extra setup — Maven wrapper is included.

### 3. Frontend (React)

```bash
cd frontend
npm install
```

## Run (3 terminals)

```bash
start-ml-service.bat    # port 8000
start-backend.bat       # port 8083 (8080 is used by Oracle on this machine)
start-frontend.bat      # port 5173
```

Open http://localhost:5173

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/analyze` | Analyze news text |
| GET | `/api/analyses` | Get analysis history (max 3) |
| GET | `/api/analyses/{id}` | Get single analysis |
| DELETE | `/api/analyses/{id}` | Delete from history |
| POST | `http://localhost:8000/analyze` | Direct ML service call |

## NLP Pipeline

1. Text preprocessing (HTML/URL removal, 512 char limit)
2. spaCy NER — countries, commodities, organizations, dates
3. BART zero-shot — risk type classification
4. DistilBERT — sentiment analysis
5. Merge into structured JSON report

## Sample Test Cases

Use the quick demo buttons in the UI, or paste:

- Floods destroy rice crops in Pakistan → weather disaster, HIGH
- EU sanctions on Russian fertilizer → political sanction, HIGH
- Miner strike halts copper in Chile → logistics disruption, MEDIUM
- India best wheat harvest → no risk, LOW
- Crude oil prices jump after OPEC cut → price surge, MEDIUM
