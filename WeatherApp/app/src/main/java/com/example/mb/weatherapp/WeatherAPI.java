package com.example.mb.weatherapp;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is the Weather API. We have used MetOffice HTML document to retrieve 
 * weather data. It provides data for the following five days including the current day.
 * It also provides weather data for all the locations of UK.
 * @author TeamGG
 * @since v3.7 16/03/2015
 */
public class WeatherAPI {

    //Object of Day for the following five days
    private Day currentWI;
    private Day secondDay;
    private Day thirdDay;
    private Day fourthDay;
    private Day fifthDay;

    //location entered by the user
    private String location;

    //Contains a list of hours for the following days
    private ArrayList<ArrayList<String>> day1Hours = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> day2Hours = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> day3Hours = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> day4Hours = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> day5Hours = new ArrayList<ArrayList<String>>();


    public WeatherAPI(String location) {
        this.location = location;

        //URL for retriving html document
        String html = "http://www.metoffice.gov.uk/public/weather/forecast/" + location;

        try {
            //retrieves html from url
            ArrayList<String> metOfficeHTML = getRSS(html);
            parseMetOffice(metOfficeHTML);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //
    public void parseMetOffice(ArrayList<String> rss) {
        int dayCount = 0;
        int dayAverageCount = 0;
        int getSunInfo = 0;
        int[] dayLine = new int[5];

        for (int i = 0; i < rss.size(); i++) {
            String line = rss.get(i);
            String day = "";
            //Date
            if (line.contains("<thead class=\"weatherDate\">")) {

                day = rss.get(i+3);

                //Processes hourly weather data
                ArrayList<String> hours = getHours(i, rss);
                ArrayList<String> conditions = getConditions(i, rss);
                ArrayList<String> temps = getTemp(i, rss);
                ArrayList<String> feelsLike = getFeelsLike(i, rss);
                ArrayList<String> wind = getWind(i, rss);
                ArrayList<String> windDirection = getWindDirection(i, rss);
                ArrayList<String> humidty = getHumidty(i, rss);

                for (int j = 0; j < hours.size(); j++) {
                    if (dayCount == 0) {
                        day1Hours.add(createHour(hours.get(j), conditions.get(j), temps.get(j), feelsLike.get(j), wind.get(j), windDirection.get(j), humidty.get(j)));
                    } else if (dayCount == 1) {
                        day2Hours.add(createHour(hours.get(j), conditions.get(j), temps.get(j), feelsLike.get(j), wind.get(j), windDirection.get(j), humidty.get(j)));
                    } else if (dayCount == 2) {
                        day3Hours.add(createHour(hours.get(j), conditions.get(j), temps.get(j), feelsLike.get(j), wind.get(j), windDirection.get(j), humidty.get(j)));
                    } else if (dayCount == 3) {
                        day4Hours.add(createHour(hours.get(j), conditions.get(j), temps.get(j), feelsLike.get(j), wind.get(j), windDirection.get(j), humidty.get(j)));
                    } else if (dayCount == 4) {
                        day5Hours.add(createHour(hours.get(j), conditions.get(j), temps.get(j), feelsLike.get(j), wind.get(j), windDirection.get(j), humidty.get(j)));
                    }
                }
                dayCount++;
            }

            if(line.contains("<span class=\"long-date\">")){
                int a=0;
                for(int j=getSunInfo; j<rss.size(); j++){
                    if(rss.get(j).contains("<thead class=\"weatherDate\">")){
                        dayLine[a]=j;
                        a++;
                    }
                }

                //Assigns the data for the days
                switch (dayAverageCount) {
                    case 0:
                        currentWI = getDayData(i, dayLine[0], rss);
                        break;
                    case 1:
                        secondDay = getDayData(i, dayLine[1],  rss);
                        break;
                    case 2:
                        thirdDay = getDayData(i, dayLine[2], rss);
                        break;
                    case 3:
                        fourthDay = getDayData(i, dayLine[3], rss);
                        break;
                    case 4:
                        fifthDay = getDayData(i, dayLine[4], rss);
                        break;
                    default:
                        break;
                }
                dayAverageCount++;
            }
        }
    }

    //Returns a list of hours for day 1
    public ArrayList<ArrayList<String>> getDay1Forecast(){
        return day1Hours;
    }

    //Returns a list of hours for day 2
    public ArrayList<ArrayList<String>> getDay2Forecast(){
        return day2Hours;
    }

    //Returns a list of hours for day 3
    public ArrayList<ArrayList<String>> getDay3Forecast(){
        return day3Hours;
    }

    //Returns a list of hours for day 4
    public ArrayList<ArrayList<String>> getDay4Forecast(){
        return day4Hours;
    }

    //Returns a list of hours for day 5
    public ArrayList<ArrayList<String>> getDay5Forecast(){
        return day5Hours;
    }

    //Returns the temp for the current day
    public String getCurrentTemp(){
        return currentWI.getTempC() + "°";
    }

    //Returns the condition for the current day
    public String getCurrentCondition(){
        return currentWI.getCondition();
    }

    //Returns the day of the current day
    public String getCurrentDay(){
        return currentWI.getDay();
    }

    //Returns the time of sunrise
    public String getCurrentSunrise(){
        return currentWI.getSunrise();
    }

    //Returns the time of sunset
    public String getCurrentSunset(){
        return currentWI.getSunset();
    }

    //Returns the temp of the second day
    public String getSecondDayTemp(){
        return secondDay.getTempC() + "°";
    }

    //Returns the condition of the second day
    public String getSecondDayCondition(){
        return secondDay.getCondition();
    }

    //Returns the day of the second day
    public String getSecondDayDay(){
        return secondDay.getDay();
    }

    //Returns the time of the sunrise of the second day
    public String getSecondDaySunrise(){
        return secondDay.getSunrise();
    }

    //Returns the time of the sunset of the second day
    public String getSecondDaySunset(){
        return secondDay.getSunset();
    }

    //third day
    public String getThirdDayTemp(){
        return thirdDay.getTempC() + "°";
    }

    public String getThirdDayCondition(){
        return thirdDay.getCondition();
    }

    public String getThirdDayDay(){
        return thirdDay.getDay();
    }

    public String getThirdDaySunrise(){
        return thirdDay.getSunrise();
    }

    public String getThirdDaySunset(){
        return thirdDay.getSunset();
    }

    //fourth day
    public String getFourthDayTemp(){
        return fourthDay.getTempC() + "°";
    }

    public String getFourthDayCondition(){
        return fourthDay.getCondition();
    }

    public String getFourthDayDay(){
        return fourthDay.getDay();
    }

    public String getFourthDaySunrise(){
        return fourthDay.getSunrise();
    }

    public String getFourthDaySunset(){
        return fourthDay.getSunset();
    }

    //fifth day
    public String getFifthDayTemp(){
        return fifthDay.getTempC() + "°";
    }

    public String getFifthDayCondition(){
        return fifthDay.getCondition();
    }

    public String getFifthDayDay(){
        return fifthDay.getDay();
    }

    public String getFifthDaySunrise(){
        return fifthDay.getSunrise();
    }

    public String getFifthDaySunset(){
        return fifthDay.getSunset();
    }

    public String getLocation() {
        return location;
    }

    //Returns an arraylist of strings contains each line of the html document
    public ArrayList<String> getRSS(String urlString) {

        ArrayList<String> lines = new ArrayList<String>();


        try{
            //creates URL object
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                //adds the html line to the ArrayList
                lines.add(inputLine);
            }

            //Closes the BufferedReader
            in.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        //returns the arraylist of html lines
        return lines;
    }

    /**
     * Takes data as argument and returns an arraylist of string for the hour.
     * @param hour
     * @param condition
     * @param temp
     * @param feelsLike
     * @param wind
     * @param windDirection
     * @param humidty
     * @return ArrayList<String>
     */
    private ArrayList<String> createHour(String hour, String condition, String temp, String feelsLike, String wind, String windDirection, String humidty) {
        return new ArrayList<String>(Arrays.asList(hour, condition, temp, feelsLike, wind, windDirection, humidty));
    }

    /**
     * Retrieves a list of hours from the HTML document. It returns an ArrayList of hours
     * @param i
     * @param rss
     * @return ArrayList<String>
     * @since v1 14/03/2015
     */
    private ArrayList<String> getHours(int i, ArrayList<String> rss) {
        ArrayList<String> hours = new ArrayList<String>();
        for (int j = i + 12; j < rss.size(); j++) {
            String hourLine = rss.get(j);
            if (hourLine.contains("</tr>")) {
                break;
            }
            //retrives and add the hour to a list from an html line
            hours.add(hourLine.substring(hourLine.indexOf(">") + 1, hourLine.indexOf("<sup>")) + ":00");
        }
        return hours;
    }

    /**
     * Retrieves a list temp for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getTemp(int i, ArrayList<String> rss) {
        ArrayList<String> temps = new ArrayList<String>();
        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherTemp\"")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            String line = rss.get(x);

            if (line.contains("data-value=")) {
                String temp = line.substring(line.indexOf(">") + 1, line.indexOf("<"));
                temps.add(temp + "°");
            }
        }

        return temps;
    }

    /**
     * Retrieves a list condition for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getConditions(int i, ArrayList<String> rss) {
        ArrayList<String> conditions = new ArrayList<String>();

        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherWX\">")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            String line = rss.get(x);

            if (line.contains("<span>")) {
                String temp = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
                conditions.add(temp);
            }
        }

        return conditions;
    }


    /**
     * Retrieves a list Feels Like temperature for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getFeelsLike(int i, ArrayList<String> rss) {
        ArrayList<String> feelsLike = new ArrayList<String>();

        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherFeel wxContent\" >")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            String line = rss.get(x);
            if (line.contains("data-c=\"")) {
                feelsLike.add(line.substring(line.indexOf("\"") + 1, line.length() - 1) + "°");
            }
        }
        return feelsLike;
    }


    /**
     * Retrieves a list wind in mph for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getWind(int i, ArrayList<String> rss) {
        ArrayList<String> winds = new ArrayList<String>();
        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherWind wxContent\" >")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            String line = rss.get(x);
            if (line.contains("data-value=")) {
                winds.add(line.substring(line.indexOf("\"") + 1, line.length() - 1));
            }
        }
        return winds;
    }


    /**
     * Retrieves a list wind Direction for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getWindDirection(int i, ArrayList<String> rss) {
        ArrayList<String> windDirections = new ArrayList<String>();
        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherWind wxContent\" >")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            String line = rss.get(x);
            if (line.contains("data-value2=")) {
                windDirections.add(line.substring(line.indexOf("\"") + 1, line.length() - 1));
            }
        }
        return windDirections;
    }


    /**
     * Retrieves a list of humidity for the day from a given list of HTML lines.
     * @param i
     * @param rss
     * @return
     */
    private ArrayList<String> getHumidty(int i, ArrayList<String> rss) {
        ArrayList<String> humidty = new ArrayList<String>();
        int j = 0;
        for (j = i; j < rss.size(); j++) {
            String line = rss.get(j);
            if (line.contains("<tr class=\"weatherHumidity wxContent\" >")) {
                break;
            }
        }

        for (int x = j; x < rss.size() && !rss.get(x).contains("</tr>"); x++) {
            if (rss.get(x).contains("%")) {
                String line = rss.get(x);
                humidty.add(line);
            }
        }
        return humidty;
    }

    /**
     * Returns a day object for the day. The day object contains info about the day.
     * @param i
     * @param x
     * @param rss
     * @return
     */
    private Day getDayData(int i, int x, ArrayList<String> rss) {
        Day day = null;
        String dayNLine = rss.get(i).substring(rss.get(i).indexOf(">")+1);
        String[] dayD = dayNLine.split(" ");
        String dayN = dayD[0];
        String temp = "";
        String condition = rss.get(i+21);
        String sunset = "";
        String sunrise = "";

        for(int j = i; j < rss.size() && !rss.get(j).contains("title=\"Minimum nighttime temperature\""); j++) {
            String line = rss.get(j);
            if (line.contains("title=\"Maximum daytime temperature\"")) {
                String tempLine = rss.get(j+1);
                temp = tempLine.substring(tempLine.indexOf("\"")+1, tempLine.length()-1);
            }
        }

        for(int y=x; y<rss.size() && !rss.get(y).contains("<span><i class=\"icon\" data-type=\"moon\""); y++){
            if(rss.get(y).contains("<div class=\"sunInner\">")){
                sunrise = getSunrise(y, rss);
                sunset = getSunset(y, rss);
            }
        }
        day = new Day(dayN, condition, temp, sunrise, sunset);
        return day;
    }

    /**
     * Returns a string containing the time of sunrise.
     * @param i
     * @param rss
     * @return
     */
    private String getSunrise(int i, ArrayList<String> rss) {
        String sunriseLine = rss.get(i+1);
        String sunrise = sunriseLine.substring(sunriseLine.indexOf(":")+2, sunriseLine.indexOf("</span>"));
        return sunrise;
    }

    /**
     * Returns a string containing the time of sunset.
     * @param i
     * @param rss
     * @return
     */
    private String getSunset(int i, ArrayList<String> rss) {
        String sunsetLine = rss.get(i+2);
        String sunset = sunsetLine.substring(sunsetLine.indexOf(":")+2, sunsetLine.indexOf("</span>"));
        return sunset;
    }

    //Inner class
    public class Day {

        private String condition;
        private String tempC;
        private String day;
        private String sunrise;
        private String sunset;

        /**
         * Constructor: creates a day object with the following data.
         * @param d
         * @param c
         * @param tempC
         * @param sr
         * @param ss
         */
        public Day(String d, String c, String tempC, String sr, String ss){
            this.condition = c;
            this.tempC = tempC;
            this.day=d;
            this.sunrise=sr;
            this.sunset=ss;
        }

        /**
         * Returns condition for the day.
         * @return
         */
        public String getCondition(){
            return condition;
        }

        /**
         * Returns temperature for the day in celsius.
         * @return
         */
        public String getTempC(){
            return tempC;
        }

        /**
         * Returns name of the day
         * @return
         */
        public String getDay(){
            return day;
        }

        /**
         * Returns the sunrise of the day
         * @return
         */
        public String getSunrise(){
            return sunrise;
        }

        /**
         * Returns the sunset of the day
         * @return
         */
        public String getSunset(){
            return sunset;
        }

    }

}




