package com.oymj.restaurantorderapp.database_feedback;


public class feedbackinfo
{
    String username;
    String date;
    int starcount;
    String comment;

    public feedbackinfo(String username,String date,int starcount,String comment)
    {
        this.username=username;
        this.date=date;
        this.starcount=starcount;
        this.comment=comment;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public int getStarcount() {
        return starcount;
    }

    public String getComment() {
        return comment;
    }
}
