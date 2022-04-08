package com.liansheng.carworld.activity.company;

import java.util.List;

public class ReqSteamBean {

    private int userCarResourceId;
    private int keys;
    //总价
    private int total;
    //保障金
    private int securityFund;
    private boolean isEngine;
    private boolean isStructure;
    private boolean isGearbox;
    private boolean isAIRBAG;
    private String plate;
    private String painting;
    private String overhaul;
    private String rust;
    private String screw;
    private String interior;
    private String skeleton;
    private String refit;
    private String describe;
    private List<String> materials;
    private boolean transfer;
    private boolean fileUp;
    private boolean logistics;
    private String otherAgreements;

    public int getUserCarResourceId() {
        return userCarResourceId;
    }

    public void setUserCarResourceId(int userCarResourceId) {
        this.userCarResourceId = userCarResourceId;
    }

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

    public boolean isIsEngine() {
        return isEngine;
    }

    public void setIsEngine(boolean isEngine) {
        this.isEngine = isEngine;
    }

    public boolean isIsStructure() {
        return isStructure;
    }

    public void setIsStructure(boolean isStructure) {
        this.isStructure = isStructure;
    }

    public boolean isIsGearbox() {
        return isGearbox;
    }

    public void setIsGearbox(boolean isGearbox) {
        this.isGearbox = isGearbox;
    }

    public boolean isIsAIRBAG() {
        return isAIRBAG;
    }

    public void setIsAIRBAG(boolean isAIRBAG) {
        this.isAIRBAG = isAIRBAG;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPainting() {
        return painting;
    }

    public void setPainting(String painting) {
        this.painting = painting;
    }

    public String getOverhaul() {
        return overhaul;
    }

    public void setOverhaul(String overhaul) {
        this.overhaul = overhaul;
    }

    public String getRust() {
        return rust;
    }

    public void setRust(String rust) {
        this.rust = rust;
    }

    public String getScrew() {
        return screw;
    }

    public void setScrew(String screw) {
        this.screw = screw;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(String skeleton) {
        this.skeleton = skeleton;
    }

    public String getRefit() {
        return refit;
    }

    public void setRefit(String refit) {
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
}
