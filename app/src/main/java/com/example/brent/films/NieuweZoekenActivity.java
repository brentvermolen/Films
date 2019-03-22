package com.example.brent.films;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Class.MyQueue;
import com.example.brent.films.Class.NieuweZoekenAdapter;
import com.example.brent.films.Model.Film;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NieuweZoekenActivity extends AppCompatActivity {

    Toolbar mToolbar;

    EditText txtNaam;
    EditText txtJaartal;
    Button btnZoeken;
    TextView lblProgress;
    ListView lstResultaten;

    ImageView imgRandHeader;

    ProgressBar prgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_zoeken);

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

        txtNaam = findViewById(R.id.txtZoeken);
        txtJaartal = findViewById(R.id.txtJaartal);
        btnZoeken = findViewById(R.id.btnZoek);
        lblProgress = findViewById(R.id.lblProgress);
        lstResultaten = findViewById(R.id.lstResultaten);

        prgLoading = findViewById(R.id.prgLoading);
    }

    private void handleEvents() {
        btnZoeken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.themoviedb.org/3/search/movie?api_key=2719fd17f1c54d219dedc3aa9309a1e2&language=nl-BE&query=" + txtNaam.getText().toString() + "&page=1&year=" + txtJaartal.getText().toString();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                new AsyncTask<Void, Integer, List<Film>>(){
                                    @Override
                                    protected void onPreExecute() {
                                        prgLoading.setVisibility(View.VISIBLE);
                                        btnZoeken.setEnabled(false);
                                    }

                                    @Override
                                    protected List<Film> doInBackground(Void... voids) {
                                        List<Film> films = new ArrayList<>();

                                        try {
                                            int count = response.getInt("total_results");

                                            for (int i = 0; i < response.getJSONArray("results").length(); i++){
                                                JSONObject film = response.getJSONArray("results").getJSONObject(i);

                                                Film f = new Film();
                                                f.setId(film.getInt("id"));
                                                f.setNaam(film.getString("title"));
                                                f.setOmschrijving(film.getString("overview"));
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                f.setReleaseDate(dateFormat.parse(film.getString("release_date")));
                                                f.setPosterPath(film.getString("poster_path"));

                                                films.add(f);
                                                publishProgress(i+ 1, count);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        return films;
                                    }

                                    @Override
                                    protected void onPostExecute(List<Film> films) {
                                        NieuweZoekenAdapter adapter = new NieuweZoekenAdapter(NieuweZoekenActivity.this, films, prgLoading);
                                        lstResultaten.setAdapter(adapter);

                                        btnZoeken.setEnabled(true);
                                        prgLoading.setVisibility(View.GONE);
                                    }

                                    @Override
                                    protected void onProgressUpdate(Integer... values) {
                                        lblProgress.setText("Films (" + values[0] + "/" + values[1] + ")");
                                    }
                                }.execute();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });

                jsonObjectRequest.setShouldCache(false);

                MyQueue.GetInstance(NieuweZoekenActivity.this).add(jsonObjectRequest);
            }
        });
    }
}
