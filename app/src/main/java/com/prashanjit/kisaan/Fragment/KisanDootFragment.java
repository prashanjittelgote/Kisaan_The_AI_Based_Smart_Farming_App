package com.prashanjit.kisaan.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.prashanjit.kisaan.KsLoginActivity;
import com.prashanjit.kisaan.ProfilrAct.PrivacyPolicyActivity;
import com.prashanjit.kisaan.ProfilrAct.ReportIssueActivity;
import com.prashanjit.kisaan.ProfilrAct.UpdateProfileActivity;
import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.common.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KisanDootFragment extends Fragment {
    private LinearLayout updateData, labDetails, privacyRow;
    private ImageView editIcon;
    private TextView logoutText, profileName, profileJob;
    CircleImageView cvimge;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_kisan_doot, container, false);
        updateData = view.findViewById(R.id.UpdateData);
        labDetails = view.findViewById(R.id.LabDetails);
        privacyRow = view.findViewById(R.id.privacyRow);
        editIcon = view.findViewById(R.id.editIcon);
        logoutText = view.findViewById(R.id.logoutText);
        profileName = view.findViewById(R.id.profileName);
        profileJob = view.findViewById(R.id.profileJob);
        cvimge = view.findViewById(R.id.profileImage);


        // 🔹 Load user data from SharedPreferences
        SharedPreferences sp = requireActivity().getSharedPreferences("UserData", getContext().MODE_PRIVATE);
        String name = sp.getString("name", "User");
        String job = sp.getString("role", "Farmer");
        String mobile = sp.getString("mobileno", "Farmer");
        getUserProfile(mobile);

        profileName.setText(name);
        profileJob.setText(job);

        // 🔹 Click Listeners

        updateData.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
        });

        labDetails.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReportIssueActivity.class));
        });

        privacyRow.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
        });

        editIcon.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
        });

        logoutText.setOnClickListener(v -> {
            logoutUser();
        });

        return view;
    }
    private void logoutUser() {
        SharedPreferences sp = requireActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();   // removes all saved login data
        editor.apply();

        // Redirect to Login screen
        Intent intent = new Intent(getContext(), KsLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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



                            String imageUrl = Urls.imageAddress + image;

                            Glide.with(getActivity())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.kisan_logorb)
                                    .into(cvimge);

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