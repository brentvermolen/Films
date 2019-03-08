package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Collectie;

import java.util.List;

@Dao
public interface CollectiesDAO {

    @Query("Delete From Collectie")
    void deleteAll();

    @Query("Select * From Collectie")
    List<Collectie> getAll();

    @Query("Select * From Collectie Where Id=(:id)")
    Collectie getById(int id);

    @Insert
    void insert(Collectie collectie);

    @Query("Delete From Collectie Where Id = (:id)")
    void deleteById(int id);
}
