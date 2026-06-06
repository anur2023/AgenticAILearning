@echo off
cd /d "%~dp0ml-service"
python -m uvicorn app:app --reload --port 8000
