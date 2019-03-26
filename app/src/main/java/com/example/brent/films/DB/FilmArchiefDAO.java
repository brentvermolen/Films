package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.FilmArchief;

import java.util.List;

@Dao
public interface FilmArchiefDAO {
    @Query("Select * From FilmArchief")
    List<FilmArchief> getAll();

    @Query("Select * From FilmArchief Where archief_id=(:id)")
    List<FilmArchief> getAllByArchiefId(int id);

    @Query("Select * From FilmArchief Where film_id=(:id)")
    List<FilmArchief> getAllByFilmId(int id);

    @Query("Delete From FilmArchief")
    void deleteAll();

    @Query("Delete From FilmArchief Where film_id=(:id)")
    void deleteByFilmId(int id);

    @Query("Delete From FilmArchief Where archief_id=(:id)")
    void deleteByArchiefId(int id);

    @Insert
    void insert(FilmArchief filmArchief);
}
