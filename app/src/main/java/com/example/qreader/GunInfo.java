package com.example.qreader;

public class GunInfo {
    String username;
    String gunname;

    public GunInfo(String username, String gunname) {
        this.username = username;
        this.gunname = gunname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGunname() {
        return gunname;
    }

    public void setGunname(String gunname) {
        this.gunname = gunname;
    }
}
