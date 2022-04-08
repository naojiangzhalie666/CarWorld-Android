package com.liansheng.carworld.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CouponBean;

public class TransporterBean implements Parcelable{
    /**
     * id : string
     * userId : string
     * driverId : string
     * customService : {"name":"string","phoneNumber":"string","sort":0,"id":"string"}
     * origin : {"province":"string","city":"string","county":"string","name":"string","street":"string","longitude":"string","latitude":"string"}
     * destination : {"province":"string","city":"string","county":"string","name":"string","street":"string","longitude":"string","latitude":"string"}
     * startDate : {"date":"2020-12-09T07:51:41.397Z","timeSlot":"morning"}
     * carModel : {"model":"string","fault":true}
     * scooterType : string
     * note : string
     * originContacts : {"name":"string","mobile":"string","wechat":"string"}
     * destinationContacts : {"name":"string","mobile":"string","wechat":"string"}
     * tonnage : five
     * checkCarPay : true
     * price : 0
     * distance : 0
     * timeOfArrival : string
     * status : creation
     * creation : 2020-12-09T07:51:41.397Z
     */

    private String id;
    private String userId;
    private String driverId;
    private CustomServiceBean customService;
    private AddressBean origin;
    private AddressBean destination;
    private StartDateBean startDate;
    private CarModelBean carModel;
    private String scooterType;
    private String note;
    private ContactsBean originContacts;
    private ContactsBean destinationContacts;
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
    private String truckRequire;
    private String truckRequireString;
    private String assessType;
    private String orderNO;
    private String driver;
    private UserCarBean car;
    private String nickname;
    private int platformPrice;
    CouponBean coupon;

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public int getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(int platformPrice) {
        this.platformPrice = platformPrice;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserCarBean getCar() {
        return car;
    }

    public void setCar(UserCarBean car) {
        this.car = car;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getTruckRequireString() {
        return truckRequireString;
    }

    public void setTruckRequireString(String truckRequireString) {
        this.truckRequireString = truckRequireString;
    }

    public String getAssessType() {
        return assessType;
    }

    public void setAssessType(String assessType) {
        this.assessType = assessType;
    }

    public String getTruckRequire() {
        return truckRequire;
    }

    public void setTruckRequire(String truckRequire) {
        this.truckRequire = truckRequire;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean getBigCar() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public ContactsBean getOriginContacts() {
        return originContacts;
    }

    public void setOriginContacts(ContactsBean originContacts) {
        this.originContacts = originContacts;
    }

    public ContactsBean getDestinationContacts() {
        return destinationContacts;
    }

    public void setDestinationContacts(ContactsBean destinationContacts) {
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


    public boolean isBigCar() {
        return bigCar;
    }


    public static class CustomServiceBean implements Parcelable {
        /**
         * name : string
         * phoneNumber : string
         * sort : 0
         * id : string
         */

        private String name;
        private String phoneNumber;
        private int sort;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.phoneNumber);
            dest.writeInt(this.sort);
            dest.writeString(this.id);
        }

        public void readFromParcel(Parcel source) {
            this.name = source.readString();
            this.phoneNumber = source.readString();
            this.sort = source.readInt();
            this.id = source.readString();
        }

        public CustomServiceBean() {
        }

        protected CustomServiceBean(Parcel in) {
            this.name = in.readString();
            this.phoneNumber = in.readString();
            this.sort = in.readInt();
            this.id = in.readString();
        }

        public static final Creator<CustomServiceBean> CREATOR = new Creator<CustomServiceBean>() {
            @Override
            public CustomServiceBean createFromParcel(Parcel source) {
                return new CustomServiceBean(source);
            }

            @Override
            public CustomServiceBean[] newArray(int size) {
                return new CustomServiceBean[size];
            }
        };
    }

    public static class StartDateBean implements Parcelable {
        /**
         * date : 2020-12-09T07:51:41.397Z
         * timeSlot : morning
         */

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.timeSlot);
        }

        public void readFromParcel(Parcel source) {
            this.date = source.readString();
            this.timeSlot = source.readString();
        }

        public StartDateBean() {
        }

        protected StartDateBean(Parcel in) {
            this.date = in.readString();
            this.timeSlot = in.readString();
        }

        public static final Creator<StartDateBean> CREATOR = new Creator<StartDateBean>() {
            @Override
            public StartDateBean createFromParcel(Parcel source) {
                return new StartDateBean(source);
            }

            @Override
            public StartDateBean[] newArray(int size) {
                return new StartDateBean[size];
            }
        };
    }

