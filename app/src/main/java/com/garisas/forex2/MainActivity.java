package com.garisas.forex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout _swipeRefreshLayout1;
    private RecyclerView _reRecyclerView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initSwipeRefreshLayout();
        _reRecyclerView1 = findViewById(R.id.recylerView1);
        bindRecylerView();
    }

    private void bindRecylerView() {
        String url = "https://openexchangerates.org/api/latest.json?app_id=89da3526069e4d7fa60d9f896d8e180f";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject root;

                try {
                    root = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject rates, timeStamps;

                try {
                    rates = root.getJSONObject("rates");
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                ForexAdapter adapter = new ForexAdapter(rates);
                _reRecyclerView1.setLayoutManager(layoutManager);
                _reRecyclerView1.setAdapter(adapter);
                _swipeRefreshLayout1.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSwipeRefreshLayout() {
        _swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);
        _swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindRecylerView();
            }
        });
    }
}