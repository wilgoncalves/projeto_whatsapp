package com.williangoncalves.whatsapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.model.Conversa;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewFotoConversa;
        TextView textViewNomeConversa, textViewUltimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageViewFotoConversa = itemView.findViewById(R.id.imageViewFotoConversa);
            textViewNomeConversa = itemView.findViewById(R.id.textViewNomeConversa);
            textViewUltimaMensagem = itemView.findViewById(R.id.textViewUltimaConversa);
        }
    }
}