    public static class CarModelBean implements Parcelable {
        /**
         * model : string
         * fault : true
         */

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.model);
            dest.writeByte(this.fault ? (byte) 1 : (byte) 0);
        }

        public void readFromParcel(Parcel source) {
            this.model = source.readString();
            this.fault = source.readByte() != 0;
        }

        public CarModelBean() {
        }

        protected CarModelBean(Parcel in) {
            this.model = in.readString();
            this.fault = in.readByte() != 0;
        }

        public static final Creator<CarModelBean> CREATOR = new Creator<CarModelBean>() {
            @Override
            public CarModelBean createFromParcel(Parcel source) {
                return new CarModelBean(source);
            }

            @Override
            public CarModelBean[] newArray(int size) {
                return new CarModelBean[size];
            }
        };
    }

    public static class ContactsBean implements Parcelable {
        /**
         * name : string
         * mobile : string
         * wechat : string
         */

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.mobile);
            dest.writeString(this.wechat);
        }

        public void readFromParcel(Parcel source) {
            this.name = source.readString();
            this.mobile = source.readString();
            this.wechat = source.readString();
        }

        public ContactsBean() {
        }

        protected ContactsBean(Parcel in) {
            this.name = in.readString();
            this.mobile = in.readString();
            this.wechat = in.readString();
        }

        public static final Creator<ContactsBean> CREATOR = new Creator<ContactsBean>() {
            @Override
            public ContactsBean createFromParcel(Parcel source) {
                return new ContactsBean(source);
            }

            @Override
            public ContactsBean[] newArray(int size) {
                return new ContactsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.driverId);
        dest.writeParcelable(this.customService, flags);
        dest.writeParcelable(this.origin, flags);
        dest.writeParcelable(this.destination, flags);
        dest.writeParcelable(this.startDate, flags);
        dest.writeParcelable(this.carModel, flags);
        dest.writeString(this.scooterType);
        dest.writeString(this.note);
        dest.writeParcelable(this.originContacts, flags);
        dest.writeParcelable(this.destinationContacts, flags);
        dest.writeString(this.tonnage);
        dest.writeByte(this.checkCarPay ? (byte) 1 : (byte) 0);
        dest.writeInt(this.price);
        dest.writeInt(this.distance);
        dest.writeString(this.timeOfArrival);
        dest.writeString(this.status);
        dest.writeString(this.creation);
        dest.writeByte(this.bigCar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        dest.writeInt(this.increasePrice);
        dest.writeString(this.increasePriceDate);
        dest.writeString(this.truckRequire);
        dest.writeString(this.truckRequireString);
        dest.writeString(this.assessType);
        dest.writeString(this.orderNO);
        dest.writeString(this.driver);
        dest.writeParcelable(this.car, flags);
        dest.writeString(this.nickname);
        dest.writeInt(this.platformPrice);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.driverId = source.readString();
        this.customService = source.readParcelable(CustomServiceBean.class.getClassLoader());
        this.origin = source.readParcelable(AddressBean.class.getClassLoader());
        this.destination = source.readParcelable(AddressBean.class.getClassLoader());
        this.startDate = source.readParcelable(StartDateBean.class.getClassLoader());
        this.carModel = source.readParcelable(CarModelBean.class.getClassLoader());
        this.scooterType = source.readString();
        this.note = source.readString();
        this.originContacts = source.readParcelable(ContactsBean.class.getClassLoader());
        this.destinationContacts = source.readParcelable(ContactsBean.class.getClassLoader());
        this.tonnage = source.readString();
        this.checkCarPay = source.readByte() != 0;
        this.price = source.readInt();
        this.distance = source.readInt();
        this.timeOfArrival = source.readString();
        this.status = source.readString();
        this.creation = source.readString();
        this.bigCar = source.readByte() != 0;
        this.paid = source.readByte() != 0;
        this.increasePrice = source.readInt();
        this.increasePriceDate = source.readString();
        this.truckRequire = source.readString();
        this.truckRequireString = source.readString();
        this.assessType = source.readString();
        this.orderNO = source.readString();
        this.driver = source.readString();
        this.car = source.readParcelable(UserCarBean.class.getClassLoader());
        this.nickname = source.readString();
        this.platformPrice=source.readInt();
    }

    public TransporterBean() {
    }

    protected TransporterBean(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.driverId = in.readString();
        this.customService = in.readParcelable(CustomServiceBean.class.getClassLoader());
        this.origin = in.readParcelable(AddressBean.class.getClassLoader());
        this.destination = in.readParcelable(AddressBean.class.getClassLoader());
        this.startDate = in.readParcelable(StartDateBean.class.getClassLoader());
        this.carModel = in.readParcelable(CarModelBean.class.getClassLoader());
        this.scooterType = in.readString();
        this.note = in.readString();
        this.originContacts = in.readParcelable(ContactsBean.class.getClassLoader());
        this.destinationContacts = in.readParcelable(ContactsBean.class.getClassLoader());
        this.tonnage = in.readString();
        this.checkCarPay = in.readByte() != 0;
        this.price = in.readInt();
        this.distance = in.readInt();
        this.timeOfArrival = in.readString();
        this.status = in.readString();
        this.creation = in.readString();
        this.bigCar = in.readByte() != 0;
        this.paid = in.readByte() != 0;
        this.increasePrice = in.readInt();
        this.increasePriceDate = in.readString();
        this.truckRequire = in.readString();
        this.truckRequireString = in.readString();
        this.assessType = in.readString();
        this.orderNO = in.readString();
        this.driver = in.readString();
        this.car = in.readParcelable(UserCarBean.class.getClassLoader());
        this.nickname = in.readString();
        this.platformPrice=in.readInt();
    }

    public static final Parcelable.Creator<TransporterBean> CREATOR = new Parcelable.Creator<TransporterBean>() {
        @Override
        public TransporterBean createFromParcel(Parcel source) {
            return new TransporterBean(source);
        }

        @Override
        public TransporterBean[] newArray(int size) {
            return new TransporterBean[size];
        }
    };
}
