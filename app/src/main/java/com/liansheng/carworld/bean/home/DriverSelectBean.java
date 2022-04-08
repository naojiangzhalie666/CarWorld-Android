package com.liansheng.carworld.bean.home;

import com.liansheng.carworld.bean.UserCarBean;
import com.liansheng.carworld.bean.me.AddressBean;

import java.util.List;

public class DriverSelectBean {

    private String username;
    private String password;
    private String nickname;
    private String mobile;
    private UserCarBean car;
    private boolean canTakeTransportOrder;
    private String id;
    private String userId;
    private String creation;
    private List<OrdersBean> orders;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserCarBean getCar() {
        return car;
    }

    public void setCar(UserCarBean car) {
        this.car = car;
    }

    public boolean isCanTakeTransportOrder() {
        return canTakeTransportOrder;
    }

    public void setCanTakeTransportOrder(boolean canTakeTransportOrder) {
        this.canTakeTransportOrder = canTakeTransportOrder;
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

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        private long driverId;
        private long employeeId;
        private AddressBean origin;
        private AddressBean destination;
        private int distance;
        private StartDateBean startDate;
        private CarModelBean carModel;
        private ContactsBean originContacts;
        private ContactsBean destinationContacts;
        private boolean checkCarPay;
        private boolean paid;
        private int deposit;
        private int price;
        private String timeOfArrival;
        private boolean bigCar;
        private String truckRequire;
        private String status;
        private boolean cancellation;
        private String cancellationNote;
        private String assessType;
        private String takeDate;
        private String completedDate;
        private int increasePrice;
        private String increasePriceDate;
        private String note;
        private String id;
        private long userId;
        private String number;
        private String creation;
        private int customServiceId;

        public long getDriverId() {
            return driverId;
        }

        public void setDriverId(long driverId) {
            this.driverId = driverId;
        }

        public long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(long employeeId) {
            this.employeeId = employeeId;
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

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
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

        public boolean isCheckCarPay() {
            return checkCarPay;
        }

        public void setCheckCarPay(boolean checkCarPay) {
            this.checkCarPay = checkCarPay;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public int getDeposit() {
            return deposit;
        }

        public void setDeposit(int deposit) {
            this.deposit = deposit;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getTimeOfArrival() {
            return timeOfArrival;
        }

        public void setTimeOfArrival(String timeOfArrival) {
            this.timeOfArrival = timeOfArrival;
        }

        public boolean isBigCar() {
            return bigCar;
        }

        public void setBigCar(boolean bigCar) {
            this.bigCar = bigCar;
        }

        public String getTruckRequire() {
            return truckRequire;
        }

        public void setTruckRequire(String truckRequire) {
            this.truckRequire = truckRequire;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isCancellation() {
            return cancellation;
        }

        public void setCancellation(boolean cancellation) {
            this.cancellation = cancellation;
        }

        public String getCancellationNote() {
            return cancellationNote;
        }

        public void setCancellationNote(String cancellationNote) {
            this.cancellationNote = cancellationNote;
        }

        public String getAssessType() {
            return assessType;
        }

        public void setAssessType(String assessType) {
            this.assessType = assessType;
        }

        public String getTakeDate() {
            return takeDate;
        }

        public void setTakeDate(String takeDate) {
            this.takeDate = takeDate;
        }

        public String getCompletedDate() {
            return completedDate;
        }

        public void setCompletedDate(String completedDate) {
            this.completedDate = completedDate;
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

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCreation() {
            return creation;
        }

        public void setCreation(String creation) {
            this.creation = creation;
        }

        public int getCustomServiceId() {
            return customServiceId;
        }

        public void setCustomServiceId(int customServiceId) {
            this.customServiceId = customServiceId;
        }

        public static class StartDateBean {
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

        public static class CarModelBean {
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


    }
}
