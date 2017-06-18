package com.example.phuwarin.followme.util.detail;

/**
 * Created by Phuwarin on 6/18/2017.
 */

public class Position {
    private String id;
    private String nameEn;
    private String nameTh;

    public Position(String id, String nameEn, String nameTh) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameTh = nameTh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }
}
