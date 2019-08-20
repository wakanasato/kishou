package com.esri.geoevent.adapter.kishou;

public class InfoBeans {

    private String areaName;
    private String regioncode;
    private String lightning;
    private String heavyRain;
    private String heavySnow;
    private String snowStrom;
    private String blizzard;
    private String flood;
    private String strongWing;
    private String storm;
    private String lowTemp;
    private String tidal;
    private String wave;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public String getLightning() {
        return lightning;
    }

    public void setLightning(String lightning) {
        this.lightning = lightning;
    }

    public String getHeavyRain() {
        return heavyRain;
    }

    public void setHeavyRain(String heavyRain) {
        this.heavyRain = heavyRain;
    }

    public String getHeavySnow() {
        return heavySnow;
    }

    public void setHeavySnow(String heavySnow) {
        this.heavySnow = heavySnow;
    }

    public String getSnowStrom() {
        return snowStrom;
    }

    public void setSnowStrom(String snowStrom) {
        this.snowStrom = snowStrom;
    }

    public String getBlizzard() {
        return blizzard;
    }

    public void setBlizzard(String blizzard) {
        this.blizzard = blizzard;
    }

    public String getFlood() {
        return flood;
    }

    public void setFlood(String flood) {
        this.flood = flood;
    }

    public String getStrongWing() {
        return strongWing;
    }

    public void setStrongWing(String strongWing) {
        this.strongWing = strongWing;
    }

    public String getStorm() {
        return storm;
    }

    public void setStorm(String storm) {
        this.storm = storm;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getTidal() {
        return tidal;
    }

    public void setTidal(String tidal) {
        this.tidal = tidal;
    }

    public String getWave() {
        return wave;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    @Override
    public String toString() {
        return "InfoBeans{" +
                "areaName='" + areaName + '\'' +
                ", regioncode='" + regioncode + '\'' +
                ", lightning='" + lightning + '\'' +
                ", heavyRain='" + heavyRain + '\'' +
                ", heavySnow='" + heavySnow + '\'' +
                ", snowStrom='" + snowStrom + '\'' +
                ", blizzard='" + blizzard + '\'' +
                ", flood='" + flood + '\'' +
                ", strongWing='" + strongWing + '\'' +
                ", storm='" + storm + '\'' +
                ", lowTemp='" + lowTemp + '\'' +
                ", tidal='" + tidal + '\'' +
                ", wave='" + wave + '\'' +
                '}';
    }
}
