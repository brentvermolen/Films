package com.example.brent.films.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NieuweZoekenAdapter extends BaseAdapter {
    private Context mContext;
    private List<Film> films;

    private List<Tag> onzichtbareTags;

    GenresDAO dao;

    public NieuweZoekenAdapter(Context c, List<Film> films) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).genresDAO();

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
        TextView lblDuur;
        TextView lblJaartal;
        ImageView imgPoster;
        ImageButton btnToevoegen;
        ImageButton btnRequest;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        this.onzichtbareTags = dao.getHiddenTags();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imgPoster = convertView.findViewById(R.id.imgPoster);
            viewHolder.lblOmschrijving = convertView.findViewById(R.id.lblOmschrijving);
            viewHolder.lblTitel = convertView.findViewById(R.id.lblTitel);
            viewHolder.lblDuur = convertView.findViewById(R.id.lblDuur);
            viewHolder.lblJaartal = convertView.findViewById(R.id.lblJaartal);

            viewHolder.btnToevoegen = convertView.findViewById(R.id.btnAdd);
            viewHolder.btnRequest = convertView.findViewById(R.id.btnRequest);

            viewHolder.btnToevoegen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);

                    Film f = (Film)viewHolder.imgPoster.getTag();
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

        Film f = films.get(position);
        viewHolder.imgPoster.setTag(f);

        //TODO Aanvragen laden
        if (DAC.Films.contains(f)){
            viewHolder.btnToevoegen.setEnabled(false);
            viewHolder.btnRequest.setEnabled(false);
        }else{
            viewHolder.btnToevoegen.setEnabled(true);
            viewHolder.btnRequest.setEnabled(true);
        }

        return convertView;
    }
}
