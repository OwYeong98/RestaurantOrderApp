package com.oymj.restaurantorderapp.adminfragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfooddetail;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.adminfragment.adminviewfeedback;
import com.oymj.restaurantorderapp.database_feedback.feedbackinfo;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;

import java.util.ArrayList;

import communicateserverthread.admincommunicate.deletefoodmenu;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class viewfeedbacklistviewadapter extends BaseAdapter
{
    private static LayoutInflater inflater=null;
    Context context;
    ArrayList<feedbackinfo> feedbacklist;
    adminviewfeedback adminviewfeedback;

    public viewfeedbacklistviewadapter(adminviewfeedback adminviewfeedback,ArrayList<feedbackinfo> feedbacklist) {
        // TODO Auto-generated constructor stub
        context=adminviewfeedback.getActivity();
        this.adminviewfeedback=adminviewfeedback;
        this.feedbacklist=feedbacklist;


        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return feedbacklist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.admin_viewfeedbacklistview, null);

        Typeface insanibu =Typeface.createFromAsset(adminviewfeedback.getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonolt =Typeface.createFromAsset(adminviewfeedback.getActivity().getAssets(),"font/immonolt.otf");



        TextView date=(TextView) rowView.findViewById(R.id.feedbacklistviewdate);
        TextView username=(TextView) rowView.findViewById(R.id.feedbacklistviewusername);
        TextView commenttitle=(TextView) rowView.findViewById(R.id.feedbacklistviewcommenttitle);
        TextView comment=(TextView) rowView.findViewById(R.id.feedbacklistviewcomment);

        date.setTypeface(immonolt);
        username.setTypeface(insanibu);
        comment.setTypeface(immonolt);
        commenttitle.setTypeface(immonolt);

        date.setText(feedbacklist.get(position).getDate());
        username.setText(feedbacklist.get(position).getUsername());
        comment.setText(feedbacklist.get(position).getComment());


        for (int loop=1;loop<=5;loop++)
        {
            ImageView star=null;
            switch (loop)
            {
                case 1:
                    star=(ImageView) rowView.findViewById(R.id.feedbacklistviewstar1);
                    break;
                case 2:
                    star=(ImageView) rowView.findViewById(R.id.feedbacklistviewstar2);
                    break;
                case 3:
                    star=(ImageView) rowView.findViewById(R.id.feedbacklistviewstar3);
                    break;
                case 4:
                    star=(ImageView) rowView.findViewById(R.id.feedbacklistviewstar4);
                    break;
                case 5:
                    star=(ImageView) rowView.findViewById(R.id.feedbacklistviewstar5);
                    break;
            }

            if (loop<=feedbacklist.get(position).getStarcount())
            {
                star.setImageResource(R.drawable.ic_star);
            }else
            {
                star.setImageResource(R.drawable.ic_emptystar);
            }
        }




        return rowView;
    }


}
