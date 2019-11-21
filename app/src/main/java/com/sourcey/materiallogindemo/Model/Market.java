package com.sourcey.materiallogindemo.Model;

public class Market {
    String marketNavigation,marketStreetWitdth,marketBuildAge;

    public Market() {
    }

    public Market(String marketNavigation, String marketStreetWitdth, String marketBuildAge) {
        this.marketNavigation = marketNavigation;
        this.marketStreetWitdth = marketStreetWitdth;
        this.marketBuildAge = marketBuildAge;
    }

    public String getMarketNavigation() {
        return marketNavigation;
    }

    public void setMarketNavigation(String marketNavigation) {
        this.marketNavigation = marketNavigation;
    }

    public String getMarketStreetWitdth() {
        return marketStreetWitdth;
    }

    public void setMarketStreetWitdth(String marketStreetWitdth) {
        this.marketStreetWitdth = marketStreetWitdth;
    }

    public String getMarketBuildAge() {
        return marketBuildAge;
    }

    public void setMarketBuildAge(String marketBuildAge) {
        this.marketBuildAge = marketBuildAge;
    }


}
