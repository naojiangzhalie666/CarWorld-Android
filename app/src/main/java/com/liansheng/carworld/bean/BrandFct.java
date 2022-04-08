package com.liansheng.carworld.bean;

import com.liansheng.carworld.utils.PinYinStringHelper;

import java.util.List;

public class BrandFct {


    String id;
    String brandId;
    String fctname;
    String seriesName;
    String fctPy;
    String levelName;
    String seriesPriceMa;
    String seriesPriceMin;
    String seriesplace;
    String word;
    boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getFctname() {
        return fctname;
    }

    public void setFctname(String fctname) {
        this.fctname = fctname;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getFctPy() {
        return fctPy;
    }

    public void setFctPy(String fctPy) {
        this.fctPy = fctPy;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getSeriesPriceMa() {
        return seriesPriceMa;
    }

    public void setSeriesPriceMa(String seriesPriceMa) {
        this.seriesPriceMa = seriesPriceMa;
    }

    public String getSeriesPriceMin() {
        return seriesPriceMin;
    }

    public void setSeriesPriceMin(String seriesPriceMin) {
        this.seriesPriceMin = seriesPriceMin;
    }

    public String getSeriesplace() {
        return seriesplace;
    }

    public void setSeriesplace(String seriesplace) {
        this.seriesplace = seriesplace;
    }

    public String getWord() {
        return PinYinStringHelper.getAlpha(fctname);
    }

    public void setWord(String word) {
        this.word = PinYinStringHelper.getAlpha(fctname);
    }

}
