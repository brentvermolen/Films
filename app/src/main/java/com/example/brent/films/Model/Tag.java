package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {
    @PrimaryKey
    private int Id;
    private String Naam;
    private boolean isHidden;

    @Ignore
    private int Count;

    @Ignore
    private List<FilmTags> Films;

    public Tag(){
        Films = new ArrayList<>();
    }

    public static Tag FromResultSet(ResultSet rs) {
        Tag tag = new Tag();

        try {
            tag.setId(rs.getInt(1));
            tag.setNaam(rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tag;
    }

    public int getId() {
        return Id;
    }

    public String getNaam() {
        return Naam;
    }

    public int getCount() {
        return Count;
    }

    public List<Film> getFilms() {
        if (Films == null){
            new ArrayList<Film>();
        }

        List<Film> films = new ArrayList<>();
        for (FilmTags ft : Films){
            films.add(ft.getFilm());
        }

        return films;
    }

    public void setFilms(List<FilmTags> films) {
        Films = films;
    }

    public List<FilmTags> getFilmTags() {
        return (Films == null) ? new ArrayList<FilmTags>() : Films;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Tag t = (Tag)obj;

            if (t.getNaam().equals(this.getNaam())){
                return true;
            }
        }catch (Exception e){}

        return false;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    @Override
    public String toString() {
        return getNaam();
    }
}
