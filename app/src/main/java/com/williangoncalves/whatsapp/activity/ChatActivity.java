package com.williangoncalves.whatsapp.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.williangoncalves.whatsapp.adapter.MensagemAdapter;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.databinding.ActivityChatBinding;

import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.helper.Base64Custom;
import com.williangoncalves.whatsapp.helper.UsuarioFirebase;
import com.williangoncalves.whatsapp.model.Mensagem;
import com.williangoncalves.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private EditText editTextMensagem;
    private Usuario usuarioDestinatario;

    // Identificadores dos usuários remetente e destinatário:
    private String idUsuarioRemetente, idUsuarioDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagemAdapter mensagemAdapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurando toolbar:
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editTextMensagem = findViewById(R.id.editTextMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        // Recuperando dados do usuario remetente:
        idUsuarioRemetente = UsuarioFirebase.getIdUsuario();

        // Recuperar dados do usuário de destino:
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());

            String foto = usuarioDestinatario.getFoto();
            if (foto != null) {
                Uri url = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.padrao);
            }

            // Recuperando dados do usuário destinatário:
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
        }

        // Configuração adapter:
        mensagemAdapter = new MensagemAdapter(mensagens, getApplicationContext());

        // Confiugração recyclerview:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(mensagemAdapter);
    }

    public void enviarMensagem(View view) {
        String textoMensagem = editTextMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            // Salvar mensagem do remetente:
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemReference = databaseReference.child("mensagens");

        mensagemReference.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        // Limpando o texto após salvar:
        editTextMensagem.setText("");
    }
}