package com.homeone.visitormanagement.modal;

public class RequestOwnerData {
    private String status;
    public String visitorName;
    private String purpose;
    private String image;
    private String owner;
    private Long uuid;
    private String id;



    public RequestOwnerData(Long uuid, String status, String visitorName, String purpose, String image, String owner) {
        this.status = status;
        this.visitorName = visitorName;
        this.purpose = purpose;
        this.image = image;
        this.owner = owner;
        this.uuid = uuid;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public RequestOwnerData() {}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getVisitor() {
        return visitorName;
    }

    public void setVisitor(String visitor) {
        this.visitorName = visitor;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
