package com.example.mario.lacrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;

    Fragment fragment;
    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_inicio:
                    fragment= new inicio();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_buscar:
                    fragment= new buscar();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_perfil:
                    fragment= new perfil();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
                    return true;


            }
            return false;
        }
    };



    public boolean onCreateOptionsMenu(Menu menu) {

        this.fragment = new inicio();
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.beginTransaction().replace(R.id.content, this.fragment).commit();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
