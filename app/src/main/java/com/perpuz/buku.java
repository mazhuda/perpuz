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

public class buku extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Membuat objek JSON Parser
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> bukuList;

    // Inisialisasi JSONArray
    JSONArray buku = null;

    // JSON url
    private static final String DATA_URL = "http://10.0.2.2/perpuz/android-buku.php";

    // Nama semua node JSON yang diambil
    private static final String TAG_BUKU = "buku";
    private static final String TAG_ID_BUKU = "id_buku";
    private static final String TAG_JUDUL = "judul";
    private static final String TAG_JENIS = "jenis";
    private static final String TAG_PENULIS = "penulis";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buku);

        // Hashmap untuk ListView
        bukuList = new ArrayList<HashMap<String, String>>();

        // Loading data saat Background Thread
        new LoadData().execute();
    }

    // Proses Background Async untuk meload semua data dari pembuatan HTTP Request
    class LoadData extends AsyncTask<String, String, String> {

        // Sebelum memulai proses background thread menampilkan Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(buku.this);
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
                buku = json.getJSONArray(TAG_BUKU);

                // looping semua data yang terambil
                for (int i = 0; i < buku.length(); i++) {
                    JSONObject c = buku.getJSONObject(i);

                    // Menyimpan setiap json item ke dalam variabel
                    String id_buku = c.getString(TAG_ID_BUKU);
                    String judul = c.getString(TAG_JUDUL);
                    String jenis = c.getString(TAG_JENIS);
                    String penulis = c.getString(TAG_PENULIS);

                    // Membuat HashMap baru
                    HashMap<String, String> map = new HashMap<String, String>();

                    // Menambahkan setiap child node ke dalam (HashMap key => value)
                    map.put(TAG_ID_BUKU, id_buku);
                    map.put(TAG_JUDUL, judul);
                    map.put(TAG_JENIS, jenis);
                    map.put(TAG_PENULIS, penulis);

                    // Menambahkan HashList ke dalam ArrayList
                    bukuList.add(map);
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
                            buku.this, bukuList,
                            R.layout.buku_item, new String[] { TAG_JUDUL, TAG_JENIS, TAG_PENULIS },
                            new int[] { R.id.judul, R.id.jenis, R.id.penulis });

                    // Mengupdate listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
