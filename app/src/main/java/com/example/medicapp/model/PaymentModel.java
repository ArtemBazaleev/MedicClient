package com.example.medicapp.model;

public class PaymentModel {
    public static final int TYPE_TWENTY_MESSAGES = 0;
    public static final int TYPE_FOURTY_MESSAGES = 1;
    public static final int TYPE_SIXTY_MESSAGES = 2;
    private int type;
    private String price;
    private String descr;
    private String mSkuId;


    public PaymentModel(int type) {
        this.type = type;
        switch (type){
            case TYPE_TWENTY_MESSAGES:
                price = "100,00 \u20BD";
                descr  = "Функция позволяет отправить в чате 20 сообщений";
                mSkuId = "type_twenty_messages";
                break;
            case TYPE_FOURTY_MESSAGES:
                price = "200,00 \u20BD";
                descr  = "Функция позволяет отправить в чате 40 сообщений";
                mSkuId = "type_fourty_messages";
                break;
            case TYPE_SIXTY_MESSAGES:
                price = "300,00 \u20BD";
                descr  = "Функция позволяет отправить в чате 60 сообщений";
                mSkuId = "type_sixty_messages";
                break;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getmSkuId() {
        return mSkuId;
    }

    public void setmSkuId(String mSkuId) {
        this.mSkuId = mSkuId;
    }
}
