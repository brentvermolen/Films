package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

@Entity(primaryKeys = { "gebruiker_id", "archief_id" })
public class GebruikerArchief {
    private int gebruiker_id;
    private int archief_id;

    @Ignore
    private Gebruiker gebruiker;
    @Ignore
    private Archief archief;

    public GebruikerArchief() {}

    public int getGebruiker_id() {
        return gebruiker_id;
    }

    public void setGebruiker_id(int gebruiker_id) {
        this.gebruiker_id = gebruiker_id;
    }

    public int getArchief_id() {
        return archief_id;
    }

    public void setArchief_id(int archief_id) {
        this.archief_id = archief_id;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public Archief getArchief() {
        return archief;
    }

    public void setArchief(Archief archief) {
        this.archief = archief;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            GebruikerArchief gebruikerArchief = (GebruikerArchief) obj;

            if (gebruikerArchief.getGebruiker_id() == this.getGebruiker_id()
                    && gebruikerArchief.getArchief_id() == this.getArchief_id()){
                return true;
            }
        }catch (Exception e){ }

        return false;
    }
}
