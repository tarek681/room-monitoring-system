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

public class Gerade extends AppCompatActivity {
    String $url_json=null;
    String JsonRaumNR=null;
    int suche;   //1 Fenster 2 Licht 3 Bewegung
    TextView Raum;
    String Ergebnis="in Folgenden Räume :   ";
    String InKeinem=Ergebnis;
    int zaehler=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerade);
        Intent intent= getIntent();
        suche=intent.getIntExtra(MainActivity.Suche,0);
        Raum =findViewById(R.id.txtgerade);

/*
        new ParseTask().execute();
        $url_json = "http://raumueberwachung.bplaced.net/android-R2.php";
        JasonRaumNR="Raum2";
        new ParseTask().execute();
        $url_json = "http://raumueberwachung.bplaced.net/android-R3.php";
        JasonRaumNR="Raum3";
        new ParseTask().execute();
        $url_json = "http://raumueberwachung.bplaced.net/android-R4.php";
        JasonRaumNR="Raum4";
        new ParseTask().execute();
        //Ergebnis= Ergebnis+"23"+"1110, "+"1i0nldkncl";
        */

        Raum.setText("Wird gesucht");
       // durchsuche();
        new ParseTask().execute();
    }






    class ParseTask extends AsyncTask<Void, Void, String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                zaehler++;
                switch (zaehler) {
                    case 1:
                        $url_json = "http://raumueberwachung.bplaced.net/android-R1.php";
                        JsonRaumNR = "Raum1";
                        break;
                    case 2:
                        $url_json = "http://raumueberwachung.bplaced.net/android-R2.php";
                        JsonRaumNR = "Raum2";
                        break;
                    case 3:
                        $url_json = "http://raumueberwachung.bplaced.net/android-R3.php";
                        JsonRaumNR = "Raum3";
                        break;
                    case 4:
                        $url_json = "http://raumueberwachung.bplaced.net/android-R4.php";
                        JsonRaumNR = "Raum4";
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

            //final ListView lView = (ListView) findViewById(R.id.lvMain);

            // String[] from = {"name_item"};
            // int[] to = {R.id.name_item};
            //ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            // HashMap<String, String> hashmap;

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray jArray = json.getJSONArray(JsonRaumNR);

                for (int i = 0; i < 1; i++) {
                    //Raum.setText("Halkm");
                    JSONObject friend = jArray.getJSONObject(i);

                    if (suche == 1) {
                        if ((friend.getString("dev_value_5")).contentEquals("1")) {
                            if (JsonRaumNR.contentEquals("Raum1")) {
                                Ergebnis = Ergebnis + "im " + JsonRaumNR;
                            } else {
                                Ergebnis = Ergebnis + "und im " + JsonRaumNR;
                            }

                        }

                    }
                    if (suche == 2) {
                        if ((friend.getString("dev_value_7")).contentEquals("1")) {
                            if (JsonRaumNR.contentEquals("Raum1")) {
                                Ergebnis = Ergebnis + "im " + JsonRaumNR;
                            } else {
                                Ergebnis = Ergebnis + "und im " + JsonRaumNR;
                            }

                        }

                    }
                    if (suche == 3) {
                        if ((friend.getString("dev_value_6")).contentEquals("1")) {

                                Ergebnis = Ergebnis + " Im " + JsonRaumNR + ", ";

                            }

                        }

                        if (zaehler == 4) {
                            if (Ergebnis.contentEquals(InKeinem)) {
                                Raum.setText("In keinem Raum");
                            } else
                                Raum.setText(Ergebnis);
                        }
                        if (zaehler < 4)
                            new ParseTask().execute();
                    }


               /* int a=0;
                String b=null;

                for (int i = 0; i < 1; i++) {

                    JSONObject friend = jArray.getJSONObject(i);
                    b=(friend.getString("dev_value_5"));

                    if(b.contentEquals("1")){

                        Fensterzuletztt.setText(friend.getString("datetime"));


                        break;
                    }

                }*/

                    //final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.item, from, to);

                } catch(JSONException e){

                    e.printStackTrace();

                }
            }
        }

}



/*if(suche==1){
                    if((friend.getString("dev_value_5")).contentEquals("1")) {
                       // Ergebnis=Ergebnis+JasonRaumNR+" ;";

                    }

                }
                if(suche==2){
                    if((friend.getString("dev_value_7")).contentEquals("1")) {
                     //   Ergebnis=Ergebnis+JasonRaumNR+" ;";

                    }

                }
                if(suche==3){
                    if((friend.getString("dev_value_6")).contentEquals("1")) {
                      //  Ergebnis=Ergebnis+JasonRaumNR+" ;";

                    }

                }*/