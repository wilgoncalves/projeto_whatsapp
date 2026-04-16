package com.williangoncalves.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.activity.ChatActivity;
import com.williangoncalves.whatsapp.activity.GrupoActivity;
import com.williangoncalves.whatsapp.adapter.ContatoAdapter;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.helper.RecyclerItemClickListener;
import com.williangoncalves.whatsapp.helper.UsuarioFirebase;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatoAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;

    public ContatosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Configurações iniciais:
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        // Configurando adapter:
        adapter = new ContatoAdapter(listaContatos, getActivity());

        // Configurando recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        //recyclerViewListaContatos.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewListaContatos.setAdapter(adapter);

        // Configurando evento de clique no recyclerview:
        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                    getActivity(),
                    recyclerViewListaContatos,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Usuario usuarioSelecionado = listaContatos.get(position);
                            boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();

                            if (cabecalho) {
                                startActivity(new Intent(getActivity(), GrupoActivity.class));
                            } else {
                                Intent intent = new Intent(new Intent(getActivity(), ChatActivity.class));
                                intent.putExtra("chatContato", usuarioSelecionado);
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
                )
        );

        /* Define usuário com e-mail vazio,
        em caso de e-mail vazio, o usuário será utilizado
        como cabeçalho, exibindo novo grupo. */
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void recuperarContatos() {
        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                        listaContatos.add(usuario);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}