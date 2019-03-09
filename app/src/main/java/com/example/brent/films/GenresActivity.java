package com.example.brent.films;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.DialogTextInput;
import com.example.brent.films.Class.GenresAdapter;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.Model.Tag;

import java.util.function.Predicate;

public class GenresActivity extends AppCompatActivity {

    ListView lstGenres;
    Toolbar mToolbar;

    ImageView btnAddTag;

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

        initViews();
        handleEvents();
    }

    private void initViews() {
        lstGenres = (ListView) findViewById(R.id.lstGenres);
        lstGenres.setAdapter(new GenresAdapter(this));

        btnAddTag = (ImageView) findViewById(R.id.btnAddTag);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
