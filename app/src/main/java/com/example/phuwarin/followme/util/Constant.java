package com.example.phuwarin.followme.util;

import android.content.Context;
import android.util.SparseArray;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.manager.ContextBuilder;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class Constant {

    private static Constant instance;
    private SparseArray<CharSequence> mapErrorCode;
    private Context mContext;

    private Constant() {
        mContext = ContextBuilder.getInstance().getContext();

        init();
    }

    /**
     * Traditional of Singleton
     **/

    public static Constant getInstance() {
        if (instance == null) {
            instance = new Constant();
        }
        return instance;
    }

    private void init() {
        mapErrorCode = new SparseArray<>(7);
        mapErrorCode.put(346, mContext.getString(R.string.error_message_346));
        mapErrorCode.put(347, mContext.getString(R.string.error_message_347));
        mapErrorCode.put(348, mContext.getString(R.string.error_message_348));
        mapErrorCode.put(349, mContext.getString(R.string.error_message_349));
        mapErrorCode.put(350, mContext.getString(R.string.error_message_350));
        mapErrorCode.put(351, mContext.getString(R.string.error_message_351));

    }

    public void setMessageErrorCode352(String text) {
        mapErrorCode.put(352, mContext.getString(R.string.error_message_352, text));
    }

    public CharSequence getMessage(int errorCode) {
        if (errorCode == 352) {
            if (mapErrorCode.get(352) == null) {
                return null;
            }
        }
        if (mapErrorCode != null && mapErrorCode.size() != 0) {
            return mapErrorCode.get(errorCode);
        }
        return null;
    }

}
