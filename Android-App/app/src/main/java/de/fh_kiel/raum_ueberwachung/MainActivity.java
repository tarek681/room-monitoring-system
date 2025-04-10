package de.fh_kiel.raum_ueberwachung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public final static String Raum="de.fh_kiel.raum_ueberwachung.Raum";
    public int RaumNR;
    public final static String Suche="de.fh_kiel.raum_ueberwachung.Suche";
    public int suche;
    Button Raum1;
    Button Raum2;
    Button Raum3;
    Button Raum4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Raum1=findViewById(R.id.btnR1);
        Raum2=findViewById(R.id.btnR2);
        Raum3=findViewById(R.id.btnR3);
        Raum4=findViewById(R.id.btnR4);
    }
    public void onClickRaum1(View view){

        RaumNR=1;
        openRaumActivity();
    }
    public void onClickRaum2(View view){

        RaumNR=2;
        openRaumActivity();
    }
    public void onClickRaum3(View view){

        RaumNR=3;
        openRaumActivity();
    }
    public void onClickRaum4(View view){

        RaumNR=4;
        openRaumActivity();
    }

    public void onClickFenster(View view){

        suche=1;
        openGerade();
    }
    public void onClickLicht(View view){

        suche=2;
        openGerade();
    }
    public void onClickBewegung(View view){

        suche=3;
        openGerade();
    }



    public void openRaumActivity(){
        Intent intent = new Intent(this,RaumActivity.class);
        intent.putExtra(Raum,RaumNR);
        startActivity(intent);

    }public void openGerade(){
        Intent intent = new Intent(this,Gerade.class);
        intent.putExtra(Suche,suche);
        startActivity(intent);
    }


}
