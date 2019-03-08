package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Acteur {
    @PrimaryKey
    private int Id;
    private String Naam;
    private String PosterPath;

    @Ignore
    private List<ActeurFilm> Films;

    public Acteur(){
        Films = new ArrayList<>();
    }

    public static Acteur FromResultSet(ResultSet rs) {
        Acteur acteur = new Acteur();

        try {
            acteur.setId(rs.getInt(1));
            acteur.setNaam(rs.getString(2));
            acteur.setPosterPath(rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acteur;
    }

    public int getId() {
        return Id;
    }

    public String getNaam() {
        return Naam;
    }

    public List<ActeurFilm> getFilms() {
        return (Films == null) ? new ArrayList<ActeurFilm>() : Films;
    }

    public void setFilms(List<ActeurFilm> films) {
        Films = films;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }
}
