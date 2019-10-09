package com.oymj.restaurantorderapp.customerfragment.listviewadapter;

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
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.editorderedfood;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.util.ArrayList;

import communicateserverthread.customercommunicate.deletefoodordered;

public class orderconfirmlistviewadapter extends BaseAdapter implements View.OnClickListener
{

    Context context;
    ArrayList<foodcartinformation> food;
    com.oymj.restaurantorderapp.customerfragment.customercartconfirmed customercartconfirmed;


    private TextView foodnametv;
    private TextView foodquantitytv;
    private ImageView foodiconiv;
    private ImageButton cancelorder;
    private ImageButton editorder;
    private TextView remark;


    private static LayoutInflater inflater=null;

    public orderconfirmlistviewadapter(customercartconfirmed customercartconfirmed, ArrayList<foodcartinformation> arrayfood) {
        // TODO Auto-generated constructor stub
        context=customercartconfirmed.getActivity();
        this.customercartconfirmed=customercartconfirmed;
        food=arrayfood;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return food.size();
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

    public class holdercart{
        TextView foodnametv;
        TextView foodquantitytv;
        ImageView foodiconiv;
        ImageButton cancelorder;
        ImageButton editorder;
        TextView remark;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        View rowView;

        rowView = inflater.inflate(R.layout.customer_cartconfirmlistview, null);
        foodnametv=(TextView) rowView.findViewById(R.id.tablefoodorderedfoodname);
        foodiconiv=(ImageView) rowView.findViewById(R.id.foodiconcartconfirm);
        foodquantitytv = (TextView) rowView.findViewById(R.id.tablefoodorderedquantity);
        cancelorder = (ImageButton) rowView.findViewById(R.id.cancelcartconfirm);
        editorder = (ImageButton) rowView.findViewById(R.id.editfoodcartconfirm);
        remark =(TextView) rowView.findViewById(R.id.tablefoodorderedremark);
        TextView remarktitle=(TextView) rowView.findViewById(R.id.remarktitle);

        Typeface insanibu =Typeface.createFromAsset(customercartconfirmed.getActivity().getAssets(),"font/insanibu.ttf");
        foodnametv.setTypeface(insanibu);

        Typeface immonolt =Typeface.createFromAsset(customercartconfirmed.getActivity().getAssets(),"font/immonolt.otf");
        remarktitle.setTypeface(immonolt);

        Typeface arial =Typeface.createFromAsset(customercartconfirmed.getActivity().getAssets(),"font/arialnarrow.ttf");
        remark.setTypeface(arial);


        foodnametv.setText(food.get(position).getFoodname());
        foodquantitytv.setText(Integer.toString(food.get(position).getQuantity()));
        remark.setText(food.get(position).getRemark());

        //set food image
        String foodname=food.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = customercartconfirmed.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", customercartconfirmed.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodiconiv.setImageResource(customercartconfirmed.getActivity().getResources().getIdentifier("smallnopic", "drawable", customercartconfirmed.getActivity().getPackageName()));
        }else
        {
            foodiconiv.setImageResource(imageid);
        }


        cancelorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deletefoodordered deletefoodordered=new deletefoodordered(customercartconfirmed,food.get(position).getFoodname());
                Thread thread=new Thread(deletefoodordered);
                thread.start();
            }
        });

        editorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction FragTrans;
                FragTrans = customercartconfirmed.getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.customermain_frame, editorderedfood.newInstance(food.get(position).getFoodname()));
                FragTrans.addToBackStack(null);
                FragTrans.commit();
            }
        }
            );


        return rowView;
    }

    @Override
    public void onClick(View v)
    {

    }
}
