package com.example.phuwarin.followme.view.state;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Phuwarin on 7/18/2017.
 */

public class BundleSavedState extends View.BaseSavedState {

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new BundleSavedState(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new BundleSavedState[size];
        }
    };
    private Bundle bundle = new Bundle();

    public BundleSavedState(Parcel source) {
        super(source);
        this.bundle = source.readBundle();
    }

    public BundleSavedState(Parcelable superState) {
        super(superState);
    }

    @TargetApi(24)
    public BundleSavedState(Parcel source, ClassLoader loader) {
        super(source, loader);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBundle(bundle);
    }

    public Bundle getBundle() {
        return this.bundle;
    }
}
