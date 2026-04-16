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
import com.williangoncalves.whatsapp.model.Conversa;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversaAdapter extends RecyclerView.Adapter<ConversaAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversaAdapter(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contato, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        holder.textViewUltimaMensagem.setText(conversa.getUltimaMensagem());

        Usuario usuario = conversa.getUsuarioExibicao();
        holder.textViewNome.setText(usuario.getNome());

        if (usuario.getFoto() != null) {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.circleImageViewFoto);
        } else {
            holder.circleImageViewFoto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewFoto;
        TextView textViewNome, textViewUltimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageViewFoto = itemView.findViewById(R.id.imageViewFotoContato);
            textViewNome = itemView.findViewById(R.id.textViewNomeContato);
            textViewUltimaMensagem = itemView.findViewById(R.id.textViewSubtituloContato);
        }
    }
}
