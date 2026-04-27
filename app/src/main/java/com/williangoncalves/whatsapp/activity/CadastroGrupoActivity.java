package com.williangoncalves.whatsapp.activity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.williangoncalves.whatsapp.databinding.ActivityCadastroGrupoBinding;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CadastroGrupoActivity extends AppCompatActivity {

    private ActivityCadastroGrupoBinding binding;
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textTotalMembros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadastroGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurando toolbar:
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        textTotalMembros = findViewById(R.id.textTotal);

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cadastro_grupo);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        // Recuperando lista de membros passada:
        if (getIntent().getExtras() != null) {
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            textTotalMembros.setText("Total: " + listaMembrosSelecionados.size());
        }
    }
}