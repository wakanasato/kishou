package com.esri.geoevent.adapter.kishou;

public class InfoBean {
    //    public InfoBeans(Boolean alertFlag, Boolean warningFlag, Boolean spAlertFlag) {
//        this.alertFlag = alertFlag;
//        this.warningFlag = warningFlag;
//        this.spAlertFlag = spAlertFlag;
//    }
    private String region_name;
    private String region_code;
    private String type_lightning;
    private String type_heavyRain;
    private String type_heavySnow;
    private String type_snowStorm;
    private String type_blizzard;
    private String type_flood;
    private String type_strongWind;
    private String type_storm;
    private String type_lowTemp;
    private String type_tidal;
    private String type_wave;
    private Boolean spAlertFlag;
    private Boolean alertFlag;
    private Boolean warningFlag;

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getType_lightning() {
        return type_lightning;
    }

    public void setType_lightning(String type_lightning) {
        this.type_lightning = type_lightning;
    }

    public String getType_heavyRain() {
        return type_heavyRain;
    }

    public void setType_heavyRain(String type_heavyRain) {
        this.type_heavyRain = type_heavyRain;
    }

    public String getType_heavySnow() {
        return type_heavySnow;
    }

    public void setType_heavySnow(String type_heavySnow) {
        this.type_heavySnow = type_heavySnow;
    }

    public String getType_snowStorm() {
        return type_snowStorm;
    }

    public void setType_snowStorm(String type_snowStorm) {
        this.type_snowStorm = type_snowStorm;
    }

    public String getType_blizzard() {
        return type_blizzard;
    }

    public void setType_blizzard(String type_blizzard) {
        this.type_blizzard = type_blizzard;
    }

    public String getType_flood() {
        return type_flood;
    }

    public void setType_flood(String type_flood) {
        this.type_flood = type_flood;
    }

    public String getType_strongWind() {
        return type_strongWind;
    }

    public void setType_strongWind(String type_strongWind) {
        this.type_strongWind = type_strongWind;
    }

    public String getType_storm() {
        return type_storm;
    }

    public void setType_storm(String type_storm) {
        this.type_storm = type_storm;
    }

    public String getType_lowTemp() {
        return type_lowTemp;
    }

    public void setType_lowTemp(String type_lowTemp) {
        this.type_lowTemp = type_lowTemp;
    }

    public String getType_tidal() {
        return type_tidal;
    }

    public void setType_tidal(String type_tidal) {
        this.type_tidal = type_tidal;
    }

    public String getType_wave() {
        return type_wave;
    }

    public void setType_wave(String type_wave) {
        this.type_wave = type_wave;
    }

    public Boolean getSpAlertFlag() {
        return spAlertFlag;
    }

    public void setSpAlertFlag(Boolean spAlertFlag) {
        this.spAlertFlag = spAlertFlag;
    }

    public Boolean getAlertFlag() {
        return alertFlag;
    }

    public void setAlertFlag(Boolean alertFlag) {
        this.alertFlag = alertFlag;
    }

    public Boolean getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(Boolean warningFlag) {
        this.warningFlag = warningFlag;
    }

//    public void releaseAll() {
//        setType_lightning("解除");
//        setType_heavyRain("解除");
//        setType_heavySnow("解除");
//        setType_snowStorm("解除");
//        setType_blizzard("解除");
//        setType_flood("解除");
//        setType_strongWind("解除");
//        setType_storm("解除");
//        setType_lowTemp("解除");
//        setType_tidal("解除");
//        setType_wave("解除");
//        setSpAlertFlag(null);
//        setAlertFlag(null);
//        setWarningFlag(null);
//
//    }

    @Override
    public String toString() {
        return "InfoBeans{" +
                "region_name='" + region_name + '\'' +
                ", region_code='" + region_code + '\'' +
                ", type_lightning='" + type_lightning + '\'' +
                ", type_heavyRain='" + type_heavyRain + '\'' +
                ", type_heavySnow='" + type_heavySnow + '\'' +
                ", type_snowStorm='" + type_snowStorm + '\'' +
                ", type_blizzard='" + type_blizzard + '\'' +
                ", type_flood='" + type_flood + '\'' +
                ", type_strongWind='" + type_strongWind + '\'' +
                ", type_storm='" + type_storm + '\'' +
                ", type_lowTemp='" + type_lowTemp + '\'' +
                ", type_tidal='" + type_tidal + '\'' +
                ", type_wave='" + type_wave + '\'' +
                ", spAlertFlag=" + spAlertFlag +
                ", alertFlag=" + alertFlag +
                ", warningFlag=" + warningFlag +
                '}';
    }
}
