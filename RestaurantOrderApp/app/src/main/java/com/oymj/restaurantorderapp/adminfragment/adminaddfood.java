package com.oymj.restaurantorderapp.adminfragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;
import communicateserverthread.admincommunicate.addfoodmenu;
import communicateserverthread.admincommunicate.editfoodmenu;

/**
 * Created by OwYeong on 11/9/2017.
 */

public class adminaddfood extends Fragment implements View.OnClickListener
{
    boolean validdesc=false;
    boolean validprice=false;
    boolean validcontainmeat=false;
    boolean validspicy=false;
    boolean validsour=false;
    boolean validsweetness=false;
    boolean validsalty=false;
    boolean validchefsuggest=false;
    boolean validpreparetime=false;
    boolean validfoodname=false;

    EditText foodname;
    EditText fooddesc;
    EditText foodprice;
    EditText containmeat;
    EditText foodspicy;
    EditText foodsalty;
    EditText foodsweetness;
    EditText foodsour;
    EditText preparetime;
    EditText chefsuggest;




    public static adminaddfood newInstance() {

        adminaddfood fragment = new adminaddfood();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.admin_addfood,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);



        foodname=(EditText) rootView.findViewById(R.id.addfoodfoodname);
        fooddesc=(EditText) rootView.findViewById(R.id.addfooddesc);
        foodprice=(EditText) rootView.findViewById(R.id.addfoodprice);
        containmeat=(EditText) rootView.findViewById(R.id.addfoodcontainmeat);
        foodspicy=(EditText) rootView.findViewById(R.id.addfoodspicy);
        foodsalty=(EditText) rootView.findViewById(R.id.addfoodsalty);
        foodsweetness=(EditText) rootView.findViewById(R.id.addfoodsweetness);
        foodsour=(EditText) rootView.findViewById(R.id.addfoodsour);
        preparetime=(EditText) rootView.findViewById(R.id.addfoodpreparetime);
        chefsuggest=(EditText) rootView.findViewById(R.id.addfoodchefsuggest);

        ImageButton sentedit=(ImageButton) rootView.findViewById(R.id.addfoodconfirmadd);
        sentedit.setOnClickListener(this);



        foodname.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodname.getText().toString();

                boolean containsymbol=input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                        ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                        ||input.contains("#")||input.contains("@")||input.contains("!")||input.contains("`")||input.contains("~")
                        ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains(".")||input.contains("?")
                        ||input.contains("{")||input.contains("}")||input.contains("[")||input.contains("]")||input.contains("|");

                boolean containnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                        ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");

                if(input.equals(""))
                {
                    input=null;
                }

