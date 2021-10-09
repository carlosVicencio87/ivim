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

public class AdapterAlcanceMinimo extends RecyclerView.Adapter<AdapterAlcanceMinimo.ViewHolderRecycler>{
    private ArrayList<AlcanceMinRecycler> ranking4recycler;
    ViewHolderRecycler viewholderAlcanceMin,anterior;
    private  RecyclerView recyclerView;
    private Context context;
    private String alcance_min_bascula;
    private SharedPreferences datosAlcanceMin;
    private SharedPreferences.Editor editor;

    public AdapterAlcanceMinimo(Mapa activity, int item, ArrayList<AlcanceMinRecycler> ranking4recycler, Resources resources)
    {
        this.ranking4recycler=ranking4recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5,parent,false);
        context=parent.getContext();
        datosAlcanceMin= context.getSharedPreferences("alcancesMin",context.MODE_PRIVATE);
        editor=datosAlcanceMin.edit();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderRecycler holder, int position) {
        viewholderAlcanceMin =holder;
        alcance_min_bascula = String.valueOf(ranking4recycler.get(position).getMin_bascula());

        holder.min_bascul.setText(alcance_min_bascula);
        holder.min_bascul.setText(alcance_min_bascula);
        final String alcanceMin  =  holder.min_bascul.getText().toString();
        holder.marco4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,alcanceMin,Toast.LENGTH_SHORT).show();
                editor.putString("alcanceMin",alcanceMin);
                editor.apply();

                if (anterior!=null)
                {
                    anterior.marco4.setBackgroundResource(R.color.blanco);
                    anterior.min_bascul.setTextColor(context.getResources().getColor(R.color.negro));
                }
                holder.marco4.setBackgroundResource(R.color.negro);
                holder.min_bascul.setTextColor(context.getResources().getColor(R.color.blanco));



                anterior=holder;

            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking4recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView min_bascul;
        LinearLayout marco4;

        public ViewHolderRecycler(View itemView) {
            super(itemView);
            min_bascul =(TextView)itemView.findViewById(R.id.alcance_min);
            marco4=(LinearLayout)itemView.findViewById(R.id.marco4);



        }
    }
}