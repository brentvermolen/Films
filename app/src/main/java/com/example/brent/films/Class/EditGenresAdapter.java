package com.example.brent.films.Class;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.brent.films.DB.DbRemoteMethods;
import com.example.brent.films.DB.FilmTagsDAO;
import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditGenresAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tag> tags;

    private Film film;

    private List<Tag> onzichtbareTags;

    GenresDAO dao;
    FilmTagsDAO filmTagsDAO;

    public EditGenresAdapter(Context c, Film film) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).genresDAO();
        filmTagsDAO = FilmsDb.getDatabase(c).filmTagsDAO();

        this.film = film;

        this.tags = new ArrayList<>(DAC.Tags);
        this.tags.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });
    }

    public int getCount() {
        return tags.size();
    }

    public Tag getItem(int position) {
        return tags.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        CheckBox chkGenre;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        this.onzichtbareTags = dao.getHiddenTags();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_edit_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.chkGenre = convertView.findViewById(R.id.chkGenre);

            viewHolder.chkGenre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Tag t = (Tag)viewHolder.chkGenre.getTag();

                    try{
                        if (isChecked){
                            final FilmTags ft = new FilmTags();
                            ft.setFilm_ID(film.getId());
                            ft.setTag_ID(t.getId());
                            ft.setFilm(film);
                            ft.setTag(t);

                            filmTagsDAO.insert(ft);
                            film.getFilmTags().add(ft);
                                    //TODO aan tag toevoegen
                            //Wordt nog niet geupdate in genres op home screen
                            DAC.FilmTags.add(ft);
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.InsertFilmTag(ft);

                                    return null;
                                }
                            }.execute();
                        }else{
                            final FilmTags ft = filmTagsDAO.getByFilmAndTagId(t.getId(), film.getId());

                            filmTagsDAO.deleteByFilmAndTagId(ft.getFilm_ID(), ft.getTag_ID());
                            film.getFilmTags().remove(ft);
                                //TODO van tag verwijderen
                            DAC.FilmTags.remove(ft);
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.DeleteFilmTag(ft);

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

        viewHolder.chkGenre.setText(tags.get(position).getNaam());
        viewHolder.chkGenre.setTag(tags.get(position));
        viewHolder.chkGenre.setChecked(film.getGenres().contains(tags.get(position)));

        return convertView;
    }
}
