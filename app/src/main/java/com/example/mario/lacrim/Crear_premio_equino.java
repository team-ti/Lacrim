package com.example.mario.lacrim;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;
import com.example.mario.lacrim.Utilidades.GeneralMethodClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Crear_premio_equino extends AppCompatActivity {

    EditText ed_nombre_premio, ed_fecha_premio, ed_descripcion_premio;
    Button bt_registrar_premio;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String id_equino;
    String interfaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_premio_equino);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");

        ed_nombre_premio = findViewById(R.id.ed_nombre_premio);
        ed_descripcion_premio = findViewById(R.id.ed_descripcion_premio);
        ed_fecha_premio = findViewById(R.id.ed_fecha_premio);

        bt_registrar_premio = findViewById(R.id.bt_registrar_premio);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEdit();
            }
        };

        ed_fecha_premio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralMethodClass.hideKeyboard(Crear_premio_equino.this);
                new DatePickerDialog(view.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_registrar_premio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();


            }
        });

    }

    private void updateEdit() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_fecha_premio.setText(sdf.format(calendar.getTime()));
    }


    private void validarcampos(){

        if (ed_nombre_premio == null || ed_nombre_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Nombre no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_fecha_premio == null || ed_fecha_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Fecha no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_descripcion_premio == null || ed_descripcion_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Descripcion no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else{
            registrarPremioEquino();
        }

    }


    private void registrarPremioEquino() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_PREMIO,ed_nombre_premio.getText().toString());
        values.put(Constantes.CAMPO_FECHA_PREMIO,ed_fecha_premio.getText().toString());
        values.put(Constantes.CAMPO_DESCRIPCION_PREMIO,ed_descripcion_premio.getText().toString());
        values.put(Constantes.CAMPO_ID_EQUINO_PREMIO,id_equino);


        db.insert(Constantes.TABLA_PREMIOS,Constantes.CAMPO_ID_PREMIO,values);

        Toast.makeText(getApplicationContext(),"Premio resgistrado",Toast.LENGTH_SHORT).show();
        db.close();

        Intent i = new Intent(this, Premiacion.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();

    }


    public void onBackPressed() {
        Intent i = new Intent(this, Premiacion.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }

}
