package com.commodity.risk.model;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    private List<EntitySpan> countries = new ArrayList<EntitySpan>();
    private List<EntitySpan> commodities = new ArrayList<EntitySpan>();
    private List<EntitySpan> organizations = new ArrayList<EntitySpan>();
    private List<EntitySpan> dates = new ArrayList<EntitySpan>();

    public Entities() {
    }

    public List<EntitySpan> getCountries() {
        return countries;
    }

    public void setCountries(List<EntitySpan> countries) {
        this.countries = countries;
    }

    public List<EntitySpan> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<EntitySpan> commodities) {
        this.commodities = commodities;
    }

    public List<EntitySpan> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<EntitySpan> organizations) {
        this.organizations = organizations;
    }

    public List<EntitySpan> getDates() {
        return dates;
    }

    public void setDates(List<EntitySpan> dates) {
        this.dates = dates;
    }
}
