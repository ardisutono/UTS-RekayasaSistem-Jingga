package com.example.inputdatasiswa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final Calendar myCalendar = Calendar.getInstance();

    private EditText editNama;
    private EditText editTempatLahir;
    private EditText editTglLahir;
    private EditText editAlamat;
    private EditText editNamaWali;
    private EditText editNomorTelp;

    private Button buttonAdd;
    private Button buttonView;

    Spinner genderSpinner;
    Spinner schoolSpinner;
    Spinner packageSpinner;

    ArrayList<String> listSekolah = new ArrayList<>();
    ArrayList<String> listPaket = new ArrayList<>();

    ArrayAdapter<String> sekolahAdapter;
    ArrayAdapter<String> paketAdapter;

    RequestQueue requestQueue;


    String myFormat = "dd/MM/yy"; //In which you need put here
    SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        schoolSpinner = (Spinner) findViewById(R.id.sekolah_spinner);
        packageSpinner = (Spinner) findViewById(R.id.paket_spinner);
        editNama = (EditText) findViewById(R.id.editNama);
        editTempatLahir = (EditText) findViewById(R.id.editTempatLahir);
        editNamaWali = (EditText) findViewById(R.id.editNamaWali);
        editNomorTelp = (EditText) findViewById(R.id.editNomorTelp);
        editAlamat = (EditText) findViewById(R.id.editAlamat);

        editTglLahir = (EditText) findViewById(R.id.editTglLahir);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
                editTglLahir.setText(df.format(myCalendar.getTime()));
            }

        };

        editTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        loadSpinnerData();
        loadGenderSpinner();
        loadPackageSpinner();

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }


    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void addEmployee(){

        final String name = editNama.getText().toString().trim();
        final String gender = genderSpinner.getSelectedItem().toString().trim();
        final String packageChosen = packageSpinner.getSelectedItem().toString().trim();
        final String birthPlace = editTempatLahir.getText().toString().trim();
        final String birthDate = editTglLahir.getText().toString().trim();
        final String school = schoolSpinner.getSelectedItem().toString().trim();
        final String address = editAlamat.getText().toString().trim();
        final String guardian = editNamaWali.getText().toString().trim();
        final String phoneNo = editNomorTelp.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_SISWA_NAMA,name);
                params.put(konfigurasi.KEY_SISWA_ALAMAT,address);
                params.put(konfigurasi.KEY_SISWA_JENIS_KELAMIN,gender);
                params.put(konfigurasi.KEY_SISWA_NAMA_WALI, guardian);
                params.put(konfigurasi.KEY_SISWA_NOMOR_TELP, phoneNo);
                params.put(konfigurasi.KEY_SISWA_SEKOLAH_ASAL, school);
                params.put(konfigurasi.KEY_SISWA_TANGGAL_LAHIR, birthDate);
                params.put(konfigurasi.KEY_SISWA_TEMPAT_LAHIR, birthPlace);
                params.put(konfigurasi.KEY_SISWA_TEMPAT_LAHIR, birthPlace);
                params.put(konfigurasi.KEY_SISWA_PAKET, packageChosen);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addEmployee();
        }

        if(v == buttonView){
           startActivity(new Intent(this,TampilSemuaSiswa.class));
        }
    }

    private void loadSpinnerData() {
        // database handler
        String getSchool = konfigurasi.GET_SCHOOL;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getSchool, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sekolah = jsonObject.optString("nama_sekolah");
                        listSekolah.add(sekolah);
                        sekolahAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, listSekolah);
                        sekolahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        schoolSpinner.setAdapter(sekolahAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadGenderSpinner() {
        List<String> gender = new ArrayList<String>();
        gender.add("F");
        gender.add("M");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, gender);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        genderSpinner.setAdapter(dataAdapter);
    }

    private void loadPackageSpinner() {
        String getPackage = konfigurasi.GET_PACKAGE;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getPackage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String paket = jsonObject.optString("paket");
                        listPaket.add(paket);
                        paketAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, listPaket);
                        paketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSpinner.setAdapter(paketAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}