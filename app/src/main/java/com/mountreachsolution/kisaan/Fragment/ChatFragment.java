package com.mountreachsolution.kisaan.Fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.kisaan.Adapter.CommunityAdapter;
import com.mountreachsolution.kisaan.POPJO.CommunityModel;
import com.mountreachsolution.kisaan.R;
import com.mountreachsolution.kisaan.common.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    SearchView searchWorker;
    RecyclerView recyclerWorkers;

    ArrayList<CommunityModel> list;
    CommunityAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        if (getActivity() != null) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(
                    ContextCompat.getColor(requireContext(), R.color.Profil)
            );
        }

        recyclerWorkers = view.findViewById(R.id.recyclerWorkers);
        searchWorker = view.findViewById(R.id.searchWorker);
        recyclerWorkers.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new CommunityAdapter(getContext(), list);
        recyclerWorkers.setAdapter(adapter);
        fetchCommunity();
        return view;
    }

    private void fetchCommunity() {



        StringRequest request = new StringRequest(Request.Method.GET, Urls.getComunity,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        Log.d("API_RESPONSE", "Raw Response: " + response);

                        if (response == null || response.trim().isEmpty()) {
                            Toast.makeText(getContext(), "Empty response from server", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (obj.getInt("success") == 1) {

                            JSONArray array = obj.getJSONArray("community");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                CommunityModel model = new CommunityModel(
                                        data.getString("id"),
                                        data.getString("name"),
                                        data.getString("mobileno"),
                                        data.getString("email"),
                                        data.getString("city"),
                                        data.getString("coname"),
                                        data.getString("count"),
                                        data.getString("charge")
                                );

                                list.add(model);
                            }

                            adapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}