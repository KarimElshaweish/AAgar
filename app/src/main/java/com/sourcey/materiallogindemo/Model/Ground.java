package com.sourcey.materiallogindemo.Model;

public class Ground {
    String navigation,groundCommType,streetWidth,groundArea,groundMeterPrice;

    public Ground() {
    }

    public Ground(String navigation, String groundCommType, String streetWidth, String groundArea, String groundMeterPrice) {
        this.navigation = navigation;
        this.groundCommType = groundCommType;
        this.streetWidth = streetWidth;
        this.groundArea = groundArea;
        this.groundMeterPrice = groundMeterPrice;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getGroundCommType() {
        return groundCommType;
    }

    public void setGroundCommType(String groundCommType) {
        this.groundCommType = groundCommType;
    }

    public String getStreetWidth() {
        return streetWidth;
    }

    public void setStreetWidth(String streetWidth) {
        this.streetWidth = streetWidth;
    }

    public String getGroundArea() {
        return groundArea;
    }

    public void setGroundArea(String groundArea) {
        this.groundArea = groundArea;
    }

    public String getGroundMeterPrice() {
        return groundMeterPrice;
    }

    public void setGroundMeterPrice(String groundMeterPrice) {
        this.groundMeterPrice = groundMeterPrice;
    }
}
