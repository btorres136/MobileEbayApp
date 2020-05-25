package edu.mobile.ebay.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Bids implements Serializable {
    @SerializedName("quantity")
    @Expose
    private double quantity;

    @SerializedName("date_set")
    @Expose
    private Date date_set;

    @SerializedName("product")
    @Expose
    private String product;

    public Bids() {
    }

    public Bids(double quantity, Date endDate, String product) {
        this.quantity = quantity;
        this.date_set = endDate;
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getEndDate() {
        return date_set;
    }

    public void setEndDate(Date date_set) {
        this.date_set = date_set;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "BidTemplate [endDate=" + date_set + ", product=" + product + ", quantity=" + quantity + "]";
    }
}
