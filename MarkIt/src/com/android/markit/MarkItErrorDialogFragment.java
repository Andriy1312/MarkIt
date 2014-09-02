package com.android.markit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class MarkItErrorDialogFragment extends DialogFragment {

    private Dialog mDialog;

    public MarkItErrorDialogFragment() {
        super();
        mDialog = null;
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}