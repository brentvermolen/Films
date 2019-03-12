package com.example.brent.films;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.DB.ActeurFilmsDAO;
import com.example.brent.films.DB.ActeursDAO;
import com.example.brent.films.DB.CollectiesDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmTagsDAO;
import com.example.brent.films.DB.FilmsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    TextView lblProgress;

    FilmsDAO filmsDAO;
    CollectiesDAO collectiesDAO;
    ActeursDAO acteursDAO;
    GenresDAO genresDAO;
    ActeurFilmsDAO acteurFilmsDAO;
    FilmTagsDAO filmTagsDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        filmsDAO = FilmsDb.getDatabase(this).filmsDAO();
        collectiesDAO = FilmsDb.getDatabase(this).collectiesDAO();
        acteursDAO = FilmsDb.getDatabase(this).acteursDAO();
        genresDAO = FilmsDb.getDatabase(this).genresDAO();
        acteurFilmsDAO = FilmsDb.getDatabase(this).acteurFilmsDAO();
        filmTagsDAO = FilmsDb.getDatabase(this).filmTagsDAO();

        lblProgress = (TextView) findViewById(R.id.lblProgress);

        new AsyncTask<Void, String, Void>(){
            @Override
            protected void onProgressUpdate(String... values) {
                lblProgress.setText(values[0]);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.e("t", "start download");

                SharedPreferences sharedPreferences = SplashScreen.this.getSharedPreferences("lastSynced", MODE_PRIVATE);
                Date date = new Date(sharedPreferences.getLong("time", 1262300400000l)); //= 01/01/2010;

                publishProgress("Bezig met laden van films");

                List<Film> films = DbRemoteMethods.GetFilms(date);
                for (Film f : films){
                    try{
                        filmsDAO.insert(f);
                    }catch (Exception e){
                        Log.e("Films", "No Insert: " + f.getNaam());
                    }
                }

                int count = films.size();
                int progress = 0;
                for (Film f : films){
                    Methodes.download342Poster(f.getId(), f.getPosterPath());
                    publishProgress("Films: " + ++progress + "/" + count);
                }

                publishProgress("Bezig met laden van collecties");

                List<Collectie> collecties = DbRemoteMethods.GetCollecties();
                for (Collectie c : collecties){
                    try{
                        collectiesDAO.insert(c);
                    }
                    catch (Exception e){
                        Log.e("Collecties", "No Insert: " + c.getNaam());
                    }
                }

                publishProgress("Bezig met laden van acteurs");
                List<Acteur> acteurs = DbRemoteMethods.GetActeurs(date);
                for (Acteur a : acteurs){
                    try{
                        acteursDAO.insert(a);
                    }catch (Exception e){
                        Log.e("Acteurs", "Insert: " + a.getNaam());
                    }
                }

                count = acteurs.size();
                progress = 0;
                for (Acteur a : acteurs){
                    Methodes.download92Poster(a.getId(), a.getPosterPath());
                    publishProgress("Acteurs: " + ++progress + "/" + count);
                }

                List<ActeurFilm> lstAf = DbRemoteMethods.GetFilmsActeurs(date);
                for(ActeurFilm af : lstAf) {
                    try{
                        acteurFilmsDAO.insert(af);
                    }catch (Exception e){ }
                }

                publishProgress("Bezig met laden van genres");
                List<Tag> tags = DbRemoteMethods.GetTags();
                for (Tag t : tags){
                    try{
                        genresDAO.insert(t);
                    }catch (Exception e){
                        Log.e("Genres", "No Insert: " + t.getNaam());
                    }
                }

                List<FilmTags> lstFt = DbRemoteMethods.GetFilmTags();
                for (FilmTags ft : lstFt){
                    try {
                        filmTagsDAO.insert(ft);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                publishProgress("Lokale gegevens laden");

                DAC.Films = filmsDAO.getAll();
                DAC.Collecties = collectiesDAO.getAll();
                DAC.Acteurs = acteursDAO.getAll();
                DAC.ActeurFilms = acteurFilmsDAO.getAll();
                DAC.Tags = genresDAO.getAll();
                DAC.FilmTags = filmTagsDAO.getAll();

                for(Collectie collectie : DAC.Collecties){
                    collectie.setFilms(Methodes.GetMoviesFromCollection(DAC.Films, collectie));
                }

                for(Film film : DAC.Films){
                    film.setCollectie(Methodes.GetCollectionFromID(DAC.Collecties, film.getCollectieID()));
                }

                for (ActeurFilm af : DAC.ActeurFilms){
                    Acteur a = Methodes.FindActeurById(DAC.Acteurs, af.getActeurId());
                    Film f = Methodes.FindFilmById(DAC.Films, af.getFilmId());

                    try {
                        af.setActeur(a);
                        af.setFilm(f);

                        lstAf = a.getFilms();
                        lstAf.add(af);
                        a.setFilms(lstAf);
                        lstAf = f.getActeurs();
                        lstAf.add(af);
                        f.setActeurs(lstAf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (FilmTags ft : DAC.FilmTags){
                    Film f = Methodes.FindFilmById(DAC.Films, ft.getFilm_ID());
                    Tag t = Methodes.FindTagById(DAC.Tags, ft.getTag_ID());

                    ft.setFilm(f);
                    ft.setTag(t);

                    lstFt = f.getFilmTags();
                    lstFt.add(ft);
                    f.setGenres(lstFt);

                    lstFt = t.getFilmTags();
                    lstFt.add(ft);
                    t.setFilms(lstFt);
                }

                long lastSynced = System.currentTimeMillis();
                sharedPreferences.edit().putLong("time", lastSynced).commit();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        }.execute();
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
