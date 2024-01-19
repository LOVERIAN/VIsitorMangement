package com.homeone.visitormanagement.modal;

public class RequestData {
    private String visitorName;
    private String owner;
    private String status;
    private long uuid;


    public RequestData(String visitorName, String owner, String status, long uuid) {
        this.visitorName = visitorName;
        this.owner = owner;
        this.status = status;
        this.uuid = uuid;
    }
    public RequestData(){

    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
