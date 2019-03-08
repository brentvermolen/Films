package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Collectie {
    @PrimaryKey
    private int Id;
    private String Naam;

    @Ignore
    private List<Film> Films;

    public Collectie(){}

    public static Collectie FromResultSet(ResultSet rs) {
        Collectie collectie = new Collectie();

        try {
            collectie.setId(rs.getInt(1));
            collectie.setNaam(rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return collectie;
    }

    public int getId() {
        return Id;
    }

    public String getNaam() {
        return Naam;
    }

    public List<Film> getFilms() {
        return (Films == null) ? new ArrayList<Film>() : Films;
    }

    public void setFilms(List<Film> films) {
        Films = films;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }
}
