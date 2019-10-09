package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.viewfeedbacklistviewadapter;
import com.oymj.restaurantorderapp.database_feedback.feedback;
import com.oymj.restaurantorderapp.database_feedback.feedbackinfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class adminviewfeedback extends Fragment
{
    public static adminviewfeedback newInstance() {
        adminviewfeedback fragment = new adminviewfeedback();

        //update current displaying fragment
        Currentfragment.currentfragment = fragment;
        Currentfragment.currentfragmentclassname = fragment.getClass().getSimpleName();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_viewfeedback, container, false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonolt = Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        TextView feedbackfromcustomerword=(TextView) rootView.findViewById(R.id.feedbackfromcustomerword);
        feedbackfromcustomerword.setTypeface(immonolt);

        //selection sort
        int n = feedback.feedbacklist.size();
        try {

            for (int i = 0; i < feedback.feedbacklist.size() - 1; i++)
            {
                int index = i;
                for (int j = i + 1; j < feedback.feedbacklist.size(); j++){
                    Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(feedback.feedbacklist.get(j).getDate());
                    Date date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(feedback.feedbacklist.get(index).getDate());
                    if (date1.after(date2)){
                        index = j;//searching for lowest index
                    }
                }
                feedbackinfo smallerdate = feedback.feedbacklist.get(index);
                feedback.feedbacklist.set(index,feedback.feedbacklist.get(i));
                feedback.feedbacklist.set(i,smallerdate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for(int x=0;x<feedback.feedbacklist.size();x++){
            System.out.println(feedback.feedbacklist.get(x).getDate());
        }



        ListView feedbacklist=(ListView) rootView.findViewById(R.id.feedbacklistview);

        viewfeedbacklistviewadapter viewfeedbacklistviewadapter=new viewfeedbacklistviewadapter(this, feedback.feedbacklist);
        feedbacklist.setAdapter(viewfeedbacklistviewadapter);

        return rootView;
    }
}
