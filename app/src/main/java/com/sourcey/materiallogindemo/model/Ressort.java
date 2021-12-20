package com.sourcey.materiallogindemo.model;

public class Ressort {
    String ressortNavigation,sweetReceptionNumber,sweetBathRomsNumber,sweetRoomsNumber
            ,SweetStreetWidth,sweetBuildAge;
    boolean sweetPoolSwitch,sweetFootballGroundSwitch,sweetVolleyBallGroundSwitch,
            sweetHairRoomSwitch,sweetEntertanmentPlace,sweetBigBathSwitch;

    public Ressort() {
    }

    public Ressort(String ressortNavigation, String sweetReceptionNumber, String sweetBathRomsNumber,
                   String sweetRoomsNumber, String sweetStreetWidth, String sweetBuildAge,
                   boolean sweetPoolSwitch, boolean sweetFootballGroundSwitch, boolean sweetVolleyBallGroundSwitch,
                   boolean sweetHairRoomSwitch, boolean sweetEntertanmentPlace, boolean sweetBigBathSwitch) {
        this.ressortNavigation = ressortNavigation;
        this.sweetReceptionNumber = sweetReceptionNumber;
        this.sweetBathRomsNumber = sweetBathRomsNumber;
        this.sweetRoomsNumber = sweetRoomsNumber;
        SweetStreetWidth = sweetStreetWidth;
        this.sweetBuildAge = sweetBuildAge;
        this.sweetPoolSwitch = sweetPoolSwitch;
        this.sweetFootballGroundSwitch = sweetFootballGroundSwitch;
        this.sweetVolleyBallGroundSwitch = sweetVolleyBallGroundSwitch;
        this.sweetHairRoomSwitch = sweetHairRoomSwitch;
        this.sweetEntertanmentPlace = sweetEntertanmentPlace;
        this.sweetBigBathSwitch = sweetBigBathSwitch;
    }

    public String getRessortNavigation() {
        return ressortNavigation;
    }

    public void setRessortNavigation(String ressortNavigation) {
        this.ressortNavigation = ressortNavigation;
    }

    public String getSweetReceptionNumber() {
        return sweetReceptionNumber;
    }

    public void setSweetReceptionNumber(String sweetReceptionNumber) {
        this.sweetReceptionNumber = sweetReceptionNumber;
    }

    public String getSweetBathRomsNumber() {
        return sweetBathRomsNumber;
    }

    public void setSweetBathRomsNumber(String sweetBathRomsNumber) {
        this.sweetBathRomsNumber = sweetBathRomsNumber;
    }

    public String getSweetRoomsNumber() {
        return sweetRoomsNumber;
    }

    public void setSweetRoomsNumber(String sweetRoomsNumber) {
        this.sweetRoomsNumber = sweetRoomsNumber;
    }

    public String getSweetStreetWidth() {
        return SweetStreetWidth;
    }

    public void setSweetStreetWidth(String sweetStreetWidth) {
        SweetStreetWidth = sweetStreetWidth;
    }

    public String getSweetBuildAge() {
        return sweetBuildAge;
    }

    public void setSweetBuildAge(String sweetBuildAge) {
        this.sweetBuildAge = sweetBuildAge;
    }

    public boolean isSweetPoolSwitch() {
        return sweetPoolSwitch;
    }

    public void setSweetPoolSwitch(boolean sweetPoolSwitch) {
        this.sweetPoolSwitch = sweetPoolSwitch;
    }

    public boolean isSweetFootballGroundSwitch() {
        return sweetFootballGroundSwitch;
    }

    public void setSweetFootballGroundSwitch(boolean sweetFootballGroundSwitch) {
        this.sweetFootballGroundSwitch = sweetFootballGroundSwitch;
    }

    public boolean isSweetVolleyBallGroundSwitch() {
        return sweetVolleyBallGroundSwitch;
    }

    public void setSweetVolleyBallGroundSwitch(boolean sweetVolleyBallGroundSwitch) {
        this.sweetVolleyBallGroundSwitch = sweetVolleyBallGroundSwitch;
    }

    public boolean isSweetHairRoomSwitch() {
        return sweetHairRoomSwitch;
    }

    public void setSweetHairRoomSwitch(boolean sweetHairRoomSwitch) {
        this.sweetHairRoomSwitch = sweetHairRoomSwitch;
    }

    public boolean isSweetEntertanmentPlace() {
        return sweetEntertanmentPlace;
    }

    public void setSweetEntertanmentPlace(boolean sweetEntertanmentPlace) {
        this.sweetEntertanmentPlace = sweetEntertanmentPlace;
    }

    public boolean isSweetBigBathSwitch() {
        return sweetBigBathSwitch;
    }

    public void setSweetBigBathSwitch(boolean sweetBigBathSwitch) {
        this.sweetBigBathSwitch = sweetBigBathSwitch;
    }
}
