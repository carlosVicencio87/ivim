package com.ivim.ivim;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAlcanceMax extends RecyclerView.Adapter<AdapterAlcanceMax.ViewHolderRecycler>{
    private ArrayList<AlcanceRecycler> ranking3recycler;
    ViewHolderRecycler viewholderAlcanceMax,anterior;
    private  RecyclerView recyclerView;
    private Context context;
    private String alcance_max_bascula;
    private SharedPreferences datosAlcanceMax;
    private SharedPreferences.Editor editor;

    public AdapterAlcanceMax(Mapa activity, int item, ArrayList<AlcanceRecycler> ranking3recycler, Resources resources)
    {
        this.ranking3recycler=ranking3recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3,parent,false);
        context=parent.getContext();
        datosAlcanceMax= context.getSharedPreferences("alcancesMax",context.MODE_PRIVATE);
        editor=datosAlcanceMax.edit();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAlcanceMax.ViewHolderRecycler holder, int position) {
        viewholderAlcanceMax =holder;
        alcance_max_bascula = ranking3recycler.get(position).getAlcance_bascula();

        holder.alcanc_bascula.setText(alcance_max_bascula);
        holder.alcanc_bascula.setText(alcance_max_bascula);
        final String alcanceMax  =  holder.alcanc_bascula.getText().toString();
        holder.marco2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,alcanceMax,Toast.LENGTH_SHORT).show();
                editor.putString("alcanceMax",alcanceMax);
                editor.apply();

                if (anterior!=null)
                {
                    anterior.marco2.setBackgroundResource(R.color.blanco);
                    anterior.alcanc_bascula.setTextColor(context.getResources().getColor(R.color.negro));
                }
                holder.marco2.setBackgroundResource(R.color.negro);
                holder.alcanc_bascula.setTextColor(context.getResources().getColor(R.color.blanco));



                anterior=holder;

            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking3recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView alcanc_bascula;
        LinearLayout marco2;

        public ViewHolderRecycler(View itemView) {
            super(itemView);
            alcanc_bascula =(TextView)itemView.findViewById(R.id.alcance_max);
            marco2=(LinearLayout)itemView.findViewById(R.id.marco2);



        }
    }
}