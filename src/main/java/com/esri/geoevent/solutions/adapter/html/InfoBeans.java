package com.esri.geoevent.solutions.adapter.html;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class InfoBeans {

    private String horseName;
    private Integer popularity;
    private Date date;
    private String stringDate;
    private String place;
    private String placeName;
    private String weather;
    private String raceName;
    private Integer horseNo;
    private String famous;
    private String score;
    private String jockey;
    private String cycle;
    private String situation;
    private String time;
    private String URL;

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Integer getHorseNo() {
        return horseNo;
    }

    public void setHorseNo(Integer horseNo) {
        this.horseNo = horseNo;
    }

    public String getFamous() {
        return famous;
    }

    public void setFamous(String famous) {
        this.famous = famous;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getJockey() {
        return jockey;
    }

    public void setJockey(String jockey) {
        this.jockey = jockey;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

//    public String getAllAsString (){
//        return horseName + " , " + popularity + " , " + date + " , " + place + " , " + weather + " , " + raceName + " , " + horseNo + " , " + famous + " , " + score + " , " + jockey + " , " + cycle + " , " + situation + " , " + time;
//    }

    public List<String> getAllAsList (){
        List<String> list = new LinkedList<>();
        list.add(horseName);
        list.add(String.valueOf(popularity));
        list.add(date.toString());
        list.add(place);
        list.add(placeName);
        list.add(weather);
        list.add(raceName);
        list.add(String.valueOf(horseNo));
        list.add(famous);
        list.add(score);
        list.add(jockey);
        list.add(cycle);
        list.add(situation);
        list.add(time);
        return list;
    }
}
