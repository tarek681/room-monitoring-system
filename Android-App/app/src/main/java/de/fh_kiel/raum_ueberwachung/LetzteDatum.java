package de.fh_kiel.raum_ueberwachung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LetzteDatum extends AppCompatActivity {
    TextView Letztedatum;
    public int option;
    public int RaumNR;
    String JsonRaumNR=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letzte_datum);

        Letztedatum           = findViewById(R.id.txt);
        Intent intent= getIntent();

        option=intent.getIntExtra(RaumActivity.option,0);

        RaumNR = intent.getIntExtra(RaumActivity.Raum,0);
        new ParseTask().execute();
    }


    private class ParseTask extends AsyncTask<Void, Void, String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String doInBackground(Void... params) {
            try {

                String $url_json = null;
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



            String FensterString=null;
            String BewegungString=null;
            String LichtString=null;
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray jArray = json.getJSONArray(JsonRaumNR);

                if(option==0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject friend = jArray.getJSONObject(i);
                        FensterString=(friend.getString("dev_value_5"));
                        if(FensterString.contentEquals("1")){
                            Letztedatum.setText("Das Fenster War zum letzten Mal am\n"
                                    +friend.getString("datetime")+"  auf");
                            break;
                        }else if(i == jArray.length()-1){
                            Letztedatum.setText("das Fenster war noch nie an");
                        }

                    }
                }
                if(option==1) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject friend = jArray.getJSONObject(i);
                        LichtString=(friend.getString("dev_value_7"));
                        if(LichtString.contentEquals("1")){
                            Letztedatum.setText(" Zum letzten mal war das licht am" + friend.getString("datetime") + "  an");
                            break;
                        }else if(i == jArray.length()-1){
                            Letztedatum.setText("das Licht war noch nie an");
                        }

                    }
                }
                if(option==2) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject friend = jArray.getJSONObject(i);
                        BewegungString=(friend.getString("dev_value_6"));
                        if(BewegungString.contentEquals("1")){

                            Letztedatum.setText("Zum letzten Mal wurde am\n " + friend.getString("datetime") + "   eine Bewegung erkannt");
                            break;
                        }else if(i == jArray.length()-1){
                            Letztedatum.setText("es gab noch nie eine Bewegung");
                        }

                    }
                }
if(Letztedatum.getText().toString().contentEquals("")){

}
                //final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.item, from, to);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}