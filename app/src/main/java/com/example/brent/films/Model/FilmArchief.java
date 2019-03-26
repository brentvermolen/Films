package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

@Entity(primaryKeys = {"film_id", "archief_id"})
public class FilmArchief {
    private int film_id;
    private int archief_id;

    @Ignore
    private Film film;
    @Ignore
    private Archief archief;

    public FilmArchief() {}

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public int getArchief_id() {
        return archief_id;
    }

    public void setArchief_id(int archief_id) {
        this.archief_id = archief_id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Archief getArchief() {
        return archief;
    }

    public void setArchief(Archief archief) {
        this.archief = archief;
    }
}
