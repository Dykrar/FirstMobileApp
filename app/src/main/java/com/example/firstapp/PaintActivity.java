package com.example.firstapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import yuku.ambilwarna.AmbilWarnaDialog;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;


public class PaintActivity extends AppCompatActivity{

    private MainCanvas paintView;
    private int defaultColor;
    private int STORAGE_PERMISSION_CODE = 1;
    private ImageView widthImageView;
    private AlertDialog dialogLineWidth;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 500;
    private SensorManager sensorMgr;
    private Sensor ShookSensor;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private SensorEventListener shookeventListener;
    private float maxValue;
    private boolean Shook = false;
    boolean teste;
    int r;
    int g;
    int b;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        paintView = findViewById(R.id.paintView);

        FragmentManager fm = getSupportFragmentManager();
        canvas_fragment fragment = new canvas_fragment();
        fm.beginTransaction().replace(R.id.ap, fragment).commit();

        Toast.makeText(getApplicationContext(), "the canvas will erase on screen rotation", Toast.LENGTH_LONG).show();

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        lightSensor = sensorMgr.getDefaultSensor(TYPE_LIGHT);

        ShookSensor = sensorMgr.getDefaultSensor(TYPE_ACCELEROMETER);

        if (lightSensor == null) {
            Toast.makeText(this, "The device hasn't a light sensor", Toast.LENGTH_SHORT).show();
        }

        maxValue = lightSensor.getMaximumRange();

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if(event.sensor.getType() == TYPE_LIGHT){

                    int color = ((ColorDrawable) paintView.getBackground()).getColor();

                    float value = event.values[0];

                    if(paintView.currentColor == color )
                    {
                        teste = true;
                    }

                    if(teste)
                    {
                        r = Color.red(color);
                        g = Color.green(color);
                        b = Color.blue(color);

                        teste = false ;
                    }

                    int newR = (int) (r * (1 - ( value/maxValue) ));
                    int newG = (int) (g * (1 - ( value/maxValue) ));
                    int newB = (int) (b * (1 - ( value/maxValue) ));

                    paintView.BackgroundDarkLight(Color.rgb(newR, newG, newB));

                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        shookeventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    long curTime = System.currentTimeMillis();

                    if ((curTime - lastUpdate) > 100) {
                        long diffTime = (curTime - lastUpdate);
                        lastUpdate = curTime;

                        x = sensorEvent.values[0];
                        y = sensorEvent.values[1];
                        z = sensorEvent.values[2];

                        float speed = Math.abs(x + y + z - last_x - last_y - last_z)
                                / diffTime * 10000;
                        if (speed > SHAKE_THRESHOLD) {
                            Shook = true;

                            paintView.clear();

                        }
                        last_x = x;
                        last_y = y;
                        last_z = z;
                    }
                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        DisplayMetrics displayMetrics = new DisplayMetrics();


        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        paintView.initialise(displayMetrics);


        paintView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    Toast.makeText(getApplicationContext(), "Background Change", Toast.LENGTH_SHORT).show();
                    paintView.changeBackground();


                    super.onLongPress(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Toast.makeText(getApplicationContext(), "Rotation 180º", Toast.LENGTH_SHORT).show();


                    if (paintView.getRotation() == 0) {
                        paintView.setRotation(180);
                    } else {
                        paintView.setRotation(0);
                    }


                    return super.onDoubleTap(e);
                }

            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_paint, menu);

        return super.onCreateOptionsMenu(menu);

    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Needed to save image")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(PaintActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }

                    })
                    .create().show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Access granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Access denied", Toast.LENGTH_LONG).show();

            }

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.undo:
                paintView.undo();
                return true;
            case R.id.redo:
                paintView.redo();
                return true;

            case R.id.Color:
                openColourPicker();
                break;

            case R.id.lineWidth:
                showLineWidthDialog();
                break;

            case R.id.load:

                paintView.Load();
                return true;

            case R.id.upload:
                paintView.Upload();

        }

        return super.onOptionsItemSelected(item);

    }

    private void openColourPicker() {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

                Toast.makeText(PaintActivity.this, "Unavailable", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                defaultColor = color;

                paintView.setColor(color);

            }

        });

        ambilWarnaDialog.show(); // add

    }

    void showLineWidthDialog() {
        final AlertDialog.Builder[] currentAlertDialog = {new AlertDialog.Builder(this)};
        View view = getLayoutInflater().inflate(R.layout.width_dialog, null);
        final SeekBar seekBar = view.findViewById(R.id.WidthSeekBar);
        final Button setLineWidthButton = view.findViewById(R.id.ApplyWidth);
        widthImageView = view.findViewById(R.id.imageViewPincel);


        setLineWidthButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                paintView.setStrokeWidth(seekBar.getProgress());

                dialogLineWidth.dismiss();
                currentAlertDialog[0] = null;


            }
        });

        seekBar.setOnSeekBarChangeListener(widthSeekBarChange);


        currentAlertDialog[0].setView(view);
        dialogLineWidth = currentAlertDialog[0].create();
        dialogLineWidth.show();


    }


    private SeekBar.OnSeekBarChangeListener widthSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.colorG1));
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            bitmap.eraseColor(getResources().getColor(R.color.colorWhite));
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {


        }
    };

    @Override
    protected void onResume() {
        super.onResume();


            paintView = findViewById(R.id.paintView);
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            boolean accelSupported = sensorMgr.registerListener(shookeventListener, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

            if (!accelSupported) {
                // on accelerometer on this device
                sensorMgr.unregisterListener(shookeventListener, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            }

            sensorMgr.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }



    protected void onPause() {
        super.onPause();


        sensorMgr.unregisterListener(lightEventListener);

        if (sensorMgr != null) {
            sensorMgr.unregisterListener(shookeventListener, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            sensorMgr = null;
        }

    }

}

