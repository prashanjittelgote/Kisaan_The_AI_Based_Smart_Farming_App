//package com.mountreachsolution.kisaan.HomeAct;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.Request;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.mountreachsolution.kisaan.Adapter.NewsAdapter;
//import com.mountreachsolution.kisaan.POPJO.NewsModel;
//import com.mountreachsolution.kisaan.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewsActivity extends AppCompatActivity {
//    RecyclerView recyclerNews;
//    List<NewsModel> newsList;
//    NewsAdapter adapter;
//
//    String url = "https://newsdata.io/api/1/news?apikey=pub_ab4fa63c77604939b313e9f6091954c7&country=in&language=en";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.Profil));
//        recyclerNews = findViewById(R.id.recyclerNews);
//        recyclerNews.setLayoutManager(new LinearLayoutManager(this));
//
//        newsList = new ArrayList<>();
//        adapter = new NewsAdapter(this, newsList);
//        recyclerNews.setAdapter(adapter);
//
//        fetchNews();
//
//    }
//    private void fetchNews() {
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                response -> {
//
//                    try {
//                        JSONObject obj = new JSONObject(response);
//                        JSONArray array = obj.getJSONArray("results");
//
//                        for (int i = 0; i < array.length(); i++) {
//
//                            JSONObject data = array.getJSONObject(i);
//
//                            String title = data.getString("title");
//                            String desc = data.optString("description");
//                            String image = data.optString("image_url");
//                            String link = data.getString("link");
//
//                            newsList.add(new NewsModel(title, desc, image, link));
//                        }
//
//                        adapter.notifyDataSetChanged();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                },
//                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());
//
//        Volley.newRequestQueue(this).add(request);
//    }
//}
package com.mountreachsolution.kisaan.HomeAct;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.kisaan.Adapter.NewsAdapter;
import com.mountreachsolution.kisaan.POPJO.NewsModel;
import com.mountreachsolution.kisaan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    RecyclerView recyclerNews;
    List<NewsModel> newsList;
    NewsAdapter adapter;

    String url = "https://newsdata.io/api/1/news?apikey=pub_ab4fa63c77604939b313e9f6091954c7&q=farming&country=in&language=mr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerNews = findViewById(R.id.recyclerNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(this, newsList);
        recyclerNews.setAdapter(adapter);

        fetchNews();
    }

    private void fetchNews() {

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {

                    try {

                        JSONObject obj = new JSONObject(response);
                        JSONArray array = obj.getJSONArray("results");

                        newsList.clear();

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject data = array.getJSONObject(i);

                            String title = data.optString("title");
                            String desc = data.optString("description");
                            String image = data.optString("image_url");
                            String link = data.optString("link");

                            newsList.add(new NewsModel(title, desc, image, link));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(NewsActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {

            // ⭐ UTF-8 Fix for Marathi Text
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, "UTF-8");
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}