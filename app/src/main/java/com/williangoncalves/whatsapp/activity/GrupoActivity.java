package com.williangoncalves.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williangoncalves.whatsapp.adapter.ContatoAdapter;
import com.williangoncalves.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.databinding.ActivityGrupoBinding;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.helper.RecyclerItemClickListener;
import com.williangoncalves.whatsapp.helper.UsuarioFirebase;
import com.williangoncalves.whatsapp.model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private ActivityGrupoBinding binding;
    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatoAdapter contatoAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private List<Usuario> listaMembros = new ArrayList<>();
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;
    private Toolbar toolbar;
    private FloatingActionButton fabAvancarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        toolbar.setTitle(R.string.toolbar_novo_grupo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMembros = findViewById(R.id.recyclerMembros);
        fabAvancarCadastro = findViewById(R.id.fabAvancarCadastro);

        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        // Configurando adapter:
        contatoAdapter = new ContatoAdapter(listaMembros, getApplicationContext());

        // Configurando recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatoAdapter);

        recyclerMembros.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), recyclerMembros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listaMembros.get(position);

                // Remover usuario selecionado da lista:
                listaMembros.remove(usuarioSelecionado);
                contatoAdapter.notifyDataSetChanged();

                // Adicionando usuário na nova lista de selecionados:
                listaMembrosSelecionados.add(usuarioSelecionado);
                grupoSelecionadoAdapter.notifyDataSetChanged();

                atualizarMembrosToolbar();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));

        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

        recyclerMembrosSelecionados.addOnItemTouchListener(
                new RecyclerItemClickListener(
                    getApplicationContext(), recyclerMembrosSelecionados,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Usuario usuarioSelecionado = listaMembrosSelecionados.get(position);

                            // Removendo da lista de membros selecionados:
                            listaMembrosSelecionados.remove(usuarioSelecionado);
                            grupoSelecionadoAdapter.notifyDataSetChanged();

                            // Adicionando à lista de membros:
                            listaMembros.add(usuarioSelecionado);
                            contatoAdapter.notifyDataSetChanged();

                            atualizarMembrosToolbar();
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

        // Configurando floating action button:
        fabAvancarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GrupoActivity.this, CadastroGrupoActivity.class);
                intent.putExtra("membros", (Serializable) listaMembrosSelecionados);
                startActivity(intent);
            }
        });
    }

    public void recuperarContatos() {
        valueEventListenerMembros = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                        listaMembros.add(usuario);
                    }
                }

                contatoAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }

    public void atualizarMembrosToolbar() {
        int totalSelecionados = listaMembrosSelecionados.size();
        int total = listaMembros.size() + totalSelecionados;
        toolbar.setSubtitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        toolbar.setSubtitle(totalSelecionados + " de " + total + " selecionados");
    }
}