package com.example.collegeapp.ui.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.collegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    ImageSlider imageSlider;
    VideoView videoView;
    ImageView gmaps;
    Uri videoUri;
    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        videoView = (VideoView) view.findViewById(R.id.videoView);
        imageSlider = (ImageSlider)view.findViewById(R.id.imageSlider);
        List<SlideModel> slideModelList = new ArrayList<>();
        //videoUri = Uri.parse("android.resource://com.example.collegeapp/"+R.raw.learnbeyond);
        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/college-app-8a166.appspot.com/o/Videos%2Flearnbeyond.mp4?alt=media&token=c4feb064-011a-44c4-864b-089309a66009");
        videoView.setVideoURI(videoUri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                if(!mp.isPlaying()){
                    videoView.start();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Slider").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    slideModelList.add(new SlideModel(dataSnapshot.child("url").getValue().toString(),ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModelList,ScaleTypes.FIT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imageSlider.setImageList(slideModelList,ScaleTypes.FIT);

        gmaps = view.findViewById(R.id.gmaps);
        Uri mapuri = Uri.parse("android.resource://com.example.collegeapp/"+R.mipmap.gmaps);
        gmaps.setImageURI(mapuri);
        gmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=KPR Institute of Engineering and Technology");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        videoView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        videoView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }
}