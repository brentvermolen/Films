package com.example.brent.films;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.GebruikersAdapter;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Class.MyQueue;
import com.example.brent.films.DB.AanvraagDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.Model.Aanvraag;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeheerActivity extends AppCompatActivity {

    Toolbar mToolbar;

    ImageView imgRandHeader;

    Button btnFilms;
    Button btnGebruikers;

    LinearLayout llFilms;
    LinearLayout llGebruikers;

    Button btnFilmToevoegen;
    Button btnBekijkAanragen;

    ListView lstGebruikers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beheer);

        setTitle("");

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgRandHeader = (ImageView) findViewById(R.id.imgRandHeader);
        Random rand = new Random();
        int randId = rand.nextInt(DAC.Films.size());
        Bitmap bm = null;
        try {
            bm = Methodes.getBitmapFromAsset(this, "films/" + DAC.Films.get(randId).getId() + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgRandHeader.setImageBitmap(bm);

        btnFilms = findViewById(R.id.btnFilms);
        btnGebruikers = findViewById(R.id.btnGebruikers);

        llFilms = findViewById(R.id.llFilms);
        llGebruikers = findViewById(R.id.llGebruikers);

        btnFilmToevoegen = findViewById(R.id.btnFilmToevoegen);
        btnBekijkAanragen = findViewById(R.id.btnAanvragenBekijken);

        lstGebruikers = findViewById(R.id.lstGebruikers);
        GebruikersAdapter adapter = new GebruikersAdapter(this);
        lstGebruikers.setAdapter(adapter);
    }

    private void handleEvents() {
        btnFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFilms.setEnabled(false);
                btnGebruikers.setEnabled(true);

                llGebruikers.setVisibility(View.GONE);
                llFilms.setVisibility(View.VISIBLE);
            }
        });

        btnGebruikers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFilms.setEnabled(true);
                btnGebruikers.setEnabled(false);

                llFilms.setVisibility(View.GONE);
                llGebruikers.setVisibility(View.VISIBLE);
            }
        });

        btnFilmToevoegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeheerActivity.this, NieuweZoekenActivity.class);
                startActivity(intent);
            }
        });
        btnBekijkAanragen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeheerActivity.this, AanvragenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
