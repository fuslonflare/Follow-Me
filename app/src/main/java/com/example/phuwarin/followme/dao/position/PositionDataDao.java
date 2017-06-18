
package com.example.phuwarin.followme.dao.position;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PositionDataDao {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("name_th")
    @Expose
    private String nameTh;

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
