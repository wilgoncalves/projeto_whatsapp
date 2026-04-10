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
import com.williangoncalves.whatsapp.adapter.ContatoAdapter;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatoAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Configurações iniciais:
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);

        // Configurando adapter:
        adapter = new ContatoAdapter(listaContatos, getActivity());

        // Configurando recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewListaContatos.setAdapter(adapter);

        return view;
    }
}