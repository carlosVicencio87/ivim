package com.ivim.ivim;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEoD  extends RecyclerView.Adapter<AdapterEoD.ViewHolderRecycler>{
    private ArrayList<EodRecycler> ranking4recycler;
    ViewHolderRecycler viewholderEoD,anterior;
    private  RecyclerView recyclerView;
    private Context context;
    private String eod_max_bascula;
    private SharedPreferences datosEod;
    private SharedPreferences.Editor editor;

    public AdapterEoD(Mapa activity, int item, ArrayList<EodRecycler> ranking4recycler, Resources resources)
    {
        this.ranking4recycler=ranking4recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item4,parent,false);
        context=parent.getContext();
        datosEod= context.getSharedPreferences("alcancesEod",context.MODE_PRIVATE);
        editor=datosEod.edit();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterEoD.ViewHolderRecycler holder, int position) {
        viewholderEoD =holder;
        eod_max_bascula = ranking4recycler.get(position).getEod_bascula();

        holder.eoD_bascul.setText(eod_max_bascula);
        holder.eoD_bascul.setText(eod_max_bascula);
        final String alcanceEod  =  holder.eoD_bascul.getText().toString();
        holder.marco3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,alcanceEod,Toast.LENGTH_SHORT).show();
                editor.putString("alcanceEod",alcanceEod);
                editor.apply();
                Log.e("cambio",datosEod.getString("modelo","no"));
                if (anterior!=null)
                {
                    anterior.marco3.setBackgroundResource(R.color.blanco);
                    anterior.eoD_bascul.setTextColor(context.getResources().getColor(R.color.negro));
                }
                holder.marco3.setBackgroundResource(R.color.negro);
                holder.eoD_bascul.setTextColor(context.getResources().getColor(R.color.blanco));



                anterior=holder;

            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking4recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView eoD_bascul;
        LinearLayout marco3;

        public ViewHolderRecycler(View itemView) {
            super(itemView);
            eoD_bascul =(TextView)itemView.findViewById(R.id.eod_max);
            marco3=(LinearLayout)itemView.findViewById(R.id.marco3);



        }
    }
}