package de.fh_kiel.raum_ueberwachung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class  RaumActivity extends AppCompatActivity {
    public final static String Raum="de.fh_kiel.raum_ueberwachung.Raum";
    public int RaumNR;
    public final static String option="de.fh_kiel.raumueberwachung.option";
    public int optionValue;
    Button Datenbutton;
    Button Fensterbutton;
    Button Bewegungbutton;
    Button Lichtbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raum2);
        Datenbutton   =findViewById(R.id.btnData);
        Fensterbutton =findViewById(R.id.btnFenster);
        Bewegungbutton =findViewById(R.id.btnBewegung);
        Lichtbutton    =findViewById(R.id.btnLicht);

        Intent intent= getIntent();
        RaumNR=intent.getIntExtra(MainActivity.Raum,0);


    }
    public void onClickData(View view){


        opendataActivity();

    }
    public void onClickBewegung(View view){

        optionValue=2;
        openLetzteDatumActivity();

    }
    public void onClickFenster(View view){

        optionValue=0;
        openLetzteDatumActivity();
    }
    public void onClickLicht(View view){

        optionValue=1;
        openLetzteDatumActivity();
    }

    public void opendataActivity(){
        Intent intentData = new Intent(this,Daten.class);
        intentData.putExtra(Raum,RaumNR);
        startActivity(intentData);
    }

    public void openLetzteDatumActivity(){
        Intent intentLetzteDatum = new Intent(this,LetzteDatum.class);
        intentLetzteDatum.putExtra(option,optionValue);
        intentLetzteDatum.putExtra(Raum,RaumNR);
        startActivity(intentLetzteDatum);
    }
}

