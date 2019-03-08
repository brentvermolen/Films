package com.example.brent.films.DB;

import android.os.AsyncTask;
import android.util.Log;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbRemoteMethods {
    static DbRemoteConnectionClass connectionClass = new DbRemoteConnectionClass();

    public static List<Film> GetFilms(){
        Connection con = null;
        List<Film> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From Films Order by 2";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Film film = Film.FromResultSet(rs);

                    data.add(film);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }

    public static List<Collectie> GetCollecties(){
        Connection con = null;
        List<Collectie> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From Collecties Order by 2";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Collectie film = Collectie.FromResultSet(rs);

                    data.add(film);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }

    public static List<ActeurFilm> GetFilmsActeurs(){
        Connection con = null;
        List<ActeurFilm> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From ActeurFilms INNER JOIN Acteurs on Acteurs.ID = ActeurID Where ImagePath != 'NULL' AND ImagePath NOT LIKE '%tvdb%'";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    ActeurFilm film = ActeurFilm.FromResultSet(rs);

                    data.add(film);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }

    public static List<Acteur> GetActeurs() {
        Connection con = null;
        List<Acteur> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From Acteurs Where ImagePath != 'NULL' AND ImagePath NOT LIKE '%tvdb%'";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Acteur film = Acteur.FromResultSet(rs);

                    data.add(film);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }

    public static List<Tag> GetTags() {
        Connection con = null;
        List<Tag> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From Tags";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Tag tag = Tag.FromResultSet(rs);

                    data.add(tag);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }

    public static List<FilmTags> GetFilmTags() {
        Connection con = null;
        List<FilmTags> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From FilmTags";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    FilmTags tag = FilmTags.FromResultSet(rs);

                    data.add(tag);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            z = "Exception: " + ex.toString();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                z = "Error in closing: " + e.toString();
            }
        }

        Log.e("DB Conn", z);
        return data;
    }
}
