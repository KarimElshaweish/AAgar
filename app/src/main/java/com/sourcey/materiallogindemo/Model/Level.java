package com.sourcey.materiallogindemo.Model;

public class Level {
    String levelNavigation,levelReceptionNumber,levelBathRoomsNumber,levelRoomsNumber,llevelNumber,levelBuildAge;
    boolean levelReadySwitch,levelcarEnternaceSwitch,levelAirCondtionSwitch;

    public Level() {
    }

    public Level(String levelNavigation, String levelReceptionNumber, String levelBathRoomsNumber, String levelRoomsNumber, String llevelNumber, String levelBuildAge, boolean levelReadySwitch, boolean levelcarEnternaceSwitch, boolean levelAirCondtionSwitch) {
        this.levelNavigation = levelNavigation;
        this.levelReceptionNumber = levelReceptionNumber;
        this.levelBathRoomsNumber = levelBathRoomsNumber;
        this.levelRoomsNumber = levelRoomsNumber;
        this.llevelNumber = llevelNumber;
        this.levelBuildAge = levelBuildAge;
        this.levelReadySwitch = levelReadySwitch;
        this.levelcarEnternaceSwitch = levelcarEnternaceSwitch;
        this.levelAirCondtionSwitch = levelAirCondtionSwitch;
    }

    public String getLevelNavigation() {
        return levelNavigation;
    }

    public void setLevelNavigation(String levelNavigation) {
        this.levelNavigation = levelNavigation;
    }

    public String getLevelReceptionNumber() {
        return levelReceptionNumber;
    }

    public void setLevelReceptionNumber(String levelReceptionNumber) {
        this.levelReceptionNumber = levelReceptionNumber;
    }

    public String getLevelBathRoomsNumber() {
        return levelBathRoomsNumber;
    }

    public void setLevelBathRoomsNumber(String levelBathRoomsNumber) {
        this.levelBathRoomsNumber = levelBathRoomsNumber;
    }

    public String getLevelRoomsNumber() {
        return levelRoomsNumber;
    }

    public void setLevelRoomsNumber(String levelRoomsNumber) {
        this.levelRoomsNumber = levelRoomsNumber;
    }

    public String getLlevelNumber() {
        return llevelNumber;
    }

    public void setLlevelNumber(String llevelNumber) {
        this.llevelNumber = llevelNumber;
    }

    public String getLevelBuildAge() {
        return levelBuildAge;
    }

    public void setLevelBuildAge(String levelBuildAge) {
        this.levelBuildAge = levelBuildAge;
    }

    public boolean isLevelReadySwitch() {
        return levelReadySwitch;
    }

    public void setLevelReadySwitch(boolean levelReadySwitch) {
        this.levelReadySwitch = levelReadySwitch;
    }

    public boolean isLevelcarEnternaceSwitch() {
        return levelcarEnternaceSwitch;
    }

    public void setLevelcarEnternaceSwitch(boolean levelcarEnternaceSwitch) {
        this.levelcarEnternaceSwitch = levelcarEnternaceSwitch;
    }

    public boolean isLevelAirCondtionSwitch() {
        return levelAirCondtionSwitch;
    }

    public void setLevelAirCondtionSwitch(boolean levelAirCondtionSwitch) {
        this.levelAirCondtionSwitch = levelAirCondtionSwitch;
    }
}
