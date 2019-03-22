package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Film;
import com.example.brent.films.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

public class AanvragenGridView extends BaseAdapter {
    private Context mContext;
    private List<Film> films;

    public AanvragenGridView(Context c, List<Film> films) {
        mContext = c;
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
        public ImageView imgPoster;
        public TextView lblJaartal;
    }

    public void removeItem(Film film){
        films.remove(film);
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inflater_aanvraag_poster, null);

            viewHolder = new ViewHolder();
            viewHolder.imgPoster = convertView.findViewById(R.id.imgPoster);
            viewHolder.lblJaartal = convertView.findViewById(R.id.lblJaartal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    InputStream is = (InputStream) new URL("http://image.tmdb.org/t/p/w342/" + films.get(position).getPosterPath()).getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    viewHolder.imgPoster.setImageBitmap(d);
                } catch (Exception e) {

                }

                return null;
            }
        }.execute();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

        viewHolder.lblJaartal.setText(dateFormat.format(films.get(position).getReleaseDate()));

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
