package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Archief {
    @PrimaryKey
    private int id;
    private String naam;
    private boolean visible;

    @Ignore
    private List<FilmArchief> films;
    @Ignore
    private List<GebruikerArchief> gebruikers;

    public Archief() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<FilmArchief> getFilms() {
        return (films == null) ? new ArrayList<FilmArchief>() : films;
    }

    public List<GebruikerArchief> getGebruikers() {
        return (gebruikers == null) ? new ArrayList<GebruikerArchief>() : gebruikers;
    }

    public void setGebruikers(List<GebruikerArchief> gebruikers) {
        this.gebruikers = gebruikers;
    }

    public void setFilms(List<FilmArchief> films) {
        this.films = films;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Archief archief = (Archief) obj;
            if (archief.getId() == this.getId() || archief.getNaam() == this.getNaam()){
                return true;
            }
        }catch (Exception ex){ }

        return false;
    }
}
