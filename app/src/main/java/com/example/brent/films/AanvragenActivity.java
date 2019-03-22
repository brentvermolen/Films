package com.example.brent.films;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brent.films.Class.AanvragenGridView;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Class.MyQueue;
import com.example.brent.films.DB.AanvraagDAO;
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

public class AanvragenActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ImageView imgRandHeader;
    ProgressBar prgLoading;
    AanvraagDAO dao;

    GridView grdAanvragen;

    List<Film> films;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aanvragen);

        setTitle("");

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dao = FilmsDb.getDatabase(this).aanvraagDAO();
        films = new ArrayList<>();

        initViews();
        handleEvents();

        setAanvragen();
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

        prgLoading = findViewById(R.id.prgLoading);

        grdAanvragen = findViewById(R.id.grdAanvragen);
    }

    private void handleEvents() {

    }



    private void setAanvragen() {
        List<Aanvraag> aanvragen = dao.getAll();

        for (final Aanvraag aanvraag : aanvragen){
            String url = "https://api.themoviedb.org/3/movie/" + aanvraag.getFilm_ID() + "?api_key=2719fd17f1c54d219dedc3aa9309a1e2&language=nl-BE&append_to_response=videos,credits";
            final Film filmToAdd = new Film();

            final List<FilmTags> filmTags = new ArrayList<>();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            new AsyncTask<Void, Film, Void>(){
                                @Override
                                protected void onPreExecute() {
                                    prgLoading.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        filmToAdd.setId(response.getInt("id"));
                                        filmToAdd.setReleaseDate(dateFormat.parse(response.getString("release_date")));
                                        filmToAdd.setPosterPath(response.getString("poster_path"));

                                        publishProgress(filmToAdd);
                                    } catch (JSONException e) {
                                        Log.e("Aanvragen", "Film " + aanvraag.getFilm_ID() + ": Mislukt: " + e.getMessage());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    prgLoading.setVisibility(View.GONE);
                                }

                                @Override
                                protected void onProgressUpdate(Film... values) {
                                    films.add(values[0]);
                                    AanvragenGridView adapter = new AanvragenGridView(AanvragenActivity.this, films);
                                    grdAanvragen.setAdapter(adapter);
                                }
                            }.execute();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Aanvragen", "Film " + aanvraag.getFilm_ID() + ": Mislukt: " + error.getMessage());
                }
            });

            MyQueue.GetInstance(AanvragenActivity.this).add(request);
        }
    }
}
