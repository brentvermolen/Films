package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Film;

import java.util.List;

@Dao
public interface FilmsDAO {

    @Query("Delete From Film")
    void deleteAll();

    @Query("Select * From Film")
    List<Film> getAll();

    @Query("Select * From Film Where Id=(:id)")
    Film getById(int id);

    @Insert
    void insert(Film film);

    @Query("Delete From Film Where Id = (:id)")
    void deleteById(int id);

    @Query("Select * From Film Where isFavorite=1")
    List<Film> getFavorits();

    @Query("Update Film Set isFavorite = (:isFavorite) Where Id = (:id)")
    void toggleFavorits(int id, int isFavorite);
}
