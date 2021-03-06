package com.example.brent.films;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films.Class.ActorsFilmGridView;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.DialogEditArchieven;
import com.example.brent.films.Class.DialogEditGenres;
import com.example.brent.films.Class.DialogTextOutput;
import com.example.brent.films.DB.ActeurFilmsDAO;
import com.example.brent.films.DB.ActeursDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmTagsDAO;
import com.example.brent.films.DB.FilmsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmArchief;
import com.example.brent.films.Model.Tag;

import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class FilmActivity extends AppCompatActivity {
    Film film;

    FilmsDAO dao;

    Toolbar mToolbar;

    ImageView imgFilmHeader;
    TextView lblTagline;
    TextView lblTitel;
    TextView lblJaartal;
    TextView lblDuur;

    Button btnOmschrijving;
    Button btnExtraInfo;

    ScrollView llOmschrijving;
    TextView lblOmschrijving;

    LinearLayout llExtraInfo;
    LinearLayout llGenres;

    LinearLayout llCollectie;
    TextView lblCollectie;
    LinearLayout llCollectieInhoud;

    LinearLayout llArchieven;
    TextView lblArchief;
    LinearLayout llArchievenInhoud;

    LinearLayout llTrailer;
    ImageButton btnTrailer;

    GridView grdActors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        film = Methodes.FindFilmById(DAC.Films, getIntent().getIntExtra("film_id", 0));
        if (film == null){
            finish();
            Toast.makeText(this, "De film werd niet gevonden", Toast.LENGTH_SHORT).show();
        }

        dao = FilmsDb.getDatabase(this).filmsDAO();

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgFilmHeader = (ImageView) findViewById(R.id.imgFilmHeader);
        lblTitel = (TextView) findViewById(R.id.lblTitel);
        lblTagline = (TextView) findViewById(R.id.lblTagline);
        lblJaartal = (TextView) findViewById(R.id.lblJaartal);
        lblDuur = (TextView) findViewById(R.id.lblDuur);

        try {
            imgFilmHeader.setImageBitmap(Methodes.getBitmapFromAsset(this, "films/" + film.getId() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblTitel.setText(film.getNaam());
        if (film.getTagline() == null){
            film.setTagline("");
        }
        if (!(film.getTagline().equals(""))){
            lblTagline.setText(film.getTagline());
        }else{
            lblTagline.setVisibility(View.GONE);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(film.getReleaseDate());
        lblJaartal.setText(String.valueOf(c.get(Calendar.YEAR)));
        int uren = film.getDuur() / 60;
        int min = film.getDuur() - (uren * 60);
        lblDuur.setText(" " + uren + "uur " + min + "min.");

        btnOmschrijving = (Button) findViewById(R.id.btnOmschrijving);
        btnExtraInfo = (Button) findViewById(R.id.btnExtraInfo);

        llOmschrijving = (ScrollView) findViewById(R.id.llOmschrijving);
        llExtraInfo = (LinearLayout) findViewById(R.id.llExtraInfo);

        lblOmschrijving = (TextView) findViewById(R.id.lblOmschrijving);
        lblOmschrijving.setText(film.getOmschrijving());

        llGenres = (LinearLayout) findViewById(R.id.llGenres);
        setGenres();

        llCollectie = (LinearLayout) findViewById(R.id.llCollectie);
        lblCollectie = (TextView) findViewById(R.id.lblCollectie);
        llCollectieInhoud = (LinearLayout) findViewById(R.id.llCollectieInhoud);

        if (film.getCollectieID() == 0){
            llCollectie.setVisibility(View.GONE);
        }else{
            lblCollectie.setText(film.getCollectie().getNaam());

            film.getCollectie().getFilms().sort(new Comparator<Film>() {
                @Override
                public int compare(Film o1, Film o2) {
                    return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                }
            });
            for (final Film f : film.getCollectie().getFilms()){
                ImageView img = new ImageView(this);
                img.setId(f.getId());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                img.setLayoutParams(params);
                img.setAdjustViewBounds(true);

                Bitmap bm;
                try {
                    bm = Methodes.getBitmapFromAsset(this, "films/" + f.getId() + ".jpg");
                    bm = Methodes.getRoundedCornerBitmap(bm, 15);
                    img.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (f.getId() == film.getId()){
                    img.setEnabled(false);
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FilmActivity.this, FilmActivity.class);
                        intent.putExtra("film_id", f.getId());
                        startActivity(intent);
                        finish();
                    }
                });

                llCollectieInhoud.addView(img);
            }
        }

        llArchieven = findViewById(R.id.llArchieven);
        lblArchief = findViewById(R.id.lblArchief);
        llArchievenInhoud = findViewById(R.id.llArchievenInhoud);

        if (film.getFilmArchiefs().size() == 0){
            llArchieven.setVisibility(View.GONE);
        }else{
            llArchieven.setVisibility(View.VISIBLE);

            film.getFilmArchiefs().sort(new Comparator<FilmArchief>() {
                @Override
                public int compare(FilmArchief o1, FilmArchief o2) {
                    return o1.getArchief().getNaam().compareTo(o2.getArchief().getNaam());
                }
            });

            llArchievenInhoud.removeAllViews();
            for (final FilmArchief archief : film.getFilmArchiefs()){
                final Button btnGenre = new Button(this, null, 0, R.style.btnTagFilmDetail);
                btnGenre.setId(archief.getArchief().getId());
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(7, 0, 7, 0);
                btnGenre.setLayoutParams(buttonLayoutParams);
                try {
                    @SuppressLint("ResourceType") XmlResourceParser parser = getResources().getXml(R.color.textcolor_btn_tag_film_detail);
                    ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
                    btnGenre.setTextColor(colors);
                } catch (Exception e) {
                    // handle exceptions
                }
                btnGenre.setText(archief.getArchief().getNaam());

                llArchievenInhoud.addView(btnGenre);
            }
        }

        llTrailer = (LinearLayout) findViewById(R.id.llTrailer);
        btnTrailer = (ImageButton) findViewById(R.id.btnTrailer);

        grdActors = (GridView) findViewById(R.id.grdActors);
        List<ActeurFilm> acteurs = film.getActeurs();
        acteurs.sort(new Comparator<ActeurFilm>() {
            @Override
            public int compare(ActeurFilm o1, ActeurFilm o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        grdActors.setAdapter(new ActorsFilmGridView(this, acteurs));

        grdActors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(FilmActivity.this, ActeurActivity.class);
                intent.putExtra("acteur_id", film.getActeurs().get(position).getActeurId());
                startActivity(intent);
            }
        });
    }

    private void setGenres() {
        llGenres.removeAllViews();

        for(final Tag tag : film.getGenres()){
            final Button btnGenre = new Button(this, null, 0, R.style.btnTagFilmDetail);
            btnGenre.setId(tag.getId());
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(7, 0, 7, 0);
            btnGenre.setLayoutParams(buttonLayoutParams);
            try {
                @SuppressLint("ResourceType") XmlResourceParser parser = getResources().getXml(R.color.textcolor_btn_tag_film_detail);
                ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
                btnGenre.setTextColor(colors);
            } catch (Exception e) {
                // handle exceptions
            }
            btnGenre.setText(tag.getNaam());

            llGenres.addView(btnGenre);
        }
    }

    private void handleEvents() {
        btnOmschrijving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOmschrijving.setEnabled(false);
                btnExtraInfo.setEnabled(true);

                llExtraInfo.setVisibility(View.GONE);
                llOmschrijving.setVisibility(View.VISIBLE);
            }
        });
        btnExtraInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOmschrijving.setEnabled(true);
                btnExtraInfo.setEnabled(false);

                llOmschrijving.setVisibility(View.GONE);
                llExtraInfo.setVisibility(View.VISIBLE);
            }
        });

        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent;
                Intent webIntent;

                String ytId = film.getTrailerId();
                if (ytId == null){
                    ytId = "";
                }

                if (ytId.equals("")){
                    Calendar c = Calendar.getInstance();
                    c.setTime(film.getReleaseDate());

                    appIntent = new Intent(Intent.ACTION_SEARCH);
                    appIntent.setPackage("com.google.android.youtube");
                    appIntent.putExtra("query", film.getNaam() + " (" + c.get(Calendar.YEAR) + ")");
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/results?search_query=" + film.getNaam() + " (" + c.get(Calendar.YEAR) + ")"));
                }else{
                    appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ytId));
                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + ytId));
                }
                try {
                    FilmActivity.this.startActivity(appIntent);
                } catch (Exception ex) {
                    FilmActivity.this.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_film, menu);

        MenuItem item = menu.findItem(R.id.action_favorite);

        if (dao.getById(film.getId()).isFavorite()){
            item.setIcon(R.drawable.ic_favorite);
        }else {
            item.setIcon(R.drawable.ic_not_favorite);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_favorite:
                Film f = dao.getById(film.getId());
                if (!f.isFavorite()){
                    item.setIcon(R.drawable.ic_favorite);
                    dao.toggleFavorits(f.getId(), 1);
                }else {
                    item.setIcon(R.drawable.ic_not_favorite);
                    dao.toggleFavorits(f.getId(), 0);
                }

                dao = FilmsDb.getDatabase(FilmActivity.this).filmsDAO();

                break;
            case R.id.action_genres:
                final DialogEditGenres dialogEditGenres = new DialogEditGenres(this, film);
                dialogEditGenres.setNegativeButton("Sluiten", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //FilmActivity.this.film = dialogEditGenres.film;
                        setGenres();
                    }
                }).show();
                setResult(1);
                break;
            case R.id.action_archieven:
                final DialogEditArchieven dialogEditArchieven = new DialogEditArchieven(this, film);
                dialogEditArchieven.setNegativeButton("Sluiten", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setGenres();
                    }
                }).show();
                setResult(1);
                break;
            case R.id.action_verwijder:
                //TODO: Verwijderen van een film + verwijderen van acteurs met 0 films + archieven
                try{
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            new DialogTextOutput(FilmActivity.this, "Verwijderen", "Bent u zeker dat u deze film wil verwijderen?")
                                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dao.deleteById(film.getId());
                                            DAC.Films.remove(film);
                                            DbRemoteMethods.DeleteFilm(film);

                                            Methodes.delete342Poster(film.getId());

                                            List<Integer> ids = DbRemoteMethods.CheckActorsWithoutFilms();
                                            ActeursDAO aDao = FilmsDb.getDatabase(FilmActivity.this).acteursDAO();

                                            for (int id : ids){
                                                aDao.deleteById(id);
                                                Methodes.delete92Poster(id);
                                            }

                                            FilmTagsDAO ftDao = FilmsDb.getDatabase(FilmActivity.this).filmTagsDAO();
                                            ftDao.deleteByFilmId(film.getId());
                                            ActeurFilmsDAO afDao = FilmsDb.getDatabase(FilmActivity.this).acteurFilmsDAO();
                                            afDao.deleteByFilmId(film.getId());

                                            Log.e("Films", "Verwijderen success");
                                        }
                                    }).setNegativeButton("Nee", null)
                                    .create().show();
                            return null;
                        }
                    }.execute();

                    Intent data = new Intent();
                    data.putExtra("film_id", film.getId());
                    DAC.Films.size();
                    setResult(1, data);
                    finish();
                }catch (Exception e){
                    Log.e("Film", "Verwijderen mislukt: " + e.getMessage());
                }
            case 16908332:
                onBackPressed();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }
}
