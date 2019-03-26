package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.GebruikerArchief;

import java.util.List;

@Dao
public interface GebruikerArchiefDAO {
    @Query("Select * From GebruikerArchief")
    List<GebruikerArchief> getAll();

    @Query("Select * From GebruikerArchief Where gebruiker_id=(:id)")
    List<GebruikerArchief> getAllByGebruikerId(int id);

    @Query("Select * From GebruikerArchief Where archief_id=(:id)")
    List<GebruikerArchief> getAllByArchiefId(int id);

    @Insert
    void insert(GebruikerArchief gebruikerArchiefDAO);

    @Query("Delete From GebruikerArchief")
    void deleteAll();

    @Query("Delete From GebruikerArchief Where gebruiker_id=(:id)")
    void deleteAllByGebruikerId(int id);

    @Query("Delete From GebruikerArchief Where archief_id=(:id)")
    void deleteAllByArchiefId(int id);
}