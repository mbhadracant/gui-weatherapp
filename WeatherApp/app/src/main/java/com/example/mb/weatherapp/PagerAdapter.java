package com.example.mb.weatherapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MB on 10/03/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    Bundle data;
    FragmentManager fm;

    public PagerAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        this.fm = fm;
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {

        //each fragment contains weather information for each day
        //only the relevant information is passed onto each fragment


        Day day = new Day();

        Bundle info = new Bundle();


        info.putString("location", data.getString("location"));
        info.putString("degree","celsius");
        info.putBoolean("fromToggle",data.getBoolean("fromToggle"));

        info.putString("properTextLocation",data.getString("properTextLocation"));

        switch(position) {


            case 0:

                info.putString("infoDay","first");



                for(int i = 0; i < 25; i++) {

                    if(data.containsKey("first" + i)) {
                        ArrayList<String> hourlyData = data.getStringArrayList("first" + i);

                        info.putStringArrayList("hourForecast"+i,hourlyData);

                    } else {
                        break;
                    }

                }


                info.putString("condition",data.getString("firstDayCondition"));
                info.putString("temp", data.getString("firstDayTemp"));
                info.putString("sunrise", data.getString("firstDaySunrise"));
                info.putString("sunset", data.getString("firstDaySunset"));
                info.putString("day", data.getString("firstDay"));




                day.setArguments(info);




                return day;







            case 1:


                info.putString("infoDay","second");


                for(int i = 0; i < 25; i++) {

                    if(data.containsKey("second" + i)) {
                        ArrayList<String> hourlyData = data.getStringArrayList("second" + i);

                        info.putStringArrayList("hourForecast"+i,hourlyData);

                    } else {
                        break;
                    }

                }


                info.putString("condition",data.getString("secondDayCondition"));
                info.putString("temp", data.getString("secondDayTemp"));
                info.putString("sunrise", data.getString("secondDaySunrise"));
                info.putString("sunset", data.getString("secondDaySunset"));
                info.putString("day", data.getString("secondDay"));




                day.setArguments(info);




                return day;
            case 2:
                info.putString("infoDay","third");


                for(int i = 0; i < 25; i++) {

                    if(data.containsKey("third" + i)) {
                        ArrayList<String> hourlyData = data.getStringArrayList("third" + i);

                        info.putStringArrayList("hourForecast"+i,hourlyData);

                    } else {
                        break;
                    }

                }


                info.putString("condition",data.getString("thirdDayCondition"));
                info.putString("temp", data.getString("thirdDayTemp"));
                info.putString("sunrise", data.getString("thirdDaySunrise"));
                info.putString("sunset", data.getString("thirdDaySunset"));
                info.putString("day", data.getString("thirdDay"));




                day.setArguments(info);




                return day;
            case 3:


                info.putString("infoDay","fourth");


                for(int i = 0; i < 25; i++) {

                    if(data.containsKey("fourth" + i)) {
                        ArrayList<String> hourlyData = data.getStringArrayList("fourth" + i);

                        info.putStringArrayList("hourForecast"+i,hourlyData);

                    } else {
                        break;
                    }

                }


                info.putString("condition",data.getString("fourthDayCondition"));
                info.putString("temp", data.getString("fourthDayTemp"));
                info.putString("sunrise", data.getString("fourthDaySunrise"));
                info.putString("sunset", data.getString("fourthDaySunset"));
                info.putString("day", data.getString("fourthDay"));




                day.setArguments(info);




                return day;

            case 4:

                info.putString("infoDay","fifth");


                for(int i = 0; i < 25; i++) {

                    if(data.containsKey("fifth" + i)) {
                        ArrayList<String> hourlyData = data.getStringArrayList("fifth" + i);

                        info.putStringArrayList("hourForecast"+i,hourlyData);

                    } else {
                        break;
                    }

                }


                info.putString("condition",data.getString("fifthDayCondition"));
                info.putString("temp", data.getString("fifthDayTemp"));
                info.putString("sunrise", data.getString("fifthDaySunrise"));
                info.putString("sunset", data.getString("fifthDaySunset"));
                info.putString("day", data.getString("fifthDay"));




                day.setArguments(info);




                return day;


            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        if(data.getInt("pages") == 4) {
            return 4;
        } else {
            return 5;
        }
    }

    public void clearAll() {

        //deletes all the fragments from the adapter

        for(int i = 0; i < getCount();i++) {


            fm.beginTransaction().remove(getItem(i)).commit();
        }

        for(int i = 0; i < getCount();i++) {


            fm.beginTransaction().remove(getItem(i));
        }

        fm.getFragments().clear();

        notifyDataSetChanged();
    }




}
