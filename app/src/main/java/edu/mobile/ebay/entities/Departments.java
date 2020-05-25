package edu.mobile.ebay.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Departments implements Serializable {

    @SerializedName(value = "departmentId")
    @Expose
    private Long departmentId;

    @SerializedName(value = "departmentName")
    @Expose
    private String departmentName;

    public Departments() {
    }

    public Departments(Long departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DepartmentTemplate [departmentId=" + departmentId + ", departmentName=" + departmentName + "]";
    }
}
