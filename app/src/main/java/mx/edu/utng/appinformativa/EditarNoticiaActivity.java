package mx.edu.utng.appinformativa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarNoticiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se relaciona con  la vista
        setContentView(R.layout.activity_editar_noticia);

        //Variables donde se asigna los valores de los elementos de la vista
        final Button btnEditar=(Button)findViewById(R.id.btnEditar);
        final Button btnBuscar=(Button)findViewById(R.id.btnBuscar);
        final Button btnRegresar=(Button)findViewById(R.id.btnRegresar);
        final EditText etNombre=findViewById(R.id.etNombre);
        final EditText etPhoto=findViewById(R.id.etPhoto);
        final EditText etDescripcion=findViewById(R.id.etDescripcion);
        final RatingBar rbValoracion=findViewById(R.id.ratingBar);

        //Método que se ejecuta al dar clic en botón buscar para hacer búsqueda de noticia
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOpenHelper dbHelper = new MyOpenHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String[] parametros = {etNombre.getText().toString()};
                String[] campos = {MyOpenHelper.nombre, MyOpenHelper.photo, MyOpenHelper.valoracion, MyOpenHelper.descripcion};

                try{
                    Cursor cursor = db.query(MyOpenHelper.tabla, campos, MyOpenHelper.nombre+"=?",parametros,null,null,null);
                    cursor.moveToFirst();
                    etNombre.setText(cursor.getString(0));
                    etPhoto.setText(cursor.getString(1));
                    rbValoracion.setRating(cursor.getFloat(2));
                    etDescripcion.setText(cursor.getString(3));
                    cursor.close();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "La película no existe", Toast.LENGTH_LONG).show();
                    limpiar();
                }
            }
            private void limpiar() {
                etNombre.setText("");
                etPhoto.setText("");
                rbValoracion.setRating(0.0f);
                etDescripcion.setText("");
            }
        });


        //Método que se ejecuta al dar clic en botón editar para hacer edición de noticia
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOpenHelper dbHelper = new MyOpenHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String[] parametros = {etNombre.getText().toString()};
                ContentValues values = new ContentValues();
                try {
                    values.put(MyOpenHelper.nombre, etNombre.getText().toString());
                    values.put(MyOpenHelper.photo, etPhoto.getText().toString());
                    values.put(MyOpenHelper.valoracion, rbValoracion.getRating());
                    values.put(MyOpenHelper.descripcion, etDescripcion.getText().toString());
                    db.update(MyOpenHelper.tabla, values, MyOpenHelper.nombre + "=?", parametros);
                    Toast.makeText(getApplicationContext(), "Se ha actualizado la información de la película", Toast.LENGTH_LONG).show();
                    db.close();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "La informacipon de la película no se pudo actualizar", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Metodo que se ejecuta al dar clic en boton regresar para volver a la vista de la lista de noticias
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crea intent
                Intent intentLista = new Intent(EditarNoticiaActivity.this, ListaNoticiaActivity.class);
                startActivity(intentLista);  //Inicia ListaNoticiaActivity
                finish(); //Finaliza ListaNoticiaActivity
            }
        });
    }
}