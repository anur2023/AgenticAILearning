package com.procurement.contract.model;

public class ContractRecord {

    private Long id;
    private String fileName;
    private String supplier;
    private String product;
    private String quantity;
    private String price;
    private String deliveryDate;
    private String paymentTerms;
    private String summary;
    private String createdAt;

    public ContractRecord() {
    }

    public ContractRecord(
            Long id,
            String fileName,
            String supplier,
            String product,
            String quantity,
            String price,
            String deliveryDate,
            String paymentTerms,
            String summary,
            String createdAt
    ) {
        this.id = id;
        this.fileName = fileName;
        this.supplier = supplier;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.paymentTerms = paymentTerms;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
