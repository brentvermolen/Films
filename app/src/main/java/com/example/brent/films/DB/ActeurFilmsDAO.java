package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;

import java.util.List;

@Dao
public interface ActeurFilmsDAO {
    @Query("Delete From ActeurFilm")
    void deleteAll();

    @Query("Select * From ActeurFilm")
    List<ActeurFilm> getAll();

    @Query("Select * From ActeurFilm Where FilmId=(:id)")
    ActeurFilm getByFilmId(int id);

    @Query("Select * From ActeurFilm Where ActeurId=(:id)")
    ActeurFilm getByActeurId(int id);

    @Insert
    void insert(ActeurFilm film);

    @Query("Delete From ActeurFilm Where FilmId = (:id)")
    void deleteByFilmId(int id);

    @Query("Delete From ActeurFilm Where ActeurId = (:id)")
    void deleteByActeurId(int id);
}
