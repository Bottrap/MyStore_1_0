package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore_1_0.Fragments.AddUsersFragment;
import com.example.mystore_1_0.Fragments.DashboardFragment;
import com.example.mystore_1_0.Fragments.LoadingFragment;
import com.example.mystore_1_0.Fragments.Profile.EditPasswordDialog;
import com.example.mystore_1_0.Fragments.Profile.ProfileFragment;
import com.example.mystore_1_0.Fragments.QrGeneratorFragment;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.IOnBackPressed;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EditPasswordDialog.ChangePasswordDialogListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView passwHeader, idHeader;
    Boolean isClosed = true;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    String negozio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Cambio il colore della status bar
        Window window = ProfileActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ProfileActivity.this, R.color.scanStatusBarColor));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);

        Intent intent = getIntent();
        Utente utenteLoggato = intent.getParcelableExtra("utente");

        negozio = utenteLoggato.getNegozio();

        View header = navigationView.getHeaderView(0);
        idHeader = header.findViewById(R.id.idHeader);
        passwHeader = header.findViewById(R.id.passwHeader);
        passwHeader.setText("Benvenuto " + utenteLoggato.getNome());
        idHeader.setText("Il tuo ID è: " + utenteLoggato.getId());


        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //drawer menu
        navigationView.bringToFront(); //si evidenzia alla pressione
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_img, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        toggle.setToolbarNavigationClickListener(v -> {
            if (isClosed){
                drawerLayout.openDrawer(GravityCompat.START);
                isClosed = false;
            }else{
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();

        // CONTROLLO SUI PERMESSI DELL'UTENTE
        switch(Integer.parseInt(utenteLoggato.getPermessi())){
            case 1: // CAPO SUPREMO
                navigationView.getMenu().findItem(R.id.nav_show_prod).setVisible(false);
                break;
            case 2: // MAGAZZINIERE
                navigationView.getMenu().findItem(R.id.nav_list_dip).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_add_dip).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_qr_gen).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_manage_prod).setVisible(false);


                break;
            case 3: // DIPENDENTE
                navigationView.getMenu().findItem(R.id.nav_list_dip).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_add_dip).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_qr_gen).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_show_prod).setVisible(false);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        Fragment editFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(editFragment instanceof IOnBackPressed) || !((IOnBackPressed) editFragment).onBackPressed())
        {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                new AlertDialog.Builder(this).setTitle("Sei sicuro di voler uscire?").setMessage("Se esci dovrai autenticarti nuovamente.")
                        .setPositiveButton("Si", (arg0, arg1) -> ProfileActivity.super.onBackPressed()).setNeutralButton("No", null).create().show();
            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                break;
            case R.id.nav_add_dip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUsersFragment()).commit();
                break;
            case R.id.nav_list_dip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowUsersFragment()).commit();
                break;
            case R.id.nav_qr_gen:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QrGeneratorFragment()).commit();
                break;
            case R.id.nav_add_prod:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoadingFragment(1)).commit();
                break;
            case R.id.nav_manage_prod:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoadingFragment(2)).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_show_prod:
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("negozio", negozio);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changePassword(String password) {
        Utente utenteLoggato = getIntent().getParcelableExtra("utente");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(utenteLoggato.getNegozio()).child("Users");
        Query checkId = reference.orderByChild("id").equalTo(utenteLoggato.getId());
        checkId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child(utenteLoggato.getId()).child("password").setValue(password);
                Toast.makeText(ProfileActivity.this, "Password modificata correttamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}