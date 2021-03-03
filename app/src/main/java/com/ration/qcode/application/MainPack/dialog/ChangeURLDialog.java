package com.ration.qcode.application.MainPack.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SharedPrefManager;

public class ChangeURLDialog extends DialogFragment {

    private EditText urltext;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v= LayoutInflater.from(getContext()).inflate(R.layout.change_url_dialog,null,false);
        urltext=v.findViewById(R.id.edit_url_text);
        urltext.setText(SharedPrefManager.getManager(getContext()).getUrl());
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.change_url)
                .setPositiveButton(getString(R.string.OK),((dialog, which) -> {
                    if (!urltext.getText().toString().isEmpty())
                    changeUrl(urltext.getText().toString());
                    else {Toast.makeText(getContext(), R.string.empty_url,Toast.LENGTH_SHORT).show();}
                }))
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    dismiss();
                })
                .setView(v)
                .create();
    }

    private void changeUrl(String url){
        try {
            SharedPrefManager.getManager(getContext()).setUrl(url);
            NetworkService.getInstance(SharedPrefManager.getManager(getContext()).getUrl()).changeRetrofitUrl(url);
        }catch (Exception e){
            Toast.makeText(getContext(),"Неверный формат домена!",Toast.LENGTH_SHORT).show();
        }
    }
}