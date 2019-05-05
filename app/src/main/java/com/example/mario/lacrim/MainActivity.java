package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
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
                case R.id.navigation_solicitud:
                    fragment= new solicitud();
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
        getMenuInflater().inflate(R.menu.menu_barra, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cerrar :
                deleteCache(this);
                eliminarshared();
                cerrar();
                return true;
            case R.id.action_info :
                startActivity(new Intent(this, Informacion.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void eliminarshared() {
        SharedPreferences settings = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    private void cerrar() {

        startActivity(new Intent(this, Login.class));
        finish();
    }


    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;

                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }

    }

}
