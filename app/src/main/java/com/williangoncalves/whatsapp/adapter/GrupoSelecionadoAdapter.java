package com.williangoncalves.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {

    private List<Usuario> contatosSelecionados;
    private Context context;

    public GrupoSelecionadoAdapter(List<Usuario> contatos, Context context) {
        this.contatosSelecionados = contatos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_grupo_selecionado, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = contatosSelecionados.get(position);
        holder.textViewNome.setText(usuario.getNome());

        if (usuario.getFoto() != null) {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.circleImageView);
        } else {
            holder.circleImageView.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView textViewNome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            textViewNome = itemView.findViewById(R.id.textViewNomeMembroSelecionado);
        }
    }

}
