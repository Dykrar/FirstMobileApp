package com.example.firstapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentButton extends Fragment implements View.OnClickListener {






    public FragmentButton() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_button, container, false);
        ((Button) v.findViewById(R.id.button)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.button2)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.button3)).setOnClickListener(this);
        return v;


    }

    public void onClick(View view) {


        int orientation = getResources().getConfiguration().orientation;



        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (view.getId()) {
                case R.id.button:
                    Fragment1 fragment1 = new Fragment1();
                    FragmentManager fragmentManager1 = getFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.fragment_show, fragment1);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                    break;

                case R.id.button2:
                    Fragment2 fragment2 = new Fragment2();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_show, fragment2);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
                    break;

                case R.id.button3:
                    Fragment3 fragment3 = new Fragment3();
                    FragmentManager fragmentManager3 = getFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_show, fragment3);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    break;
            }


        }
        else {
            switch (view.getId()){
                case R.id.button:
                    Fragment1 fragment1 = new Fragment1();
                    FragmentManager fragmentManager1 = getFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.fragment, fragment1);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                    break;

                case R.id.button2:
                    Fragment2 fragment2 = new Fragment2();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment, fragment2);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
                    break;

                case R.id.button3:
                    Fragment3 fragment3 = new Fragment3();
                    FragmentManager fragmentManager3 = getFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment, fragment3);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    break;
            }

        }





    }
}