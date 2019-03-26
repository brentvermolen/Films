package com.example.brent.films.DB;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.brent.films.Model.*;

import java.util.concurrent.Executors;

@Database(entities =  { Film.class, Tag.class, Collectie.class, ActeurFilm.class, Acteur.class, FilmTags.class,
                        Aanvraag.class, Gebruiker.class, Archief.class, FilmArchief.class, GebruikerArchief.class },
            version = 1)
public abstract class FilmsDb extends RoomDatabase {
    private static FilmsDb INSTANCE;
    private static final String DB_NAME = "Films.db";

    public abstract FilmsDAO filmsDAO();
    public abstract GenresDAO genresDAO();
    public abstract CollectiesDAO collectiesDAO();
    public abstract ActeursDAO acteursDAO();
    public abstract ActeurFilmsDAO acteurFilmsDAO();
    public abstract FilmTagsDAO filmTagsDAO();
    public abstract AanvraagDAO aanvraagDAO();
    public abstract GebruikersDAO gebruikersDAO();
    public abstract ArchiefDAO archiefDAO();
    public abstract FilmArchiefDAO filmArchiefDAO();
    public abstract GebruikerArchiefDAO gebruikerArchiefDAO();

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
        private final AanvraagDAO aanvraagDAO;
        private final GebruikersDAO gebruikersDAO;
        private final ArchiefDAO archiefDAO;
        private final GebruikerArchiefDAO gebruikerArchiefDAO;
        private final FilmArchiefDAO filmArchiefDAO;

        public EmptyAndFillDbAsync(FilmsDb instance) {
            filmsDao = instance.filmsDAO();
            genresDAO = instance.genresDAO();
            collectiesDAO = instance.collectiesDAO();
            acteursDAO = instance.acteursDAO();
            filmTagsDAO = instance.filmTagsDAO();
            acteurFilmsDAO = instance.acteurFilmsDAO();
            aanvraagDAO = instance.aanvraagDAO();
            gebruikersDAO = instance.gebruikersDAO();
            archiefDAO = instance.archiefDAO();
            gebruikerArchiefDAO = instance.gebruikerArchiefDAO();
            filmArchiefDAO = instance.filmArchiefDAO();
        }

        protected Void insertData() {
            filmsDao.deleteAll();
            genresDAO.deleteAll();
            collectiesDAO.deleteAll();
            acteursDAO.deleteAll();
            filmTagsDAO.deleteAll();
            acteurFilmsDAO.deleteAll();
            aanvraagDAO.deleteAll();
            gebruikersDAO.deleteAll();
            archiefDAO.deleteAll();
            gebruikerArchiefDAO.deleteAll();
            filmArchiefDAO.deleteAll();

            return null;
        }
    }
}
