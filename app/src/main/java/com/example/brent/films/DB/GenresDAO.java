package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;

import java.util.List;

@Dao
public interface GenresDAO {
    @Query("Delete From Tag")
    void deleteAll();

    @Query("Select * From Tag")
    List<Tag> getAll();

    @Query("Select * From Tag Where Id = (:id)")
    Tag getById(int id);

    @Query("Select * From Tag Where isHidden = 1")
    List<Tag> getHiddenTags();

    @Query("Select * From Tag Where isHidden = 0")
    List<Tag> getVisibleTags();

    @Insert
    void insert(Tag tag);

    @Query("Delete From Tag Where Id = (:id)")
    void deleteById(int id);

    @Query("Update Tag Set isHidden = (:hidden) Where Id = (:id)")
    void updateVisibility(int id, int hidden);
}
