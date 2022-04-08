package com.example.email_test;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


public class loggedIn extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logged_in, container, false);



        WebView web_view = new WebView(getActivity());
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.setInitialScale(1);
        getActivity().setContentView(web_view);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String user = sharedPref.getString("username", null);

        String dbURL = "https://email-test-35515-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(dbURL);
        DatabaseReference dbUser = database.getReference().child("users").child(user);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                web_view.loadUrl(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error while reading data");
            }
        });


        //String durl = "http://collect.touch-and-tell.se/v2.35.4/generic.html?device=624b4f6fa23e9500043e154b&redirect=true";
        //web_view.loadUrl(durl);

        return view;
    }
}