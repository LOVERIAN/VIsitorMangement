package com.homeone.visitormanagement.modal;

public class RequestData {
    private String visitorName;
    private String owner;
    private String status;


    public RequestData(String visitorName, String owner, String status) {
        this.visitorName = visitorName;
        this.owner = owner;
        this.status = status;
    }
    public RequestData(){

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
