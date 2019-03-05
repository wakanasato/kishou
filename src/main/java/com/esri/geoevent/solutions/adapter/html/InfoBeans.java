package com.esri.geoevent.solutions.adapter.html;

import java.util.LinkedList;
import java.util.List;

public class InfoBeans {

    String date;
    String place;
    String weather;
    String raceName;
    String horseNo;
    String famous;
    String score;
    String jockey;
    String cycle;
    String situation;
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getHorseNo() {
        return horseNo;
    }

    public void setHorseNo(String horseNo) {
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
    
    public String getAllAsString (){
        return date + " , " + place + " , " + weather + " , " + raceName + " , " + horseNo + " , " + famous + " , " + score + " , " + jockey + " , " + cycle + " , " + situation + " , " + time;
    }

    public List<String> getAllAsList (){
        List<String> list = new LinkedList<>();
        list.add(date);
        list.add(place);
        list.add(weather);
        list.add(raceName);
        list.add(horseNo);
        list.add(famous);
        list.add(score);
        list.add(jockey);
        list.add(cycle);
        list.add(situation);
        list.add(time);
        return list;
    }
}
