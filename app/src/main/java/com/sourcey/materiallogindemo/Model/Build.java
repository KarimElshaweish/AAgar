package com.sourcey.materiallogindemo.Model;

public class Build {
    String buildNavigation,durationType,buildStreetWidth,buildRoomsNumber,buildMarketNumber,buildRomsNumber,bbuildAge;
    boolean buildReadySwitch;

    public Build() {
    }

    public Build(String buildNavigation, String durationType, String buildStreetWidth, String buildRoomsNumber, String buildMarketNumber, String buildRomsNumber, String bbuildAge, boolean buildReadySwitch) {
        this.buildNavigation = buildNavigation;
        this.durationType = durationType;
        this.buildStreetWidth = buildStreetWidth;
        this.buildRoomsNumber = buildRoomsNumber;
        this.buildMarketNumber = buildMarketNumber;
        this.buildRomsNumber = buildRomsNumber;
        this.bbuildAge = bbuildAge;
        this.buildReadySwitch = buildReadySwitch;
    }

    public String getBuildNavigation() {
        return buildNavigation;
    }

    public void setBuildNavigation(String buildNavigation) {
        this.buildNavigation = buildNavigation;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getBuildStreetWidth() {
        return buildStreetWidth;
    }

    public void setBuildStreetWidth(String buildStreetWidth) {
        this.buildStreetWidth = buildStreetWidth;
    }

    public String getBuildRoomsNumber() {
        return buildRoomsNumber;
    }

    public void setBuildRoomsNumber(String buildRoomsNumber) {
        this.buildRoomsNumber = buildRoomsNumber;
    }

    public String getBuildMarketNumber() {
        return buildMarketNumber;
    }

    public void setBuildMarketNumber(String buildMarketNumber) {
        this.buildMarketNumber = buildMarketNumber;
    }

    public String getBuildRomsNumber() {
        return buildRomsNumber;
    }

    public void setBuildRomsNumber(String buildRomsNumber) {
        this.buildRomsNumber = buildRomsNumber;
    }

    public String getBbuildAge() {
        return bbuildAge;
    }

    public void setBbuildAge(String bbuildAge) {
        this.bbuildAge = bbuildAge;
    }

    public boolean isBuildReadySwitch() {
        return buildReadySwitch;
    }

    public void setBuildReadySwitch(boolean buildReadySwitch) {
        this.buildReadySwitch = buildReadySwitch;
    }
}
