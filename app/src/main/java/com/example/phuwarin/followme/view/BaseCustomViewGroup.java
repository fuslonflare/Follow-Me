package com.example.phuwarin.followme.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;

import com.example.phuwarin.followme.view.state.BundleSavedState;

/**
 * Created by Phuwarin on 7/18/2017.
 */

public class BaseCustomViewGroup extends FrameLayout {
    public BaseCustomViewGroup(@NonNull Context context) {
        super(context);
    }

    public BaseCustomViewGroup(@NonNull Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCustomViewGroup(@NonNull Context context,
                               @Nullable AttributeSet attrs,
                               @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseCustomViewGroup(@NonNull Context context,
                               @Nullable AttributeSet attrs,
                               @AttrRes int defStyleAttr,
                               @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        // Save Children's state as a Bundle
        Bundle childrenStates = new Bundle();
        for (int i = 0; i < getChildCount(); i++) {
            int id = getChildAt(i).getId();
            if (id != 0) {
                SparseArray childrenState = new SparseArray();
                getChildAt(i).saveHierarchyState(childrenState);
                childrenStates.putSparseParcelableArray(String.valueOf(id), childrenState);
            }
        }

        // Save it to Parcelable
        BundleSavedState ss = new BundleSavedState(superState);
        ss.getBundle().putBundle("childrenStates", childrenStates);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        // Restore SparseArray
        Bundle childrenStates = ss.getBundle().getBundle("childrenStates");
        // Restore Children's state
        for (int i = 0; i < getChildCount(); i++) {
            int id = getChildAt(i).getId();
            if (id != 0) {
                if (childrenStates.containsKey(String.valueOf(id))) {
                    SparseArray childrenState =
                            childrenStates.getSparseParcelableArray(String.valueOf(id));
                    getChildAt(i).restoreHierarchyState(childrenState);
                }
            }
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

}
