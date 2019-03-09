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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brent.films.DB.DbRemoteMethods;
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
        ImageButton btnEditGenre;
        ImageButton btnDeleteGenre;

        ToggleButton btnToggleZichtbaarheid;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        this.onzichtbareTags = dao.getHiddenTags();

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
                    Tag t = (Tag)viewHolder.lblGenre.getTag();
                    if (((ToggleButton)v).isChecked()){
                        dao.updateVisibility(t.getId(), 0);
                        //Toast.makeText(mContext, "Verwijderd", Toast.LENGTH_SHORT).show();
                    }else{
                        dao.updateVisibility(t.getId(), 1);
                        //Toast.makeText(mContext, "Toegevoegd", Toast.LENGTH_SHORT).show();
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
                            final Tag tag = (Tag)viewHolder.lblGenre.getTag();
                            tag.setNaam(dialogTextInput.txt.getText().toString());

                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    DbRemoteMethods.UpdateTag(tag);

                                    return null;
                                }
                            }.execute();

                            viewHolder.lblGenre.setText(dialogTextInput.txt.getText().toString());
                            DAC.Tags.remove(tag);
                            DAC.Tags.add(tag);

                            dao.update(tag.getId(), tag.getNaam());
                        }
                    });

                    dialogTextInput.show();
                }
            });

            viewHolder.btnDeleteGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setMessage("Bent u zeker dat u dit genre wil verwijderen?");

                    builder.setNegativeButton("Nee", null)
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Tag tag = (Tag)viewHolder.lblGenre.getTag();

                                    new AsyncTask<Void, Void, Void>(){
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            DbRemoteMethods.DeleteTag(tag);

                                            return null;
                                        }
                                    }.execute();

                                    tags.remove(tag);
                                    notifyDataSetChanged();

                                    DAC.Tags.remove(tag);
                                    dao.deleteById(tag.getId());
                                }
                            }).show();
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblGenre.setText(tags.get(position).getNaam());
        viewHolder.lblGenre.setTag(tags.get(position));

        boolean isZichtbaar = !onzichtbareTags.contains(tags.get(position));
        viewHolder.btnToggleZichtbaarheid.setChecked(isZichtbaar);

        return convertView;
    }
}
