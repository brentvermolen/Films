package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Acteur;

import java.util.List;

@Dao
public interface ActeursDAO {

    @Query("Delete From Acteur")
    void deleteAll();

    @Query("Select * From Acteur")
    List<Acteur> getAll();

    @Query("Select * From Acteur Where Id=(:id)")
    Acteur getById(int id);

    @Insert
    void insert(Acteur film);

    @Query("Delete From Acteur Where Id = (:id)")
    void deleteById(int id);
}
