package com.mountreachsolution.kisaan.Fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mountreachsolution.kisaan.Adapter.AgentAdapter;
import com.mountreachsolution.kisaan.POPJO.AgentModel;
import com.mountreachsolution.kisaan.R;
import com.mountreachsolution.kisaan.common.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerAgents;

    private AgentAdapter agentAdapter;
    private List<AgentModel> agentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_market, container, false);
        // Status bar color
        if (getActivity() != null) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(
                    ContextCompat.getColor(requireContext(), R.color.Profil)
            );
        }

        // Find IDs
        searchView = view.findViewById(R.id.searchView);
        recyclerAgents = view.findViewById(R.id.recyclerAgents);

        // Initialize List
        agentList = new ArrayList<>();

        // Setup RecyclerView
        recyclerAgents.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        recyclerAgents.setHasFixedSize(true);

        // Initialize Adapter
        agentAdapter = new AgentAdapter(getContext(), agentList);
        recyclerAgents.setAdapter(agentAdapter);

        // Call API
        fetchAgents();
        return view;
    }
    private void fetchAgents() {

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.GET, Urls.getAgent,
                response -> {

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getInt("success") == 1) {

                            JSONArray array = obj.getJSONArray("agents");
                            agentList.clear();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                AgentModel model = new AgentModel(
                                        data.getString("id"),
                                        data.getString("name"),
                                        data.getString("city"),
                                        data.getString("mobileno"),
                                        data.getString("email"),
                                        data.getString("image")
                                );

                                agentList.add(model);
                            }

                            agentAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getContext(),
                                    "No agents found",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),
                                "Parsing error",
                                Toast.LENGTH_SHORT).show();
                    }

                },
                error -> Toast.makeText(getContext(),
                        "Volley Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

}