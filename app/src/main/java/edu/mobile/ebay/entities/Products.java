package edu.mobile.ebay.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Products implements Serializable {

    @SerializedName("owner")
    @Expose
    private String Owner;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String Description;

    @SerializedName("endBid")
    @Expose
    private Date endBid;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("imagePath")
    @Expose
    private String imagePath;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("itemPath")
    @Expose
    private String itemPath;

    @SerializedName("department")
    @Expose
    private String Department;

    @SerializedName("bids")
    @Expose
    private List<Bids> Bids;

    public Products() {
    }

    public Products(String owner, String id, String description, Date endBid, String state, String imagePath,
                           String title, String itemPath, String department, List<Bids> bids) {
        Owner = owner;
        this.id = id;
        Description = description;
        this.endBid = endBid;
        this.state = state;
        this.imagePath = imagePath;
        this.title = title;
        this.itemPath = itemPath;
        Department = department;
        Bids = bids;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getEndBid() {
        return endBid;
    }

    public void setEndBid(Date endBid) {
        this.endBid = endBid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemPath() {
        return itemPath;
    }

    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public List<Bids> getBids() {
        return Bids;
    }

    public void setBids(List<Bids> bids) {
        Bids = bids;
    }

    @Override
    public String toString() {
        return "ProductTemplate [Bids=" + Bids + ", Department=" + Department + ", Description="
                + Description + ", Owner=" + Owner + ", endBid=" + endBid + ", id=" + id + ", imagePath=" + imagePath
                + ", itemPath=" + itemPath + ", state=" + state + ", title=" + title + "]";
    }
}
