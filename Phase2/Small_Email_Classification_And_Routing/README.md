# Smart Email Classification and Routing

Full-stack application that classifies customer emails with an NLP model and routes them into category columns on a React dashboard.

## Architecture

```text
React Dashboard (5173)
        |
Spring Boot API (8081)
        |
Python ML Service (8000)
```

- **React**: email composer + category board
- **Spring Boot**: REST API on port 8081 + local JSON storage (`backend/data/emails.json`)
- **Python FastAPI**: TF-IDF + Multinomial Naive Bayes inference

## Categories

- Technical
- Billing
- Account
- General Inquiry
- Complaint
- Refund
- Feedback

Only emails you classify through the dashboard are stored locally.

## Prerequisites

- Python 3.10+
- Java 8+
- Maven
- Node.js 18+

## Setup

### 1. Train ML model

```bash
cd ml-service
pip install -r requirements.txt
python train_model.py
```

### 2. Start ML service

```bash
cd ml-service
uvicorn app:app --reload --port 8000
```

### 3. Start Spring Boot backend

```bash
cd backend
mvn spring-boot:run
```

### 4. Start React frontend

```bash
cd frontend
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173)

## API Endpoints

- `GET /api/categories`
- `GET /api/emails/grouped`
- `POST /api/emails/classify`
- `DELETE /api/emails/{id}`

## Example

Input:

```text
My payment failed and money was deducted from my account.
```

Predicted category:

```text
Billing
```

The email is stored locally and shown in the Billing column.
