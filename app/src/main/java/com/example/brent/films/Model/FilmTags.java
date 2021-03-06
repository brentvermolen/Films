package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;
@Entity(primaryKeys = {"Film_ID", "Tag_ID"})
public class FilmTags {
    private int Film_ID;
    private int Tag_ID;

    @Ignore
    private Film Film;
    @Ignore
    private Tag Tag;

    public FilmTags(){}

    public static FilmTags FromResultSet(ResultSet rs) {
        FilmTags filmTags = new FilmTags();

        try {
            filmTags.setFilm_ID(rs.getInt(1));
            filmTags.setTag_ID(rs.getInt(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filmTags;
    }

    public int getFilm_ID() {
        return Film_ID;
    }

    public int getTag_ID() {
        return Tag_ID;
    }

    public Film getFilm() {
        return Film;
    }

    public Tag getTag() {
        return Tag;
    }

    public void setFilm(Film film) {
        Film = film;
    }

    public void setTag(Tag tag) {
        Tag = tag;
    }

    public void setFilm_ID(int film_ID) {
        Film_ID = film_ID;
    }

    public void setTag_ID(int tag_ID) {
        Tag_ID = tag_ID;
    }

    @Override
    public String toString() {
        return "F: " + getFilm_ID() + " - T: " + getTag_ID();
    }

    @Override
    public boolean equals(Object obj) {
        try{
            FilmTags t = (FilmTags) obj;

            if (t.getFilm_ID() == this.getFilm_ID() && t.getTag_ID() == this.getTag_ID()){
                return true;
            }
        }catch (Exception e){}

        return false;
    }
}
