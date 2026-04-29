package com.williangoncalves.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.williangoncalves.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.databinding.ActivityCadastroGrupoBinding;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.model.Grupo;
import com.williangoncalves.whatsapp.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private ActivityCadastroGrupoBinding binding;
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textTotalParticipantes;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private RecyclerView recyclerMembrosGrupo;
    private CircleImageView circleImageGrupo;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadastroGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurando toolbar:
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        toolbar.setTitle(R.string.novo_grupo);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        toolbar.setSubtitle(R.string.defina_nome_subtitulo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        textTotalParticipantes = findViewById(R.id.textTotalParticipantes);
        recyclerMembrosGrupo = findViewById(R.id.recyclerMembrosGrupo);
        circleImageGrupo = findViewById(R.id.circleImageGrupo);
        grupo = new Grupo();

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        // Configurando evento de clique para circle image:
        circleImageGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

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

            textTotalParticipantes.setText("Participantes: " + listaMembrosSelecionados.size());
        }

        // Configurando RecyclerView:
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosGrupo.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosGrupo.setHasFixedSize(true);
        recyclerMembrosGrupo.setAdapter(grupoSelecionadoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                Uri localImagemSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);

                if (imagem != null) {
                    circleImageGrupo.setImageBitmap(imagem);

                    // Recuperando dados da imagem no Firebase:
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    byte[] dadosImagem = byteArrayOutputStream.toByteArray();

                    // Salvando imagem no firebase:
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("grupos")
                            .child("" + ".jpeg");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}