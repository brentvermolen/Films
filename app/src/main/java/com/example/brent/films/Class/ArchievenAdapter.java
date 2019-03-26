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

import com.example.brent.films.DB.ArchiefDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Archief;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArchievenAdapter extends BaseAdapter {
    private Context mContext;
    private List<Archief> archieven;

    private List<Archief> onzichtbare;

    ArchiefDAO dao;

    public ArchievenAdapter(Context c) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).archiefDAO();

        this.archieven = new ArrayList<>(Methodes.FindGebruikerById(DAC.Gebruikers, mContext.getResources().getInteger(R.integer.gebruiker_id)).getArchieven());
        this.archieven.sort(new Comparator<Archief>() {
            @Override
            public int compare(Archief o1, Archief o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });
    }

    public int getCount() {
        return archieven.size();
    }

    public Archief getItem(int position) {
        return archieven.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        TextView lblGenre;
        ImageButton btnEditGenre;
        ImageButton btnDeleteGenre;

        ToggleButton btnToggleZichtbaarheid;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        onzichtbare = dao.getOnzichtbare();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.lblGenre = convertView.findViewById(R.id.lblGenre);
            viewHolder.btnEditGenre = convertView.findViewById(R.id.btnEditGenre);
            viewHolder.btnDeleteGenre = convertView.findViewById(R.id.btnDeleteGenre);
            viewHolder.btnToggleZichtbaarheid = convertView.findViewById(R.id.tglVisibility);

            viewHolder.btnToggleZichtbaarheid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Archief a = (Archief) viewHolder.lblGenre.getTag();

                    if (((ToggleButton)v).isChecked()){
                        dao.updateVisibility(a.getId(), 1);
                    }else{
                        dao.updateVisibility(a.getId(), 0);
                    }
                }
            });

            viewHolder.btnEditGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogTextInput dialogTextInput = new DialogTextInput(mContext, "Wijzig Genre", viewHolder.lblGenre.getText().toString());

                    dialogTextInput.setPositiveButton("Wijzig", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Archief archief = (Archief) viewHolder.lblGenre.getTag();

                            archief.setNaam(dialogTextInput.txt.getText().toString());

                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.UpdateArchief(archief);

                                    return null;
                                }
                            }.execute();

                            viewHolder.lblGenre.setText(archief.getNaam());
                            DAC.Archieven.remove(archief);
                            DAC.Archieven.add(archief);

                            dao.update(archief.getId(), archief.getNaam());
                        }
                    });

                    dialogTextInput.show();
                }
            });

            viewHolder.btnDeleteGenre.setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblGenre.setText(archieven.get(position).getNaam());
        viewHolder.lblGenre.setTag(archieven.get(position));

        boolean isZichtbaar = !onzichtbare.contains(archieven.get(position));
        viewHolder.btnToggleZichtbaarheid.setChecked(isZichtbaar);

        return convertView;
    }
}
