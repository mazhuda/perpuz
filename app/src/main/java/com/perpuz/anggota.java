package com.perpuz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class anggota extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Membuat objek JSON Parser
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> anggotaList;

    // Inisialisasi JSONArray
    JSONArray anggota = null;

    // JSON url
    private static final String DATA_URL = "http://10.0.2.2/perpuz/android-anggota.php";

    // Nama semua node JSON yang diambil
    private static final String TAG_ANGGOTA = "anggota";
    private static final String TAG_ID_ANGGOTA = "id_anggota";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_ALAMAT = "alamat";
    private static final String TAG_EMAIL = "email";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anggota);

        // Hashmap untuk ListView
        anggotaList = new ArrayList<HashMap<String, String>>();

        // Loading data saat Background Thread
        new LoadData().execute();
    }

    // Proses Background Async untuk meload semua data dari pembuatan HTTP Request
    class LoadData extends AsyncTask<String, String, String> {

        // Sebelum memulai proses background thread menampilkan Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(anggota.this);
            pDialog.setMessage("Loading bro...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // Pengambilan data JSON
        protected String doInBackground(String... args) {

            // Pembuatan Parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // Pengambilan data JSON berupa string dari URL
            JSONObject json = jsonParser.makeHttpRequest(DATA_URL, "GET",
                    params);

            // Mengecek log cat dari JSON reponse
            Log.d("Data JSON: ", json.toString());

            try {
                anggota = json.getJSONArray(TAG_ANGGOTA);

                // looping semua data yang terambil
                for (int i = 0; i < anggota.length(); i++) {
                    JSONObject c = anggota.getJSONObject(i);

                    // Menyimpan setiap json item ke dalam variabel
                    String id_anggota = c.getString(TAG_ID_ANGGOTA);
                    String nama = c.getString(TAG_NAMA);
                    String alamat = c.getString(TAG_ALAMAT);
                    String email = c.getString(TAG_EMAIL);

                    // Membuat HashMap baru
                    HashMap<String, String> map = new HashMap<String, String>();

                    // Menambahkan setiap child node ke dalam (HashMap key => value)
                    map.put(TAG_ID_ANGGOTA, id_anggota);
                    map.put(TAG_NAMA, nama);
                    map.put(TAG_ALAMAT, alamat);
                    map.put(TAG_EMAIL, email);

                    // Menambahkan HashList ke dalam ArrayList
                    anggotaList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Setelah proses background selesai maka singkirkan progress dialog
        protected void onPostExecute(String file_url) {

            // Menyingkirkan dialog setelah mendapatkan semua data
            pDialog.dismiss();

            // Mengupdate UI dari proses Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    // Mengupdate data JSON yang telah terparser ke dalam ListView
                    ListAdapter adapter = new SimpleAdapter(
                            anggota.this, anggotaList,
                            R.layout.anggota_item, new String[] { TAG_NAMA, TAG_ALAMAT, TAG_EMAIL },
                            new int[] { R.id.nama, R.id.alamat, R.id.email });

                    // Mengupdate listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
