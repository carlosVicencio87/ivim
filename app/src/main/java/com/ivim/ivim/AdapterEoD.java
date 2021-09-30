package com.ivim.ivim;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEoD  extends RecyclerView.Adapter<AdapterEoD.ViewHolderRecycler>{
    private ArrayList<EodRecycler> ranking4recycler;
    ViewHolderRecycler viewholderEoD;
    private  RecyclerView recyclerView;
    private Context context;
    private String eod_max_bascula;

    public AdapterEoD(Mapa activity, int item, ArrayList<EodRecycler> ranking4recycler, Resources resources)
    {
        this.ranking4recycler=ranking4recycler;
    }
    @Override
    public AdapterEoD.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item4,parent,false);
        context=parent.getContext();
        return new AdapterEoD.ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEoD.ViewHolderRecycler holder, int position) {
        viewholderEoD =holder;
        eod_max_bascula = ranking4recycler.get(position).getEod_bascula();

        holder.eoD_bascul.setText(eod_max_bascula);
        holder.eoD_bascul.setText(eod_max_bascula);
    }

    @Override
    public int getItemCount(){
        return ranking4recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView eoD_bascul;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            eoD_bascul =(TextView)itemView.findViewById(R.id.eod_max);




        }
    }
}