package com.williangoncalves.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.activity.ChatActivity;
import com.williangoncalves.whatsapp.adapter.ConversaAdapter;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.helper.RecyclerItemClickListener;
import com.williangoncalves.whatsapp.helper.UsuarioFirebase;
import com.williangoncalves.whatsapp.model.Conversa;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewListaConversas;
    private ConversaAdapter adapter;
    private ArrayList<Conversa> listaConversas = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        // Configurações iniciais:
        recyclerViewListaConversas = view.findViewById(R.id.recyclerViewListaConversas);

        // Configurando adapter:
        adapter = new ConversaAdapter(listaConversas, getActivity());

        // Configurando recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaConversas.setLayoutManager(layoutManager);
        recyclerViewListaConversas.setHasFixedSize(true);
        recyclerViewListaConversas.setAdapter(adapter);

        // Configurar evento de clique:
        recyclerViewListaConversas.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerViewListaConversas,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conversa> listaConversasAtualizada = adapter.getConversas();
                Conversa conversaSelecionada = listaConversasAtualizada.get(position);

                if (conversaSelecionada.getIsGroup().equals("true")) {
                    Intent intent = new Intent(new Intent(getActivity(), ChatActivity.class));
                    intent.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(new Intent(getActivity(), ChatActivity.class));
                    intent.putExtra("chatContato", conversaSelecionada.getUsuarioExibicao());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));

        // Configurando conversas:
        String identificadorUsuario = UsuarioFirebase.getIdUsuario();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef = databaseReference.child("conversas")
                .child(identificadorUsuario);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto) {
        //Log.d("pesquisa", texto);
        List<Conversa> listaConversasBusca = new ArrayList<>();

        for (Conversa conversa : listaConversas) {
            if (conversa.getUsuarioExibicao() != null) {
                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(texto) || ultimaMensagem.contains(texto)) {
                    listaConversasBusca.add(conversa);
                }
            } else {
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(texto) || ultimaMensagem.contains(texto)) {
                    listaConversasBusca.add(conversa);
                }
            }
        }

        adapter = new ConversaAdapter(listaConversasBusca, getActivity());
        recyclerViewListaConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarConversas() {
        adapter = new ConversaAdapter(listaConversas, getActivity());
        recyclerViewListaConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas() {
        listaConversas.clear();
        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Recuperar conversas:
                Conversa conversa = snapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}