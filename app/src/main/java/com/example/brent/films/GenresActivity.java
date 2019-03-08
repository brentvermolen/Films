package com.example.brent.films;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.brent.films.Class.GenresAdapter;
import com.example.brent.films.DB.DbRemoteMethods;

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
                //TODO: Update style
                AlertDialog.Builder builder = new AlertDialog.Builder(GenresActivity.this);

                final EditText txt = new EditText(GenresActivity.this);
                builder.setView(txt);

                builder.setPositiveButton("Opslaan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbRemoteMethods.InsertTag(txt.getText().toString());
                    }
                });

                builder.create().show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
