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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class BlankFragment extends Fragment {

    String json;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        TextView question = view.findViewById(R.id.question);
        TextView type = view.findViewById(R.id.type);
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_startFragment);
            }
        });
        // "http://api.touch-and-tell.se/checkin/624b4f6fa23e9500043e154b"; link for getting data from API
        // "http://collect.touch-and-tell.se/v2.35.7/generic.html?device=624b4f6fa23e9500043e154b&redirect=true"; survey link in database


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String user = sharedPref.getString("username", null); // get username

        String dbURL = "https://email-test-35515-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(dbURL);
        DatabaseReference dbUser = database.getReference().child("users").child(user);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String URL = dataSnapshot.getValue(String.class);

                StringBuilder sb = new StringBuilder(); // build api link from survey link
                sb.append("https://api.touch-and-tell.se/checkin/");
                String id = URL.substring(61, 85);
                sb.append(id);
                String API = sb.toString();


                // fetch JSON from API
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        API,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Rest Response", response.toString());
                                json = response.toString();

                                // Turn string into another json object and query that to get question text and type
                                JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
                                JsonArray questions = convertedObject.getAsJsonArray("questions");
                                JsonObject q1 = questions.get(0).getAsJsonObject();
                                String text = q1.getAsJsonArray("text").get(0).getAsJsonObject().get("text").getAsString();
                                String type1 = q1.get("type").getAsString();
                                question.setText(text);
                                type.setText(type1);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Rest Response", error.toString());
                            }
                        }
                );

                requestQueue.add(objectRequest);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error while reading data");
            }
        });




        return view;
    }
}