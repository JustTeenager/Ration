package com.ration.qcode.application.MainPack.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.ration.qcode.application.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROTEINS;

public class ComplicatedProductAdapter extends RecyclerView.Adapter<ComplicatedProductAdapter.ProductHolder> {

    private Context mContext;

    public ArrayList<Intent> getProductMaterials() {
        return productMaterials;
    }

    private ArrayList<Intent> productMaterials;
    private ArrayList<Double> listProteins;
    private ArrayList<Double> listFats;
    private ArrayList<Double> listCarb;
    private ArrayList<Double> listFa;
    private ArrayList<Double> listKl;
    private ArrayList<Double> listGr;

    public ArrayList<Double> getListProteins() {
        return listProteins;
    }

    public ArrayList<Double> getListFats() {
        return listFats;
    }

    public ArrayList<Double> getListCarb() {
        return listCarb;
    }

    public ArrayList<Double> getListFa() {
        return listFa;
    }

    public ArrayList<Double> getListKl() {
        return listKl;
    }

    public ArrayList<Double> getListGr() {
        return listGr;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(mContext).inflate(R.layout.complicated_product_item,null));
    }

    public ComplicatedProductAdapter(Context context){
        this.mContext=context;
        productMaterials=new ArrayList<>();
        listCarb=new ArrayList<>();
        listFats=new ArrayList<>();
        listProteins=new ArrayList<>();
        listFa=new ArrayList<>();
        listKl =new ArrayList<>();
        listGr=new ArrayList<>();
    }

    public void addProduct(Intent intent){
        productMaterials.add(intent);
        listProteins.add(Double.parseDouble(intent.getStringExtra(PROTEINS)));
        listFats.add(Double.parseDouble(intent.getStringExtra(FATS)));
        listCarb.add(Double.parseDouble(intent.getStringExtra(CARBOHYDRATES)));
        listFa.add(Double.parseDouble(intent.getStringExtra(FA)));
        listKl.add(Double.parseDouble(intent.getStringExtra(KL)));
        listGr.add(Double.parseDouble(intent.getStringExtra(GR)));
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.bind(productMaterials.get(position),position);
    }

    @Override
    public int getItemCount() {
        return productMaterials.size();
    }






    public class ProductHolder extends RecyclerView.ViewHolder{

        public double getProteins() {
            return proteins;
        }

        public void setProteins(double proteins) {
            this.proteins = proteins;
        }

        public double getFats() {
            return fats;
        }

        public void setFats(double fats) {
            this.fats = fats;
        }

        public double getCarb() {
            return carb;
        }

        public double getFa() {
            return fa;
        }

        public void setFa(double fa) {
            this.fa = fa;
        }

        public double getKl() {
            return kl;
        }

        public void setKl(double kl) {
            this.kl = kl;
        }

        public double getGr() {
            return gr;
        }

        public double getProteins100() {
            return proteins100;
        }

        public double getFats100() {
            return fats100;
        }

        public double getCarb100() {
            return carb100;
        }
        public double getFa100() {
            return fa100;
        }
        public double getKl100() {
            return kl100;
        }
        public double getGr100() {
            return gr100;
        }

        private TextView textViewProducts, textViewProteins, textViewFats, textViewCarbohydrates,
                textViewFA, textViewKl;

        private EditText editTextGr;

        private int postition;

        private DecimalFormat decimalFormat;
        private double proteins = 0, fats, carb, fa, kl, gr;

        private double proteins100, fats100, carb100, fa100, kl100, gr100;

        public ProductHolder(View itemView) {
            super(itemView);
            decimalFormat=new DecimalFormat("#.##");
            textViewProducts = (TextView) itemView.findViewById(R.id.textViewProduct);
            textViewProteins = (TextView) itemView.findViewById(R.id.textViewProteins);
            textViewFats = (TextView) itemView.findViewById(R.id.textViewFats);
            textViewCarbohydrates = (TextView) itemView.findViewById(R.id.textViewCarbohydrates);
            textViewFA = (TextView) itemView.findViewById(R.id.textViewFA);
            textViewKl = (TextView) itemView.findViewById(R.id.textViewKg);
            editTextGr = (EditText) itemView.findViewById(R.id.edit_text_gr);
            editTextGr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editTextGr.getText().toString().isEmpty()) editTextGr.setText("0");
                    gr= Double.parseDouble(editTextGr.getText().toString());
                    listGr.set(postition,gr);
                    /*Intent intent=new Intent();
                    intent.putExtra(PROTEINS,proteins);
                    intent.putExtra(FA,fa);
                    intent.putExtra(GR,gr);
                    intent.putExtra(FATS,fats);
                    intent.putExtra(CARBOHYDRATES,carb);
                    intent.putExtra(KL,kl);*/
                    productMaterials.get(postition).putExtra(GR,editTextGr.getText().toString());
                    Log.e("gr=", String.valueOf(listGr.get(postition)));
                }
            });
        }

        public void bind(Intent intent, int position) {
            this.postition=position;

            if (intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null) {

                proteins = Double.parseDouble(intent.getStringExtra(PROTEINS));
                fats = Double.parseDouble(intent.getStringExtra(FATS));
                carb = Double.parseDouble(intent.getStringExtra(CARBOHYDRATES));
                fa = Double.parseDouble(intent.getStringExtra(FA));
                kl = Double.parseDouble(intent.getStringExtra(KL));
                gr = Double.parseDouble(intent.getStringExtra(GR));

                proteins100 = proteins;
                fats100 = fats;
                carb100 = carb;
                fa100 = fa;
                kl100 = kl;
                gr100 = gr;

                textViewProducts.setText(intent.getStringExtra(PRODUCTS));
                textViewProteins.setText(decimalFormat.format(proteins));
                textViewFats.setText(decimalFormat.format(fats));
                textViewCarbohydrates.setText(decimalFormat.format(carb));
                textViewFA.setText(decimalFormat.format(fa));
                textViewKl.setText(decimalFormat.format(kl));
                editTextGr.setText(decimalFormat.format(gr));
            }
        }
    }
}
