package com.liansheng.carworld.bean.child;

import com.liansheng.carworld.bean.UserCarBean;
import com.liansheng.carworld.bean.me.CompanyBean;

public class ChildInfoBean {

    private String id;
    private String score;
    private CompanyBean company;
    private int pendingOrderCounts;
    private int processingOrderCounts;
    private int completedOrderCounts;
    private int receivedOrderCounts;
    private int waitAssessOrderCounts;
    private String username;
    private String password;
    private String mobile;
    private UserCarBean car;
    private boolean canTakeTransportOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public int getPendingOrderCounts() {
        return pendingOrderCounts;
    }

    public void setPendingOrderCounts(int pendingOrderCounts) {
        this.pendingOrderCounts = pendingOrderCounts;
    }

    public int getProcessingOrderCounts() {
        return processingOrderCounts;
    }

    public void setProcessingOrderCounts(int processingOrderCounts) {
        this.processingOrderCounts = processingOrderCounts;
    }

    public int getCompletedOrderCounts() {
        return completedOrderCounts;
    }

    public void setCompletedOrderCounts(int completedOrderCounts) {
        this.completedOrderCounts = completedOrderCounts;
    }

    public int getReceivedOrderCounts() {
        return receivedOrderCounts;
    }

    public void setReceivedOrderCounts(int receivedOrderCounts) {
        this.receivedOrderCounts = receivedOrderCounts;
    }

    public int getWaitAssessOrderCounts() {
        return waitAssessOrderCounts;
    }

    public void setWaitAssessOrderCounts(int waitAssessOrderCounts) {
        this.waitAssessOrderCounts = waitAssessOrderCounts;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
