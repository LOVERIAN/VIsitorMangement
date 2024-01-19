package com.homeone.visitormanagement.modal;

public class PromotionData {
    private String img_link;
    private String promotion_link;

    public PromotionData(String img_link, String promotion_link) {
        this.img_link = img_link;
        this.promotion_link = promotion_link;
    }

    public PromotionData() {
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getPromotion_link() {
        return promotion_link;
    }

    public void setPromotion_link(String promotion_link) {
        this.promotion_link = promotion_link;
    }
}