                if(input!=null)
                {
                    if(input.length()>30||input.length()<3)
                    {
                        foodname.setError("food name should have at least 3 char and at most 30 char!");
                        validfoodname = false;
                    }else
                    {
                        if(containnumber||containsymbol)
                        {
                            foodname.setError("food name should contain symbol or number");
                            validfoodname = false;
                        }else
                        {
                            validfoodname=true;
                        }
                    }

                }else {
                    foodname.setError("food name should not be null");
                    validfoodname = false;
                }
            }
        });

        fooddesc.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=fooddesc.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }

                if(input!=null)
                {
                    if(input.length()<20||input.length()>160)
                    {
                        validdesc=false;
                        fooddesc.setError("description should have at least 20 char and at most 160 char");
                    }else
                    {
                        validdesc=true;
                    }
                }else
                {
                    fooddesc.setError("description should not be empty!!");
                    validdesc=false;
                }

            }
        });

        foodprice.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodprice.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }

                if(input!=null)
                {
                    double doubleinput=Double.parseDouble(input);
                    if(doubleinput<0)
                    {
                        validprice=false;
                        fooddesc.setError("price should be more that RM0");
                    }else
                    {
                        validprice=true;
                    }
                }else {
                    foodprice.setError("price should not be null");
                    validprice = false;
                }
            }
        });

        containmeat.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=containmeat.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }

                if(input!=null)
                {
                    if(input.length() !=1)
                    {
                        validcontainmeat=false;
                        containmeat.setError("You should only key 1 char!(Y or N)");
                    }else
                    {
                        if(input.equals("Y") || input.equals("N"))
                        {
                            validcontainmeat=true;
                        }else
                        {
                            containmeat.setError("You should only key in Y or N!!");
                            validcontainmeat=false;
                        }

                    }
                }else
                {
                    containmeat.setError("It shoud not be null");
                    validcontainmeat=false;
                }

            }
        });

        foodspicy.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodspicy.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }

                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<0 || intinput>3)
                    {
                        validspicy=false;
                        foodspicy.setError("You should only enter 0 to 3");
                    }else
                    {
                        validspicy=true;
                    }

                }else
                {
                    foodspicy.setError("Food spicy should not be null");
                    validspicy=false;
                }
            }
        });

        foodsalty.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodsalty.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }


                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<0 || intinput>3)
                    {
                        validsalty=false;
                        foodsalty.setError("You should only enter 0 to 3");
                    }else
                    {
                        validsalty=true;
                    }
                }else
                {
                    foodsalty.setError("Food salty should not be null");
                    validsalty=false;
                }



            }
        });

        foodsour.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodsour.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }



                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<0 || intinput>3)
                    {
                        validsour=false;
                        foodsour.setError("You should only enter 0 to 3");
                    }else
                    {
                        validsour=true;
                    }
                }else
                {
                    foodsour.setError("Food sour should not be null");
                    validsour=false;
                }

            }
        });

        foodsweetness.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=foodsweetness.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }


                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<0 || intinput>3)
                    {
                        validsweetness=false;
                        foodsweetness.setError("You should only enter 0 to 3");
                    }else
                    {
                        validsweetness=true;
                    }
                }else
                {
                    foodsweetness.setError("Food sweetness should not be null");
                    validsweetness=false;
                }




            }
        });

        preparetime.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=preparetime.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }



                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<1)
                    {
                        validpreparetime=false;
                        preparetime.setError("prepare time should have at least 1 minute");
                    }else
                    {
                        validpreparetime=true;
                    }
                }else
                {
                    preparetime.setError("Food prepare time should not be null");
                    validpreparetime=false;
                }


            }
        });

        chefsuggest.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=chefsuggest.getText().toString();

                if(input.equals(""))
                {
                    input=null;
                }


                if(input!=null)
                {
                    int intinput=Integer.parseInt(input);
                    if(intinput<0 || intinput>5)
                    {
                        validchefsuggest=false;
                        chefsuggest.setError("You should only enter 0 to 5");
                    }else
                    {
                        validchefsuggest=true;
                    }
                }else
                {
                    chefsuggest.setError("Chef suggest should not be null");
                    validchefsuggest=false;
                }



            }
        });



        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.addfoodconfirmadd:
                if(validfoodname&&validdesc&&validprice&&validcontainmeat&&validspicy&&validsalty&&validsweetness&&validsour&&validchefsuggest&&validpreparetime)
                {
                    //if all valid true
                    String foodnames=foodname.getText().toString();
                    String addfooddesc=fooddesc.getText().toString();
                    double addfoodprice=Double.parseDouble(foodprice.getText().toString());

                    int addcontainmeat;
                    if(containmeat.getText().toString().contains("Y"))
                    {
                        addcontainmeat=1;
                    }else
                    {
                        addcontainmeat=0;
                    }

                    int addfoodspicy=Integer.parseInt(foodspicy.getText().toString());
                    int addfoodsalty=Integer.parseInt(foodsalty.getText().toString());
                    int addfoodsweetness=Integer.parseInt(foodsweetness.getText().toString());
                    int addfoodsour=Integer.parseInt(foodsour.getText().toString());
                    int addchefsuggest=Integer.parseInt(chefsuggest.getText().toString());
                    int addpreparetime=Integer.parseInt(preparetime.getText().toString());




                    addfoodmenu addfoodmenu=new addfoodmenu(this,foodnames,addfooddesc,addfoodprice,addfoodspicy,addcontainmeat,addfoodsalty,addfoodsweetness,addfoodsour,addchefsuggest,addpreparetime);

                    Thread thread=new Thread(addfoodmenu);

                    thread.start();
                }else
                {
                    //if got error show error
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Please make sure all information is without error!! ");
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
        }

    }
}
