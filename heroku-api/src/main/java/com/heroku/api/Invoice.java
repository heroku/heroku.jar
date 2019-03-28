package com.heroku.api;

public class Invoice {
  private static final long serialVersionUID = 1L;

  String id;
  Integer addons_total;
  Integer database_total;
  Double charges_total;
  String created_at;
  Integer credits_total;
  Double dyno_units;
  Integer number;
  String payment_status;
  String period_end;
  String period_start;
  Integer platform_total;
  Integer state;
  Integer total;
  String updated_at;
  Integer weighted_dyno_hours;

  public Invoice() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getAddons_total() {
    return addons_total;
  }

  public void setAddons_total(Integer addons_total) {
    this.addons_total = addons_total;
  }

  public Integer getDatabase_total() {
    return database_total;
  }

  public void setDatabase_total(Integer database_total) {
    this.database_total = database_total;
  }

  public Double getCharges_total() {
    return charges_total;
  }

  public void setCharges_total(Double charges_total) {
    this.charges_total = charges_total;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public Integer getCredits_total() {
    return credits_total;
  }

  public void setCredits_total(Integer credits_total) {
    this.credits_total = credits_total;
  }

  public Double getDyno_units() {
    return dyno_units;
  }

  public void setDyno_units(Double dyno_units) {
    this.dyno_units = dyno_units;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getPayment_status() {
    return payment_status;
  }

  public void setPayment_status(String payment_status) {
    this.payment_status = payment_status;
  }

  public String getPeriod_end() {
    return period_end;
  }

  public void setPeriod_end(String period_end) {
    this.period_end = period_end;
  }

  public String getPeriod_start() {
    return period_start;
  }

  public void setPeriod_start(String period_start) {
    this.period_start = period_start;
  }

  public Integer getPlatform_total() {
    return platform_total;
  }

  public void setPlatform_total(Integer platform_total) {
    this.platform_total = platform_total;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  public Integer getWeighted_dyno_hours() {
    return weighted_dyno_hours;
  }

  public void setWeighted_dyno_hours(Integer weighted_dyno_hours) {
    this.weighted_dyno_hours = weighted_dyno_hours;
  }
}
