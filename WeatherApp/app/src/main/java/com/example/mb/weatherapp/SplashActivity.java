package com.example.mb.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;


public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //check if device is connected to internet
        if(!haveNetworkConnection()) {

            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("This app requires internet connection to run.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            //get weather data while loading splash screen
            new loadSomeStuff().execute();
        }






    }

    public class loadSomeStuff extends AsyncTask<String, Integer, WeatherAPI> {

        @Override
        protected WeatherAPI doInBackground(String... params) {
            //gets weather info for London
            return new WeatherAPI("london");


        }

        @Override
        protected void onPostExecute(WeatherAPI api) {
            super.onPostExecute(api);


            //creates a intent
            //all the data will be put here and sent to the weather activity

            Intent mainIntent = new Intent(SplashActivity.this, WeatherActivity.class);

            mainIntent.putExtra("pages",5);




            for(int i = 0; i < api.getDay1Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay1Forecast().get(i);

                mainIntent.putExtra("first" + i,hour);



            }

            for(int i = 0; i < api.getDay2Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay2Forecast().get(i);

                mainIntent.putExtra("second" + i,hour);



            }

            for(int i = 0; i < api.getDay3Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay3Forecast().get(i);

                mainIntent.putExtra("third" + i,hour);



            }

            for(int i = 0; i < api.getDay4Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay4Forecast().get(i);

                mainIntent.putExtra("fourth" + i,hour);



            }

            for(int i = 0; i < api.getDay5Forecast().size(); i++ ) {


                ArrayList<String> hour = api.getDay5Forecast().get(i);

                mainIntent.putExtra("fifth" + i,hour);



            }



            mainIntent.putExtra("firstDayTemp",api.getCurrentTemp());
            mainIntent.putExtra("firstDayCondition",api.getCurrentCondition());
            mainIntent.putExtra("firstDaySunrise",api.getCurrentSunrise());
            mainIntent.putExtra("firstDaySunset",api.getCurrentSunset());

            mainIntent.putExtra("secondDayTemp",api.getSecondDayTemp());
            mainIntent.putExtra("secondDayCondition",api.getSecondDayCondition());
            mainIntent.putExtra("secondDaySunrise",api.getSecondDaySunrise());
            mainIntent.putExtra("secondDaySunset",api.getSecondDaySunset());

            mainIntent.putExtra("thirdDayTemp",api.getThirdDayTemp());
            mainIntent.putExtra("thirdDayCondition",api.getThirdDayCondition());
            mainIntent.putExtra("thirdDaySunrise",api.getThirdDaySunrise());
            mainIntent.putExtra("thirdDaySunset",api.getThirdDaySunset());

            mainIntent.putExtra("fourthDayTemp",api.getFourthDayTemp());
            mainIntent.putExtra("fourthDayCondition",api.getFourthDayCondition());
            mainIntent.putExtra("fourthDaySunrise",api.getFourthDaySunrise());
            mainIntent.putExtra("fourthDaySunset",api.getFourthDaySunset());

            try {
                mainIntent.putExtra("fifthDayTemp", api.getFifthDayTemp());
                mainIntent.putExtra("fifthDayCondition", api.getFifthDayCondition());
                mainIntent.putExtra("fifthDaySunrise", api.getFifthDaySunrise());
                mainIntent.putExtra("fifthDaySunset", api.getFifthDaySunset());
            } catch(NullPointerException e) {
                mainIntent.putExtra("pages",4);
            }





            mainIntent.putExtra("locationProperText",api.getLocation());





            String location = api.getLocation();
            location = location.substring(0, 1).toUpperCase() + location.substring(1);
            mainIntent.putExtra("location", location);
            mainIntent.putExtra("degree", "celsius");








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

                mainIntent.putExtra("firstDay",currentDay);
                mainIntent.putExtra("secondDay",nextDay);
                mainIntent.putExtra("thirdDay",thirdDay);
                mainIntent.putExtra("fourthDay",fourthDay);
                mainIntent.putExtra("fifthDay",fifthDay);






                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }


        }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    }


