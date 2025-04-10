package de.fh_kiel.raum_ueberwachung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Daten extends AppCompatActivity {
    public int RaumNR;
    String JsonRaumNR=null;
    TextView Bewegung;
    TextView Datum;
    TextView Fenster;
    TextView Licht;
    TextView Temperatur;
    TextView Feushtigkeit;
    TextView LuftDruck;
    TextView Batteriespannung;
    TextView Signalstaerke;
    TextView GetWay;
    Button Aktualesieren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daten);


        Bewegung           = findViewById(R.id.txtb);
        Fenster            = findViewById(R.id.txtfe);
        Licht              = findViewById(R.id.txtl);
        Temperatur         = findViewById(R.id.txtt);
        Feushtigkeit       = findViewById(R.id.txtf);
        LuftDruck          = findViewById(R.id.txtld);
        Batteriespannung   = findViewById(R.id.txtbs);
        Datum              = findViewById(R.id.txtd);
        Signalstaerke      = findViewById(R.id.txtss);
        GetWay             = findViewById(R.id.txtgw);

        Aktualesieren      =(Button)findViewById(R.id.btn);

        Intent intent= getIntent();
        RaumNR = intent.getIntExtra(RaumActivity.Raum,0);
        new ParseTask().execute();
    }

    public void onclick(View view){

        new ParseTask().execute();
    }


    class ParseTask extends AsyncTask<Void, Void, String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json=null;

switch (RaumNR) {
    case 1:
         $url_json = "http://raumueberwachung.bplaced.net/android-R1.php";
        JsonRaumNR="Raum1";
        break;
    case 2:
         $url_json = "http://raumueberwachung.bplaced.net/android-R2.php";
        JsonRaumNR="Raum2";
        break;
    case 3:
         $url_json = "http://raumueberwachung.bplaced.net/android-R3.php";
        JsonRaumNR="Raum3";
        break;
    case 4:
        $url_json = "http://raum-wachung.bplaced.net/android.php";
        JsonRaumNR="Raum4";
        break;
    default:
        break;
}


                URL url = new URL($url_json);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);



            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray jArray = json.getJSONArray(JsonRaumNR);


                    JSONObject friend = jArray.getJSONObject(0);
                    if((friend.getString("dev_value_6")).contentEquals("1")) {
                        Bewegung.setText("ja es gab in den letzten 5 min.");
                        Bewegung.setBackgroundColor(0xFFF44336);
                    }else {
                        Bewegung.setText("Keine in den letzten 5 min.");

                        Bewegung.setBackgroundColor(0xFF26E32D);
                    }

                    if((friend.getString("dev_value_5")).contentEquals("1")) {
                        Fenster.setText("ist auf");
                        Fenster.setBackgroundColor(0xFFF44336);
                    }else {
                        Fenster.setText("ist zu");

                        Fenster.setBackgroundColor(0xFF26E32D);
                    }
                    if((friend.getString("dev_value_7")).contentEquals("1")) {
                        Licht.setText("ist an");
                        Licht.setBackgroundColor(0xFFF44336);
                    }else {
                        Licht.setText("ist aus");

                        Licht.setBackgroundColor(0xFF26E32D);
                    }

                    Temperatur.setText("  "+friend.getString("dev_value_2")+" C°");
                    Feushtigkeit.setText("  "+friend.getString("dev_value_3")+" %");
                    LuftDruck.setText("  "+friend.getString("dev_value_8") +" hPa");
                    Batteriespannung.setText("  "+friend.getString("dev_value_4")+" V");
                    GetWay.setText(friend.getString("gtw_id"));
                    Datum.setText(friend.getString("datetime"));
                    Signalstaerke.setText("  "+friend.getString("gtw_rssi")+"  dBm");


            } catch (JSONException e) {

                e.printStackTrace();

            }
        }
    }

}