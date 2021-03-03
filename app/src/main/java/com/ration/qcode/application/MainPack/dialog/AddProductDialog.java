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
import android.widget.Toast;

import com.ration.qcode.application.MainPack.validator.Validator;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.Constants;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SharedPrefManager;
import com.ration.qcode.application.utils.internet.AddProductAPI;
import com.ration.qcode.application.utils.internet.AddProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepdev on 10.04.17.
 */

public class AddProductDialog extends DialogFragment implements View.OnClickListener {

    private EditText editName;
    private EditText editFa;
    private EditText editKkal;
    private EditText editBelok;
    private EditText editUglevod;
    private EditText editJiry;
    private Button buttonAdd;
    private Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_product_dialog, container, false);
        editName = (EditText) view.findViewById(R.id.edit_name);
        editFa = (EditText) view.findViewById(R.id.edit_fa);
        editKkal = (EditText) view.findViewById(R.id.edit_kkal);
        editBelok = (EditText) view.findViewById(R.id.edit_belok);
        editUglevod = (EditText) view.findViewById(R.id.edit_uglevod);
        editJiry = (EditText) view.findViewById(R.id.edit_jiry);

        buttonAdd = (Button) view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);

        validator = new Validator();

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
        String name = editName.getText().toString();
        String fa = editFa.getText().toString();
        String kkal = editKkal.getText().toString();
        String belok = editBelok.getText().toString();
        String uglevod = editUglevod.getText().toString();
        String jiry = editJiry.getText().toString();

        if (validator.validetName(name)) {
            if (validator.validetFA(fa)) {
                if (validator.validetKkal(kkal)) {
                    if (validator.validetBelki(belok)) {
                        if (validator.validetUglevod(uglevod)) {
                            if (validator.validetJiry(jiry)) {

                                //DataBaseHelper.getInstance(getActivity()).insertIntoProduct(name+"|",belok,jiry,uglevod,fa,kkal,"100","0");

                                NetworkService.getInstance(SharedPrefManager.getManager(getContext()).getUrl())
                                        .getApi(AddProductAPI.class)
                                        .insertProduct(name, fa, kkal, belok, uglevod, jiry,"100","0")
                                        .enqueue(
                                                new Callback<AddProductResponse>() {
                                                    @Override
                                                    public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {
                                                        if (response.isSuccessful()) {
                                                            if (response.body().getStatus().equals("ok")) {
                                                                Toast.makeText(getContext(), response.body().getAnswer(), Toast.LENGTH_SHORT).show();
                                                                dismiss();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<AddProductResponse> call, Throwable t) {
                                                        dismiss();
                                                    }
                                                });

                            } else {
                                Toast.makeText(getContext(), "Поле Жиры НЕ заполнено!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Поле Углеводы НЕ заполнено!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Поле Белки НЕ заполнено!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Поле Килокалории НЕ заполнено!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Поле ФА НЕ заполнено!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Название продукта введено не верно! Попробуйте еще раз!", Toast.LENGTH_SHORT).show();
            editName.setText("");
        }
    }
}