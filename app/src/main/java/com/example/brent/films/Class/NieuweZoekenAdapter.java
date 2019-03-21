package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmTagsDAO;
import com.example.brent.films.DB.FilmsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.Model.Aanvraag;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NieuweZoekenAdapter extends BaseAdapter {
    private Context mContext;
    private List<Film> films;

    private ProgressBar prgLoading;

    public NieuweZoekenAdapter(Context c, List<Film> films, ProgressBar progressBar) {
        mContext = c;

        prgLoading = progressBar;

        this.films = films;
    }

    public int getCount() {
        return films.size();
    }

    public Film getItem(int position) {
        return films.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        TextView lblOmschrijving;
        TextView lblTitel;
        TextView lblJaartal;
        ImageView imgPoster;
        ImageButton btnToevoegen;
        ImageButton btnRequest;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_nieuw_zoeken_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imgPoster = convertView.findViewById(R.id.imgPoster);
            viewHolder.lblOmschrijving = convertView.findViewById(R.id.lblOmschrijving);
            viewHolder.lblTitel = convertView.findViewById(R.id.lblTitel);
            viewHolder.lblJaartal = convertView.findViewById(R.id.lblJaartal);

            viewHolder.btnToevoegen = convertView.findViewById(R.id.btnAdd);
            viewHolder.btnRequest = convertView.findViewById(R.id.btnRequest);

            viewHolder.btnToevoegen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);

                    final Film f = (Film)viewHolder.imgPoster.getTag();

                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected void onPreExecute() {
                            prgLoading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            final Film filmToAdd = new Film();
                            filmToAdd.setId(f.getId());
                            final List<FilmTags> filmTags = new ArrayList<>();

                            String url = "https://api.themoviedb.org/3/movie/" + f.getId() + "?api_key=2719fd17f1c54d219dedc3aa9309a1e2&language=nl-BE&append_to_response=videos,credits";

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                filmToAdd.setCollectieID(response.getJSONObject("belongs_to_collection").getInt("id"));
                                                Collectie collectie = new Collectie();
                                                collectie.setId(filmToAdd.getCollectieID());
                                                collectie.setNaam(response.getJSONObject("belongs_to_collection").getString("name"));
                                                collectie.setPosterPath(response.getJSONObject("belongs_to_collection").getString("poster_path"));
                                                filmToAdd.setCollectie(collectie);

                                                for (int i = 0; i < response.getJSONArray("genres").length(); i++){
                                                    JSONObject genre = response.getJSONArray("genres").getJSONObject(i);
                                                    FilmTags ft = new FilmTags();
                                                    ft.setFilm_ID(filmToAdd.getId());
                                                    ft.setTag_ID(genre.getInt("id"));

                                                    Tag tag = new Tag();
                                                    tag.setId(genre.getInt("id"));
                                                    tag.setNaam(genre.getString("name"));
                                                    ft.setTag(tag);

                                                    filmTags.add(ft);
                                                }
                                                filmToAdd.setGenres(filmTags);

                                                filmToAdd.setOmschrijving(response.getString("overview"));
                                                filmToAdd.setNaam(f.getNaam());
                                                filmToAdd.setReleaseDate(f.getReleaseDate());
                                                filmToAdd.setPosterPath(response.getString("poster_path"));
                                                filmToAdd.setDuur(response.getInt("runtime"));
                                                filmToAdd.setTagline(response.getString("tagline"));

                                                for (int i = 0; i < response.getJSONObject("videos").getJSONArray("results").length(); i++){
                                                    JSONObject video = response.getJSONObject("videos").getJSONArray("results").getJSONObject(i);
                                                    if (video.getString("type").equals("Trailer") && video.getString("site").equals("YouTube")){
                                                        filmToAdd.setTrailerId(video.getString("key"));
                                                        break;
                                                    }
                                                }
                                                List<ActeurFilm> acteurs = new ArrayList<>();
                                                for (int i = 0; i < response.getJSONObject("credits").getJSONArray("cast").length(); i++){
                                                    JSONObject a = response.getJSONObject("credits").getJSONArray("cast").getJSONObject(i);
                                                    if (a.getInt("order") < 15){
                                                        Acteur acteur = new Acteur();
                                                        acteur.setId(a.getInt("id"));
                                                        acteur.setNaam(a.getString("name"));
                                                        acteur.setPosterPath(a.getString("profile_path"));

                                                        ActeurFilm af = new ActeurFilm();
                                                        af.setKarakter(a.getString("character"));
                                                        af.setSort(a.getInt("order"));
                                                        af.setActeurId(acteur.getId());
                                                        af.setActeur(acteur);
                                                        af.setFilmId(filmToAdd.getId());

                                                        acteurs.add(af);
                                                    }
                                                }
                                                filmToAdd.setActeurs(acteurs);

                                                String url = "https://api.themoviedb.org/3/movie/" + f.getId() + "?api_key=2719fd17f1c54d219dedc3aa9309a1e2&language=en-US&append_to_response=videos";

                                                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url, null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                try {
                                                                    if (filmToAdd.getOmschrijving().equals("")) {
                                                                        filmToAdd.setOmschrijving(response.getString("overview"));
                                                                    }

                                                                    if (filmToAdd.getPosterPath().equals("")){
                                                                        filmToAdd.setOmschrijving(response.getString("poster_path"));
                                                                    }

                                                                    if (filmToAdd.getTagline().equals("")){
                                                                        filmToAdd.setTagline(response.getString("tagline"));
                                                                    }

                                                                    if (filmToAdd.getTrailerId() == null){
                                                                        for (int i = 0; i < response.getJSONObject("videos").getJSONArray("results").length(); i++){
                                                                            JSONObject video = response.getJSONObject("videos").getJSONArray("results").getJSONObject(i);
                                                                            if (video.getString("type").equals("Trailer") && video.getString("site").equals("YouTube")){
                                                                                filmToAdd.setTrailerId(video.getString("key"));
                                                                                break;
                                                                            }
                                                                        }
                                                                    }

                                                                    DbRemoteMethods.InsertFilm(filmToAdd);

                                                                    if (!DAC.Collecties.contains(filmToAdd.getCollectie())){
                                                                        DbRemoteMethods.InsertCollectie(filmToAdd.getCollectie());
                                                                    }

                                                                    for (ActeurFilm af : filmToAdd.getActeurs()){
                                                                        if (!DAC.Acteurs.contains(af.getActeur())){
                                                                            DbRemoteMethods.InsertActeur(af.getActeur());
                                                                        }
                                                                        DbRemoteMethods.InsertFilmActeur(af);
                                                                    }

                                                                    for(FilmTags ft : filmTags){
                                                                        if (!DAC.Tags.contains(ft.getTag())){
                                                                            DbRemoteMethods.InsertTag(ft.getTag());
                                                                        }
                                                                        DbRemoteMethods.InsertFilmTag(ft);
                                                                    }

                                                                    Toast.makeText(mContext, "Film toegevoegd! Zal aan je collectie worden toegevoegd wanneer je de app opnieuw opstart!", Toast.LENGTH_LONG).show();
                                                                } catch (JSONException e) {
                                                                    Log.e("Toevoegen", "Film " + f.getId() + ": Mislukt: " + e.getMessage());
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("Toevoegen", "Film " + f.getId() + ": Mislukt: " + error.getMessage());
                                                    }
                                                });

                                                MyQueue.GetInstance(mContext).add(request2);
                                            } catch (JSONException e) {
                                                Log.e("Toevoegen", "Film " + f.getId() + ": Mislukt: " + e.getMessage());
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Toevoegen", "Film " + f.getId() + ": Mislukt: " + error.getMessage());
                                }
                            });

                            MyQueue.GetInstance(mContext).add(request);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            prgLoading.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            });

            viewHolder.btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);

                    Film f = (Film)viewHolder.imgPoster.getTag();
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Film f = films.get(position);
        viewHolder.imgPoster.setTag(f);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    InputStream is = (InputStream) new URL("http://image.tmdb.org/t/p/w342/" + f.getPosterPath()).getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    viewHolder.imgPoster.setImageBitmap(d);
                } catch (Exception e) {

                }

                return null;
            }
        }.execute();

        viewHolder.lblTitel.setText(f.getNaam());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        viewHolder.lblJaartal.setText(dateFormat.format(f.getReleaseDate()));
        viewHolder.lblOmschrijving.setText(f.getOmschrijving());

        viewHolder.btnToevoegen.setVisibility(View.VISIBLE);
        viewHolder.btnRequest.setVisibility(View.VISIBLE);

        Aanvraag aanvraag = new Aanvraag();
        aanvraag.setFilm_ID(f.getId());
        if (DAC.Aanvragen.contains(aanvraag)){
            viewHolder.btnRequest.setVisibility(View.GONE);
        }

        if (DAC.Films.contains(f)){
            viewHolder.btnToevoegen.setVisibility(View.GONE);
            viewHolder.btnRequest.setVisibility(View.GONE);
        }

        return convertView;
    }
}
