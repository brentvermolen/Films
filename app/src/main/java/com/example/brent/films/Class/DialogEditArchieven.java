package com.example.brent.films.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.brent.films.Model.Film;
import com.example.brent.films.R;

public class DialogEditArchieven extends AlertDialog.Builder {

    private Context mContext;
    public ListView lstGenres;

    public Film film;

    public DialogEditArchieven(Context context, Film film){
        super(context);
        mContext = context;

        this.film = film;

        setDialog();
    }

    private void setDialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_edit_genres, null);

        lstGenres = view.findViewById(R.id.lstGenres);
        EditArchievenAdapter adapter = new EditArchievenAdapter(mContext, film);

        lstGenres.setAdapter(adapter);

        this.setView(view);
    }
}
