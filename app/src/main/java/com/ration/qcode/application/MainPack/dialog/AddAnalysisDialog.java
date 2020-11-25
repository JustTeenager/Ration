package com.ration.qcode.application.MainPack.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deepdev on 10.04.17.
 */

public class AddAnalysisDialog extends DialogFragment implements View.OnClickListener {
    DataBaseHelper db;
    private EditText editDate;
    private EditText editFA;
    private EditText editNotice;
    private Button buttonSave;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date datenows = new Date();
    String datenow = dateFormat.format(datenows);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_analysis_fragment, container, false);
        db = DataBaseHelper.getInstance(getActivity());
        editDate = (EditText) view.findViewById(R.id.editDate);
        editFA = (EditText) view.findViewById(R.id.editFAlevel);
        editNotice = (EditText) view.findViewById(R.id.editNotice);

        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        editDate.setText(datenow);

        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onClick(View view) {
        String date = editDate.getText().toString();
        String faLevel = editFA.getText().toString();
        String notice = editNotice.getText().toString();
        if (!date.isEmpty() && !faLevel.isEmpty()) {

            if(notice.isEmpty()) {notice = "";}
            senDataToActivity(date, faLevel, notice);
            db.insertIntoAnalize(date, faLevel, notice);
        }
        dismiss();
    }

    private void senDataToActivity(String date, String faLevel, String notice) {
        ((OnReceivedData) getTargetFragment()).onDataReceived(date, faLevel, notice);
    }

    public interface OnReceivedData {
        void onDataReceived(String date, String faLevel, String notice);
    }



}