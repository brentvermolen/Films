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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GebruikersDAO;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Gebruiker;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GebruikersAdapter extends BaseAdapter {
    private Context mContext;

    private List<Gebruiker> gebruikers;

    GebruikersDAO dao;

    public GebruikersAdapter(Context c) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).gebruikersDAO();

        this.gebruikers = new ArrayList<>(DAC.Gebruikers);
        this.gebruikers.sort(new Comparator<Gebruiker>() {
            @Override
            public int compare(Gebruiker o1, Gebruiker o2) {
                return o1.getAchternaam().compareTo(o2.getAchternaam());
            }
        });
    }

    public int getCount() {
        return gebruikers.size();
    }

    public Gebruiker getItem(int position) {
        return gebruikers.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        TextView lblGebruiker;
        ImageButton btnEditGebruiker;
        ImageButton btnDeleteGebruiker;

        TextView lblLastLogin;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_gebruiker_item, null);

            viewHolder = new ViewHolder();
            viewHolder.lblGebruiker = convertView.findViewById(R.id.lblGebruiker);
            viewHolder.btnEditGebruiker = convertView.findViewById(R.id.btnEditArchieven);
            viewHolder.btnDeleteGebruiker = convertView.findViewById(R.id.btnVerwijderGebruiker);
            viewHolder.lblLastLogin = convertView.findViewById(R.id.lblLaatstOnline);

            viewHolder.btnEditGebruiker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gebruiker gebruiker = (Gebruiker)viewHolder.lblGebruiker.getTag();
                    Toast.makeText(mContext, "Wijzig " + gebruiker.getVoornaam(), Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.btnDeleteGebruiker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    final Gebruiker gebruiker = (Gebruiker)viewHolder.lblGebruiker.getTag();

                    builder.setMessage("Bent u zeker dat u deze gebruiker wil verwijderen?");

                    builder.setNegativeButton("Nee", null)
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext, "Verwijder " + gebruiker.getVoornaam(), Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Gebruiker gebruiker = gebruikers.get(position);

        viewHolder.lblGebruiker.setText(gebruiker.getAchternaam() + " " + gebruiker.getVoornaam());
        viewHolder.lblGebruiker.setTag(gebruikers.get(position));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        viewHolder.lblLastLogin.setText(dateFormat.format(gebruiker.getLastLogin()));

        return convertView;
    }
}
