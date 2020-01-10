package com.example.backendlesscollectionusage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;


public class MainActivity extends AppCompatActivity {

    private static final String APP_ID = "<your app id>";
    private static final String API_KEY = "your api key";

    private RecyclerView autoload_recyclerView;
    private CustomRecyclerViewAdapter autoload_recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Backendless.initApp(this, APP_ID, API_KEY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoload_recyclerViewAdapter = new CustomRecyclerViewAdapter( this, 20, 70);

        autoload_recyclerView = findViewById(R.id.autoload_recyclerView);
        autoload_recyclerView.setHasFixedSize(true);
        autoload_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        autoload_recyclerView.setAdapter(autoload_recyclerViewAdapter);
    }
}

