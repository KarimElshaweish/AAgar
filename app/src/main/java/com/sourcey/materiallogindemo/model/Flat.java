package com.sourcey.materiallogindemo.model;

public class Flat {
    boolean family,furnished,kitchen,extension,carEnterance,airCondition;
    String durationType,reception,bathRoom,rooms,flatLevel,flatAge;

    public Flat() {
    }

    public Flat(boolean family, boolean furnished, boolean kitchen, boolean extension, boolean carEnterance
            , boolean airCondition, String durationType, String reception, String bathRoom, String rooms
            , String flatLevel, String flatAge) {
        this.family = family;
        this.furnished = furnished;
        this.kitchen = kitchen;
        this.extension = extension;
        this.carEnterance = carEnterance;
        this.airCondition = airCondition;
        this.durationType = durationType;
        this.reception = reception;
        this.bathRoom = bathRoom;
        this.rooms = rooms;
        this.flatLevel = flatLevel;
        this.flatAge = flatAge;
    }

    public boolean isFamily() {
        return family;
    }

    public void setFamily(boolean family) {
        this.family = family;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    public boolean isCarEnterance() {
        return carEnterance;
    }

    public void setCarEnterance(boolean carEnterance) {
        this.carEnterance = carEnterance;
    }

    public boolean isAirCondition() {
        return airCondition;
    }

    public void setAirCondition(boolean airCondition) {
        this.airCondition = airCondition;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
    }

    public String getBathRoom() {
        return bathRoom;
    }

    public void setBathRoom(String bathRoom) {
        this.bathRoom = bathRoom;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getFlatLevel() {
        return flatLevel;
    }

    public void setFlatLevel(String flatLevel) {
        this.flatLevel = flatLevel;
    }

    public String getFlatAge() {
        return flatAge;
    }

    public void setFlatAge(String flatAge) {
        this.flatAge = flatAge;
    }
}
