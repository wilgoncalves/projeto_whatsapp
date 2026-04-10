package com.williangoncalves.whatsapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.helper.Base64Custom;
import com.williangoncalves.whatsapp.helper.UsuarioFirebase;
import com.williangoncalves.whatsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText editNome, editEmail, editSenha;
    private Button buttonCadastrar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        /*buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCadastroUsuario();
            }
        });*/
    }

    public void validarCadastroUsuario(View view) {
        String textoNome = editNome.getText().toString();
        String textoEmail = editEmail.getText().toString();
        String textoSenha = editSenha.getText().toString();

        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario(usuario);
                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha a sua senha",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this, "Preencha o seu e-mail",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroActivity.this, "Preencha o seu nome",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarUsuario(final Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário!",
                            Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                    try {
                        String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setIdUsuario(idUsuario);
                        usuario.salvarUsuario();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta já está cadastrada!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}