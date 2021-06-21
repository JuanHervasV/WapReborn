package com.JH.wapreborn.Entidades;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JH.wapreborn.Model.ListarRecojos;
import com.JH.wapreborn.R;
import com.JH.wapreborn.io.APIRetrofitInterface;

import java.util.ArrayList;

public class ListarAdapter extends RecyclerView.Adapter<ListarAdapter.ViewHolder> implements View.OnClickListener {
    LayoutInflater inflater;
    ArrayList<Recojoinfo> model;
    ImageView img;
    //Listener
    private View.OnClickListener listener;

    public ListarAdapter(Context context, ArrayList<Recojoinfo> model){
        this.inflater = LayoutInflater.from(context);
        this.model = model;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recojo_item, parent, false);
        view.setOnClickListener(this);


        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nombre = model.get(position).getHoraInicio() + " - " +model.get(position).getHoraFin() + " " + model.get(position).getRazonSocial() + " " + model.get(position).getCodigoPostal();

        holder.nombres.setText(nombre);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombres, descrip;
        ImageView imagen;
        private APIRetrofitInterface jsonPlaceHolderApi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombres = itemView.findViewById(R.id.idNombre);

        }
    }
}

