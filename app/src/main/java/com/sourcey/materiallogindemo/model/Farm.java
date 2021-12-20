package com.sourcey.materiallogindemo.model;

public class Farm {
    String farmeWaterFailNumber,treeNumber;
    boolean farmHomeHairRoomSwitch;

    public Farm() {
    }

    public Farm(String farmeWaterFailNumber, String treeNumber, boolean farmHomeHairRoomSwitch) {
        this.farmeWaterFailNumber = farmeWaterFailNumber;
        this.treeNumber = treeNumber;
        this.farmHomeHairRoomSwitch = farmHomeHairRoomSwitch;
    }

    public String getFarmeWaterFailNumber() {
        return farmeWaterFailNumber;
    }

    public void setFarmeWaterFailNumber(String farmeWaterFailNumber) {
        this.farmeWaterFailNumber = farmeWaterFailNumber;
    }

    public String getTreeNumber() {
        return treeNumber;
    }

    public void setTreeNumber(String treeNumber) {
        this.treeNumber = treeNumber;
    }

    public boolean isFarmHomeHairRoomSwitch() {
        return farmHomeHairRoomSwitch;
    }

    public void setFarmHomeHairRoomSwitch(boolean farmHomeHairRoomSwitch) {
        this.farmHomeHairRoomSwitch = farmHomeHairRoomSwitch;
    }
}
