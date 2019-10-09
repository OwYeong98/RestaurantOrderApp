package com.oymj.restaurantorderapp.customerfragment;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.listviewadapter.foodlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;
import communicateserverthread.customercommunicate.sentfeedback;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class customerfeedback extends Fragment implements View.OnClickListener
{
    private int starcount=0;
    ImageButton star1;
    ImageButton star2;
    ImageButton star3;
    ImageButton star4;
    ImageButton star5;

    boolean validcomment=false;
    EditText comment;


    public static customerfeedback newInstance() {
        customerfeedback fragment = new customerfeedback();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_feedback,container,false);

        //the star in feedback page
        star1=(ImageButton) rootView.findViewById(R.id.feedbackstar1);
        star2=(ImageButton) rootView.findViewById(R.id.feedbackstar2);
        star3=(ImageButton) rootView.findViewById(R.id.feedbackstar3);
        star4=(ImageButton) rootView.findViewById(R.id.feedbackstar4);
        star5=(ImageButton) rootView.findViewById(R.id.feedbackstar5);

        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);

        ImageButton nexttime=(ImageButton) rootView.findViewById(R.id.nexttimethanksbutton);
        ImageButton submitfeedback=(ImageButton) rootView.findViewById(R.id.submitfeedbackbutton);

        nexttime.setOnClickListener(this);
        submitfeedback.setOnClickListener(this);

        comment=(EditText) rootView.findViewById(R.id.feedbackcomment);

        comment.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {

            }


            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                String input=comment.getText().toString();

                if(input.length()>100)
                {
                    validcomment=false;
                    comment.setError("Your remark should not be too long");
                }else
                {
                    validcomment=true;
                }

            }

        });



        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.nexttimethanksbutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.customermain_frame, customerhomepage.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();
                break;
            case R.id.submitfeedbackbutton:
                if(validcomment==true)
                {
                    sentfeedback sentfeedback=new sentfeedback(this,starcount,comment.getText().toString());
                    Thread thread= new Thread(sentfeedback);
                    thread.start();
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Please make sure all information with error is corrected!!");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton(
                            "Ok!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert = dialog.create();
                    alert.show();
                }
                break;
            case R.id.feedbackstar1:
                setstarcount(1);
                break;
            case R.id.feedbackstar2:
                setstarcount(2);
                break;
            case R.id.feedbackstar3:
                setstarcount(3);
                break;
            case R.id.feedbackstar4:
                setstarcount(4);
                break;
            case R.id.feedbackstar5:
                setstarcount(5);
                break;
        }

    }

    private void setstarcount(int starcount)
    {
        this.starcount=starcount;

        for(int loop=1;loop<=5;loop++)
        {
            ImageButton star=null;
            switch (loop)
            {
                case 1:
                    star=(ImageButton) getActivity().findViewById(R.id.feedbackstar1);
                    break;
                case 2:
                    star=(ImageButton) getActivity().findViewById(R.id.feedbackstar2);
                    break;
                case 3:
                    star=(ImageButton) getActivity().findViewById(R.id.feedbackstar3);
                    break;
                case 4:
                    star=(ImageButton) getActivity().findViewById(R.id.feedbackstar4);
                    break;
                case 5:
                    star=(ImageButton) getActivity().findViewById(R.id.feedbackstar5);
                    break;
            }

            if(loop<=starcount)
            {
                star.setImageResource(R.drawable.ic_star);
            }else
            {
                star.setImageResource(R.drawable.ic_emptystar);
            }

        }
    }

}
