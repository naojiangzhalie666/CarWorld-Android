package com.liansheng.carworld.bean.company;

import java.util.List;

public class SteamGuaranteeUpdateBean
{

    private int keys;
    private int total;
    private int securityFund;
    private boolean isEngine;
    private boolean isStructure;
    private boolean isGearbox;
    private boolean isAIRBAG;
    private boolean plate;
    private boolean painting;
    private boolean overhaul;
    private boolean rust;
    private boolean screw;
    private boolean interior;
    private boolean skeleton;
    private boolean refit;
    private String describe;
    private List<String> materials;
    private boolean transfer;
    private boolean fileUp;
    private boolean logistics;
    private String otherAgreements;
    private String platformGuaranteeId;

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSecurityFund() {
        return securityFund;
    }

    public void setSecurityFund(int securityFund) {
        this.securityFund = securityFund;
    }

    public boolean isEngine() {
        return isEngine;
    }

    public void setEngine(boolean engine) {
        isEngine = engine;
    }

    public boolean isStructure() {
        return isStructure;
    }

    public void setStructure(boolean structure) {
        isStructure = structure;
    }

    public boolean isGearbox() {
        return isGearbox;
    }

    public void setGearbox(boolean gearbox) {
        isGearbox = gearbox;
    }

    public boolean isAIRBAG() {
        return isAIRBAG;
    }

    public void setAIRBAG(boolean AIRBAG) {
        isAIRBAG = AIRBAG;
    }

    public boolean isPlate() {
        return plate;
    }

    public void setPlate(boolean plate) {
        this.plate = plate;
    }

    public boolean isPainting() {
        return painting;
    }

    public void setPainting(boolean painting) {
        this.painting = painting;
    }

    public boolean isOverhaul() {
        return overhaul;
    }

    public void setOverhaul(boolean overhaul) {
        this.overhaul = overhaul;
    }

    public boolean isRust() {
        return rust;
    }

    public void setRust(boolean rust) {
        this.rust = rust;
    }

    public boolean isScrew() {
        return screw;
    }

    public void setScrew(boolean screw) {
        this.screw = screw;
    }

    public boolean isInterior() {
        return interior;
    }

    public void setInterior(boolean interior) {
        this.interior = interior;
    }

    public boolean isSkeleton() {
        return skeleton;
    }

    public void setSkeleton(boolean skeleton) {
        this.skeleton = skeleton;
    }

    public boolean isRefit() {
        return refit;
    }

    public void setRefit(boolean refit) {
        this.refit = refit;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public boolean isFileUp() {
        return fileUp;
    }

    public void setFileUp(boolean fileUp) {
        this.fileUp = fileUp;
    }

    public boolean isLogistics() {
        return logistics;
    }

    public void setLogistics(boolean logistics) {
        this.logistics = logistics;
    }

    public String getOtherAgreements() {
        return otherAgreements;
    }

    public void setOtherAgreements(String otherAgreements) {
        this.otherAgreements = otherAgreements;
    }

    public String getPlatformGuaranteeId() {
        return platformGuaranteeId;
    }

    public void setPlatformGuaranteeId(String platformGuaranteeId) {
        this.platformGuaranteeId = platformGuaranteeId;
    }
}
