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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;
import com.example.mario.lacrim.Utilidades.GeneralMethodClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Crear_equino extends AppCompatActivity {

    EditText ed_nombre, ed_fecha_nacimiento, ed_lugar_nacimiento, ed_color, ed_microship, ed_criador, ed_propietario;
    Spinner sp_sexo, sp_tipo, sp_andar;
    Button bt_registrar;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equino);

        ed_nombre = findViewById(R.id.ed_nombre);
        ed_fecha_nacimiento = findViewById(R.id.ed_fecha_nacimiento);
        ed_lugar_nacimiento = findViewById(R.id.ed_lugar_nacimiento);
        ed_color = findViewById(R.id.ed_color);
        ed_microship = findViewById(R.id.ed_microship);
        ed_criador = findViewById(R.id.ed_criador);
        ed_propietario = findViewById(R.id.ed_propietario);
        sp_sexo = findViewById(R.id.sp_sexo);
        sp_tipo = findViewById(R.id.sp_tipo);
        sp_andar = findViewById(R.id.sp_andar);
        bt_registrar = findViewById(R.id.bt_registrar);


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


        ed_fecha_nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralMethodClass.hideKeyboard(Crear_equino.this);
                new DatePickerDialog(view.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        bt_registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    private void updateEdit() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_fecha_nacimiento.setText(sdf.format(calendar.getTime()));
    }

    private void validarcampos(){

        if (ed_nombre == null || ed_nombre.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Nombre no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_fecha_nacimiento == null || ed_fecha_nacimiento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Fecha de nacimiento no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_lugar_nacimiento == null || ed_lugar_nacimiento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Lugar de nacimiento no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_color == null || ed_color.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Color no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_microship == null || ed_microship.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Microship no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_criador == null || ed_criador.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Criador no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_propietario == null || ed_propietario.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Propietario no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(sp_sexo == null || sp_sexo.getSelectedItem().toString().equals("--Seleccionar sexo--")){

            Toast.makeText(getApplicationContext(),"Sexo no puede estar vacio",Toast.LENGTH_SHORT).show();


        }else if (sp_tipo == null || sp_tipo.getSelectedItem().toString().equals("--Seleccionar tipo--")){

            Toast.makeText(getApplicationContext(),"Tipo no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(sp_andar == null || sp_andar.getSelectedItem().toString().equals("--Seleccionar andar--")){

            Toast.makeText(getApplicationContext(),"Andar no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else{
            registrarEquino();
        }

    }


    private void registrarEquino() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_EQUINO,ed_nombre.getText().toString());
        values.put(Constantes.CAMPO_FECHA_EQUINO,ed_fecha_nacimiento.getText().toString());
        values.put(Constantes.CAMPO_LUGAR_EQUINO,ed_lugar_nacimiento.getText().toString());
        values.put(Constantes.CAMPO_COLOR_EQUINO,ed_color.getText().toString());
        values.put(Constantes.CAMPO_MICROCHIP_EQUINO,ed_microship.getText().toString());
        values.put(Constantes.CAMPO_CRIADOR_EQUINO,ed_criador.getText().toString());
        values.put(Constantes.CAMPO_PROPIETARIO_EQUINO,ed_propietario.getText().toString());
        values.put(Constantes.CAMPO_SEXO_EQUINO,sp_sexo.getSelectedItem().toString());
        values.put(Constantes.CAMPO_TIPO_EQUINO,sp_tipo.getSelectedItem().toString());
        values.put(Constantes.CAMPO_ANDAR_EQUINO,sp_andar.getSelectedItem().toString());


        db.insert(Constantes.TABLA_EQUINO,Constantes.CAMPO_ID_EQUINO,values);

        Toast.makeText(getApplicationContext(),"Equino resgistrado",Toast.LENGTH_SHORT).show();
        db.close();

        startActivity(new Intent(Crear_equino.this, MainActivity.class));
        finish();

    }


    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
