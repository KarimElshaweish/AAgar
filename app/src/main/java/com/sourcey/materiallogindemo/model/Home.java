package com.sourcey.materiallogindemo.model;

public class Home {
    String homeNavigation,homeReceptionNumber,homeBathRomsNumber,homeRoomsNumber,homeBuildAge;
    Boolean homeReadySwitch,homeDriverRoomSwitch,homeBillGirlRoomSwitch,homeHairRoomSwitch,
            homeHailSwitch,homeKitchenSwitch;

    public Home() {
    }

    public Home(String homeNavigation, String homeReceptionNumber, String homeBathRomsNumber, String homeRoomsNumber,
                String homeBuildAge, Boolean homeReadySwitch, Boolean homeDriverRoomSwitch, Boolean homeBillGirlRoomSwitch,
                Boolean homeHairRoomSwitch, Boolean homeHailSwitch, Boolean homeKitchenSwitch) {
        this.homeNavigation = homeNavigation;
        this.homeReceptionNumber = homeReceptionNumber;
        this.homeBathRomsNumber = homeBathRomsNumber;
        this.homeRoomsNumber = homeRoomsNumber;
        this.homeBuildAge = homeBuildAge;
        this.homeReadySwitch = homeReadySwitch;
        this.homeDriverRoomSwitch = homeDriverRoomSwitch;
        this.homeBillGirlRoomSwitch = homeBillGirlRoomSwitch;
        this.homeHairRoomSwitch = homeHairRoomSwitch;
        this.homeHailSwitch = homeHailSwitch;
        this.homeKitchenSwitch = homeKitchenSwitch;
    }

    public String getHomeNavigation() {
        return homeNavigation;
    }

    public void setHomeNavigation(String homeNavigation) {
        this.homeNavigation = homeNavigation;
    }

    public String getHomeReceptionNumber() {
        return homeReceptionNumber;
    }

    public void setHomeReceptionNumber(String homeReceptionNumber) {
        this.homeReceptionNumber = homeReceptionNumber;
    }

    public String getHomeBathRomsNumber() {
        return homeBathRomsNumber;
    }

    public void setHomeBathRomsNumber(String homeBathRomsNumber) {
        this.homeBathRomsNumber = homeBathRomsNumber;
    }

    public String getHomeRoomsNumber() {
        return homeRoomsNumber;
    }

    public void setHomeRoomsNumber(String homeRoomsNumber) {
        this.homeRoomsNumber = homeRoomsNumber;
    }

    public String getHomeBuildAge() {
        return homeBuildAge;
    }

    public void setHomeBuildAge(String homeBuildAge) {
        this.homeBuildAge = homeBuildAge;
    }

    public Boolean getHomeReadySwitch() {
        return homeReadySwitch;
    }

    public void setHomeReadySwitch(Boolean homeReadySwitch) {
        this.homeReadySwitch = homeReadySwitch;
    }

    public Boolean getHomeDriverRoomSwitch() {
        return homeDriverRoomSwitch;
    }

    public void setHomeDriverRoomSwitch(Boolean homeDriverRoomSwitch) {
        this.homeDriverRoomSwitch = homeDriverRoomSwitch;
    }

    public Boolean getHomeBillGirlRoomSwitch() {
        return homeBillGirlRoomSwitch;
    }

    public void setHomeBillGirlRoomSwitch(Boolean homeBillGirlRoomSwitch) {
        this.homeBillGirlRoomSwitch = homeBillGirlRoomSwitch;
    }

    public Boolean getHomeHairRoomSwitch() {
        return homeHairRoomSwitch;
    }

    public void setHomeHairRoomSwitch(Boolean homeHairRoomSwitch) {
        this.homeHairRoomSwitch = homeHairRoomSwitch;
    }

    public Boolean getHomeHailSwitch() {
        return homeHailSwitch;
    }

    public void setHomeHailSwitch(Boolean homeHailSwitch) {
        this.homeHailSwitch = homeHailSwitch;
    }

    public Boolean getHomeKitchenSwitch() {
        return homeKitchenSwitch;
    }

    public void setHomeKitchenSwitch(Boolean homeKitchenSwitch) {
        this.homeKitchenSwitch = homeKitchenSwitch;
    }
}
