package com.ration.qcode.application.MainPack.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ration.qcode.application.MainPack.activity.AddProductActivity;
import com.ration.qcode.application.MainPack.activity.SearchComplicatedProductActivity;
import com.ration.qcode.application.MainPack.adapter.ProductsInfoListAdapter;
import com.ration.qcode.application.R;

import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.MENU;

@SuppressLint("ValidFragment")
public class ChooseProductDialog extends DialogFragment {
    private String date;
    private String menu;
    private Intent intent;

    @SuppressLint("ValidFragment")
    public ChooseProductDialog(String date, String menu, Intent intent){
        this.date = date;
        this.menu = menu;
        this.intent = intent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.choose_product)
                .setPositiveButton(R.string.complicated_product, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent inten = new Intent(getActivity(), SearchComplicatedProductActivity.class);
                        if (intent.getStringExtra("From menu") != null) {
                            inten.putExtra("From menu", "yes");
                            inten.putExtra(MENU, menu);
                            inten.putExtra(DATE, date);
                        }
                        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(inten);
                    }
                })
                .setNegativeButton(R.string.no_complicated_product, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addProduct();
                    }
                })
                .create();
    }

    private void addProduct() {
        Intent inten = new Intent(getActivity(), AddProductActivity.class);
        if (intent.getStringExtra("From menu") != null) {
            inten.putExtra("From menu", "yes");
            inten.putExtra(MENU, menu);
            inten.putExtra(DATE, date);
        }
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inten);
    }
}
