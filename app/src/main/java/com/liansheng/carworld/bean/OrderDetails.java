package com.liansheng.carworld.bean;

import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CouponBean;

import java.io.Serializable;

public class OrderDetails{
    /**
     * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * driverId : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * customService : {"phoneNumber":"string","name":"string"}
     * origin : {"province":"string","city":"string","county":"string","name":"string","street":"string","longitude":"string","latitude":"string"}
     * destination : {"province":"string","city":"string","county":"string","name":"string","street":"string","longitude":"string","latitude":"string"}
     * startDate : {"date":"2020-12-21T02:35:37.134Z","timeSlot":"morning"}
     * carModel : {"model":"string","fault":true}
     * scooterType : blueCard
     * note : string
     * originContacts : {"name":"string","mobile":"string","wechat":"string"}
     * destinationContacts : {"name":"string","mobile":"string","wechat":"string"}
     * tonnage : string
     * checkCarPay : true
     * price : 0
     * distance : 0
     * timeOfArrival : string
     * status : creation
     * creation : 2020-12-21T02:35:37.134Z
     */

    private String id;
    private String driverId;
    private CustomServiceBean customService;
    private AddressBean origin;
    private AddressBean destination;
    private StartDateBean startDate;
    private CarModelBean carModel;
    private String scooterType;
    private String note;
    private OriginContactsBean originContacts;
    private DestinationContactsBean destinationContacts;
    private DriverBean driver;
    private String tonnage;
    private boolean checkCarPay;
    private int price;
    private int distance;
    private String timeOfArrival;
    private String status;
    private String creation;
    private boolean bigCar;
    private boolean paid;
    private int increasePrice;
    private String increasePriceDate;
    private String orderNO;
    private String truckRequireString;
    private String assessType;
    private int platformPrice;
    private String userId;
    private CouponBean coupon;

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(int platformPrice) {
        this.platformPrice = platformPrice;
    }

    public String getAssessType() {
        return assessType;
    }

    public void setAssessType(String assessType) {
        this.assessType = assessType;
    }

    public String getTruckRequireString() {
        return truckRequireString;
    }

    public void setTruckRequireString(String truckRequireString) {
        this.truckRequireString = truckRequireString;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public int getIncreasePrice() {
        return increasePrice;
    }

    public void setIncreasePrice(int increasePrice) {
        this.increasePrice = increasePrice;
    }

    public String getIncreasePriceDate() {
        return increasePriceDate;
    }

    public void setIncreasePriceDate(String increasePriceDate) {
        this.increasePriceDate = increasePriceDate;
    }

    public DriverBean getDriver() {
        return driver;
    }

    public void setDriver(DriverBean driver) {
        this.driver = driver;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isBigCar() {
        return bigCar;
    }
    public void setBigCar(boolean bigCar) {
        this.bigCar = bigCar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public CustomServiceBean getCustomService() {
        return customService;
    }

    public void setCustomService(CustomServiceBean customService) {
        this.customService = customService;
    }

    public AddressBean getOrigin() {
        return origin;
    }

    public void setOrigin(AddressBean origin) {
        this.origin = origin;
    }

    public AddressBean getDestination() {
        return destination;
    }

    public void setDestination(AddressBean destination) {
        this.destination = destination;
    }

    public StartDateBean getStartDate() {
        return startDate;
    }

    public void setStartDate(StartDateBean startDate) {
        this.startDate = startDate;
    }

    public CarModelBean getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModelBean carModel) {
        this.carModel = carModel;
    }

    public String getScooterType() {
        return scooterType;
    }

    public void setScooterType(String scooterType) {
        this.scooterType = scooterType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OriginContactsBean getOriginContacts() {
        return originContacts;
    }

    public void setOriginContacts(OriginContactsBean originContacts) {
        this.originContacts = originContacts;
    }

    public DestinationContactsBean getDestinationContacts() {
        return destinationContacts;
    }

    public void setDestinationContacts(DestinationContactsBean destinationContacts) {
        this.destinationContacts = destinationContacts;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public boolean isCheckCarPay() {
        return checkCarPay;
    }

    public void setCheckCarPay(boolean checkCarPay) {
        this.checkCarPay = checkCarPay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(String timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public static class CustomServiceBean{

        private String phoneNumber;
        private String name;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class StartDateBean{

        private String date;
        private String timeSlot;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }
    }

    public static class CarModelBean{

        private String model;
        private boolean fault;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public boolean isFault() {
            return fault;
        }

        public void setFault(boolean fault) {
            this.fault = fault;
        }
    }

    public static class OriginContactsBean {

        private String name;
        private String mobile;
        private String wechat;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }
    }

    public static class DestinationContactsBean{

        private String name;
        private String mobile;
        private String wechat;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }
    }
    public static class DriverBean{

        private String name;
        private String company;
        private String mobile;
        private String cardNo;
        private String score;
        private PositionBean position;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public PositionBean getPosition() {
            return position;
        }

        public void setPosition(PositionBean position) {
            this.position = position;
        }

        public class PositionBean {
            private String longitude;
            private String latitude;

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }
        }
    }
}
