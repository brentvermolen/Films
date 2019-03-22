package com.example.brent.films.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brent.films.R;

public class DialogTextOutput extends AlertDialog.Builder {

    private Context mContext;
    public TextView lbl;

    public DialogTextOutput(Context context, String titel, String tekst){
        super(context);
        mContext = context;

        setDialog(titel, tekst);
    }

    private void setDialog(String titel, String tekst) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_text_output, null);

        TextView lblTitel = view.findViewById(R.id.lblTitel);
        lblTitel.setText(titel);

        lbl = (TextView) view.findViewById(R.id.lblOutput);
        lbl.setText(tekst);

        this.setView(view);
    }
}
