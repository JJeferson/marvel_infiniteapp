package com.a.marvel_infiniteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.marvel_infiniteapp.adapter.Adapter;
import com.a.marvel_infiniteapp.model.modelo;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class listaMarvel extends AppCompatActivity {

    private EditText pesquisa;
    private Button btPesquisa;
    private Button btVoltar;
    private TextView     textview;
    private RecyclerView recyclerView_ID;


    private List<modelo> listaFilmes = new ArrayList<>();
    //  public static String[] ID_Posicao = {""};

    public static final String Reference_File = "ReferenceFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_marvel);


        pesquisa      =      (EditText) findViewById(R.id.pesquisa);
        btPesquisa    =      (Button) findViewById(R.id.btPesquisa);
        btVoltar    =      (Button) findViewById(R.id.btVoltar);
        textview      =      (TextView) findViewById(R.id.textview);

        recyclerView_ID = (RecyclerView) findViewById(R.id.recyclerView_ID);


        final SharedPreferences sharedPreferences = getSharedPreferences(Reference_File, MODE_PRIVATE);
        String conexao = sharedPreferences.getString("conexao","");

        if (conexao.equals("sim")){
            textview.setText("Com acesso a internet!");
            textview.setTextColor(Color.GREEN);
        }else{
            textview.setText("Sem Conexão com internet!");
            textview.setTextColor(Color.RED);
        }
        // textview.setText(testaConec);//sim ou nao

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_ID.setLayoutManager(layoutManager);
        recyclerView_ID.setHasFixedSize(true);
        final Adapter adapter = new Adapter(listaFilmes);
        recyclerView_ID.setAdapter(adapter);

        listaFilmes.clear();
        alimentaRecyclerView();


        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaFilmes.clear();
                alimentaRecyclerView();

                adapter.notifyDataSetChanged();

            }
        });


        btPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaFilmes.clear();
                pesquisa(pesquisa.getText().toString());
                pesquisa.setText("");

                adapter.notifyDataSetChanged();

            }
        });


        //--------------------------------------------------------------
        //Evento Onclick

        //evento de click
        recyclerView_ID.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView_ID,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                final int posicao = position;
                                dados_nota(posicao);
                                adapter.notifyDataSetChanged();


                            }


                            @Override
                            public void onLongItemClick(View view, int position) {



                            }


                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );//Fim do evento de lick


    }


  public void dados_nota(final int posicao){


      //Criando o AlertDialog
      final AlertDialog.Builder alertDialogEmissao = new AlertDialog.Builder(listaMarvel.this);


      //---------------------------------
      final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
      View v = inflater.inflate(R.layout.alertdialog_dados_nota, null, false);
      //Aqui declarar os componentes da view

      final TextView tituloView        = v.findViewById(R.id.titulo_view);
      final TextView generoView        = v.findViewById(R.id.genero_view);
      final TextView anoView           = v.findViewById(R.id.ano_view);
      final TextView notaView          = v.findViewById(R.id.nota_view);
      final ImageView posterView       = v.findViewById(R.id.poster_view);
      final EditText insereNotaView    = v.findViewById(R.id.insereNota_view);

      alertDialogEmissao.setView(v);
      //Faz não poder sair
      alertDialogEmissao.setCancelable(false);

      final modelo modelo = listaFilmes.get(posicao);

                                tituloView.setText(modelo.getTitulo());
                                generoView.setText(modelo.getGenero());
                                anoView.setText(modelo.getAno());
                                notaView.setText(modelo.getNota());
                                String caminhoImagemPoster = modelo.getPoster();
                                Glide.with(posterView).load(caminhoImagemPoster).into(posterView);


      alertDialogEmissao.setPositiveButton("Gravar nota!", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

              String recebeNota = insereNotaView.getText().toString();

                   if(modelo.getNota()==null || modelo.getNota()=="")
                  {
                      insereNota(modelo.getID(),modelo.getTitulo(),recebeNota);

                  }else {
                    alteraNota(modelo.getTitulo(),recebeNota);

                  }





          }



      });//fim do positive


      alertDialogEmissao.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

          }
      });//fim do cancelar alert Dialog

      //deixa visivel
      alertDialogEmissao.create();
      alertDialogEmissao.show();

  }

    public void alteraNota(String recebeTitulo,String recebeNota) {

        try {
            SQLiteDatabase bancoDados = openOrCreateDatabase("exibeFilmesMarvel", MODE_PRIVATE, null);
            bancoDados.execSQL("UPDATE notasFilmes  SET nota="+"'"+ recebeNota +"'"+"  WHERE titulo=" + "'"+recebeTitulo+"'");
            Toast.makeText(listaMarvel.this, "Nota Alterada", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(listaMarvel.this, "!!ERRO NA ALTERAÇÃO!!!", Toast.LENGTH_SHORT).show();
        }  //fim do try
    }


   public void insereNota(int ID,String titulo,String nota){

       try {


           //abre o banco
           SQLiteDatabase bancoDados = openOrCreateDatabase("exibeFilmesMarvel", MODE_PRIVATE, null);
           bancoDados.execSQL("INSERT INTO notasFilmes (ID,titulo,nota) " +
                   "VALUES ('" + ID + "','" + titulo + "','" + nota + "')");


           Toast.makeText(listaMarvel.this, "Nota Gravada", Toast.LENGTH_SHORT).show();


       } catch (Exception e) {
           e.printStackTrace();
           Toast.makeText(listaMarvel.this, "!!ERRO NO CADASTRO!!!", Toast.LENGTH_SHORT).show();
       }  //fim do try

   }

    public void pesquisa(String recebePesquisa){
           String formataPesquisa="%"+recebePesquisa+"%";
           try {

            SQLiteDatabase bancoDados = openOrCreateDatabase( "exibeFilmesMarvel", MODE_PRIVATE, null);

               Cursor cursor = bancoDados.rawQuery("" +
                       " SELECT DISTINCT   \n" +
                       "                          notasFilmes.nota,\n" +
                       "                          dadosFilmes.ID,\n" +
                       "                          dadosFilmes.titulo,\n" +
                       "                          dadosFilmes.genero,\n" +
                       "                          dadosFilmes.ano,\n" +
                       "                          dadosFilmes.poster\n" +
                       "                          \n" +
                       "                      FROM dadosFilmes \n" +
                       "                      LEFT JOIN notasFilmes   ON notasFilmes.titulo = dadosFilmes.titulo \n" +
                       " \n" +
                                             " where dadosFilmes.titulo like "+"'"+formataPesquisa+"'"+" \n" +
                       "                      order by nota DESC"    , null);



               //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("dadosFilmes.ID");
            int indiceColunaTitulo = cursor.getColumnIndex("dadosFilmes.titulo");
            int indiceColunaGenero = cursor.getColumnIndex("dadosFilmes.genero");
            int indiceColunaAno = cursor.getColumnIndex("dadosFilmes.ano");
            int indiceColunaPoster = cursor.getColumnIndex("dadosFilmes.poster");
            int indiceColunaNota = cursor.getColumnIndex("notasFilmes.nota");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {
                String nota = "";
                if(cursor.getString(indiceColunaNota)==null || cursor.getString(indiceColunaNota)=="")
                {
                    nota="Ainda sem nota";
                }else {
                    nota =cursor.getString(indiceColunaNota);
                }
                modelo modelo = new modelo (Integer.parseInt(cursor.getString(indiceColunaID)),
                        cursor.getString(indiceColunaTitulo),
                        cursor.getString(indiceColunaGenero),
                        cursor.getString(indiceColunaAno),
                        cursor.getString(indiceColunaPoster),
                        nota);
                listaFilmes.add(modelo);
                cursor.moveToNext();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try

    }

    public void alimentaRecyclerView(){

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase( "exibeFilmesMarvel", MODE_PRIVATE, null);


            Cursor cursor = bancoDados.rawQuery("" +
                    " SELECT DISTINCT   \n" +
                    "                          notasFilmes.nota,\n" +
                    "                          dadosFilmes.ID,\n" +
                    "                          dadosFilmes.titulo,\n" +
                    "                          dadosFilmes.genero,\n" +
                    "                          dadosFilmes.ano,\n" +
                    "                          dadosFilmes.poster\n" +
                    "                          \n" +
                    "                      FROM dadosFilmes \n" +
                    "                      LEFT JOIN notasFilmes   ON notasFilmes.titulo = dadosFilmes.titulo \n" +
                    " \n" +
                    "                      order by nota DESC"    , null);

            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("dadosFilmes.ID");
            int indiceColunaTitulo = cursor.getColumnIndex("dadosFilmes.titulo");
            int indiceColunaGenero = cursor.getColumnIndex("dadosFilmes.genero");
            int indiceColunaAno = cursor.getColumnIndex("dadosFilmes.ano");
            int indiceColunaPoster = cursor.getColumnIndex("dadosFilmes.poster");
            int indiceColunaNota = cursor.getColumnIndex("notasFilmes.nota");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {

                String nota = "";
                if(cursor.getString(indiceColunaNota)==null || cursor.getString(indiceColunaNota)=="")
                {
                    nota="Ainda sem nota";
                }else {
                    nota =cursor.getString(indiceColunaNota);
                }
                nota =cursor.getString(indiceColunaNota);
                modelo modelo = new modelo (Integer.parseInt(cursor.getString(indiceColunaID)),
                        cursor.getString(indiceColunaTitulo),
                        cursor.getString(indiceColunaGenero),
                        cursor.getString(indiceColunaAno),
                        cursor.getString(indiceColunaPoster),
                        nota);
                listaFilmes.add(modelo);
                cursor.moveToNext();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try

    }
    public void alimentaRecyclerViewFilmes(){

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase( "exibeFilmesMarvel", MODE_PRIVATE, null);


            Cursor cursor = bancoDados.rawQuery(" select * from dadosFilmes", null);

           // Cursor cursor = bancoDados.rawQuery("SELECT  * FROM dadosFilmes ", null);

            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("ID");
            int indiceColunaTitulo = cursor.getColumnIndex("titulo");
            int indiceColunaGenero = cursor.getColumnIndex("genero");
            int indiceColunaAno = cursor.getColumnIndex("ano");
            int indiceColunaPoster = cursor.getColumnIndex("poster");
           // int indiceColunaNota = cursor.getColumnIndex("nota");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {

               modelo modelo = new modelo (Integer.parseInt(cursor.getString(indiceColunaID)),
                       cursor.getString(indiceColunaTitulo),
                       cursor.getString(indiceColunaGenero),
                       cursor.getString(indiceColunaAno),
                       cursor.getString(indiceColunaPoster),
                       "" );
                listaFilmes.add(modelo);
                cursor.moveToNext();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try

    }

}