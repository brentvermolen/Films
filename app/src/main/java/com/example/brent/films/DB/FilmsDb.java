package com.example.brent.films.DB;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;

import java.util.concurrent.Executors;

@Database(entities =  {Film.class, Tag.class, Collectie.class, ActeurFilm.class, Acteur.class, FilmTags.class }, version = 1)
public abstract class FilmsDb extends RoomDatabase {
    private static FilmsDb INSTANCE;
    private static final String DB_NAME = "Films.db";

    public abstract FilmsDAO filmsDAO();
    public abstract GenresDAO genresDAO();
    public abstract CollectiesDAO collectiesDAO();
    public abstract ActeursDAO acteursDAO();
    public abstract ActeurFilmsDAO acteurFilmsDAO();
    public abstract FilmTagsDAO filmTagsDAO();

    public static FilmsDb getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (FilmsDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FilmsDb.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            new EmptyAndFillDbAsync(INSTANCE).insertData();
                                        }
                                    });

                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    /*public void clearDb(){
        if (INSTANCE != null){
            new EmptyAndFillDbAsync(INSTANCE).execute();
        }
    }*/

    private static class EmptyAndFillDbAsync {
        private final FilmsDAO filmsDao;
        private final GenresDAO genresDAO;
        private final CollectiesDAO collectiesDAO;
        private final ActeursDAO acteursDAO;
        private final FilmTagsDAO filmTagsDAO;
        private final ActeurFilmsDAO acteurFilmsDAO;

        public EmptyAndFillDbAsync(FilmsDb instance) {
            filmsDao = instance.filmsDAO();
            genresDAO = instance.genresDAO();
            collectiesDAO = instance.collectiesDAO();
            acteursDAO = instance.acteursDAO();
            filmTagsDAO = instance.filmTagsDAO();
            acteurFilmsDAO = instance.acteurFilmsDAO();
        }

        protected Void insertData() {
            filmsDao.deleteAll();
            genresDAO.deleteAll();
            collectiesDAO.deleteAll();
            acteursDAO.deleteAll();
            filmTagsDAO.deleteAll();
            acteurFilmsDAO.deleteAll();

            return null;
        }
    }
}
