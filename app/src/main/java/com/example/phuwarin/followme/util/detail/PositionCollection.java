package com.example.phuwarin.followme.util.detail;

import android.content.Context;

import com.example.phuwarin.followme.dao.position.PositionDataDao;
import com.example.phuwarin.followme.manager.ContextBuilder;

import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PositionCollection {

    private static PositionCollection instance;
    private Context mContext;

    private List<PositionDataDao> positionList;

    private PositionCollection() {
        mContext = ContextBuilder.getInstance().getContext();
    }

    /**
     * Traditional of Singleton
     **/

    public static PositionCollection getInstance() {
        if (instance == null)
            instance = new PositionCollection();
        return instance;
    }

    public List<PositionDataDao> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionDataDao> positionList) {
        this.positionList = positionList;
    }
}
