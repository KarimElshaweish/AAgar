package com.sourcey.materiallogindemo.model;

public class Notification {
    private String content;
    private String title;
    private String offerID;
    private boolean seen;
    private String notificaitonID;


    public Notification(String content, String title, String offerID,String notificaitonID) {
        this.content = content;
        this.title = title;
        this.offerID = offerID;
        seen=false;
        this.notificaitonID=notificaitonID;
    }

    public Notification() {
    }
    public boolean isSeen() {
        return seen;
    }

    public String getNotificaitonID() {
        return notificaitonID;
    }

    public void setNotificaitonID(String notificaitonID) {
        this.notificaitonID = notificaitonID;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }
}
