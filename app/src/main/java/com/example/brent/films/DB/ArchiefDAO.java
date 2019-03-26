package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Archief;

import java.util.List;

@Dao
public interface ArchiefDAO {
    @Query("Select * From Archief")
    List<Archief> getAll();

    @Query("Select * From Archief Where id=(:id)")
    Archief getById(int id);

    @Insert
    void insert(Archief archief);

    @Query("Delete From Archief")
    void deleteAll();

    @Query("Delete From Archief Where id=(:id)")
    void deleteById(int id);
}
