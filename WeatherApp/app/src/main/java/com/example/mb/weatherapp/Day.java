package com.example.mb.weatherapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;

/**
 * Created by MB on 10/03/15.
 */

public class Day extends Fragment {

    private View myFragmentView;
    TranslateAnimation translateAnimation;
    RotateAnimation anim;
    AlphaAnimation fadeAnimation;
    ScrollView scrollView;
    boolean settingsShowing = false;
    boolean pagerShowing = true;
    boolean mapsShowing = false;
    boolean clickable = true;
    float degree = 0f;
    LinearLayout settings,root, maps;
    ViewPager pager;
    TextView title,currentCondition,currentTemp;
    Bundle data;
    ImageView mapsIcon;
    boolean blockShowing = false;
    ArrayList<LinearLayout> moreDataList;
    String degreeShow;
    ToggleButton toggleButton;
    String locationProperText;
    AutoCompleteTextView searchLocation;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.forecast,container,false);


        //gets the weather data
        data = getArguments();

        //set variables
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Font.ttf");
        Typeface thin = Typeface.createFromAsset(getActivity().getAssets(), "Thin.ttf");
        LinearLayout dataLayout = (LinearLayout)myFragmentView.findViewById(R.id.dataLayout);
        root = (LinearLayout)myFragmentView.findViewById(R.id.root);
        scrollView = (ScrollView)myFragmentView.findViewById(R.id.scrollView);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setFadingEdgeLength(2);
        settings = (LinearLayout)getActivity().findViewById(R.id.settings);
        title = (TextView)getActivity().findViewById(R.id.title);
        locationProperText = data.getString("properTextLocation");
        toggleButton = (ToggleButton)getActivity().findViewById(R.id.toggleButton);
        pager = (ViewPager)getActivity().findViewById(R.id.pager);
        final ImageView settingsIcon = (ImageView)getActivity().findViewById(R.id.settingsIcon);
        moreDataList = new ArrayList<LinearLayout>();
        maps = (LinearLayout)getActivity().findViewById(R.id.maps);
        searchLocation = (AutoCompleteTextView)getActivity().findViewById(R.id.searchText);
        searchLocation = (AutoCompleteTextView)getActivity().findViewById(R.id.searchText);
        mapsIcon = (ImageView) getActivity().findViewById(R.id.mapsIcon);




        //gets weather toggle button is checked or not
        if(toggleButton.isChecked()) {
            degreeShow = "fahrenheit";
        } else {
            degreeShow = "celsius";
        }

        try {

            if (data.getBoolean("fromToggle")) {
                maps.setVisibility(View.VISIBLE);
                System.out.println("fffff");
            }
        }catch (NullPointerException e) {

        }

        //get width and height of screen

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final int height = size.y;



        //when the location button is clicked
        mapsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                searchLocation.setText("");
                if(pagerShowing && !settingsShowing && !mapsShowing) {

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchLocation.getWindowToken(), 0);


                    mapsShowing = true;
                    pager.setVisibility(View.GONE);


                    pagerShowing = false;


                    title.setTextSize(40);

                    if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                        title.setTextSize(40);
                        currentTemp.setTextSize(70);
                        if(locationProperText.length() >= 15) {
                            title.setTextSize(25);
                        } else {
                            title.setTextSize(40);
                        }
                    } else {
                        title.setTextSize(25);
                        currentTemp.setTextSize(60);
                    }



                    title.setText("Location");
                } else if(!pagerShowing && !settingsShowing && mapsShowing) {
                   pager.setVisibility(View.VISIBLE);
                    mapsShowing = false;
                    pagerShowing = true;
                    if(locationProperText.length() >= 15) {
                        title.setTextSize(25);
                    } else {
                        title.setTextSize(40);
                    }

                    if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                        title.setTextSize(40);
                        currentTemp.setTextSize(70);
                        if(locationProperText.length() >= 15) {
                            title.setTextSize(25);
                        } else {
                            title.setTextSize(40);
                        }
                    } else {
                        title.setTextSize(25);
                        currentTemp.setTextSize(60);
                    }

                    title.setText(locationProperText);
                }
            }
        });



        //gets sunrise and sunset
        String sunrise = data.getString("sunrise");
        String sunset = data.getString("sunset");





        //resize title text is length is too long

        if(locationProperText.length() >= 15) {
            title.setTextSize(25);
        } else {
            title.setTextSize(40);
        }

        title.setText(locationProperText);




        //when settings icon is clicked

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(settingsShowing && clickable && !mapsShowing ) {

                    anim = new RotateAnimation(degree, degree+90f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            settingsShowing = false;
                            pagerShowing = true;

                            pager.setVisibility(View.VISIBLE);
                            maps.setVisibility(View.VISIBLE);


                            if(locationProperText.length() >= 15) {
                                title.setTextSize(25);
                            } else {
                                title.setTextSize(40);
                            }

                            if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                                title.setTextSize(40);
                                currentTemp.setTextSize(70);
                                if(locationProperText.length() >= 15) {
                                    title.setTextSize(25);
                                } else {
                                    title.setTextSize(40);
                                }
                            } else {
                                title.setTextSize(25);
                                currentTemp.setTextSize(60);
                            }


                            title.setText(locationProperText);





                            clickable = false;

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            clickable = true;


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    //Setup anim with desired properties
                    anim.setFillAfter(true);
                    degree += 90f;
                    anim.setDuration(300);
                    settingsIcon.startAnimation(anim);
                } else if(!settingsShowing && clickable && !mapsShowing) {

                    anim = new RotateAnimation(degree, degree+90f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                            pager.setVisibility(View.GONE);
                            maps.setVisibility(View.GONE);

                            title.setTextSize(40);
                            title.setText("Settings");
                            settingsShowing = true;
                            pagerShowing = false;


                            if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                                title.setTextSize(40);
                                currentTemp.setTextSize(70);
                                if(locationProperText.length() >= 15) {
                                    title.setTextSize(25);
                                } else {
                                    title.setTextSize(40);
                                }
                            } else {
                                title.setTextSize(25);
                                currentTemp.setTextSize(60);
                            }

                            clickable = false;


                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            settingsShowing = true;
                            clickable = true;

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    //Setup anim with desired properties
                    anim.setFillAfter(true);
                    degree += 90f;
                    anim.setDuration(300);
                    settingsIcon.startAnimation(anim);
                }


            }
        });


        //set current day text
        TextView currentDay = (TextView)myFragmentView.findViewById(R.id.current_day);
        currentDay.setText(data.getString("day"));
        currentDay.setTypeface(font);
        currentDay.setShadowLayer(2, 1, 1, Color.BLACK);



        // parse through each hourly forecast and add the relevant information to each layout
        for (int i = 0; i < 24;i++) {

            RelativeLayout rl = new RelativeLayout(this.getActivity());
            rl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
            rl.setPadding(0,3,0,0);
            String hourText = "";
            String conditionText = "";
            String tempText = "";
            String feelsLike = "";
            String windSpeed = "";
            String windDirection = "";
            String humidity = "";


            if(data.containsKey("hourForecast" + i)) {
                ArrayList<String> hourlyData = data.getStringArrayList("hourForecast" + i);

                hourText = hourlyData.get(0);
                conditionText = hourlyData.get(1);



                if(degreeShow.equals("fahrenheit")) {
                    tempText = Integer.toString((int)(Math.round((9.0/5.0)*Integer.parseInt(hourlyData.get(2).substring(0,hourlyData.get(2).length()-1)) + 32))) + "°";
                } else {
                    tempText = hourlyData.get(2);
                }

                if(degreeShow.equals("fahrenheit")) {
                    feelsLike = Integer.toString((int)(Math.round((9.0/5.0)*Integer.parseInt(hourlyData.get(3).substring(0,hourlyData.get(3).length()-1)) + 32))) + "°";
                } else {
                    feelsLike = hourlyData.get(3);
                }

                windSpeed = hourlyData.get(4);
                windDirection = hourlyData.get(5);
                humidity = hourlyData.get(6);


            } else {
                break;
            }

            // Create hour text
            TextView hour = new TextView(this.getActivity());
            hour.setTypeface(font);
            hour.setText(hourText);
            hour.setTextSize(25);
            hour.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams leftParam =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            27);
            leftParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            hour.setGravity(Gravity.CENTER_VERTICAL);
            hour.setLayoutParams(leftParam);

            rl.addView(hour);


            ImageView condition = new ImageView(this.getActivity());
            condition.setLayoutParams(new ViewGroup.LayoutParams(32,32));

            int hourNumber = Integer.parseInt(hourText.substring(0,2));

            boolean daytime = false;

            if(hourNumber >= Integer.parseInt(sunrise.substring(0,2)) && hourNumber <= Integer.parseInt(sunset.substring(0,2))) {
                daytime = true;
            }


            switch (conditionText){
                case "Sunny day":
                    condition.setImageResource(R.drawable.sunny_day);
                    break;
                case "Clear night":
                    condition.setImageResource(R.drawable.clear_night);
                    break;
                case "Cloudy":
                    condition.setImageResource(R.drawable.cloudy);
                    break;
                case "Drizzle":
                    condition.setImageResource(R.drawable.drizzle);
                    break;
                case "Fog":
                    condition.setImageResource(R.drawable.fog);
                    break;
                case "Hail shower day":
                        condition.setImageResource(R.drawable.hail_shower_day);
                    break;
                case "Hail shower night":
                    condition.setImageResource(R.drawable.hail_shower_night);
                    break;
                case "Hail":
                    condition.setImageResource(R.drawable.hail);
                    break;
                case "Heavy shower day":
                    condition.setImageResource(R.drawable.heavy_rain_shower_day);
                    break;
                case "Heavy shower night":
                    condition.setImageResource(R.drawable.heavy_rain_shower_night);
                    break;
                case "Heavy rain":
                    condition.setImageResource(R.drawable.heavy_rain);
                    break;
                case "Heavy snow shower day":
                    condition.setImageResource(R.drawable.heavy_snow_shower_day);
                    break;
                case "Heavy snow shower night":
                    condition.setImageResource(R.drawable.heavy_rain_shower_night);
                    break;
                case "Heavy snow":
                    condition.setImageResource(R.drawable.heavy_snow);
                    break;
                case "Light shower day":
                    condition.setImageResource(R.drawable.light_rain_shower_day);
                    break;
                case "Light shower night":
                    condition.setImageResource(R.drawable.light_rain_shower_night);
                    break;
                case "Light rain":
                        condition.setImageResource(R.drawable.light_rain);
                    break;
                case "Light snow shower day":
                    condition.setImageResource(R.drawable.light_snow_shower_day);
                    break;
                case "Light snow shower night":
                    condition.setImageResource(R.drawable.light_snow_shower_night);
                    break;
                case "Light snow":
                    condition.setImageResource(R.drawable.light_snow);
                    break;
                case "Mist":
                    condition.setImageResource(R.drawable.mist);
                    break;
                case "Overcast":
                    condition.setImageResource(R.drawable.overcast);
                    break;
                case "Partly cloudy night":
                    condition.setImageResource(R.drawable.partly_cloudy_night);
                    break;
                case "Sunny intervals":
                    condition.setImageResource(R.drawable.sunny_intervals);
                    break;
                case "Thunder shower day":
                    condition.setImageResource(R.drawable.thunder_shower_day);
                    break;
                case "Thunder shower night":
                    condition.setImageResource(R.drawable.thunder_shower_night);
                    break;
                case "Thunder":
                    condition.setImageResource(R.drawable.sunny_intervals);
                    break;



                default:
                    condition.setImageResource(R.drawable.no_data);
            }

            RelativeLayout.LayoutParams middleParam =
                    new RelativeLayout.LayoutParams(30,30);
            middleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            condition.setLayoutParams(middleParam);
            rl.addView(condition);

            TextView temp = new TextView(this.getActivity());
            temp.setText(tempText);
            temp.setTextSize(25);
            temp.setTextColor(Color.WHITE);
            temp.setTypeface(font);





            RelativeLayout.LayoutParams rightParam =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            rightParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rightParam.addRule(RelativeLayout.TEXT_ALIGNMENT_TEXT_START);
            temp.setLayoutParams(rightParam);
            temp.setGravity(Gravity.CENTER_VERTICAL);
            rl.addView(temp);



            RelativeLayout.LayoutParams rightParamMoreInfo =
                    new RelativeLayout.LayoutParams(30,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            rightParamMoreInfo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);



            final LinearLayout moreData = new LinearLayout(this.getActivity());
            moreData.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            moreData.setOrientation(LinearLayout.VERTICAL);
            moreData.setGravity(Gravity.CENTER_HORIZONTAL);

            final RelativeLayout conditionLayout = new RelativeLayout(this.getActivity());
            conditionLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));


            RelativeLayout.LayoutParams midParam =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            midParam.addRule(RelativeLayout.CENTER_HORIZONTAL);



            TextView conditionData = new TextView(this.getActivity());
            conditionData.setTypeface(font);
            conditionData.setTextSize(25);
            conditionData.setText(conditionText);
            conditionData.setTextColor(Color.WHITE);
            conditionData.setLayoutParams(midParam);
            conditionData.setGravity(Gravity.CENTER);

            conditionLayout.addView(conditionData);

            moreData.addView(conditionLayout);


            final RelativeLayout feelsLikeLayout = new RelativeLayout(this.getActivity());
            conditionLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            TextView feelsLikeDescription = new TextView(this.getActivity());
            feelsLikeDescription.setTypeface(font);
            feelsLikeDescription.setTextSize(20);
            feelsLikeDescription.setText("Feels Like");
            feelsLikeDescription.setTextColor(Color.WHITE);
            feelsLikeDescription.setLayoutParams(leftParam);

            TextView feelsLikeData = new TextView(this.getActivity());
            feelsLikeData.setTypeface(font);
            if(feelsLike.length() == 2) {
                feelsLike += "  ";
            }


            feelsLikeData.setText(feelsLike);
            feelsLikeData.setTextSize(20);
            feelsLikeData.setTextColor(Color.WHITE);
            feelsLikeData.setLayoutParams(rightParamMoreInfo);



            feelsLikeLayout.addView(feelsLikeData);
            feelsLikeLayout.addView(feelsLikeDescription);

            moreData.addView(feelsLikeLayout);

            final RelativeLayout windSpeedLayout = new RelativeLayout(this.getActivity());
            windSpeedLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            TextView windSpeedDescription = new TextView(this.getActivity());
            windSpeedDescription.setTypeface(font);
            windSpeedDescription.setTextSize(20);
            windSpeedDescription.setText("Wind Speed(mph)");
            windSpeedDescription.setTextColor(Color.WHITE);
            windSpeedDescription.setLayoutParams(leftParam);

            TextView windSpeedData = new TextView(this.getActivity());
            windSpeedData.setTypeface(font);

            if(windSpeed.length() == 1) {
                windSpeed += "   ";
            } else if(windSpeed.length() == 2) {
                windSpeed += "   ";
            }


            windSpeedData.setText(windSpeed);
            windSpeedData.setTextSize(20);
            windSpeedData.setTextColor(Color.WHITE);
            windSpeedData.setLayoutParams(rightParamMoreInfo);

            windSpeedLayout.addView(windSpeedDescription);
            windSpeedLayout.addView(windSpeedData);

            moreData.addView(windSpeedLayout);

            final RelativeLayout windDirectionLayout = new RelativeLayout(this.getActivity());
            windDirectionLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            TextView windDirectionDescription = new TextView(this.getActivity());
            windDirectionDescription.setTypeface(font);
            windDirectionDescription.setTextSize(20);
            windDirectionDescription.setText("Wind Direction");
            windDirectionDescription.setTextColor(Color.WHITE);
            windDirectionDescription.setLayoutParams(leftParam);

            TextView windDirectionData = new TextView(this.getActivity());
            windDirectionData.setTypeface(font);
            if(windDirection.length() == 1) {
                windDirection += "   ";

            } else if(windDirection.length() == 2) {
                windDirection += "  ";
            }
            windDirectionData.setText(windDirection);
            windDirectionData.setTextSize(20);
            windDirectionData.setTextColor(Color.WHITE);
            windDirectionData.setLayoutParams(rightParamMoreInfo);


            windDirectionLayout.addView(windDirectionDescription);
            windDirectionLayout.addView(windDirectionData);

            moreData.addView(windDirectionLayout);

            final RelativeLayout humidityLayout = new RelativeLayout(this.getActivity());
            humidityLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            TextView humidityDescription = new TextView(this.getActivity());
            humidityDescription.setTypeface(font);
            humidityDescription.setTextSize(20);
            humidityDescription.setText("Humidity");
            humidityDescription.setTextColor(Color.WHITE);
            humidityDescription.setLayoutParams(leftParam);
            humidityDescription.setGravity(Gravity.CENTER_VERTICAL);

            TextView humidityData = new TextView(this.getActivity());
            humidityData.setTypeface(font);

            if(humidity.length() == 1) {
                humidity += "  ";

            } else if(humidity.length() == 2) {
                humidity += " ";
            } else {
                humidity += " ";
            }
            humidityData.setText(humidity);
            humidityData.setTextSize(20);
            humidityData.setTextColor(Color.WHITE);
            humidityData.setLayoutParams(rightParamMoreInfo);

            humidityLayout.addView(humidityDescription);
            humidityLayout.addView(humidityData);

            moreData.addView(humidityLayout);







            ImageView line = new ImageView(this.getActivity());
            line.setImageResource(R.drawable.line);
            line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2));

            dataLayout.addView(line);

            moreDataList.add(moreData);


            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(moreData.getVisibility() == View.VISIBLE) {
                        moreData.setVisibility(View.GONE);
                    } else {
                        moreData.setVisibility(View.VISIBLE);

                    }

                        for(LinearLayout l : moreDataList) {
                            if(l != moreData) {
                                l.setVisibility(View.GONE);
                            }
                        }




            }});

            moreData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.setVisibility(View.GONE);


                }
            });

            moreData.setVisibility(View.GONE);


            dataLayout.addView(rl);
            dataLayout.addView(moreData);

        }


        title.setVisibility(View.VISIBLE);


        //set condition text
        currentCondition = (TextView)myFragmentView.findViewById(R.id.current_condition);
        currentCondition.setText(data.getString("condition"));
        currentCondition.setTypeface(font);
        currentCondition.setShadowLayer(2, 1, 1, Color.BLACK);


        //set background image
        switch (data.getString("condition")){
            case "Sunny day":
                root.setBackgroundResource(R.drawable.sunny_day_bg);
                break;
            case "Clear night":
                root.setBackgroundResource(R.drawable.clear_night_bg);
                break;
            case "Cloudy":
                root.setBackgroundResource(R.drawable.cloudy_bg);
                break;
            case "Drizzle":
                root.setBackgroundResource(R.drawable.drizzle_bg);
                break;
            case "Fog":
                root.setBackgroundResource(R.drawable.fog_bg);
                break;
            case "Hail shower day":
                root.setBackgroundResource(R.drawable.hail_shower_day_bg);
                break;
            case "Hail shower night":
                root.setBackgroundResource(R.drawable.hail_shower_night_bg);
                break;
            case "Hail":
                root.setBackgroundResource(R.drawable.hail_bg);
                break;
            case "Heavy shower day":
                root.setBackgroundResource(R.drawable.heavy_shower_day_bg);
                break;
            case "Heavy shower night":
                root.setBackgroundResource(R.drawable.heavy_shower_night_bg);
                break;
            case "Heavy rain":
                root.setBackgroundResource(R.drawable.heavy_rain_bg);
                break;
            case "Heavy snow shower day":
                root.setBackgroundResource(R.drawable.heavy_snow_shower_day_bg);
                break;
            case "Heavy snow shower night":
                root.setBackgroundResource(R.drawable.heavy_snow_shower_night_bg);
                break;
            case "Heavy snow":
                root.setBackgroundResource(R.drawable.heavy_snow);
                break;
            case "Light shower day":
                root.setBackgroundResource(R.drawable.light_shower_day_bg);
                break;
            case "Light shower night":
                root.setBackgroundResource(R.drawable.light_shower_night_bg);
                break;
            case "Light rain":
                root.setBackgroundResource(R.drawable.light_rain_bg);
                break;
            case "Light snow shower day":
                root.setBackgroundResource(R.drawable.light_snow_shower_day_bg);
                break;
            case "Light snow shower night":
                root.setBackgroundResource(R.drawable.light_snow_shower_night_bg);
                break;
            case "Light snow":
                root.setBackgroundResource(R.drawable.light_snow_bg);
                break;
            case "Mist":
                root.setBackgroundResource(R.drawable.mist_bg);
                break;
            case "Overcast":
                root.setBackgroundResource(R.drawable.overcast_bg);
                break;
            case "Partly cloudy night":
                root.setBackgroundResource(R.drawable.partly_cloudy_night_bg);
                break;
            case "Sunny intervals":
                root.setBackgroundResource(R.drawable.sunny_intervals_bg);
                break;
            case "Thunder shower day":
                root.setBackgroundResource(R.drawable.thunder_shower_day_bg);
                break;
            case "Thunder shower night":
                root.setBackgroundResource(R.drawable.thunder_shower_night_bg);
                break;
            case "Thunder":
                root.setBackgroundResource(R.drawable.sunny_intervals_bg);
                break;



            default:
                root.setBackgroundResource(R.drawable.no_data);
        }

        //set current temp text
        currentTemp = (TextView)myFragmentView.findViewById(R.id.current_temp);
        currentTemp.setShadowLayer(2, 1, 1, Color.BLACK);
        if(toggleButton.isChecked()) {
            String currentTempText = Integer.toString((int)(Math.round((9.0/5.0)*Integer.parseInt(data.getString("temp").substring(0,data.getString("temp").length()-1)) + 32))) + "°";
            currentTemp.setText(currentTempText);
        } else {
            currentTemp.setText(data.getString("temp"));
        }
        currentTemp.setTypeface(font);



        ImageView line = new ImageView(this.getActivity());

        line.setImageResource(R.drawable.line);
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        dataLayout.addView(line);


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                    title.setTextSize(40);
                    currentTemp.setTextSize(70);
                    if(locationProperText.length() >= 15) {
                        title.setTextSize(25);
                    } else {
                        title.setTextSize(40);
                    }
                } else {
                    title.setTextSize(25);
                    currentTemp.setTextSize(60);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                    title.setTextSize(40);
                    currentTemp.setTextSize(70);
                    if(locationProperText.length() >= 15) {
                        title.setTextSize(25);
                    } else {
                        title.setTextSize(40);
                    }
                } else {
                    title.setTextSize(25);
                    currentTemp.setTextSize(60);
                }


            }
        });


        if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            title.setTextSize(40);
            currentTemp.setTextSize(70);
            if(locationProperText.length() >= 15) {
                title.setTextSize(25);
            } else {
                title.setTextSize(40);
            }
        } else {
            title.setTextSize(25);
            currentTemp.setTextSize(60);
        }




        return myFragmentView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //change layout when screen changes orientation

        final Configuration config = newConfig;


        // Checks the orientation of the screen
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           title.setTextSize(25);
            currentTemp.setTextSize(60);


        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            title.setTextSize(40);
            currentTemp.setTextSize(70);
            if(locationProperText.length() >= 15) {
                title.setTextSize(25);
            } else {
                title.setTextSize(40);
            }
        }


    }

    //gets screen orientation
    public int getScreenOrientation()
    {
        Display getOrient = getActivity().getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
}