package com.example.brent.films.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brent.films.DB.FilmsDb;
import com.example.brent.films.DB.GenresDAO;
import com.example.brent.films.Model.Tag;
import com.example.brent.films.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenresAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tag> tags;

    private List<Tag> onzichtbareTags;

    GenresDAO dao;

    public GenresAdapter(Context c) {
        mContext = c;
        dao = FilmsDb.getDatabase(c).genresDAO();

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
        TextView lblGenre;
        LinearLayout llEigenGenre;
        ImageView btnEditGenre;
        ImageView btnDeleteGenre;

        LinearLayout llBestaandGenre;
        ToggleButton btnToggleZichtbaarheid;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        this.onzichtbareTags = dao.getHiddenTags();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_genre_item, null);

            viewHolder = new ViewHolder();
            viewHolder.lblGenre = convertView.findViewById(R.id.lblGenre);
            viewHolder.llEigenGenre = convertView.findViewById(R.id.llEigenGenre);
            viewHolder.btnEditGenre = convertView.findViewById(R.id.btnEditGenre);
            viewHolder.btnDeleteGenre = convertView.findViewById(R.id.btnDeleteGenre);
            viewHolder.llBestaandGenre = convertView.findViewById(R.id.llBestaandGenre);
            viewHolder.btnToggleZichtbaarheid = convertView.findViewById(R.id.tglVisibility);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblGenre.setText(tags.get(position).getNaam());
        if (tags.get(position).getId() >= 100){
            viewHolder.llBestaandGenre.setVisibility(View.GONE);
            viewHolder.llEigenGenre.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llEigenGenre.setVisibility(View.GONE);
            viewHolder.llBestaandGenre.setVisibility(View.VISIBLE);

            boolean isZichtbaar = !onzichtbareTags.contains(tags.get(position));
            viewHolder.btnToggleZichtbaarheid.setChecked(isZichtbaar);
            viewHolder.btnToggleZichtbaarheid.setTag(isZichtbaar);
        }

        viewHolder.btnToggleZichtbaarheid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tag t = tags.get(position);
                if (((ToggleButton)v).isChecked()){
                    //dao.deleteById(t.getId());
                    //t.setHidden(false);
                    //dao.insert(t);
                    dao.updateVisibility(t.getId(), 0);
                    Toast.makeText(mContext, "Verwijderd", Toast.LENGTH_SHORT).show();
                }else{
                    //dao.deleteById(t.getId());
                    //t.setHidden(true);
                    //dao.insert(t);
                    dao.updateVisibility(t.getId(), 1);
                    Toast.makeText(mContext, "Toegevoegd", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
