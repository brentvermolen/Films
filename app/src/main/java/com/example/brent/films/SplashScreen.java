package com.example.brent.films;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.DB.AanvraagDAO;
import com.example.brent.films.DB.ActeurFilmsDAO;
import com.example.brent.films.DB.ActeursDAO;
import com.example.brent.films.DB.ArchiefDAO;
import com.example.brent.films.DB.CollectiesDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmArchiefDAO;
import com.example.brent.films.DB.FilmTagsDAO;
import com.example.brent.films.DB.FilmsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GebruikerArchiefDAO;
import com.example.brent.films.DB.GebruikersDAO;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Aanvraag;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Archief;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmArchief;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Gebruiker;
import com.example.brent.films.Model.GebruikerArchief;
import com.example.brent.films.Model.Tag;

import java.io.IOException;
import java.io.InputStream;
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
    AanvraagDAO aanvraagDAO;
    GebruikersDAO gebruikersDAO;
    ArchiefDAO archiefDAO;
    FilmArchiefDAO filmArchiefDAO;
    GebruikerArchiefDAO gebruikerArchiefDAO;


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
        aanvraagDAO = FilmsDb.getDatabase(this).aanvraagDAO();
        gebruikersDAO = FilmsDb.getDatabase(this).gebruikersDAO();
        archiefDAO = FilmsDb.getDatabase(this).archiefDAO();
        filmArchiefDAO = FilmsDb.getDatabase(this).filmArchiefDAO();
        gebruikerArchiefDAO = FilmsDb.getDatabase(this).gebruikerArchiefDAO();

        lblProgress = (TextView) findViewById(R.id.lblProgress);

        final int user_id = getResources().getInteger(R.integer.gebruiker_id);

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
                //date = new Date(1262300400000l);

                publishProgress("Bezig met laden van films");

                List<Film> films = DbRemoteMethods.GetFilms(date);
                for (Film f : films){
                    try{
                        filmsDAO.insert(f);
                    }catch (Exception e){
                        e.printStackTrace();
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
                        e.printStackTrace();
                    }
                }

                publishProgress("Bezig met laden van acteurs");
                List<Acteur> acteurs = DbRemoteMethods.GetActeurs(date);
                for (Acteur a : acteurs){
                    try{
                        acteursDAO.insert(a);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                count = acteurs.size();
                progress = 0;
                for (Acteur a : acteurs){
                    Methodes.download92Poster(a.getId(), a.getPosterPath());
                    publishProgress("Acteurs: " + ++progress + "/" + count);
                }

                publishProgress("Acteurs bij juiste films zetten");

                List<ActeurFilm> lstAf = DbRemoteMethods.GetFilmsActeurs(date);
                for(ActeurFilm af : lstAf) {
                    try{
                        acteurFilmsDAO.insert(af);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                publishProgress("Bezig met laden van genres");
                List<Tag> tags = DbRemoteMethods.GetTags(date);
                for (Tag t : tags){
                    try{
                        genresDAO.insert(t);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                List<FilmTags> lstFt = DbRemoteMethods.GetFilmTags(date);
                for (FilmTags ft : lstFt){
                    try {
                        filmTagsDAO.insert(ft);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                publishProgress("Aanvragen inladen");

                List<Aanvraag> aanvraags = DbRemoteMethods.GetAanvragen(date);
                for (Aanvraag aanvraag : aanvraags){
                    try{
                        aanvraagDAO.insert(aanvraag);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                publishProgress("Gebruikers laden");

                List<Gebruiker> gebruikers = DbRemoteMethods.GetGebruikers();
                for (Gebruiker gebruiker : gebruikers){
                    try{
                        gebruikersDAO.insert(gebruiker);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                publishProgress("Archieven laden");

                List<Archief> archieven = DbRemoteMethods.GetArchievenVanGebruiker(user_id);
                for (Archief archief : archieven){
                    try{
                        archiefDAO.insert(archief);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                Object[] arr = DbRemoteMethods.GetArchievenEnGebruikerFilms(user_id, date);
                List<FilmArchief> filmArchiefs = (List<FilmArchief>) arr[0];
                List<GebruikerArchief> gebruikerArchiefs = (List<GebruikerArchief>) arr[1];

                for(FilmArchief fa : filmArchiefs){
                    try{
                        filmArchiefDAO.insert(fa);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                for (GebruikerArchief ga : gebruikerArchiefs){
                    try{
                        gebruikerArchiefDAO.insert(ga);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                long lastSynced = System.currentTimeMillis();
                sharedPreferences.edit().putLong("time", lastSynced).commit();

                publishProgress("Lokale gegevens laden");

                DAC.Films = filmsDAO.getAll();
                DAC.Collecties = collectiesDAO.getAll();
                DAC.Acteurs = acteursDAO.getAll();
                DAC.ActeurFilms = acteurFilmsDAO.getAll();
                DAC.Tags = genresDAO.getAll();
                DAC.FilmTags = filmTagsDAO.getAll();
                DAC.Aanvragen = aanvraagDAO.getAll();
                DAC.Gebruikers= gebruikersDAO.getAll();
                DAC.Archieven = archiefDAO.getAll();
                DAC.FilmArchieven = filmArchiefDAO.getAll();
                DAC.GebruikerArchieven = gebruikerArchiefDAO.getAll();

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

                    try{
                        ft.setFilm(f);
                        ft.setTag(t);

                        lstFt = f.getFilmTags();
                        lstFt.add(ft);
                        f.setGenres(lstFt);

                        lstFt = t.getFilmTags();
                        lstFt.add(ft);
                        t.setFilms(lstFt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                for (FilmArchief fa : DAC.FilmArchieven){
                    Archief archief = Methodes.FindArchiefById(DAC.Archieven, fa.getArchief_id());
                    Film film = Methodes.FindFilmById(DAC.Films, fa.getFilm_id());

                    try{
                        fa.setArchief(archief);
                        fa.setFilm(film);

                        filmArchiefs = archief.getFilmArchief();
                        filmArchiefs.add(fa);
                        archief.setFilms(filmArchiefs);

                        filmArchiefs = film.getArchiefs();
                        filmArchiefs.add(fa);
                        film.setArchiefs(filmArchiefs);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                for (GebruikerArchief ga : DAC.GebruikerArchieven){
                    Gebruiker gebruiker = Methodes.FindGebruikerById(DAC.Gebruikers, ga.getGebruiker_id());
                    Archief archief = Methodes.FindArchiefById(DAC.Archieven, ga.getArchief_id());

                    try{
                        ga.setGebruiker(gebruiker);
                        ga.setArchief(archief);

                        gebruikerArchiefs = gebruiker.getGebruikerArchieven();
                        gebruikerArchiefs.add(ga);
                        gebruiker.setArchieven(gebruikerArchiefs);

                        gebruikerArchiefs = archief.getGebruikers();
                        gebruikerArchiefs.add(ga);
                        archief.setGebruikers(gebruikerArchiefs);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

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
