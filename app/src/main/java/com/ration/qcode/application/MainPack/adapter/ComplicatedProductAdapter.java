package com.ration.qcode.application.MainPack.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    private EditText mTotalGrEditText;
    private TextWatcher watcher;
    private int selected_position = -1;

    public ComplicatedProductAdapter(Context context, EditText editText) {
        this.mContext = context;
        this.mTotalGrEditText = editText;
        productMaterials = new ArrayList<>();
        listCarb = new ArrayList<>();
        listFats = new ArrayList<>();
        listProteins = new ArrayList<>();
        listFa = new ArrayList<>();
        listKl = new ArrayList<>();
        listGr = new ArrayList<>();
        deletedStringsList = new ArrayList<>();
    }


    public ArrayList<Intent> getProductMaterials() {
        return productMaterials;
    }


    public void setEditText(EditText text) {
        this.mTotalGrEditText = text;
    }

    private ArrayList<Intent> productMaterials;
    private ArrayList<Double> listProteins;
    private ArrayList<Double> listFats;
    private ArrayList<Double> listCarb;
    private ArrayList<Double> listFa;
    private ArrayList<Double> listKl;
    private ArrayList<Double> listGr;
    private ArrayList<String> deletedStringsList;


    public ArrayList<String> getDeletedStringsList() {
        return deletedStringsList;
    }

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
        return new ProductHolder(LayoutInflater.from(mContext).inflate(R.layout.complicated_product_item, null));
    }

    public void addProduct(Intent intent, EditText edit) {
        productMaterials.add(intent);
        listProteins.add(Double.parseDouble(intent.getStringExtra(PROTEINS)));
        listFats.add(Double.parseDouble(intent.getStringExtra(FATS)));
        listCarb.add(Double.parseDouble(intent.getStringExtra(CARBOHYDRATES)));
        listFa.add(Double.parseDouble(intent.getStringExtra(FA)));
        listKl.add(Double.parseDouble(intent.getStringExtra(KL)));
        listGr.add(Double.parseDouble(intent.getStringExtra(GR)));
        mTotalGrEditText = edit;
    }

    public void removeProduct(int position) {
        deletedStringsList.add(productMaterials.get(position).getStringExtra(PRODUCTS));
        productMaterials.remove(position);
        listProteins.remove(position);
        listFats.remove(position);
        listCarb.remove(position);
        listFa.remove(position);
        listKl.remove(position);
        listGr.remove(position);
        notifyDataSetChanged();
        //notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.bind(productMaterials.get(position), position);
        holder.itemView.setBackgroundColor(selected_position == position ? Color.GREEN : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return listFa.size();
    }


    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

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

        private TextView mTextViewGr;
        //private EditText mEditTextGr;

        private int postition;

        private DecimalFormat decimalFormat;
        private double proteins = 0, fats, carb, fa, kl, gr;

        private double proteins100, fats100, carb100, fa100, kl100, gr100;

        public ProductHolder(View itemView) {
            super(itemView);
            decimalFormat = new DecimalFormat("#.##");
            textViewProducts = (TextView) itemView.findViewById(R.id.textViewProduct);
            textViewProteins = (TextView) itemView.findViewById(R.id.textViewProteins);
            textViewFats = (TextView) itemView.findViewById(R.id.textViewFats);
            textViewCarbohydrates = (TextView) itemView.findViewById(R.id.textViewCarbohydrates);
            textViewFA = (TextView) itemView.findViewById(R.id.textViewFA);
            textViewKl = (TextView) itemView.findViewById(R.id.textViewKg);
            mTextViewGr = (TextView) itemView.findViewById(R.id.edit_text_gr);


            //itemView.setOnFocusChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }



        @Override
        public boolean onLongClick(View v) {
            removeProduct(postition);
            return true;
        }



        @Override
        public void onClick(View v) {

            try{
                mTotalGrEditText.removeTextChangedListener(watcher);
            }
            catch (Exception e){e.printStackTrace();}

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            mTotalGrEditText.setText("");
            notifyItemChanged(selected_position);

            watcher=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        if (mTotalGrEditText.getText().toString().isEmpty())
                            mTextViewGr.setText("0");
                        gr = Double.parseDouble(mTotalGrEditText.getText().toString());
                        if (gr > 0) {
                            calculate(gr / listGr.get(postition));
                            listGr.set(postition, gr);
                            productMaterials.get(postition).putExtra(GR, mTotalGrEditText.getText().toString());
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            //mTotalGrEditText.setText(String.valueOf(gr));
            mTotalGrEditText.addTextChangedListener(watcher);
            //mEditTextGr=
        }


        private void calculate(double coeff) {
            proteins100 = getListProteins().get(postition);
            fats100 = getListFats().get(postition);
            carb100 = getListCarb().get(postition);
            fa100 = getListFa().get(postition);
            kl100 = getListKl().get(postition);

            ///рез-т=предыдущие белки/предыдущие граммы * на новые граммы
            proteins = proteins100 * coeff;
            getListProteins().set(postition, proteins);
            fats = fats100 * coeff;
            getListFats().set(postition, fats);
            carb = carb100 * coeff;
            getListCarb().set(postition, carb);
            fa = fa100 * coeff;
            getListFa().set(postition, fa);
            kl = kl100 * coeff;
            getListKl().set(postition, kl);

            textViewProteins.setText(decimalFormat.format(proteins));
            textViewFats.setText(decimalFormat.format(fats));
            textViewCarbohydrates.setText(decimalFormat.format(carb));
            textViewFA.setText(decimalFormat.format(fa));
            textViewKl.setText(decimalFormat.format(kl));
            mTextViewGr.setText(decimalFormat.format(gr));
            // editTextGr.setText(decimalFormat.format(gr));

        }

        public void bind(Intent intent, int position) {
            this.postition = position;

            if (intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null) {
                proteins = Double.parseDouble(String.valueOf(listProteins.get(position)));
                fats = Double.parseDouble(String.valueOf(listFats.get(position)));
                carb = Double.parseDouble(String.valueOf(listCarb.get(position)));
                fa = Double.parseDouble(String.valueOf(listFa.get(position)));
                kl = Double.parseDouble(String.valueOf(listKl.get(position)));
                gr = Double.parseDouble(String.valueOf(listGr.get(position)));

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
                mTextViewGr.setText(decimalFormat.format(gr));
                //mTotalGrEditText.setText(decimalFormat.format(gr));
            }
        }
    }
}