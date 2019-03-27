package com.example.brent.films.DB;

import android.util.Log;

import com.example.brent.films.Model.Aanvraag;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.ActeurFilm;
import com.example.brent.films.Model.Archief;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.FilmArchief;
import com.example.brent.films.Model.FilmTags;
import com.example.brent.films.Model.Gebruiker;
import com.example.brent.films.Model.GebruikerArchief;
import com.example.brent.films.Model.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static List<Tag> GetTags(Date date) {
        Connection con = null;
        List<Tag> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                String query = "SELECT Tags.* FROM Tags Inner Join FilmTags on Tags.ID = FilmTags.Tag_ID Inner Join Films on Films.ID = FilmTags.Film_ID Where Films.Toegevoegd > '" + format.format(date) + "'";
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

    public static List<FilmTags> GetFilmTags(Date date) {
        Connection con = null;
        List<FilmTags> data = new ArrayList<>();
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                String query = "SELECT FilmTags.* From FilmTags Inner Join Films on Films.ID = FilmTags.Film_ID Where Films.Toegevoegd > '" + format.format(date) + "'";
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
                String query = "Select Max(ID) From Tags Where ID>=100";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);
                int max = 99;
                try{
                    rs.first();
                    max = rs.getInt(1);
                }catch (Exception e){ }

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

    public static void InsertTag(Tag tag) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "SET IDENTITY_INSERT Tags ON; Insert Into Tags(ID, Naam) Values (" + tag.getId() + ", '" + tag.getNaam() + "'); SET IDENTITY_INSERT Tags OFF;";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static List<Acteur> GetActeurs(Date date) {
        Connection con = null;
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

    public static void InsertFilmTag(FilmTags filmTags) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Insert Into FilmTags Values (" + filmTags.getFilm_ID() + ", " + filmTags.getTag_ID() + ")";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void InsertFilmActeur(ActeurFilm acteurFilm) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Insert Into ActeurFilms Values (" + acteurFilm.getActeurId() +
                        ", " + acteurFilm.getFilmId() +
                        ", '" + acteurFilm.getKarakter() +
                        "', " + acteurFilm.getSort() + ")";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void DeleteFilmTag(FilmTags filmTags) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Delete From FilmTags Where Film_ID=" + filmTags.getFilm_ID() + " AND Tag_ID=" + filmTags.getTag_ID();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void DeleteFilm(Film film) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Delete From FilmArchiefs Where Film_ID=" + film.getId();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);

                query = "Delete From ActeurFilms Where FilmID=" + film.getId();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);

                query = "Delete From FilmTags Where Film_ID=" + film.getId();
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.execute(query);

                query = "Delete From Films Where ID=" + film.getId();
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

    public static List<Integer> CheckActorsWithoutFilms() {
        Connection con = null;
        String z = "";

        List<Integer> ids = new ArrayList<>();

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "SELECT Acteurs.ID, Acteurs.Naam, Count(Films.ID) As Aantal FROM Acteurs Inner Join ActeurFilms on Acteurs.ID = ActeurFilms.ActeurID Inner Join Films on Films.ID = ActeurFilms.FilmID Group by Acteurs.ID, Acteurs.Naam Having Count(Films.ID)=0";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    ids.add(rs.getInt(1));
                }

                for(int id : ids){
                    try{
                        query = "Delete From Acteurs Where ID=" + id;
                        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        stmt.execute(query);
                    }catch (Exception e){
                        Log.e("Acteurs", "Verwijderen mislukt: " + id);
                    }
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

        return ids;
    }

    public static List<Aanvraag> GetAanvragen(Date date) {
        Connection con = null;
        String z = "";

        List<Aanvraag> aanvragen = new ArrayList<>();

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                String query = "Select * From Aanvraags Where AangevraagOp>'" + format.format(date) + "'";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Aanvraag aanvraag = new Aanvraag();
                    aanvraag.setFilm_ID(rs.getInt(1));
                    aanvraag.setAangevraagdOp(rs.getDate(2));
                    aanvraag.setGebruiker_ID(rs.getInt(3));
                    aanvragen.add(aanvraag);
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

        return aanvragen;
    }

    public static List<Aanvraag> GetAanvragen() {
        Connection con = null;
        String z = "";

        List<Aanvraag> aanvragen = new ArrayList<>();

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Select * From Aanvraags";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Aanvraag aanvraag = new Aanvraag();
                    aanvraag.setFilm_ID(rs.getInt(1));
                    aanvraag.setAangevraagdOp(rs.getDate(2));
                    aanvraag.setGebruiker_ID(rs.getInt(3));
                    aanvragen.add(aanvraag);
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

        return aanvragen;
    }

    public static void InsertFilm(Film film) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date today = new Date();
                today.setTime(Calendar.getInstance().getTimeInMillis());
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                String query = "Insert Into Films Values (" + film.getId() +
                        ", '" + film.getNaam().replace("'", "\"") +
                        "', '" + dateFormat1.format(film.getReleaseDate()) +
                        "', '" + film.getTagline().replace("'", "\"") +
                        "', " + film.getDuur() +
                        ", '" + film.getOmschrijving().replace("'", "\"") +
                        "', '" + film.getTrailerId() +
                        "', '" + dateFormat.format(today) +
                        "', '" + film.getPosterPath() +
                        "', " + film.getCollectieID() + ")";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void InsertCollectie(Collectie collectie) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Insert Into Collecties Values (" + collectie.getId() +
                        ", '" + collectie.getNaam() +
                        "', '" + collectie.getPosterPath() + "')";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void InsertActeur(Acteur acteur) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Insert Into Acteurs Values (" + acteur.getId() +
                        ", '" + acteur.getNaam() +
                        "', '" + acteur.getPosterPath() + "')";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static Aanvraag InsertAanvraag(Aanvraag aanvraag) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = new Date(Calendar.getInstance().getTimeInMillis());
                aanvraag.setAangevraagdOp(date);
                aanvraag.setGebruiker_ID(1);

                String query = "Insert Into Aanvraags Values (" + aanvraag.getFilm_ID() +
                        ", '" + format.format(date) +
                        "', " + 1 + ")";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
        return aanvraag;
    }

    public static void DeleteAanvraag(int film_id) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Delete From Aanvraags Where FilmId=" + film_id;
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static List<Gebruiker> GetGebruikers() {
        List<Gebruiker> gebruikers = new ArrayList<>();
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "SELECT ID, Voornaam, Achternaam, Geboortedatum, Email, Adres, Postcode, Max(ProviderKey) as Login" +
                        " FROM [vermolens_trakt].[dbo].[Gebruikers]" +
                        " Inner Join GebruikerLogins on Gebruikers.ID = Gebruiker_ID" +
                        " Group by ID, Voornaam, Achternaam, Geboortedatum, Email, Adres, Postcode";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    SimpleDateFormat dateFormatGeboorteDatum = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat dateFormatLastLogin = new SimpleDateFormat("dd-MM-yyyy hh:ss");

                    Gebruiker gebruiker = new Gebruiker();
                    gebruiker.setId(rs.getInt(1));
                    gebruiker.setVoornaam(rs.getString(2));
                    gebruiker.setAchternaam(rs.getString(3));
                    gebruiker.setGeboorteDatum(dateFormatGeboorteDatum.parse(rs.getString(4)));
                    gebruiker.setEmail(rs.getString(5));
                    gebruiker.setAdres(rs.getString(6));
                    gebruiker.setPostcode(rs.getInt(7));
                    gebruiker.setLastLogin(dateFormatLastLogin.parse(rs.getString(8)));
                    gebruikers.add(gebruiker);
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
        return gebruikers;
    }

    public static List<Archief> GetArchievenVanGebruiker(int user_id) {
        List<Archief> archieven = new ArrayList<>();

        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "SELECT Archiefs.* FROM Archiefs Inner Join GebruikerArchiefs on Archief_ID=Archiefs.ID Where Gebruiker_ID=" + user_id;
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    Archief archief = new Archief();
                    archief.setId(rs.getInt(1));
                    archief.setNaam(rs.getString(2));
                    archieven.add(archief);
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

        return archieven;
    }

    public static Object[] GetArchievenEnGebruikerFilms(int user_id, Date date) {
        Connection con = null;
        List<FilmArchief> filmArchiefs = new ArrayList<>();
        List<GebruikerArchief> gebruikerArchiefs = new ArrayList<>();

        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                String query = "SELECT FilmArchiefs.*, Gebruiker_ID FROM Archiefs Inner Join [vermolens_trakt].[dbo].[FilmArchiefs] on Archiefs.ID = FilmArchiefs.Archief_ID Inner Join GebruikerArchiefs on GebruikerArchiefs.Archief_ID = FilmArchiefs.Archief_ID Inner Join Films on Film_ID=Films.ID Where Films.Toegevoegd > '" + format.format(date) + "' And GebruikerArchiefs.Gebruiker_ID=" + user_id;
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()){
                    FilmArchief fa = new FilmArchief();
                    GebruikerArchief ga = new GebruikerArchief();

                    fa.setFilm_id(rs.getInt(1));
                    fa.setArchief_id(rs.getInt(2));

                    ga.setArchief_id(rs.getInt(2));
                    ga.setGebruiker_id(rs.getInt(3));

                    filmArchiefs.add(fa);
                    if (!gebruikerArchiefs.contains(ga)){
                        gebruikerArchiefs.add(ga);
                    }
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
        return new Object[] { filmArchiefs, gebruikerArchiefs };
    }

    public static void UpdateArchief(Archief archief) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Update Archiefs Set Naam = '" + archief.getNaam() + "' Where ID = " + archief.getId();
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

    public static void InsertFilmArchief(FilmArchief filmArchief) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Insert Into FilmArchiefs Values (" + filmArchief.getFilm_id() + ", " + filmArchief.getArchief_id() + ")";
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public static void DeleteFilmArchief(FilmArchief filmArchief) {
        Connection con = null;
        String z = "";

        try {
            con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "Delete From FilmArchiefs Where Film_ID=" + filmArchief.getFilm_id() + " AND Archief_ID=" + filmArchief.getArchief_id();
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
}
