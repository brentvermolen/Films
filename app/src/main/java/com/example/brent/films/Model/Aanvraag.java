package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.brent.films.Class.TimeStampConverter;

import java.util.Date;

@Entity
public class Aanvraag {
    @PrimaryKey
    int Film_ID;
    @TypeConverters(TimeStampConverter.class)
    Date AangevraagdOp;
    int Gebruiker_ID;

    public Aanvraag(){ }

    public int getFilm_ID() {
        return Film_ID;
    }

    public void setFilm_ID(int film_ID) {
        Film_ID = film_ID;
    }

    public Date getAangevraagdOp() {
        return AangevraagdOp;
    }

    public void setAangevraagdOp(Date aangevraagdOp) {
        AangevraagdOp = aangevraagdOp;
    }

    public int getGebruiker_ID() {
        return Gebruiker_ID;
    }

    public void setGebruiker_ID(int gebruiker_ID) {
        Gebruiker_ID = gebruiker_ID;
    }

    @Override
    public boolean equals(Object obj) {
        try{
           Aanvraag aanvraag = (Aanvraag) obj;

           if (aanvraag.getFilm_ID() == this.getFilm_ID()){
               return true;
           }
        }catch (Exception e){
            try{
                Integer id = (Integer)obj;

                if (id == this.getFilm_ID()){
                    return true;
                }
            }catch (Exception ex){ }
        }

        return false;
    }
}
