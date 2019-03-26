package com.example.brent.films;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brent.films.Class.ArchievenAdapter;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.DialogTextInput;
import com.example.brent.films.Class.GenresAdapter;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.Model.Tag;

import java.util.function.Predicate;

public class GenresActivity extends AppCompatActivity {

    ListView lstGenres;
    LinearLayout item_favorieten;
    Toolbar mToolbar;

    ImageView btnAddTag;

    SharedPreferences sharedPref;

    LinearLayout llGenres;
    ListView lstArchieven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        setTitle("");

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPref = getSharedPreferences("Visibility", MODE_PRIVATE);

        initViews();
        handleEvents();
    }

    private void initViews() {
        item_favorieten = (LinearLayout) findViewById(R.id.lst_item_favorieten);
        ((TextView)item_favorieten.findViewById(R.id.lblGenre)).setText("Favorieten");
        ((ImageButton)item_favorieten.findViewById(R.id.btnEditGenre)).setVisibility(View.GONE);
        ((ImageButton)item_favorieten.findViewById(R.id.btnDeleteGenre)).setVisibility(View.GONE);

        final ToggleButton tglVisibility = (ToggleButton)item_favorieten.findViewById(R.id.tglVisibility);
        tglVisibility.setChecked(sharedPref.getBoolean("favorieten", true));

        tglVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putBoolean("favorieten", tglVisibility.isChecked()).commit();
            }
        });

        lstGenres = (ListView) findViewById(R.id.lstGenres);
        lstGenres.setAdapter(new GenresAdapter(this));

        btnAddTag = (ImageView) findViewById(R.id.btnAddTag);

        llGenres = (LinearLayout) findViewById(R.id.llGenres);
        lstArchieven = (ListView) findViewById(R.id.lstArchieven);
        lstArchieven.setAdapter(new ArchievenAdapter(this));
    }

    private void handleEvents() {
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogTextInput dialogTextInput = new DialogTextInput(GenresActivity.this, "Nieuw Genre");

                dialogTextInput.setPositiveButton("Opslaan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String naam = dialogTextInput.txt.getText().toString();

                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {
                                DbRemoteMethods.InsertTag(naam);

                                return null;
                            }
                        }.execute();

                        int max = 0;
                        for (Tag t : DAC.Tags){
                            if (t.getId() > max){
                                max = t.getId();
                            }
                        }

                        Tag t = new Tag();
                        t.setId(++max);
                        t.setNaam(naam);

                        DAC.Tags.add(t);

                        lstGenres.setAdapter(new GenresAdapter(GenresActivity.this));
                    }
                });

                dialogTextInput.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_genres_archieven, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_toggle:
                if (item.getTitle().equals("Archieven")){
                    item.setTitle("Genres");
                    llGenres.setVisibility(View.GONE);
                    lstArchieven.setVisibility(View.VISIBLE);
                }else{
                    item.setTitle("Archieven");
                    lstArchieven.setVisibility(View.GONE);
                    llGenres.setVisibility(View.VISIBLE);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
