import csv
import json
from collections import defaultdict
from datetime import datetime, timezone
from pathlib import Path

PROJECT_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = PROJECT_DIR / "email_keywords_extracted.csv"
OUTPUT_FILE = PROJECT_DIR / "backend" / "src" / "main" / "resources" / "seed-emails.json"


def main():
    grouped = defaultdict(list)

    with DATA_FILE.open(encoding="utf-8", newline="") as handle:
        reader = csv.DictReader(handle)
        for row in reader:
            category = row["category"]
            if len(grouped[category]) < 10:
                grouped[category].append(
                    {
                        "emailText": row["email_text"],
                        "category": category,
                        "confidence": 1.0,
                        "source": "seed",
                        "createdAt": datetime.now(timezone.utc).isoformat(),
                    }
                )

    emails = []
    email_id = 1
    for category in sorted(grouped.keys()):
        for item in grouped[category]:
            item["id"] = email_id
            emails.append(item)
            email_id += 1

    OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT_FILE.write_text(json.dumps(emails, indent=2), encoding="utf-8")
    print(f"Wrote {len(emails)} seed emails to {OUTPUT_FILE}")


if __name__ == "__main__":
    main()
