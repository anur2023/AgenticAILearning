import re
from typing import Any

import spacy

_nlp = None


def get_nlp():
    global _nlp
    if _nlp is None:
        _nlp = spacy.load("en_core_web_sm")
    return _nlp


def extract_contract_fields(text: str) -> dict[str, Any]:
    supplier = re.search(r"Supplier\s*:?\s*(.*?)\s*Product", text, re.I)
    product = re.search(r"Product\s*:?\s*(.*?)\s*Quantity", text, re.I)
    quantity = re.search(r"Quantity\s*:?\s*(.*?)\s*Price", text, re.I)
    price = re.search(r"Price\s*:?\s*₹?([0-9,]+)", text, re.I)
    delivery_date = re.search(r"Delivery Date\s*:?\s*(.*?)\s*Payment", text, re.I)
    payment_terms = re.search(r"Payment Terms\s*:?\s*(.*)", text, re.I)

    return {
        "supplier": supplier.group(1).strip() if supplier else None,
        "product": product.group(1).strip() if product else None,
        "quantity": quantity.group(1).strip() if quantity else None,
        "price": price.group(1).strip().replace(",", "") if price else None,
        "delivery_date": delivery_date.group(1).strip() if delivery_date else None,
        "payment_terms": payment_terms.group(1).strip() if payment_terms else None,
    }


def extract_entities(text: str) -> dict[str, Any]:
    doc = get_nlp()(text)
    result = {
        "supplier": None,
        "quantity": None,
        "price": None,
        "delivery_date": None,
    }

    for ent in doc.ents:
        if ent.label_ == "ORG" and result["supplier"] is None:
            result["supplier"] = ent.text
        elif ent.label_ == "QUANTITY" and result["quantity"] is None:
            result["quantity"] = ent.text
        elif ent.label_ == "MONEY" and result["price"] is None:
            result["price"] = ent.text.replace(",", "")
        elif ent.label_ == "DATE" and result["delivery_date"] is None:
            result["delivery_date"] = ent.text

    return result


def merge_extractions(regex_data: dict, ner_data: dict) -> dict:
    return {
        "supplier": regex_data["supplier"] or ner_data["supplier"],
        "product": regex_data["product"],
        "quantity": regex_data["quantity"] or ner_data["quantity"],
        "price": regex_data["price"] or ner_data["price"],
        "delivery_date": regex_data["delivery_date"] or ner_data["delivery_date"],
        "payment_terms": regex_data["payment_terms"],
    }
