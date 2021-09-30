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

public class AdapterAlcanceMinimo extends RecyclerView.Adapter<AdapterAlcanceMinimo.ViewHolderRecycler>{
    private ArrayList<AlcanceMinRecycler> ranking4recycler;
    ViewHolderRecycler viewholderAlcanceMin;
    private  RecyclerView recyclerView;
    private Context context;
    private String alcance_min_bascula;

    public AdapterAlcanceMinimo(Mapa activity, int item, ArrayList<AlcanceMinRecycler> ranking4recycler, Resources resources)
    {
        this.ranking4recycler=ranking4recycler;
    }
    @Override
    public AdapterAlcanceMinimo.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5,parent,false);
        context=parent.getContext();
        return new AdapterAlcanceMinimo.ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlcanceMinimo.ViewHolderRecycler holder, int position) {
        viewholderAlcanceMin =holder;
        alcance_min_bascula = String.valueOf(ranking4recycler.get(position).getMin_bascula());

        holder.min_bascul.setText(alcance_min_bascula);
        holder.min_bascul.setText(alcance_min_bascula);
    }

    @Override
    public int getItemCount(){
        return ranking4recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView min_bascul;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            min_bascul =(TextView)itemView.findViewById(R.id.alcance_min);




        }
    }
}