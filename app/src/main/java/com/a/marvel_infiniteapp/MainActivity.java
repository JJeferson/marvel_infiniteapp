package com.a.marvel_infiniteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static String testaConec;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;


    public static final String Reference_File = "ReferenceFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView       =      (TextView) findViewById(R.id.textView);
        textView1      =      (TextView) findViewById(R.id.textView1);
        textView2      =      (TextView) findViewById(R.id.textView2);

        textView2.setText("Bem vindo ao App");
        textView.setText("Os dados estão sendo sicronizados...");
        textView1.setText("Logo o app irá iniciar.");


       criaBancoDados();

       rest();
    }



    public void rest(){
        isOnline(getApplicationContext());


       final SharedPreferences sharedPreferences = getSharedPreferences(Reference_File, MODE_PRIVATE);
       final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (testaConec=="sim"){


            editor.putString("conexao","sim");
            editor.commit();

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://private-b34167-rvmarvel.apiary-mock.com/saga")

                        .build();
                //-------------------------------------------------------------


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    String json = response.body().string();
                                    limpaListaFilmes();
                                    alimentaFilmes(json);
                                    startActivity(new Intent(MainActivity.this, listaMarvel.class));

                                } catch (IOException e) {
                                    String erro ="Erro: "+e.getMessage();
                                }

                            }
                        });
                    }

                    ;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{

            //Caso sem Internet no aparelho
            editor.putString("conexao","nao");
            editor.commit();
            startActivity(new Intent(MainActivity.this, listaMarvel.class));


        }

    }//fim


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            testaConec="sim";
        }else{
            testaConec="nao";
        }
        if (netInfo != null && netInfo.isConnected())

            return true;

        else
            return false;
    }

     public void criaBancoDados(){

         try {

             SQLiteDatabase bancoDados = openOrCreateDatabase("exibeFilmesMarvel", MODE_PRIVATE, null);

             bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notasFilmes (ID INTEGER ,titulo VARCHAR, nota VARCHAR);");

             bancoDados.execSQL("CREATE TABLE dadosFilmes (\n" +
                     "    ID     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                     "    titulo VARCHAR,\n" +
                     "    genero VARCHAR,\n" +
                     "    ano    VARCHAR,\n" +
                     "    poster VARCHAR\n" +
                     ");\n");

         } catch (Exception e) {
             e.printStackTrace();

         }//fim do try

     }
     public void alimentaFilmes(String Json ){

         try {


             JSONArray jsonArray = new JSONArray(Json);
             JSONObject jsonObject;

             for (int i = 0; i < jsonArray.length(); i++) {
                 jsonObject = jsonArray.getJSONObject(i);



                 String gravaTitulo = jsonObject.getString("title");
                 String gravaGenero = jsonObject.getString("genre");
                 String gravaAno    = jsonObject.getString("year");
                 String gravaPoster = jsonObject.getString("poster");
                 try {

                     //abre o banco
                     SQLiteDatabase bancoDados = openOrCreateDatabase("exibeFilmesMarvel", MODE_PRIVATE, null);
                     bancoDados.execSQL("INSERT INTO dadosFilmes (titulo,genero,ano,poster) " +
                             "VALUES ('" + gravaTitulo + "','" + gravaGenero + "','" + gravaAno + "','" + gravaPoster+"')");


                     Toast.makeText(MainActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();


                 } catch (Exception e) {
                     e.printStackTrace();
                     Toast.makeText(MainActivity.this, "!!ERRO NO CADASTRO!!!", Toast.LENGTH_SHORT).show();
                 }  //fim do try

             }





         } catch (JSONException e) {
             String erro ="Erro: "+e.getMessage();
         }

     }


    public void limpaListaFilmes() {

        try {
            //abre o banco
            SQLiteDatabase bancoDados = openOrCreateDatabase("exibeFilmesMarvel", MODE_PRIVATE, null);
            bancoDados.execSQL("DELETE FROM dadosFilmes");
             Toast.makeText(getApplicationContext(), "Lista Limpa com Sucesso ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERRO DURANTE EXCLUSÃO ", Toast.LENGTH_SHORT).show();

        }  //fim do try

    }


}