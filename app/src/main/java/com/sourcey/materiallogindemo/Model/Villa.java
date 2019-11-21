package com.sourcey.materiallogindemo.Model;

public class Villa {
    String navigation,villaReceptionNumber,villaBathRoomsNumber,villaStreetWidth,
            villaRoomsNumber,villaLevelNumber,villaBuildAge;
    boolean villaReceptionSwitch=false,villaDriverSwitch=false,villaBillGirlRoomSwitch=false,VillaPoolSwitch=false,
            villaHairRoomSwitch=false,VillaHallSwitch=false,villaVaultSwitch=false,villaReadySwitch=false,
            villaKitchenSwitch=false,extentionSwitch=false,villaCarEnternaceSwitch=false,villaElvatorSwitch=false,
            villaAirCondtionSwitch=false,villaDublexSwitch=false;

    public Villa() {
    }

    public Villa(String navigation, String villaReceptionNumber, String villaBathRoomsNumber, String villaStreetWidth, String villaRoomsNumber, String villaLevelNumber, String villaBuildAge, boolean villaReceptionSwitch, boolean villaDriverSwitch, boolean villaBillGirlRoomSwitch, boolean villaPoolSwitch, boolean villaHairRoomSwitch, boolean villaHallSwitch, boolean villaVaultSwitch, boolean villaReadySwitch, boolean villaKitchenSwitch, boolean extentionSwitch, boolean villaCarEnternaceSwitch, boolean villaElvatorSwitch, boolean villaAirCondtionSwitch, boolean villaDublexSwitch) {
        this.navigation = navigation;
        this.villaReceptionNumber = villaReceptionNumber;
        this.villaBathRoomsNumber = villaBathRoomsNumber;
        this.villaStreetWidth = villaStreetWidth;
        this.villaRoomsNumber = villaRoomsNumber;
        this.villaLevelNumber = villaLevelNumber;
        this.villaBuildAge = villaBuildAge;
        this.villaReceptionSwitch = villaReceptionSwitch;
        this.villaDriverSwitch = villaDriverSwitch;
        this.villaBillGirlRoomSwitch = villaBillGirlRoomSwitch;
        VillaPoolSwitch = villaPoolSwitch;
        this.villaHairRoomSwitch = villaHairRoomSwitch;
        VillaHallSwitch = villaHallSwitch;
        this.villaVaultSwitch = villaVaultSwitch;
        this.villaReadySwitch = villaReadySwitch;
        this.villaKitchenSwitch = villaKitchenSwitch;
        this.extentionSwitch = extentionSwitch;
        this.villaCarEnternaceSwitch = villaCarEnternaceSwitch;
        this.villaElvatorSwitch = villaElvatorSwitch;
        this.villaAirCondtionSwitch = villaAirCondtionSwitch;
        this.villaDublexSwitch = villaDublexSwitch;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getVillaReceptionNumber() {
        return villaReceptionNumber;
    }

    public void setVillaReceptionNumber(String villaReceptionNumber) {
        this.villaReceptionNumber = villaReceptionNumber;
    }

    public String getVillaBathRoomsNumber() {
        return villaBathRoomsNumber;
    }

    public void setVillaBathRoomsNumber(String villaBathRoomsNumber) {
        this.villaBathRoomsNumber = villaBathRoomsNumber;
    }

    public String getVillaStreetWidth() {
        return villaStreetWidth;
    }

    public void setVillaStreetWidth(String villaStreetWidth) {
        this.villaStreetWidth = villaStreetWidth;
    }

    public String getVillaRoomsNumber() {
        return villaRoomsNumber;
    }

    public void setVillaRoomsNumber(String villaRoomsNumber) {
        this.villaRoomsNumber = villaRoomsNumber;
    }

    public String getVillaLevelNumber() {
        return villaLevelNumber;
    }

    public void setVillaLevelNumber(String villaLevelNumber) {
        this.villaLevelNumber = villaLevelNumber;
    }

    public String getVillaBuildAge() {
        return villaBuildAge;
    }

    public void setVillaBuildAge(String villaBuildAge) {
        this.villaBuildAge = villaBuildAge;
    }

    public boolean isVillaReceptionSwitch() {
        return villaReceptionSwitch;
    }

    public void setVillaReceptionSwitch(boolean villaReceptionSwitch) {
        this.villaReceptionSwitch = villaReceptionSwitch;
    }

    public boolean isVillaDriverSwitch() {
        return villaDriverSwitch;
    }

    public void setVillaDriverSwitch(boolean villaDriverSwitch) {
        this.villaDriverSwitch = villaDriverSwitch;
    }

    public boolean isVillaBillGirlRoomSwitch() {
        return villaBillGirlRoomSwitch;
    }

    public void setVillaBillGirlRoomSwitch(boolean villaBillGirlRoomSwitch) {
        this.villaBillGirlRoomSwitch = villaBillGirlRoomSwitch;
    }

    public boolean isVillaPoolSwitch() {
        return VillaPoolSwitch;
    }

    public void setVillaPoolSwitch(boolean villaPoolSwitch) {
        VillaPoolSwitch = villaPoolSwitch;
    }

    public boolean isVillaHairRoomSwitch() {
        return villaHairRoomSwitch;
    }

    public void setVillaHairRoomSwitch(boolean villaHairRoomSwitch) {
        this.villaHairRoomSwitch = villaHairRoomSwitch;
    }

    public boolean isVillaHallSwitch() {
        return VillaHallSwitch;
    }

    public void setVillaHallSwitch(boolean villaHallSwitch) {
        VillaHallSwitch = villaHallSwitch;
    }

    public boolean isVillaVaultSwitch() {
        return villaVaultSwitch;
    }

    public void setVillaVaultSwitch(boolean villaVaultSwitch) {
        this.villaVaultSwitch = villaVaultSwitch;
    }

    public boolean isVillaReadySwitch() {
        return villaReadySwitch;
    }

    public void setVillaReadySwitch(boolean villaReadySwitch) {
        this.villaReadySwitch = villaReadySwitch;
    }

    public boolean isVillaKitchenSwitch() {
        return villaKitchenSwitch;
    }

    public void setVillaKitchenSwitch(boolean villaKitchenSwitch) {
        this.villaKitchenSwitch = villaKitchenSwitch;
    }

    public boolean isExtentionSwitch() {
        return extentionSwitch;
    }

    public void setExtentionSwitch(boolean extentionSwitch) {
        this.extentionSwitch = extentionSwitch;
    }

    public boolean isVillaCarEnternaceSwitch() {
        return villaCarEnternaceSwitch;
    }

    public void setVillaCarEnternaceSwitch(boolean villaCarEnternaceSwitch) {
        this.villaCarEnternaceSwitch = villaCarEnternaceSwitch;
    }

    public boolean isVillaElvatorSwitch() {
        return villaElvatorSwitch;
    }

    public void setVillaElvatorSwitch(boolean villaElvatorSwitch) {
        this.villaElvatorSwitch = villaElvatorSwitch;
    }

    public boolean isVillaAirCondtionSwitch() {
        return villaAirCondtionSwitch;
    }

    public void setVillaAirCondtionSwitch(boolean villaAirCondtionSwitch) {
        this.villaAirCondtionSwitch = villaAirCondtionSwitch;
    }

    public boolean isVillaDublexSwitch() {
        return villaDublexSwitch;
    }

    public void setVillaDublexSwitch(boolean villaDublexSwitch) {
        this.villaDublexSwitch = villaDublexSwitch;
    }
}
