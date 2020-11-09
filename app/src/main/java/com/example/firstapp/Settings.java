package com.example.firstapp;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import yuku.ambilwarna.AmbilWarnaDialog;

public class Settings extends AppCompatActivity {
    RelativeLayout sLayout;
    int mDefaultColor;
    Button mButton;
    Button bButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        sLayout = (RelativeLayout) findViewById(R.id.layout_settings);
        mDefaultColor = ContextCompat.getColor(Settings.this, R.color.colorPrimary);

        mButton = (Button) findViewById(R.id.button_color);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        bButton = (Button) findViewById(R.id.menu_button);
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker2();
            }
        });

        //set shared preference
        SharedPreferences sharepref = getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
        int colorValue = sharepref.getInt("color", 0);

        if (colorValue == 0) {
            sLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        } else {
            sLayout.setBackgroundColor(colorValue);
        }

    }

/*



MUDAR COR LETRAS!


///////////////////////////////////////////////////////////////////////////////////
private int getInverseColor(int color){
    int red = Color.red(color);
    int green = Color.green(color);
    int blue = Color.blue(color);
    int alpha = Color.alpha(color);
    return Color.argb(alpha, 255-red, 255-green, 255-blue);
}
////////////////////////////////////////////////////////////////////////////////////


MUDAR COR LETRAS!


 */



    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                SharedPreferences sharepref = Settings.this.getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharepref.edit();
                editor.putInt("color", mDefaultColor);
                editor.apply();
                sLayout.setBackgroundColor(mDefaultColor);

            }
        });
        colorPicker.show();
    }

    public void openColorPicker2() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                SharedPreferences sharepref = Settings.this.getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharepref.edit();
                editor.putInt("color2", mDefaultColor);
                editor.apply();

            }
        });
        colorPicker.show();
    }
}