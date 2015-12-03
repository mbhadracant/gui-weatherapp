package com.example.mb.weatherapp;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeatherActivity extends FragmentActivity {

    ViewPager viewPager;
    int width,height;
    LinearLayout settings, maps;
    Bundle data;
    PagerAdapter pagerAdapter;
    AutoCompleteTextView searchLocation;
    TextView title;
    Toast toast;
    String degree = "celsius";
    ToggleButton toggleButton;
    String location;
    String actionFrom;
    ImageView settingsIcon;
    String properLocationText = "";
    boolean aboutShowing = false,contactShowing = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);


        //gets the intent from the splash activity which contains data

        Intent i = getIntent();
        data = i.getExtras();


        // gets screen width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;


        // set variables
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),data);
        settings = (LinearLayout)findViewById(R.id.settings);
        settingsIcon = (ImageView)findViewById(R.id.settingsIcon);
        maps = (LinearLayout)findViewById(R.id.maps);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        searchLocation = (AutoCompleteTextView)findViewById(R.id.searchText);
        final LinearLayout about = (LinearLayout)findViewById(R.id.about);
        final TextView aboutInfo = (TextView)findViewById(R.id.aboutinfo);
        final LinearLayout contact = (LinearLayout)findViewById(R.id.contact);
        final TextView contactInfo = (TextView)findViewById(R.id.contactinfo);
        viewPager = (ViewPager) findViewById(R.id.pager);
        Typeface font = Typeface.createFromAsset(getAssets(), "Font.ttf");
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),data);




        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when the toggle button is clicked, the api recollects data and reformats the data
                // according the degrees

                actionFrom = "toggleButton";

                StringBuffer stringbf = new StringBuffer();
                Matcher m = Pattern.compile("([a-z])([a-z]*)",
                        Pattern.CASE_INSENSITIVE).matcher(location);
                while (m.find()) {
                    m.appendReplacement(stringbf,
                            m.group(1).toUpperCase() + m.group(2).toLowerCase());
                }

                properLocationText = m.appendTail(stringbf).toString();
                properLocationText =  properLocationText.replace("-"," ");
                location = location.replace(" ","-");
                location = location.toLowerCase();
                location = location.replace(".","");
                location = location.replace("'","");

                toast = Toast.makeText(getApplicationContext(), "Updating Degree Change...", Toast.LENGTH_SHORT);
                toast.show();


                new loadSomeStuff().execute(location);


            }
        });


        about.setOnClickListener(new View.OnClickListener() {

            //expands the about section when clicked

            @Override
            public void onClick(View v) {

                if(aboutShowing) {
                    aboutShowing = false;
                    aboutInfo.setVisibility(View.GONE);

                } else {
                    aboutShowing = true;
                    aboutInfo.setVisibility(View.VISIBLE);

                }
            }
        });



        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //expands the contact session when clicked

                if(contactShowing) {
                    contactShowing = false;
                    contactInfo.setVisibility(View.GONE);

                } else {
                    contactShowing = true;
                    contactInfo.setVisibility(View.VISIBLE);

                }
            }
        });



        //sets up the location list when searching location

        final String[] locations = getResources().getStringArray(R.array.locations);
        final ArrayList<String> locationsList = new ArrayList<>(Arrays.asList(locations));

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
        searchLocation.setAdapter(adapter);

        searchLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actionFrom = "locationButton";
                location = searchLocation.getAdapter().getItem(position).toString();
                properLocationText = searchLocation.getAdapter().getItem(position).toString();

                StringBuffer stringbf = new StringBuffer();
                Matcher m = Pattern.compile("([a-z])([a-z]*)",
                        Pattern.CASE_INSENSITIVE).matcher(properLocationText);
                while (m.find()) {
                    m.appendReplacement(stringbf,
                            m.group(1).toUpperCase() + m.group(2).toLowerCase());
                }

                properLocationText = m.appendTail(stringbf).toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchLocation.getWindowToken(), 0);

                toast = Toast.makeText(getApplicationContext(), "Updating Location", Toast.LENGTH_SHORT);

                toast.show();

                location = location.replace(" ","-");
                location = location.toLowerCase();
                location = location.replace(".","");
                location = location.replace("'","");

                new loadSomeStuff().execute(location);

            }
        });



        data.putInt("width",width);
        data.putInt("height",height);
        location = data.getString("location");


        title = (TextView)findViewById(R.id.title);
        title.setText(location);
        title.setTypeface(font);



        viewPager.setAdapter(pagerAdapter);


        data.putString("properTextLocation",data.getString("location"));




    }


    public class loadSomeStuff extends AsyncTask<String, Integer, WeatherAPI> {



        @Override
        protected WeatherAPI doInBackground(String... params) {



            return new WeatherAPI(params[0]);


        }

        @Override
        protected void onPostExecute(WeatherAPI api) {
            super.onPostExecute(api);

            //gets the weather forecast information for whatever location the user selected

           Bundle newData = new Bundle();

            newData.putInt("pages",5);




            for(int i = 0; i < api.getDay1Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay1Forecast().get(i);

                newData.putStringArrayList("first" + i,hour);



            }

            for(int i = 0; i < api.getDay2Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay2Forecast().get(i);

                newData.putStringArrayList("second" + i,hour);



            }

            for(int i = 0; i < api.getDay3Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay3Forecast().get(i);

                newData.putStringArrayList("third" + i,hour);



            }

            for(int i = 0; i < api.getDay4Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay4Forecast().get(i);

                newData.putStringArrayList("fourth" + i,hour);



            }

            for(int i = 0; i < api.getDay5Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay5Forecast().get(i);

                newData.putStringArrayList("fifth" + i,hour);



            }



            newData.putString("firstDayTemp",api.getCurrentTemp());
            newData.putString("firstDayCondition",api.getCurrentCondition());
            newData.putString("firstDaySunrise",api.getCurrentSunrise());
            newData.putString("firstDaySunset",api.getCurrentSunset());

            newData.putString("secondDayTemp",api.getSecondDayTemp());
            newData.putString("secondDayCondition",api.getSecondDayCondition());
            newData.putString("secondDaySunrise",api.getSecondDaySunrise());
            newData.putString("secondDaySunset",api.getSecondDaySunset());

            newData.putString("thirdDayTemp",api.getThirdDayTemp());
            newData.putString("thirdDayCondition",api.getThirdDayCondition());
            newData.putString("thirdDaySunrise",api.getThirdDaySunrise());
            newData.putString("thirdDaySunset",api.getThirdDaySunset());

            newData.putString("fourthDayTemp",api.getFourthDayTemp());
            newData.putString("fourthDayCondition",api.getFourthDayCondition());
            newData.putString("fourthDaySunrise",api.getFourthDaySunrise());
            newData.putString("fourthDaySunset",api.getFourthDaySunset());

            try {
                newData.putString("fifthDayTemp", api.getFifthDayTemp());
                newData.putString("fifthDayCondition", api.getFifthDayCondition());
                newData.putString("fifthDaySunrise", api.getFifthDaySunrise());
                newData.putString("fifthDaySunset", api.getFifthDaySunset());
            } catch(NullPointerException e) {
                newData.putInt("pages",4);
            }







            String location = api.getLocation();
            location = location.substring(0, 1).toUpperCase() + location.substring(1);
            location = location.replace("-"," ");



            newData.putString("location", location);








            Calendar calendar = Calendar.getInstance();

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


            String currentDay = "", nextDay = "", thirdDay = "",fourthDay = "", fifthDay = "";

            if(dayOfWeek == Calendar.MONDAY) {
                currentDay = "Monday";
                nextDay = "Tuesday";
                thirdDay = "Wednesday";
                fourthDay = "Thursday";
                fifthDay = "Friday";


            } else if(dayOfWeek == Calendar.TUESDAY) {
                currentDay = "Tuesday";
                nextDay = "Wednesday";
                thirdDay = "Thursday";
                fourthDay = "Friday";
                fifthDay = "Saturday";

            } else if(dayOfWeek == Calendar.WEDNESDAY) {
                currentDay = "Wednesday";
                nextDay = "Thursday";
                thirdDay = "Friday";
                fourthDay = "Saturday";
                fifthDay = "Sunday";
            } else if(dayOfWeek == Calendar.THURSDAY) {
                currentDay = "Thursday";
                nextDay = "Friday";
                thirdDay = "Saturday";
                fourthDay = "Sunday";
                fifthDay = "Monday";

            } else if(dayOfWeek == Calendar.FRIDAY) {
                currentDay = "Friday";
                nextDay = "Saturday";
                thirdDay = "Sunday";
                fourthDay = "Monday";
                fifthDay = "Tuesday";
            }  else if(dayOfWeek == Calendar.SATURDAY) {
                currentDay = "Saturday";
                nextDay = "Sunday";
                thirdDay = "Monday";
                fourthDay = "Tuesday";
                fifthDay = "Wednesday";
            } else if(dayOfWeek == Calendar.SUNDAY) {
                currentDay = "Sunday";
                nextDay = "Monday";
                thirdDay = "Tuesday";
                fourthDay = "Wednesday";
                fifthDay = "Thursday";
            }

            newData.putString("firstDay",currentDay);
            newData.putString("secondDay",nextDay);
            newData.putString("thirdDay",thirdDay);
            newData.putString("fourthDay",fourthDay);
            newData.putString("fifthDay",fifthDay);




            newData.putString("degree", degree);

            newData.putString("properTextLocation",properLocationText);

            if(actionFrom == "toggleButton") {
                float degree = 90f;
                RotateAnimation anim = new RotateAnimation(degree, degree + 90f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);


                //Setup anim with desired properties
                anim.setFillAfter(true);

                anim.setDuration(300);
                newData.putBoolean("fromToggle",true);
                settingsIcon.startAnimation(anim);
            } else {
                newData.putBoolean("fromToggle",false);
            }



                viewPager.setVisibility(View.VISIBLE);



             pagerAdapter.clearAll();


            viewPager.removeAllViews();
            viewPager.setAdapter(null);


            pagerAdapter.clearAll();

            pagerAdapter = new PagerAdapter(getSupportFragmentManager(),newData);
            viewPager.setAdapter(pagerAdapter);






            toast.cancel();





        }


    }








}