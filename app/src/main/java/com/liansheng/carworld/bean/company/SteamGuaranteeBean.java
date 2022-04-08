package com.liansheng.carworld.bean.company;

import com.liansheng.carworld.bean.home.SaleCardBean;

import java.util.List;

public class SteamGuaranteeBean {


    private String userCarResourceId;
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
    private long id;
    private String userId;
    private long buyerId;
    private long sellerId;
    private String number;
    private boolean buyerSecurityFund;
    private boolean sellerSecurityFund;
    private String status;
    private List<StatusInfosBean> statusInfos;
    private boolean buyerConfirm;
    private boolean sellerConfirm;
    private boolean cancelation;
    private String creation;
    private SaleCardBean carResource;
    private String user;
    private UserBean seller;
    private UserBean buyer;

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

    public UserBean getSeller() {
        return seller;
    }

    public void setSeller(UserBean seller) {
        this.seller = seller;
    }

    public UserBean getBuyer() {
        return buyer;
    }

    public void setBuyer(UserBean buyer) {
        this.buyer = buyer;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserCarResourceId() {
        return userCarResourceId;
    }

    public void setUserCarResourceId(String userCarResourceId) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(long buyerId) {
        this.buyerId = buyerId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isBuyerSecurityFund() {
        return buyerSecurityFund;
    }

    public void setBuyerSecurityFund(boolean buyerSecurityFund) {
        this.buyerSecurityFund = buyerSecurityFund;
    }

    public boolean isSellerSecurityFund() {
        return sellerSecurityFund;
    }

    public void setSellerSecurityFund(boolean sellerSecurityFund) {
        this.sellerSecurityFund = sellerSecurityFund;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StatusInfosBean> getStatusInfos() {
        return statusInfos;
    }

    public void setStatusInfos(List<StatusInfosBean> statusInfos) {
        this.statusInfos = statusInfos;
    }

    public boolean isBuyerConfirm() {
        return buyerConfirm;
    }

    public void setBuyerConfirm(boolean buyerConfirm) {
        this.buyerConfirm = buyerConfirm;
    }

    public boolean isSellerConfirm() {
        return sellerConfirm;
    }

    public void setSellerConfirm(boolean sellerConfirm) {
        this.sellerConfirm = sellerConfirm;
    }

    public boolean isCancelation() {
        return cancelation;
    }

    public void setCancelation(boolean cancelation) {
        this.cancelation = cancelation;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public SaleCardBean getCarResource() {
        return carResource;
    }

    public void setCarResource(SaleCardBean carResource) {
        this.carResource = carResource;
    }

    public static class UserBean{
            //{
        //        "id":"637617641712621306",
        //            "mobile":"13333333333",
        //            "headphoto":null,
        //            "name":"陈小帅"
        //    }
        private String id;
        private String mobile;
        private String headphoto;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getHeadphoto() {
            return headphoto;
        }

        public void setHeadphoto(String headphoto) {
            this.headphoto = headphoto;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class StatusInfosBean {
        private String title;
        private String date;
        private boolean showUp;
        private boolean showDown;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isShowUp() {
            return showUp;
        }

        public void setShowUp(boolean showUp) {
            this.showUp = showUp;
        }

        public boolean isShowDown() {
            return showDown;
        }

        public void setShowDown(boolean showDown) {
            this.showDown = showDown;
        }
    }
}
