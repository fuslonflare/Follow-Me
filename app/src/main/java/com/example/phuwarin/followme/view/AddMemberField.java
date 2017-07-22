package com.example.phuwarin.followme.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.view.state.BundleSavedState;

/**
 * Created by Phuwarin on 7/18/2017.
 */
public class AddMemberField extends BaseCustomViewGroup {

    AppCompatEditText editText;
    ImageButton buttonQrCode;

    public AddMemberField(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public AddMemberField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public AddMemberField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public AddMemberField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.field_add_member, this);
    }

    private void initInstances() {
        // findViewById here
        editText = findViewById(R.id.edit_member_id);
        buttonQrCode = findViewById(R.id.button_qr_code);

        //buttonQrCode.setOnClickListener(this);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public String getMemberId() {
        return editText.getText().toString();
    }

    public void setMemberId(String scannedId) {
        Log.d("QrCodeTAG", scannedId);
        editText.setText(scannedId);
    }

    // TODO: Add QR code feature
    /*@Override
    public void onClick(View view) {
        if (view == buttonQrCode) {
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, REQUEST_SCAN_QR_CODE);
        }
    }*/
}
