package com.williangoncalves.whatsapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.adapter.ConversaAdapter;
import com.williangoncalves.whatsapp.model.Conversa;

import java.util.ArrayList;

public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewListaConversas;
    private ConversaAdapter adapter;
    private ArrayList<Conversa> listaConversa = new ArrayList<>();

    public ConversasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        // Configurações iniciais:
        recyclerViewListaConversas = view.findViewById(R.id.recyclerViewListaConversas);

        // Configurando adapter:
        adapter = new ConversaAdapter(listaConversa, getActivity());

        // Configurando recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaConversas.setLayoutManager(layoutManager);
        recyclerViewListaConversas.setHasFixedSize(true);
        recyclerViewListaConversas.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewListaConversas.setAdapter(adapter);
        
        return view;
    }
}