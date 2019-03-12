package com.example.mario.lacrim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Informacion extends AppCompatActivity {

    Button bt_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        bt_volver = findViewById(R.id.bt_volver);


        bt_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Informacion.this.finish();
            }
        });
    }
}
