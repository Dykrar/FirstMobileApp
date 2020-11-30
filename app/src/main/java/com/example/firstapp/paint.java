package com.example.firstapp;



import java.util.ArrayList;

public class paint {




    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public ArrayList<Float> getXxs() {
        return Xxs;
    }

    public void setXxs(ArrayList<Float> xxs) {
        Xxs = xxs;
    }

    public ArrayList<Float> getYys() {
        return Yys;
    }

    public void setYys(ArrayList<Float> yys) {
        Yys = yys;
    }

    public float getXi() {
        return Xi;
    }

    public void setXi(float xi) {
        Xi = xi;
    }

    public float getYi() {
        return Yi;
    }

    public void setYi(float yi) {
        Yi = yi;
    }

    private float Xi;

    private float Yi;

    private int currentColor;

    private int strokeWidth;

    private ArrayList<Float> Xxs = new ArrayList<>();

    private ArrayList<Float> Yys = new ArrayList<>();


    public paint() {

    }

    public paint(float Xi, float Yi, ArrayList<Float> Xxs, ArrayList<Float> Yys, int currentColor, int strokeWidth ) {

        this.currentColor = currentColor;
        this.strokeWidth = strokeWidth;
        this.Xi = Xi;
        this.Yi = Yi;
        this.Yys = Yys;
        this.Xxs = Xxs;

    }


}
