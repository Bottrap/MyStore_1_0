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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore_1_0.Fragments.AddProductFragment;
import com.example.mystore_1_0.Fragments.AddUsersFragment;
import com.example.mystore_1_0.Fragments.DashboardFragment;
import com.example.mystore_1_0.Fragments.Profile.EditPasswordDialog;
import com.example.mystore_1_0.Fragments.Profile.ProfileFragment;
import com.example.mystore_1_0.Fragments.QrGeneratorFragment;
import com.example.mystore_1_0.Fragments.ShowProductFragment;
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

        Intent intent = getIntent();
        Utente utente = intent.getParcelableExtra("utente");

        View header = navigationView.getHeaderView(0);
        idHeader = header.findViewById(R.id.idHeader);
        passwHeader = header.findViewById(R.id.passwHeader);
        passwHeader.setText("Benvenuto " + utente.getNome());
        idHeader.setText("Il tuo ID è: " + utente.getId());


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
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();

        //Controllo sui permessi dell'utente
        if((Integer.parseInt(utente.getPermessi()) == 2) || (Integer.parseInt(utente.getPermessi()) == 3)){
            navigationView.getMenu().findItem(R.id.nav_add_dip).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_list_dip).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qr_gen).setVisible(false);
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
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ProfileActivity.super.onBackPressed();
                            }
                        }).setNeutralButton("No", null).create().show();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment()).commit();
                break;
            case R.id.nav_show_prod:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_logout:
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changePassword(String password) {
        Utente utente = getIntent().getParcelableExtra("utente");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1").child("Users");
        Query checkId = reference.orderByChild("id").equalTo(utente.getId());
        checkId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child(utente.getId()).child("password").setValue(password);
                Toast.makeText(ProfileActivity.this, "Password modificata correttamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}