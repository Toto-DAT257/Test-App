package com.example.email_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class StartFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Button start = view.findViewById(R.id.start);
        Button logOut = view.findViewById(R.id.log_out);
        Button toto = view.findViewById(R.id.toto);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_loggedIn);
            }
        });
        toto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_blankFragment);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("username", "no username");
                editor.putString("URL", null);
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_registration2);
            }
        });


        String username = sharedPref.getString("username", "no username");
        ((TextView)view.findViewById(R.id.hello)).setText("Hello " + username);

        return view;

    }
}