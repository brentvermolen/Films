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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    public static List<Film> GetFilms(Date addedAfter){
        Connection con = null;
        List<Film> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strFormat = format.format(addedAfter);

                String query = "Select * From Films Where Toegevoegd >= '" + strFormat + "' Order by 2";
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

    public static List<ActeurFilm> GetFilmsActeurs(Date date){
        Connection con = null;
        List<ActeurFilm> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                String query = "Select ActeurFilms.* From Films Inner Join ActeurFilms on Films.ID = ActeurFilms.FilmID INNER JOIN Acteurs on Acteurs.ID = ActeurID Where Films.Toegevoegd >= '" + strDate + "' AND Acteurs.ImagePath != 'NULL' AND Acteurs.ImagePath NOT LIKE '%tvdb%'";
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

    public static Tag InsertTag(String naam) {
        Connection con = null;
        String z = "";
        Tag tag = new Tag();

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select Max(ID) From Tags";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);
                rs.first();
                int max = rs.getInt(1);

                query = "SET IDENTITY_INSERT Tags ON; Insert Into Tags(ID, Naam) Values (" + ++max + ", '" + naam + "'); SET IDENTITY_INSERT Tags OFF;";
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);

                tag.setId(max);
                tag.setNaam(naam);
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
        return tag;
    }

    public static void UpdateTag(Tag tag) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Update Tags Set Naam = '" + tag.getNaam() + "' Where ID = " + tag.getId();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.executeUpdate(query);
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
    }

    public static void DeleteTag(Tag tag) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Delete From FilmTags Where Tag_ID = " + tag.getId();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);

                query = "Delete From Tags Where ID = " + tag.getId();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);
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
    }

    public static List<Acteur> GetActeurs(Date date) {Connection con = null;
        List<Acteur> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                String query = "Select Distinct Acteurs.* From Acteurs Inner Join ActeurFilms on Acteurs.ID = ActeurFilms.ActeurID Inner join Films on Films.ID = ActeurFilms.FilmID Where Films.Toegevoegd >= '" + strDate + "' AND Acteurs.ImagePath != 'NULL' AND Acteurs.ImagePath NOT LIKE '%tvdb%'";
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
}
