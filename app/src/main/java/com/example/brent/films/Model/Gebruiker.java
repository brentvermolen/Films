package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.brent.films.Class.TimeStampConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Gebruiker {
    @PrimaryKey
    private int id;
    private String voornaam;
    private String achternaam;
    @TypeConverters(TimeStampConverter.class)
    private Date geboorteDatum;
    private String email;
    private String adres;
    private int postcode;
    @TypeConverters(TimeStampConverter.class)
    private Date lastLogin;

    @Ignore
    private List<GebruikerArchief> archieven;

    public Gebruiker(){ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(Date geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<GebruikerArchief> getGebruikerArchieven() {
        return (archieven == null) ? new ArrayList<GebruikerArchief>() : archieven;
    }

    public List<Archief> getArchieven(){
        List<Archief> archiefs = new ArrayList<>();

        for (GebruikerArchief ga : getGebruikerArchieven()){
            archiefs.add(ga.getArchief());
        }

        return archiefs;
    }

    public void setArchieven(List<GebruikerArchief> archieven) {
        this.archieven = archieven;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Gebruiker gebruiker = (Gebruiker) obj;
            if (gebruiker.getId() == this.getId()){
                return true;
            }
        }catch (Exception e){ }

        return false;
    }
}
