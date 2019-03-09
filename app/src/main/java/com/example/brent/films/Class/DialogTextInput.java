package com.example.brent.films.Class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films.R;

public class DialogTextInput extends AlertDialog.Builder {

    private Context mContext;
    public EditText txt;

    public DialogTextInput(Context context, String titel, String edit){
        super(context);
        mContext = context;

        setDialog(titel,true, edit);
    }

    public DialogTextInput(Context context, String titel) {
        super(context);
        mContext = context;

        setDialog(titel, false, "");
    }

    private void setDialog(String titel, boolean isEdit, String edit) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_text_input, null);

        TextView lblTitel = view.findViewById(R.id.lblTitel);
        lblTitel.setText(titel);

        txt = (EditText) view.findViewById(R.id.txtInput);

        if (isEdit){
            txt.setText(edit);
        }

        this.setView(view);
    }
}
