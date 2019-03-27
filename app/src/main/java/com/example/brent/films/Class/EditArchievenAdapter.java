package com.example.brent.films.Class;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.brent.films.DB.ArchiefDAO;
import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmArchiefDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.Model.Archief;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmArchief;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.util.Comparator;
import java.util.List;

public class EditArchievenAdapter extends BaseAdapter {
    private Context mContext;

    private Film film;

    private List<Archief> onzichtbareArchieven;

    ArchiefDAO dao;
    private FilmArchiefDAO filmArchiefDAO;

    public EditArchievenAdapter(Context c, Film film) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).archiefDAO();
        filmArchiefDAO = FilmsDb.getDatabase(c).filmArchiefDAO();

        this.film = film;

        DAC.Archieven.sort(new Comparator<Archief>() {
            @Override
            public int compare(Archief o1, Archief o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });
    }

    public int getCount() {
        return DAC.Archieven.size();
    }

    public Archief getItem(int position) {
        return DAC.Archieven.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        //CheckBox chkGenre;
        Switch chkGenre;
        TextView lblGenre;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        this.onzichtbareArchieven = dao.getOnzichtbare();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_edit_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.chkGenre = convertView.findViewById(R.id.chkGenre);
            viewHolder.lblGenre = convertView.findViewById(R.id.lblGenre);

            viewHolder.chkGenre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Archief t = (Archief) viewHolder.chkGenre.getTag();

                    try{
                        if (isChecked){
                            final FilmArchief ft = new FilmArchief();
                            ft.setFilm_id(film.getId());
                            ft.setArchief_id(t.getId());
                            ft.setFilm(film);
                            ft.setArchief(t);

                            filmArchiefDAO.insert(ft);
                            film.getFilmArchiefs().add(ft);
                            DAC.Archieven.get(position).getFilmArchief().add(ft);
                            DAC.FilmArchieven.add(ft);
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.InsertFilmArchief(ft);

                                    return null;
                                }
                            }.execute();
                        }else{
                            final FilmArchief ft = filmArchiefDAO.getByFilmAndArchiefId(film.getId(), t.getId());

                            filmArchiefDAO.deleteByFilmAndArchiefId(ft.getFilm_id(), ft.getArchief_id());
                            film.getFilmArchiefs().remove(ft);
                            DAC.Archieven.get(position).getFilmArchief().remove(ft);
                            DAC.FilmArchieven.remove(ft);
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.DeleteFilmArchief(ft);

                                    return null;
                                }
                            }.execute();
                        }
                    }catch (Exception e){

                    }
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblGenre.setText(DAC.Archieven.get(position).getNaam());
        viewHolder.chkGenre.setTag(DAC.Archieven.get(position));
        viewHolder.chkGenre.setChecked(film.getArchiefs().contains(DAC.Archieven.get(position)));

        return convertView;
    }
}
