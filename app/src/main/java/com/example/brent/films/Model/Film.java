package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import com.example.brent.films.Class.TimeStampConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class Film {
    public Film(){
        Acteurs = new ArrayList<>();
        Genres = new ArrayList<>();
    }

    public static Film FromResultSet(ResultSet rs) {
        Film film = new Film();

        try {
            film.setId(rs.getInt(1));
            film.setNaam(rs.getString(2));
            try {
                film.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(3)));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("NieuweFilm",  film.getId() + ": Release date");
            }
            film.setTagline(rs.getString(4));
            film.setDuur(rs.getInt(5));
            film.setOmschrijving(rs.getString(6));
            film.setTrailerId(rs.getString(7));
            film.setToegevoegd(rs.getDate(8));
            film.setPosterPath(rs.getString(9));
            film.setCollectieID(rs.getInt(10));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return film;
    }

    @PrimaryKey
    private int Id;
    private String Naam;
    @TypeConverters(TimeStampConverter.class)
    private Date ReleaseDate;
    private String Tagline;
    private int Duur;
    private String Omschrijving;
    @TypeConverters(TimeStampConverter.class)
    private Date Toegevoegd;
    private String TrailerId;
    private String PosterPath;
    private int CollectieID;

    private boolean isFavorite;

    @Ignore
    private List<ActeurFilm> Acteurs;
    @Ignore
    private List<FilmTags> Genres;

    @Ignore
    private Collectie Collectie;

    public int getId() {
        return Id;
    }

    public String getNaam() {
        return Naam;
    }

    public Date getReleaseDate() {
        return ReleaseDate;
    }

    public String getTagline() {
        return Tagline;
    }

    public int getDuur() {
        return Duur;
    }

    public String getOmschrijving() {
        return Omschrijving;
    }

    public String getTrailerId() {
        return TrailerId;
    }

    public int getCollectieID() {
        return CollectieID;
    }

    public List<ActeurFilm> getActeurs() {
        return (Acteurs == null) ? new ArrayList<ActeurFilm>() : Acteurs;
    }

    public List<FilmTags> getFilmTags() {
        return (Genres == null) ? new ArrayList<FilmTags>() : Genres;
    }


    public Date getToegevoegd() {
        return Toegevoegd;
    }

    public void setToegevoegd(Date toegevoegd) {
        Toegevoegd = toegevoegd;
    }

    public Collectie getCollectie() {
        return Collectie;
    }

    public void setActeurs(List<ActeurFilm> acteurs) {
        Acteurs = acteurs;
    }

    public void setGenres(List<FilmTags> genres) {
        Genres = genres;
    }

    public void setCollectie(com.example.brent.films.Model.Collectie collectie) {
        Collectie = collectie;
    }

    public List<Tag> getGenres(){
        if (Genres == null){
            return new ArrayList<>();
        }

        List<Tag> genres = new ArrayList<>();
        for (FilmTags ft : Genres){
            genres.add(ft.getTag());
        }

        genres.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });

        return genres;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public void setReleaseDate(Date releaseDate) {
        ReleaseDate = releaseDate;
    }

    public void setTagline(String tagline) {
        Tagline = tagline;
    }

    public void setDuur(int duur) {
        Duur = duur;
    }

    public void setOmschrijving(String omschrijving) {
        Omschrijving = omschrijving;
    }

    public void setTrailerId(String trailerId) {
        TrailerId = trailerId;
    }

    public void setCollectieID(int collectieID) {
        CollectieID = collectieID;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Film film = (Film)obj;

            if (film.getId() == this.getId()){
                return true;
            }
        }catch (Exception e){

        }

        return false;
    }
}
