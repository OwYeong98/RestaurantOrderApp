package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.salesreportmonthspinneradapter;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.salesreportyearspinneradapter;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 10/16/2017.
 */

public class adminsalesreport extends Fragment implements AdapterView.OnItemSelectedListener {
    GraphView graphView;
    Spinner yearspinner;
    Spinner monthspinner;
    ArrayList<String> year=new ArrayList<String>();
    ArrayList<String > month=new ArrayList<String>();

    public static adminsalesreport newInstance() {
        adminsalesreport fragment = new adminsalesreport();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_salesreport, container, false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        yearspinner = (Spinner) rootView.findViewById(R.id.salereportyearspinner);

        //count all the year that recorded in orderhistory
        Calendar cal = Calendar.getInstance();
        //add all year from first year to today year to arraylist
        for (int loop=countfirstyear();loop<=cal.get(Calendar.YEAR);loop++)
        {
            year.add(Integer.toString(loop));
        }
        year=bubblesortarraylist(year);

        salesreportyearspinneradapter salesreportyearspinneradapter= new salesreportyearspinneradapter(this,year);
        yearspinner.setAdapter(salesreportyearspinneradapter);
        yearspinner.setSelection(0);
        yearspinner.setOnItemSelectedListener(this);






        monthspinner = (Spinner) rootView.findViewById(R.id.salereportmonthspinner);


        graphView = (GraphView) rootView.findViewById(R.id.salesreportgraph);










        return rootView;
    }

