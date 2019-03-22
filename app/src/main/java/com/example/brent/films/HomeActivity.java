package com.example.brent.films;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.DialogSorterenOp;
import com.example.brent.films.DB.FilmsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Class.MoviesGridView;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    Toolbar mToolbar;

    FilmsDAO filmsDAO;
    GenresDAO genresDAO;

    ImageView imgRandHeader;

    LinearLayout llGenres;
    ImageButton btnFavorieten;
    Button btnAlleFilms;

    GridView grdMovies;

    List<Film> currentlyShown;

    private List<Button> btns = new ArrayList<>();
    private int currentSort;
    private boolean currentSortDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("");

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        setSupportActionBar(mToolbar);

        currentSort = 0;
        currentSortDesc = false;

        currentlyShown = DAC.Films;

        filmsDAO = FilmsDb.getDatabase(this).filmsDAO();
        genresDAO = FilmsDb.getDatabase(this).genresDAO();

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgRandHeader = (ImageView) findViewById(R.id.imgRandHeader);
        Random rand = new Random();
        Bitmap bm = null;
        try {
            int randId = rand.nextInt(DAC.Films.size());
            bm = Methodes.getBitmapFromAsset(this, "films/" + DAC.Films.get(randId).getId() + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgRandHeader.setImageBitmap(bm);

        llGenres = (LinearLayout) findViewById(R.id.llGenres);

        btnAlleFilms = (Button) findViewById(R.id.btnTagAlle);
        btnAlleFilms.setEnabled(false);
        btnAlleFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Button btn : btns) {
                    btn.setEnabled(true);
                }
                btnFavorieten.setEnabled(true);
                btnAlleFilms.setEnabled(false);

                showFilms(DAC.Films);
            }
        });

        btnFavorieten = (ImageButton) findViewById(R.id.btnTagFav);
        btnFavorieten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Button btn : btns){
                    btn.setEnabled(true);
                }
                btnAlleFilms.setEnabled(true);
                btnFavorieten.setEnabled(false);

                List<Film> favo = filmsDAO.getFavorits();
                favo.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return o1.getNaam().compareTo(o2.getNaam());
                    }
                });

                showFilms(favo);
            }
        });

        grdMovies = (GridView) findViewById(R.id.grdMovies);

        setGenreBar();
    }

    private void setGenreBar() {
        llGenres.removeAllViews();
        btns.clear();

        genresDAO = FilmsDb.getDatabase(this).genresDAO();
        List<Tag> tags = genresDAO.getHiddenTags();

        DAC.Tags.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });

        for(final Tag tag : DAC.Tags){
            if (tags.contains(tag)){
                continue;
            }

            final Button btnGenre = new Button(this, null, 0, R.style.btnTag);
            btnGenre.setId(tag.getId());
            btns.add(btnGenre);
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(7, 0, 7, 0);
            btnGenre.setLayoutParams(buttonLayoutParams);
            //btnGenre.setTextColor(ContextCompat.getColor(this, R.color.textcolor_btn_tag));
            try {
                @SuppressLint("ResourceType") XmlResourceParser parser = getResources().getXml(R.color.textcolor_btn_tag);
                ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
                btnGenre.setTextColor(colors);
            } catch (Exception e) {
                // handle exceptions
            }
            btnGenre.setText(tag.getNaam());

            btnGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Button btn : btns){
                        btn.setEnabled(true);
                    }
                    btnFavorieten.setEnabled(true);
                    btnAlleFilms.setEnabled(true);
                    btnGenre.setEnabled(false);

                    showFilms(tag.getFilms());
                }
            });

            llGenres.addView(btnGenre);
        }

        btnAlleFilms.performClick();
    }

    private void handleEvents() {
        grdMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, FilmActivity.class);
                intent.putExtra("film_id", currentlyShown.get(position).getId());
                startActivityForResult(intent, 2);
            }
        });
    }

    private void showFilms(List<Film> films){
        setTitle(films.size() + " Films");

        currentlyShown = films;

        sortCurrentlyShown();
        grdMovies.setAdapter(new MoviesGridView(this, films));
    }

    private void sortCurrentlyShown() {
        switch (currentSort){
            case 0:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getNaam().compareTo(o1.getNaam());
                        }else{
                            return o1.getNaam().compareTo(o2.getNaam());
                        }
                    }
                });
                break;
            case 1:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getReleaseDate().compareTo(o1.getReleaseDate());
                        }else{
                            return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                        }
                    }
                });
                break;
            case 2:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getDuur() - o1.getDuur();
                        }else {
                            return o1.getDuur() - o2.getDuur();
                        }
                    }
                });
                break;
            case 3:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getToegevoegd().compareTo(o1.getToegevoegd());
                        }else {
                            return o1.getToegevoegd().compareTo(o2.getToegevoegd());
                        }
                    }
                });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                Intent intent = new Intent(HomeActivity.this, ZoekenActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.action_genres:
                Intent intent1 = new Intent(HomeActivity.this, GenresActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.action_sort:
                final DialogSorterenOp dialogSorterenOp = new DialogSorterenOp(HomeActivity.this, currentSort, currentSortDesc);

                dialogSorterenOp.setPositiveButton("Sorteer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentSort = dialogSorterenOp.getSpinnerValue();
                        currentSortDesc = dialogSorterenOp.getDescending();

                        showFilms(currentlyShown);
                    }
                }).setNegativeButton("Stop", null);

                dialogSorterenOp.show();
                break;
            case R.id.action_film_beheer:
                Intent intent2 = new Intent(HomeActivity.this, BeheerActivity.class);
                startActivity(intent2);
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                setGenreBar();
            case 2:
                if (resultCode == 1 && data != null){
                    int film_id = data.getIntExtra("film_id", 0);
                    currentlyShown.remove((Integer)film_id);
                    showFilms(currentlyShown);
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!btnFavorieten.isEnabled()){
            showFilms(filmsDAO.getFavorits());
        }
    }
}
