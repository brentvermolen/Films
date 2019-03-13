package com.example.brent.films.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.FilmTags;

import java.util.List;

@Dao
public interface FilmTagsDAO {
    @Query("Delete From FilmTags")
    void deleteAll();

    @Query("Select * From FilmTags")
    List<FilmTags> getAll();

    @Query("Select * From FilmTags Where Film_ID=(:id)")
    List<FilmTags> getByFilmId(int id);

    @Query("Select * From FilmTags Where Tag_ID=(:id)")
    List<FilmTags> getByTagId(int id);

    @Query("Select * From FilmTags Where Tag_ID=(:tag_id) AND Film_ID=(:film_id)")
    FilmTags getByFilmAndTagId(int tag_id, int film_id);

    @Insert
    void insert(FilmTags film);

    @Query("Delete From FilmTags Where Film_ID = (:id)")
    void deleteByFilmId(int id);

    @Query("Delete From FilmTags Where Tag_ID = (:id)")
    void deleteByTagId(int id);

    @Query("Delete From FilmTags Where Film_ID=(:film_id) AND Tag_ID=(:tag_id)")
    void deleteByFilmAndTagId(int film_id, int tag_id);
}
