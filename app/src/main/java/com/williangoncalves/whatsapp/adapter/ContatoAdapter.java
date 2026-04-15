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

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public ContatoAdapter(List<Usuario> listaContatos, Context context) {
        this.contatos = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contato, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = contatos.get(position);

        holder.textViewNome.setText(usuario.getNome());
        holder.textViewEmail.setText(usuario.getEmail());

        if (usuario.getFoto() != null) {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.fotoPerfil);
        } else {
            holder.fotoPerfil.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView textViewNome, textViewEmail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoPerfil = itemView.findViewById(R.id.imageViewFotoContato);
            textViewNome = itemView.findViewById(R.id.textViewNomeContato);
            textViewEmail = itemView.findViewById(R.id.textViewEmailContato);
        }
    }
}
