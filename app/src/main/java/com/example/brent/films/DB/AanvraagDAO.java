package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Aanvraag;

import java.util.List;

@Dao
public interface AanvraagDAO {
    @Query("Delete From Aanvraag")
    void deleteAll();

    @Query("Delete From Aanvraag Where Film_ID=(:id)")
    void deleteByFilmId(int id);

    @Insert
    void insert(Aanvraag aanvraag);

    @Query("Select * From Aanvraag")
    List<Aanvraag> getAll();
}
