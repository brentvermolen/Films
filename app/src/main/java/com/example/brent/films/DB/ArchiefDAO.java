package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("Update Archief Set naam=(:naam) Where id=(:id)")
    void update(int id, String naam);

    @Query("Update Archief Set visible=(:visible) Where id=(:id)")
    void updateVisibility(int id, int visible);

    @Query("Select * From Archief Where visible=0")
    List<Archief> getOnzichtbare();
}
