package com.example.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public  SliderAdapter(Context context){
        this.context = context;
    }


    // Arrays
    public int[] slide_images = {
            R.drawable.ze2,
            R.drawable.chiko,
            R.drawable.di
    };

    public String[] slide_headings = {
            "José",
            "Francisco",
            "Diogo"
    };

    public String[] slide_desc = {
            "José Maria Mendes \n" +
                    "\n"+
                    "fc55954@alunos.fc.ul.pt\n "+
                    "\n"+
                    "“A computer once beat me at chess, but it was no match for me at kick boxing.”\n"+
                    "— Emo Philips"
            ,
            "Francisco Caldeira \n"                     +
                    "\n"+
                    "fc55734@alunos.fc.ul.pt \n"+
                    "\n"+
                    "“Being able to break security doesn’t make you a hacker anymore than being able to hotwire cars makes you an automotive engineer.”\n" +
                    "— Eric Raymond”"
            ,
            "Diogo Rodrigues \n" +
                    "\n"+
                    "fc55740@alunos.fc.ul.pt \n"+
                    "\n"+
                    "“There is only one problem with common sense; it’s not very common.”\n"+
                    "— Milt Bryce"

    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText((slide_desc[position]));

        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);


    }
}
