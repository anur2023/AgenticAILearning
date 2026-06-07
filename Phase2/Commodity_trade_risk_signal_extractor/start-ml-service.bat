@echo off
cd /d "%~dp0ml-service"
.\venv\Scripts\python -m uvicorn app:app --host 0.0.0.0 --port 8000
