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

public class Crear_alimento_equino extends AppCompatActivity {

    EditText ed_nombre_alimento, ed_fecha_alimento, ed_descripcion_alimento;
    Button bt_registrar_alimento;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String id_equino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_alimento_equino);

        id_equino = getIntent().getExtras().getString("id");

        ed_nombre_alimento = findViewById(R.id.ed_nombre_alimentacion);
        ed_descripcion_alimento = findViewById(R.id.ed_descripcion_alimentacion);
        ed_fecha_alimento = findViewById(R.id.ed_fecha_alimentacion);

        bt_registrar_alimento = findViewById(R.id.bt_registrar_alimentacion);


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

        ed_fecha_alimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralMethodClass.hideKeyboard(Crear_alimento_equino.this);
                new DatePickerDialog(view.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_registrar_alimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();


            }
        });

    }


    private void updateEdit() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_fecha_alimento.setText(sdf.format(calendar.getTime()));
    }


    private void validarcampos(){

        if (ed_nombre_alimento == null || ed_nombre_alimento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Nombre no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_fecha_alimento == null || ed_fecha_alimento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Fecha no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_descripcion_alimento == null || ed_descripcion_alimento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Descripcion no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else{
            registrarAlimentacionEquino();
        }

    }


    private void registrarAlimentacionEquino() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_ALIMENTO,ed_nombre_alimento.getText().toString());
        values.put(Constantes.CAMPO_FECHA_ALIMENTO,ed_fecha_alimento.getText().toString());
        values.put(Constantes.CAMPO_DESCRIPCION_ALIMENTO,ed_descripcion_alimento.getText().toString());
        values.put(Constantes.CAMPO_ID_EQUINO_ALIMENTO,id_equino);


        db.insert(Constantes.TABLA_ALIMENTOS,Constantes.CAMPO_ID_ALIMENTO,values);

        Toast.makeText(getApplicationContext(),"Alimentacion resgistrada",Toast.LENGTH_SHORT).show();
        db.close();

        startActivity(new Intent(Crear_alimento_equino.this, Alimentacion.class));
        finish();

    }


    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id",id_equino);
        startActivity(i);
        finish();
    }
}
