package com.oymj.restaurantorderapp.adminfragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;
import communicateserverthread.admincommunicate.editfoodmenu;

/**
 * Created by OwYeong on 10/13/2017.
 */

public class admineditfooddetail extends Fragment implements View.OnClickListener
{
    boolean validdesc=true;
    boolean validprice=true;
    boolean validcontainmeat=true;
    boolean validspicy=true;
    boolean validsour=true;
    boolean validsweetness=true;
    boolean validsalty=true;
    boolean validchefsuggest=true;
    boolean validpreparetime=true;

    EditText fooddesc;
    EditText foodprice;
    EditText containmeat;
    EditText foodspicy;
    EditText foodsalty;
    EditText foodsweetness;
    EditText foodsour;
    EditText preparetime;
    EditText chefsuggest;



    public static admineditfooddetail newInstance(String foodname) {
        Bundle bundle=new Bundle();
        bundle.putString("foodname",foodname);

        admineditfooddetail fragment = new admineditfooddetail();
        fragment.setArguments(bundle);

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.admin_editfooddetail,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        TextView foodname=(TextView) rootView.findViewById(R.id.editfooddetailfoodname);

        foodname.setText(getArguments().getString("foodname"));

        fooddesc=(EditText) rootView.findViewById(R.id.editfooddetaildesc);
        foodprice=(EditText) rootView.findViewById(R.id.editfooddetailprice);
        containmeat=(EditText) rootView.findViewById(R.id.editfooddetailcontainmeat);
        foodspicy=(EditText) rootView.findViewById(R.id.editfooddetailspicy);
        foodsalty=(EditText) rootView.findViewById(R.id.editfooddetailsalty);
        foodsweetness=(EditText) rootView.findViewById(R.id.editfooddetailsweetness);
        foodsour=(EditText) rootView.findViewById(R.id.editfooddetailsour);
        preparetime=(EditText) rootView.findViewById(R.id.editfooddetailpreparetime);
        chefsuggest=(EditText) rootView.findViewById(R.id.editfooddetailchefsuggest);

        ImageButton sentedit=(ImageButton) rootView.findViewById(R.id.editfooddetailconfirmedit);
        sentedit.setOnClickListener(this);

        ImageView foodimage=(ImageView) rootView.findViewById(R.id.editfooddetailfoodimage);

        String imagename=getArguments().getString("foodname").toLowerCase();
        imagename=imagename.replace(" ","");

        //check if image exist
        int imageid = getActivity().getResources().getIdentifier("big"+imagename, "drawable", getActivity().getPackageName());

        //if image not exist will return 0
        if(imageid==0)
        {
            foodimage.setImageResource(getActivity().getResources().getIdentifier("bignopic", "drawable", getActivity().getPackageName()));
        }else
        {
            foodimage.setImageResource(getActivity().getResources().getIdentifier("big"+imagename, "drawable", getActivity().getPackageName()));
        }


        for(int loop=0;loop<foodmenu.foodlist.size();loop++)
        {
            if(getArguments().getString("foodname").equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                fooddesc.setText(foodmenu.foodlist.get(loop).getFooddesc());
                foodprice.setText(Double.toString(foodmenu.foodlist.get(loop).getFoodprice()));
                foodspicy.setText(Integer.toString(foodmenu.foodlist.get(loop).getFoodspicy()));
                foodsalty.setText(Integer.toString(foodmenu.foodlist.get(loop).getFoodsalty()));
                foodsweetness.setText(Integer.toString(foodmenu.foodlist.get(loop).getFoodsweetness()));
                foodsour.setText(Integer.toString(foodmenu.foodlist.get(loop).getFoodsour()));
                preparetime.setText(Integer.toString(foodmenu.foodlist.get(loop).getPreparetime()));
                chefsuggest.setText(Integer.toString(foodmenu.foodlist.get(loop).getChefsuggest()));
                if(foodmenu.foodlist.get(loop).getContainmeat()==0)
                {
                    containmeat.setText("N");
                }else
                {
                    containmeat.setText("Y");
                }

            }
        }

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
            case R.id.editfooddetailconfirmedit:
                if(validdesc&&validprice&&validcontainmeat&&validspicy&&validsalty&&validsweetness&&validsour&&validchefsuggest&&validpreparetime)
                {
                    //if all valid true
                    String editfooddesc=fooddesc.getText().toString();
                    double editfoodprice=Double.parseDouble(foodprice.getText().toString());

                    int editcontainmeat;
                    if(containmeat.getText().toString().contains("Y"))
                    {
                        editcontainmeat=1;
                    }else
                    {
                        editcontainmeat=0;
                    }

                    int editfoodspicy=Integer.parseInt(foodspicy.getText().toString());
                    int editfoodsalty=Integer.parseInt(foodsalty.getText().toString());
                    int editfoodsweetness=Integer.parseInt(foodsweetness.getText().toString());
                    int editfoodsour=Integer.parseInt(foodsour.getText().toString());
                    int editchefsuggest=Integer.parseInt(chefsuggest.getText().toString());
                    int editpreparetime=Integer.parseInt(preparetime.getText().toString());



                    editfoodmenu editfoodmenu=new editfoodmenu(this,getArguments().getString("foodname"),editfooddesc,editfoodprice,editfoodspicy,editcontainmeat,editfoodsalty,editfoodsweetness,editfoodsour,editchefsuggest,editpreparetime);

                    Thread thread=new Thread(editfoodmenu);

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
