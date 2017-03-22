package com.signmeastory.apps.gb;

import java.util.ArrayList;

public class Page {
	private String image;
    private String id;
    private String text;
    private String textHtml;
    private String animImages="";
    private ArrayList<String> audios;
    private int time;
    private int animDur;
    private String tween="";
    
    public String getAnimImages() {
        return animImages;
    }
 
    public void setAnimImages(String animImages) {
        this.animImages = animImages;
    }
    
    public String getImage() {
        return image;
    }
 
    public void setImage(String image) {
        this.image = image;
    }
 
    public String getId() {
        return id;
    }
    
    public void setTime(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }
    
    public void setAnimDur(int animDur) {
        this.animDur = animDur;
    }
    
    public int getAnimDur() {
        return animDur;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getText() {
        return text;
    }
 
    public void setText(String text) {
        this.text = text;
    }
    
    public String getTextHtml() {
        return textHtml;
    }
 
    public void setTextHtml(String textHtml) {
        this.textHtml = textHtml;
    }
 
    public ArrayList<String> getAudios() {
        return audios;
    }
 
    public void setAudio(ArrayList<String> audios) {
        this.audios = audios;
    }
  
    public String getTween() {
        return tween;
    }
 
    public void setTween(String tween) {
        this.tween = tween;
    }
    
    @Override
    public String toString() {
        return id + ": " + image;
    }
 
    public String getDetails() {
        String result = id + ": " + image + "\n" + text + "-" + audios;
        return result;
    }

}

