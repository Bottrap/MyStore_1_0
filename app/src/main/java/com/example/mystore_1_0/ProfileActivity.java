package com.example.mystore_1_0;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mystore_1_0.Fragments.AddUsersFragment;
import com.example.mystore_1_0.Fragments.HomeFragment;
import com.example.mystore_1_0.Fragments.ShowUsersFragment;
import com.google.android.material.navigation.NavigationView;
public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_add_dip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUsersFragment()).commit();
                break;
            case R.id.nav_list_dip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowUsersFragment()).commit();
                break;
            case R.id.nav_logout:
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}