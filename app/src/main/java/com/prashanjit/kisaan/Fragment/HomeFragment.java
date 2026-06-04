package com.prashanjit.kisaan.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.prashanjit.kisaan.HomeAct.AboutActivity;
import com.prashanjit.kisaan.HomeAct.GovSchemesActivity;
import com.prashanjit.kisaan.HomeAct.HowItWorksActivity;
import com.prashanjit.kisaan.HomeAct.NewsActivity;
import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.common.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    private View view;

    private TextView tvWelcome, tvSubHeader;

    private CardView cardAbout, cardHowWorks, cardNews, cardGov;
    CircleImageView ivProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sp = requireActivity().getSharedPreferences("UserData", getContext().MODE_PRIVATE);
        String name = sp.getString("name", "User");
        String job = sp.getString("role", "Farmer");
        String mobile = sp.getString("mobileno", "Farmer");


        initViews();
        setClickListeners();
        getUserProfile(mobile);

        return view;
    }
    private void initViews() {

        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvSubHeader = view.findViewById(R.id.tvSubHeader);

        cardAbout = view.findViewById(R.id.cardAbout);
        cardHowWorks = view.findViewById(R.id.cardHowWorks);
        cardNews = view.findViewById(R.id.cardNews);
        cardGov = view.findViewById(R.id.cardGov);
        ivProfile = view.findViewById(R.id.ivProfile);
    }
    private void setClickListeners() {

        cardAbout.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AboutActivity.class)));

        cardHowWorks.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), HowItWorksActivity.class)));

        cardNews.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), NewsActivity.class)));

        cardGov.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), GovSchemesActivity.class)));

    }
    private void getUserProfile(String mobile) {


        StringRequest request = new StringRequest(Request.Method.POST, Urls.getUsser,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        int success = jsonObject.getInt("success");

                        if (success == 1) {

                            JSONObject user = jsonObject.getJSONObject("user");

                            String name = user.getString("name");
                            String email = user.getString("email");
                            String mobile1 = user.getString("mobileno");
                            String image = user.getString("image");

                            tvWelcome.setText("Welcome, "+name);



                            String imageUrl = Urls.imageAddress + image;

                            Glide.with(getActivity())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.kisan_logorb)
                                    .into(ivProfile);

                        } else {

                            Toast.makeText(getContext(),
                                    "User not found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mobileno", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}