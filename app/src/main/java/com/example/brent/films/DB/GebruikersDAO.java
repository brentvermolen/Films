package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Gebruiker;

import java.util.List;

@Dao
public interface GebruikersDAO {
    @Query("Select * From Gebruiker")
    List<Gebruiker> getAll();

    @Query("Select * From Gebruiker Where id=(:id)")
    Gebruiker getById(int id);

    @Insert
    void insert(Gebruiker gebruiker);

    @Query("Delete From Gebruiker")
    void deleteAll();
}
