package com.example.brent.films.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.Collectie;
import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

public class Methodes {
    public static List<Film> GetMoviesFromCollection(List<Film> films, Collectie collectie){
        List<Film> fs = new ArrayList<>();

        for(Film f : films){
            if (f.getCollectieID() == collectie.getId()){
                fs.add(f);
            }
        }

        return fs;
    }

    public static Acteur FindActeurById(List<Acteur> Acteurs, int ID){
        for (Acteur a : Acteurs){
            if (a.getId() == ID){
                return a;
            }
        }

        return null;
    }

    public static Film FindFilmById(List<Film> Films, int ID){
        for (Film a : Films){
            if (a.getId() == ID){
                return a;
            }
        }

        return null;
    }

    public static Tag FindTagById(List<Tag> Tags, int ID) {
        for (Tag a : Tags){
            if (a.getId() == ID){
                return a;
            }
        }

        return null;
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) throws IOException
    {
        return BitmapFactory.decodeStream(new FileInputStream(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/Films/" + strName));
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static List<Film> SortFilmsByNameAndCollection(List<Film> films){
        films.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                /*if (o1.getCollectieID() == o2.getCollectieID() && o1.getCollectieID() != 0){
                    return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                }*/

                return o1.getNaam().compareTo(o2.getNaam());
            }
        });

        return films;
    }

    public static Collectie GetCollectionFromID(List<Collectie> collecties, int ID) {
        for (Collectie c : collecties){
            if (c.getId() == ID){
                return c;
            }
        }

        return null;
    }

    public static List<Film> FilterFilmsByName(String filter) {
        List<Film> films = new ArrayList<>();

        for (Film f : DAC.Films){
            if (f.getNaam().toLowerCase().contains(filter.toLowerCase())){
                films.add(f);
            }
        }

        return films;
    }

    public static List<Acteur> FilterActeursByName(String filter) {
        List<Acteur> acteurs = new ArrayList<>();

        for (Acteur a : DAC.Acteurs){
            if (a.getNaam().toLowerCase().contains(filter.toLowerCase())){
                acteurs.add(a);
            }
        }

        return acteurs;
    }

    public static boolean download342Poster(int id, String poster) {
        File dir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        if (!dir.exists()){
            dir.mkdirs();
        }

        File dirFilms = new File(dir.getAbsolutePath() + "/Films/films/");
        if (!dirFilms.exists()){
            if (dirFilms.mkdir()){
                File nomedia = new File(dirFilms.getAbsolutePath() + "/.NOMEDIA");
                try {
                    nomedia.createNewFile();
                    Log.e("Films", "Create .NOMEDIA");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Films", "Create .NOMEDIA failed");
                }
            }else{
                Log.e("Films", "Dir create failed");
            }
        }

        File flePoster = new File(dirFilms.getAbsolutePath() + "/" + id + ".jpg");

        if (!flePoster.exists()){
            try (FileOutputStream out = new FileOutputStream(flePoster)) {
                InputStream is = (InputStream) new URL("http://image.tmdb.org/t/p/w342/" + poster).getContent();
                Bitmap d = BitmapFactory.decodeStream(is);
                is.close();
                d.compress(Bitmap.CompressFormat.JPEG, 85, out);
            } catch (Exception e) {
                Log.e("Films", id + ": Foto opslaan mislukt");
                return false;
            }
        }

        return true;
    }

    public static boolean download92Poster(int id, String poster) {
        File dir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        if (!dir.exists()){
            dir.mkdirs();
        }

        File dirFilms = new File(dir.getAbsolutePath() + "/Films/acteurs/");
        if (!dirFilms.exists()){
            if (dirFilms.mkdir()){
                File nomedia = new File(dirFilms.getAbsolutePath() + "/.NOMEDIA");
                try {
                    nomedia.createNewFile();
                    Log.e("Acteurs", "Create .NOMEDIA");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Acteurs", "Create .NOMEDIA failed");
                }
            }else{
                Log.e("Acteurs", "Dir create failed");
            }
        }

        File flePoster = new File(dirFilms.getAbsolutePath() + "/" + id + ".jpg");

        if (!flePoster.exists()){
            try (FileOutputStream out = new FileOutputStream(flePoster)) {
                InputStream is = (InputStream) new URL("http://image.tmdb.org/t/p/w92/" + poster).getContent();
                Bitmap d = BitmapFactory.decodeStream(is);
                is.close();
                d.compress(Bitmap.CompressFormat.JPEG, 85, out);
            } catch (Exception e) {
                Log.e("Acteurs", id + ": Foto opslaan mislukt");
                if (flePoster.exists()){
                    flePoster.delete();
                }
                return false;
            }
        }

        return true;
    }
}