    private int countfirstyear() {
        int firstyear=0000;
        ArrayList<Integer> yearlist = new ArrayList<Integer>();

        //check all order history year and record all year that appear
        for (int loop = 0; loop < orderhistoryrecord.orderhistory.size(); loop++) {

            try {

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(orderhistoryrecord.orderhistory.get(loop).getDate());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                boolean alreadyavailable = false;

                int year=cal.get(Calendar.YEAR);
                for (int yearloop = 0; yearloop < yearlist.size(); yearloop++) {
                    if (yearlist.get(yearloop)==year) {
                        alreadyavailable = true;
                    }
                }

                //if havent record before
                if (alreadyavailable == false) {
                    //add the years to arraylist
                    yearlist.add(year);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //check when is the first year start if there are at least 1 year
        if(yearlist.size()>0)
        {
            firstyear=yearlist.get(0);

            for(int loop=0;loop<yearlist.size();loop++)
            {
                if(yearlist.get(loop)<firstyear)
                {
                    firstyear=yearlist.get(loop);
                }
            }

        }

        return firstyear;
    }

    private int countmonthoffirstyear(int firstyear) {
        ArrayList<Integer> firstyearmonthlist= new ArrayList<Integer>();

        //check all order history of the firstyear and record month of first year
        for (int loop = 0; loop < orderhistoryrecord.orderhistory.size(); loop++) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(orderhistoryrecord.orderhistory.get(loop).getDate());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);


                if(cal.get(Calendar.YEAR)==firstyear)
                {
                    int month=cal.get(Calendar.MONTH);
                    boolean monthalreadyavailable=false;

                    for (int monthcheckloop=0;monthcheckloop>firstyearmonthlist.size();monthcheckloop++)
                    {

                        if(firstyearmonthlist.get(monthcheckloop)==month)
                        {
                            monthalreadyavailable=true;
                        }
                    }

                    if (monthalreadyavailable==false)
                    {
                        firstyearmonthlist.add(month);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int firstmonthoffirstyear=0;
        if(firstyearmonthlist.size()>0)
        {
            firstmonthoffirstyear=firstyearmonthlist.get(0);
            //check first month from the record
            for (int loop=0;loop<firstyearmonthlist.size();loop++)
            {
                if(firstyearmonthlist.get(loop)<firstmonthoffirstyear)
                {
                    firstmonthoffirstyear=firstyearmonthlist.get(loop);
                }

            }
        }

        //month is 0 indexed
        return firstmonthoffirstyear+1;
    }





    public DataPoint[] getsalesdatapoint(int year,int month) {
        int[] monthday={0,31,28,31,30,31,30,31,31,30,31,30,31};
        boolean leapyear= false;


        //if (year is not divisible by 4) then (it is a common year)
        //else if (year is not divisible by 100) then (it is a leap year)
        //else if (year is not divisible by 400) then (it is a common year)
        //else (it is a leap year)
        if((year%4==0) && (year%100!=0) || (year%400==0))
        {
            leapyear=true;
        }

        if(leapyear==true)
        {
            //if is leap year then february has 29 day
            monthday[2]=29;
        }else
        {
            //if not leap year then february has 28 day
            monthday[2]=28;
        }

        int daycount=monthday[month];

        double[] salesofday= new double[daycount+1];//store all the sales in each index for example index 0 store day 1 sales

        //calculate all the sales from sales history
        for(int loop=0;loop<orderhistoryrecord.orderhistory.size();loop++)
        {
            try{
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(orderhistoryrecord.orderhistory.get(loop).getDate());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                //Calendar.MONTH is 0 index based
                if (cal.get(Calendar.YEAR)==year && cal.get(Calendar.MONTH)+1==month)
                {
                    salesofday[cal.get(Calendar.DAY_OF_MONTH)]+=orderhistoryrecord.orderhistory.get(loop).getTotalpayment();
                }
            }catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        Toast.makeText(getActivity(), "Position sales of day 6: "+salesofday[5]+ "Position sales of day 7: "+salesofday[6], Toast.LENGTH_LONG).show();

        DataPoint[] sales = new DataPoint[daycount+1];


        //store all data into datapoint which is use for graph view
        for (int loop=0;loop<daycount+1;loop++)
        {

            int day=loop;
            double salesfortheday =salesofday[loop];

            DataPoint data= new DataPoint(day,salesfortheday);

            sales[loop] = data;
        }

        return sales;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        Spinner spinner = (Spinner) parent;

        if(spinner.getId()== R.id.salereportyearspinner)
        {
            month.clear();
            if(position==year.size()-1)
            {
                Toast.makeText(getActivity(), "Last year", Toast.LENGTH_LONG).show();
                Calendar cal=Calendar.getInstance();
                String[] monthalpha={null,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

                for (int loop=1;loop<=cal.get(Calendar.MONTH)+1;loop++)
                {
                    month.add(monthalpha[loop]);
                }

                salesreportmonthspinneradapter salesreportmonthspinneradapter=new salesreportmonthspinneradapter(this,month);
                monthspinner.setAdapter(salesreportmonthspinneradapter);

                monthspinner.setSelection(0);
                monthspinner.setOnItemSelectedListener(this);


            }else if(position==0)
            {
                Toast.makeText(getActivity(), "First year", Toast.LENGTH_LONG).show();
                //if it is the first year then we count the first year start month
                int firstmonth=countmonthoffirstyear(Integer.parseInt(year.get(0)));

                String[] monthalpha={null,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

                for (int loop=firstmonth;loop<=12;loop++)
                {
                    month.add(monthalpha[loop]);
                }

                salesreportmonthspinneradapter salesreportmonthspinneradapter=new salesreportmonthspinneradapter(this,month);
                monthspinner.setAdapter(salesreportmonthspinneradapter);

                monthspinner.setSelection(0);
                monthspinner.setOnItemSelectedListener(this);
            }else
            {
                Toast.makeText(getActivity(), "Middle year", Toast.LENGTH_LONG).show();
                String[] monthalpha={null,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

                for (int loop=1;loop<=12;loop++)
                {
                    month.add(monthalpha[loop]);
                }

                salesreportmonthspinneradapter salesreportmonthspinneradapter=new salesreportmonthspinneradapter(this,month);
                monthspinner.setAdapter(salesreportmonthspinneradapter);


                monthspinner.setSelection(0);
                monthspinner.setOnItemSelectedListener(this);
            }
        }else if(spinner.getId()==R.id.salereportmonthspinner)
        {
            int yearreport;
            int monthreport=0;

            yearreport=Integer.parseInt(year.get(yearspinner.getSelectedItemPosition()));

            //convert back from alphabet month to integer
            switch (month.get(position))
            {
                case "Jan":
                    monthreport=1;
                    break;
                case "Feb":
                    monthreport=2;
                    break;
                case "Mar":
                    monthreport=3;
                    break;
                case "Apr":
                    monthreport=4;
                    break;
                case "May":
                    monthreport=5;
                    break;
                case "Jun":
                    monthreport=6;
                    break;
                case "Jul":
                    monthreport=7;
                    break;
                case "Aug":
                    monthreport=8;
                    break;
                case "Sep":
                    monthreport=9;
                    break;
                case "Oct":
                    monthreport=10;
                    break;
                case "Nov":
                    monthreport=11;
                    break;
                case "Dec":
                    monthreport=12;
                    break;
            }
            Toast.makeText(getActivity(), "Year position: "+yearreport+ "Month: "+monthreport, Toast.LENGTH_LONG).show();

            graphView.removeAllSeries();

            LineGraphSeries<DataPoint> sales = new LineGraphSeries<DataPoint>(getsalesdatapoint(yearreport,monthreport));
            sales.setDrawDataPoints(true);
            sales.setDataPointsRadius(10);

            graphView.getViewport().setScalableY(true);
            graphView.getViewport().setScrollableY(true);

            graphView.addSeries(sales);

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value,boolean isValueX)
                {
                    //value store the x axis value
                    //is valueX indicate that the value is for X-axis if it is true
                    if(isValueX==true)
                    {
                        return super.formatLabel(value,isValueX);
                    }else
                    {
                        return "RM "+super.formatLabel(value,isValueX);
                    }

                }
            });


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }



    private ArrayList<String> bubblesortarraylist(ArrayList<String> arraylist) {
        int size = arraylist.size();
        String temp = null;

        for (int loop = 0; loop < size; loop++) {
            for (int innerloop = 1; innerloop < (size - loop); innerloop++) {
                if (Integer.parseInt(arraylist.get(innerloop - 1)) > Integer.parseInt(arraylist.get(innerloop))) {
                    //swap the index
                    temp = arraylist.get(innerloop - 1);
                    arraylist.set(innerloop - 1, arraylist.get(innerloop));
                    arraylist.set(innerloop, temp);
                }

            }
        }

        return  arraylist;
    }


}
