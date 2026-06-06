# AI-Powered Procurement Contract Analyzer

Full-stack application for analyzing procurement contracts with NLP, OCR, and text summarization.

## Architecture

```
React Frontend (5173) → Spring Boot API (8082) → Python ML Service (8000)
                              ↓
                      backend/data/contracts.json
```

## Project Structure

```
├── frontend/          React + Vite UI
├── backend/           Spring Boot REST API + JSON storage
├── ml-service/        FastAPI NLP pipeline (OCR, NER, summarization)
├── start-*.bat        Windows startup scripts
└── procurement_contracts.csv
```

## Prerequisites

- **Node.js** 18+
- **Java** 8+ and **Maven**
- **Python** 3.9+
- **Tesseract OCR** (for scanned PDFs)
- **Poppler** (required by pdf2image on Windows)

## Setup

### 1. ML Service

```bash
cd ml-service
pip install -r requirements.txt
python -m spacy download en_core_web_sm
```

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Runs on **http://localhost:8082**

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**

## Quick Start (Windows)

Start in this order:

1. `start-ml-service.bat`
2. `start-backend.bat`
3. `start-frontend.bat`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/contracts` | List all analyzed contracts |
| GET | `/api/contracts/{id}` | Get one contract |
| POST | `/api/contracts/analyze` | Upload PDF/DOCX and analyze |
| DELETE | `/api/contracts/{id}` | Delete a contract |

## Extracted Fields

- Supplier
- Product
- Quantity
- Price
- Delivery Date
- Payment Terms
- Summary

## Notes

- First analysis may take longer while T5-small and spaCy models load.
- Contract extraction works best with documents following the procurement template in `procurement_contracts.csv`.
- Scanned PDFs require Tesseract and Poppler installed and on PATH.
