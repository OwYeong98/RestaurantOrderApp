package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Canvas;
import android.graphics.Paint;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.salesreportmonthspinneradapter;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.salesreportyearspinneradapter;
import com.oymj.restaurantorderapp.database_orderhistory.foodorderhistory;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 10/24/2017.
 */

public class adminfoodsalesreport extends Fragment implements AdapterView.OnItemSelectedListener {
    Random random = new Random();

    BarChart barchart;
    Spinner yearspinner;
    Spinner monthspinner;
    ArrayList<String> year=new ArrayList<String>();
    ArrayList<String > month=new ArrayList<String>();
    ArrayList<graphviewbarchartdatastore> barchartdata= new ArrayList<graphviewbarchartdatastore>();

    public static adminfoodsalesreport newInstance() {
        adminfoodsalesreport fragment = new adminfoodsalesreport();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_foodsalesreport, container, false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        yearspinner = (Spinner) rootView.findViewById(R.id.foodsalereportyearspinner);

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






        monthspinner = (Spinner) rootView.findViewById(R.id.foodsalereportmonthspinner);


        barchart = (BarChart) rootView.findViewById(R.id.foodsalesreportgraph);










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

    public BarData getfoodsalesdatapoint(int year,int month) {
        //clear old data
        barchartdata.clear();

        //make every food have its class to store totalsold
        for(int loop=0;loop< foodmenu.foodlist.size();loop++)
        {
            //make their bar draw at even number x axis in the bar chart
            barchartdata.add(new graphviewbarchartdatastore(loop,foodmenu.foodlist.get(loop).getFoodname()));
        }


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
                    //get list of food ordered in the order
                    ArrayList<foodorderhistory> foodordered=orderhistoryrecord.orderhistory.get(loop).getFoodordered();

                    //loop all the list of food
                    for (int foodloop=0;foodloop<foodordered.size();foodloop++)
                    {
                        //find where the foodname data store at which index
                        for (int findfoodindex=0;findfoodindex<barchartdata.size();findfoodindex++)
                        {
                            //if found add the food quantity to the data
                            if(barchartdata.get(findfoodindex).getFoodname().equals(foodordered.get(foodloop).getFoodname()))
                            {
                                barchartdata.get(findfoodindex).addTotalsold(foodordered.get(foodloop).getQuantity());
                            }
                        }
                    }

                }
            }catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        ArrayList<BarDataSet> bardataSets = null;

        ArrayList<BarEntry> food = new ArrayList<>();

        for(int loop=0;loop<barchartdata.size();loop++)
        {
            BarEntry data = new BarEntry(barchartdata.get(loop).getStoredat(), barchartdata.get(loop).getTotalsold());
            food.add(data);
        }
        BarDataSet barDataSet1 = new BarDataSet(food, "foods");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);


        BarData foodsolddata=new BarData(barDataSet1);
        foodsolddata.setBarWidth(0.5f);



        return foodsolddata;

    }

    private ArrayList<String> getXAxisTitle() {
        ArrayList<String> title = new ArrayList<>();

        for(int outerloop=0;outerloop<barchartdata.size();outerloop++)
        {
            for(int loop=0;loop<barchartdata.size();loop++)
            {
                if(barchartdata.get(loop).getStoredat()==outerloop)
                {
                    title.add(barchartdata.get(loop).getFoodname());
                }
            }
        }

        for(int loop=0;loop<title.size();loop++)
        {
            if(title.get(loop).length()>10)
            {
                String[] splitspace=title.get(loop).split(" ");

                if(splitspace.length==2)
                {
                    title.set(loop,splitspace[0]+"\n"+splitspace[1]);
                }else
                {
                    String result="";
                    for (int combineloop=0;combineloop<splitspace.length;combineloop++)
                    {
                        if (combineloop==0)
                        {
                            result=splitspace[0];
                        }else
                        {
                            result+="\n"+splitspace[combineloop];
                        }
                    }
                    title.set(loop,result);
                }
            }
        }

        return title;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        Spinner spinner = (Spinner) parent;

        if(spinner.getId()== R.id.foodsalereportyearspinner)
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
        }else if(spinner.getId()==R.id.foodsalereportmonthspinner)
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

            barchart.setDrawBarShadow(false);
            barchart.setDrawValueAboveBar(true);
            barchart.setDrawGridBackground(true);
            barchart.setPinchZoom(false);
            barchart.setData(getfoodsalesdatapoint(yearreport,monthreport));




            XAxis xaxis=barchart.getXAxis();
            xaxis.setValueFormatter(new MyXAxisValueFormatter(getXAxisTitle()));
            xaxis.setGranularity(1f);
            xaxis.setTextSize(2f);


            barchart.setXAxisRenderer(new CustomXAxisRenderer(barchart.getViewPortHandler(), barchart.getXAxis(), barchart.getTransformer(YAxis.AxisDependency.LEFT)));
            barchart.notifyDataSetChanged();


            barchart.moveViewTo(getfoodsalesdatapoint(yearreport,monthreport).getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);


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

class MyXAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<String> foodname;

    public MyXAxisValueFormatter(ArrayList<String> foodname) {

        this.foodname=foodname;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return foodname.get((int) value);
    }

}

class CustomXAxisRenderer extends XAxisRenderer {
    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        String line[] = formattedLabel.split("\n");
        if(line.length>1)
        {
            for (int loop=0;loop<line.length;loop++)
            {
                Utils.drawXAxisValue(c, line[loop], x, y+mAxisLabelPaint.getTextSize()*(loop+1), mAxisLabelPaint, anchor, angleDegrees);
            }
            //Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
            //Utils.drawXAxisValue(c, line[1], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
            //Utils.drawXAxisValue(c, line[2], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);

        }else
        {
            Utils.drawXAxisValue(c, line[0], x, y+35, mAxisLabelPaint, anchor, angleDegrees);

        }

    }
}
