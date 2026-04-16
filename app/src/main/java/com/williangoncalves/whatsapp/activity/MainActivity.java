package com.williangoncalves.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.williangoncalves.whatsapp.R;
import com.williangoncalves.whatsapp.config.ConfiguracaoFirebase;
import com.williangoncalves.whatsapp.fragment.ContatosFragment;
import com.williangoncalves.whatsapp.fragment.ConversasFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView materialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.whatsapp_name);
        setSupportActionBar(toolbar);

        // Configurar abas:
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add(R.string.fragment_conversas, ConversasFragment.class)
                .add(R.string.fragment_contatos, ContatosFragment.class)
                .create()
        );
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout smartTabLayout = findViewById(R.id.smartTabLayout);
        smartTabLayout.setViewPager(viewPager);

        // Configuração do searchview:
        materialSearchView = findViewById(R.id.materialSearchPrincipal);

        // Listener para o searchview:
        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();
            }
        });

        // Listener para caixa de texto
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("evento", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("evento", "onQueryTextChange");
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);

                if (newText != null && !newText.isEmpty()) {
                    fragment.pesquisarConversas(newText.toLowerCase());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // Configurar botão de pesquisa:
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        materialSearchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSair) {
            deslogarUsuario();
            finish();
        }

        if (item.getItemId() == R.id.menuConfiguracoes) {
            abrirConfiguracoes();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirConfiguracoes() {
        startActivity(new Intent(this, ConfiguracoesActivity.class));
    }
}