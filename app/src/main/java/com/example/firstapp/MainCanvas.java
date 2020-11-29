package com.example.firstapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class MainCanvas extends View {

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    public static int BRUSH_SIZE = 10;
    public int currentColor;
    boolean load = false;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("Drawings");
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int height;
    private int width;
    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();
    private ArrayList<paint> desenho = new ArrayList<>();
    private float Xi;
    private float Yi;
    private ArrayList<Float> Xxs = new ArrayList<>();
    private ArrayList<Float> Yys = new ArrayList<>();
    private paint linha;


    public MainCanvas(Context context) {

        super(context, null);

    }

    public MainCanvas(Context context, AttributeSet attrs) {

        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        setBackgroundColor(DEFAULT_COLOR);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

    }


    public void initialise(DisplayMetrics displayMetrics) {

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBitmap.isMutable();
        mCanvas = new Canvas(mBitmap);


        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

        invalidate();

    }


    @Override

    protected void onDraw(Canvas canvas) {


        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (Draw draw : paths) {

            mPaint.setColor(draw.color);
            mPaint.setStrokeWidth(draw.strokeWidth);
            mPaint.setMaskFilter(null);

            mCanvas.drawPath(draw.path, mPaint);


        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.restore();

    }

    private void touchStart(float x, float y) {

        mPath = new Path();

        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        paths.add(draw);

        mPath.reset();
        mPath.moveTo(x, y);

        mX = x;
        mY = y;


    }

    private void touchMove(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            mX = x;
            mY = y;
        }
    }

    private void touchUp() {

        mPath.lineTo(mX, mY);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                Xi = x;
                Yi = y;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                linha = new paint(Xi, Yi, Xxs, Yys, currentColor, strokeWidth);
                desenho.add(linha);
                Xxs = new ArrayList<Float>();
                Yys = new ArrayList<Float>();


                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                Xxs.add(x);
                Yys.add(y);
                invalidate();
                break;
        }


        return true;
    }

    public void clear() {

        backgroundColor = DEFAULT_BG_COLOR;

        paths.clear();
        invalidate();

    }

    public void undo() {

        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nothing to undo", Toast.LENGTH_LONG).show();
        }
    }

    public void redo() {

        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nothing to redo", Toast.LENGTH_LONG).show();
        }
    }

    public void setStrokeWidth(int width) {

        strokeWidth = width;

    }

    public void setColor(int color) {

        currentColor = color;
    }

    public void Load() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                Xxs.clear();
                Yys.clear();

                for (DataSnapshot dataSnapshot : Snapshot.child("Desenho").getChildren()) {
                    currentColor = dataSnapshot.child("currentColor").getValue(Integer.class);
                    strokeWidth = dataSnapshot.child("strokeWidth").getValue(Integer.class);
                    Xi = dataSnapshot.child("xi").getValue(Float.class);
                    Yi = dataSnapshot.child("yi").getValue(Float.class);
                    Yys = (ArrayList<Float>) dataSnapshot.child("yys").getValue();
                    Xxs = (ArrayList<Float>) dataSnapshot.child("xxs").getValue();

                    if (Xxs != null) {
                        float[] floatValues = new float[Xxs.size()];
                        for (int i = 0; i < Xxs.size(); i++) {
                            floatValues[i] = Float.parseFloat(String.valueOf(Xxs.get(i)));
                        }

                        float[] floatValuesY = new float[Yys.size()];
                        for (int i = 0; i < Yys.size(); i++) {
                            floatValuesY[i] = Float.parseFloat(String.valueOf(Yys.get(i)));
                        }

                        mPath = new Path();
                        Draw draw = new Draw(currentColor, strokeWidth, mPath);
                        paths.add(draw);
                        mPath.reset();
                        mPath.moveTo(Xi, Yi);
                        mX = Xi;
                        mY = Yi;


                        for (int i = 0; i < floatValues.length; i++) {
                            for (i = 0; i < floatValuesY.length; i++) {

                                float x = floatValues[i];
                                float y = floatValuesY[i];
                                float dx = Math.abs(x - mX);
                                float dy = Math.abs(y - mY);
                                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

                                    mX = x;
                                    mY = y;
                                }

                            }
                        }

                        mPath.lineTo(mX, mY);

                    } else {
                        mPath = new Path();
                        Draw draw = new Draw(currentColor, strokeWidth, mPath);
                        paths.add(draw);
                        mPath.reset();
                        mPath.moveTo(Xi, Yi);
                        mX = Xi;
                        mY = Yi;

                        float dx = Math.abs(Xi - mX);
                        float dy = Math.abs(Yi - mY);
                        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                            mPath.quadTo(mX, mY, (Xi + mX) / 2, (Yi + mY) / 2);

                            mX = Xi;
                            mY = Yi;
                        }

                        mPath.lineTo(mX, mY);


                    }


                }
                backgroundColor = Snapshot.child("background").getValue(Integer.class);
                setBackgroundColor(backgroundColor);

                invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void Upload() {

        reference.child("Desenho").setValue(desenho);
        reference.child("background").setValue(backgroundColor);


    }

    public void changeBackground() {
        backgroundColor = currentColor;
        setBackgroundColor(backgroundColor);
    }

    public void BackgroundDarkLight(int color) {
        backgroundColor = color;
        setBackgroundColor(backgroundColor);
    }

    public void BackgroundDarkLight1(int color) {
        backgroundColor = color;
        setBackgroundColor(backgroundColor);
    }

}
