package com.example.email_test;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;


public class Registration extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        EditText editText = view.findViewById(R.id.editTextTextEmailAddress);
        Button button = view.findViewById(R.id.button);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String stored = sharedPref.getString("username", "no username");

        if (!stored.equals("no username")) {
            Navigation.findNavController(getActivity(), R.id.fragmentContainerView).navigate(R.id.action_registration_to_startFragment);
        }


        button.setOnClickListener(view1 -> {
            String input = editText.getText().toString();
            if (input.isEmpty()){
                return;
            }
            String dbURL = "https://email-test-35515-default-rtdb.europe-west1.firebasedatabase.app/";
            FirebaseDatabase database = FirebaseDatabase.getInstance(dbURL);
            DatabaseReference dbUser = database.getReference().child("users").child(input);

            dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        Log.e(TAG,"Not exist");
                    } else {
                        Log.e(TAG,"Exist");
                        //String value = dataSnapshot.getValue(String.class);
                        //Log.e(TAG, "Value is: " + value);


                        editor.putString("username", input);
                        editor.apply();

                        Navigation.findNavController(view).navigate(R.id.action_registration_to_startFragment);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,"Error while reading data");
                }
            });








        });


        return view;
    }

    private void readFromDB(String search) {
        // get db reference
        String dbURL = "https://email-test-35515-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(dbURL);
        DatabaseReference myRef = database.getReference("users");
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String value;
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String DBResult = dataSnapshot.child(search).getValue(String.class);
                Log.e(TAG, "Value is: " + DBResult);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}