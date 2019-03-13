package com.example.brent.films.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

public class DialogEditGenres extends AlertDialog.Builder {

    private Context mContext;
    public ListView lstGenres;

    public Film film;

    public DialogEditGenres(Context context, Film film){
        super(context);
        mContext = context;

        this.film = film;

        setDialog();
    }

    private void setDialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_edit_genres, null);

        lstGenres = view.findViewById(R.id.lstGenres);
        EditGenresAdapter adapter = new EditGenresAdapter(mContext, film);

        lstGenres.setAdapter(adapter);

        this.setView(view);
    }
}
