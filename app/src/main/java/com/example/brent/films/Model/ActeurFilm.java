package com.example.brent.films.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;

@Entity(primaryKeys = {"ActeurId", "FilmId"})
public class ActeurFilm {
    private int ActeurId;
    private int FilmId;
    private String Karakter;
    private int Sort;

    @Ignore
    private Acteur Acteur;
    @Ignore
    private Film Film;

    public ActeurFilm(){}

    public static ActeurFilm FromResultSet(ResultSet rs) {
        ActeurFilm acteurFilm = new ActeurFilm();

        try {
            acteurFilm.setActeurId(rs.getInt(1));
            acteurFilm.setFilmId(rs.getInt(2));
            acteurFilm.setKarakter(rs.getString(3));
            acteurFilm.setSort(rs.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acteurFilm;
    }

    public int getActeurId() {
        return ActeurId;
    }

    public int getFilmId() {
        return FilmId;
    }

    public String getKarakter() {
        return Karakter;
    }

    public int getSort() {
        return Sort;
    }

    public Acteur getActeur() {
        return Acteur;
    }

    public Film getFilm() {
        return Film;
    }

    public void setActeur(Acteur acteur) {
        Acteur = acteur;
    }

    public void setFilm(Film film) {
        Film = film;
    }

    public void setActeurId(int acteurId) {
        ActeurId = acteurId;
    }

    public void setFilmId(int filmId) {
        FilmId = filmId;
    }

    public void setKarakter(String karakter) {
        Karakter = karakter;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
