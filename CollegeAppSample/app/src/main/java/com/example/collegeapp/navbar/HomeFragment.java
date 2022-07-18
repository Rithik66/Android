package com.example.collegeapp.navbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.collegeapp.R;

import java.util.List;


public class HomeFragment extends Fragment {

    List<SlideModel> slideModelList;
    ImageSlider imageSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageSlider = view.findViewById(R.id.imageSlider);
        slideModelList.add(new SlideModel("", ScaleTypes.FIT));
        slideModelList.add(new SlideModel("https://bit.ly/3fLJf72", ScaleTypes.FIT));

        imageSlider.setImageList(slideModelList);
        return view;
    }
}