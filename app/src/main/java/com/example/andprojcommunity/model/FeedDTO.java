package com.example.andprojcommunity.model;

import java.io.Serializable;

public class FeedDTO implements Serializable {
    private int no;
    private String userID;
    private String title;
    private String mainText;
    private int feedType;
    private String date;

    public FeedDTO(){
        this(0,null,null,null,0,null);
    }

    public FeedDTO(int no, String userID, String title, String mainText, int cName, String date){
        this.no = no;
        this.userID = userID;
        this.title = title;
        this.mainText = mainText;
        this.feedType = cName;
        this.date = date;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public int getcName() {
        return feedType;
    }

    public void setcName(int cName) {
        this.feedType = cName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    @Override
    public String toString() {
        return "CommunityItemDTO{" +
                "no=" + no +
                ", userID='" + userID + '\'' +
                ", title='" + title + '\'' +
                ", mainText='" + mainText + '\'' +
                ", cName=" + feedType +
                ", date='" + date + '\'' +
                '}';
    }
//
//    @Override
//    public int compareTo(FeedDTO feed) {
//        if(feed.no > no){
//            return -1;
//        }else if(feed.no < no){
//            return 1;
//        }
//        return 0;
//    }
}
