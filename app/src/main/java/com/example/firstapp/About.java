package com.example.firstapp;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.viewpager.widget.ViewPager;

        import android.os.Bundle;
        import android.text.Html;
        import android.widget.LinearLayout;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.view.View;
        import android.widget.TextView;

public class About extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        mSlideViewPager = (ViewPager) findViewById(R.id.view);
        mDotLayout = (LinearLayout) findViewById(R.id.dots);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        //apply shared preferences
        SharedPreferences sharedPref = getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
        int colorValue = sharedPref.getInt("color", 0);
        if(colorValue ==0){
            mDotLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
        else{
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(colorValue);
        }
    }

    public void addDotsIndicator(int position){


        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorGrey));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}