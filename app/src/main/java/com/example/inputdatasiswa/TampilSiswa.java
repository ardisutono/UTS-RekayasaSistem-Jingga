package com.example.inputdatasiswa;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class TampilSiswa extends AppCompatActivity implements View.OnClickListener{
    final Calendar myCalendar = Calendar.getInstance();

    private EditText editNama;
    private EditText editTempatLahir;
    private EditText editTglLahir;
    private EditText editAlamat;
    private EditText editNamaWali;
    private EditText editNomorTelp;
    private EditText editNomorInduk;

    Spinner genderSpinner;
    Spinner schoolSpinner;
    Spinner packageSpinner;

    ArrayList<String> listSekolah = new ArrayList<>();
    ArrayList<String> listPaket = new ArrayList<>();

    ArrayAdapter<String> sekolahAdapter;
    ArrayAdapter<String> paketAdapter;

    RequestQueue requestQueue;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    String myFormat = "dd/MM/yy"; //In which you need put here
    SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_siswa);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.STUDENT_ID);

        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        schoolSpinner = (Spinner) findViewById(R.id.sekolah_spinner);
        packageSpinner =(Spinner) findViewById(R.id.paket_spinner);
        editNomorInduk = (EditText) findViewById(R.id.editNomorInduk);
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
                editTglLahir.setText(df.format(myCalendar.getTime()));
            }

        };

        editTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TampilSiswa.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        loadSpinnerData();
        loadGenderSpinner();
        loadPackageSpinner();

        editNomorInduk.setText(id);

        getStudent();
    }

    private void getStudent(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showStudent(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_STUDENT,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showStudent(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String jenisKelamin = c.getString(konfigurasi.TAG_JENIS_KELAMIN);
            String tempatLahir = c.getString(konfigurasi.TAG_TEMPAT_LAHIR);
            String tanggalLahir = c.getString(konfigurasi.TAG_TANGGAL_LAHIR);
            String sekolahAsal = c.getString(konfigurasi.TAG_SEKOLAH_ASAL);
            String alamat = c.getString(konfigurasi.TAG_ALAMAT);
            String namaWali = c.getString(konfigurasi.TAG_NAMA_WALI);
            String nomorTelp = c.getString(konfigurasi.TAG_NOMOR_TELP);

            editNama.setText(nama);
            editTempatLahir.setText(tempatLahir);
            editNamaWali.setText(namaWali);
            editNomorTelp.setText(nomorTelp);
            editAlamat.setText(alamat);
            editTglLahir.setText(tanggalLahir);
            genderSpinner.setSelection(((ArrayAdapter)genderSpinner.getAdapter()).getPosition(jenisKelamin));
            //schoolSpinner.setSelection(Integer.parseInt(sekolahAsal));
            //schoolSpinner.setSelection(((ArrayAdapter)schoolSpinner.getAdapter()).getPosition(sekolahAsal));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateStudent(){
        final String nomorInduk = editNomorInduk.getText().toString().trim();
        final String name = editNama.getText().toString().trim();
        final String gender = genderSpinner.getSelectedItem().toString().trim();
        final String birthPlace = editTempatLahir.getText().toString().trim();
        final String birthDate = editTglLahir.getText().toString().trim();
        final String school = schoolSpinner.getSelectedItem().toString().trim();
        final String packageChosen = packageSpinner.getSelectedItem().toString().trim();

        final String address = editAlamat.getText().toString().trim();
        final String guardian = editNamaWali.getText().toString().trim();
        final String phoneNo = editNomorTelp.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilSiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_SISWA_NOMOR_INDUK,nomorInduk);
                hashMap.put(konfigurasi.KEY_SISWA_NAMA,name);
                hashMap.put(konfigurasi.KEY_SISWA_ALAMAT,address);
                hashMap.put(konfigurasi.KEY_SISWA_JENIS_KELAMIN,gender);
                hashMap.put(konfigurasi.KEY_SISWA_NAMA_WALI, guardian);
                hashMap.put(konfigurasi.KEY_SISWA_NOMOR_TELP, phoneNo);
                hashMap.put(konfigurasi.KEY_SISWA_SEKOLAH_ASAL, school);
                hashMap.put(konfigurasi.KEY_SISWA_TANGGAL_LAHIR, birthDate);
                hashMap.put(konfigurasi.KEY_SISWA_TEMPAT_LAHIR, birthPlace);
                hashMap.put(konfigurasi.KEY_SISWA_PAKET, packageChosen);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_STUDENT,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void deleteStudent(){
        class DeleteStudent extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSiswa.this, "Deleting...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilSiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_STUDENT, id);
                return s;
            }
        }

        DeleteStudent de = new DeleteStudent();
        de.execute();
    }

    private void confirmDeleteStudent(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Siswa ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteStudent();
                        startActivity(new Intent(TampilSiswa.this,TampilSemuaSiswa.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateStudent();
        }

        if(v == buttonDelete){
            confirmDeleteStudent();
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
                        ArrayAdapter<String> sekolahAdapter;
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sekolah = jsonObject.optString("nama_sekolah");
                        String id = jsonObject.optString( "id");
                        listSekolah.add(sekolah);
                        sekolahAdapter = new ArrayAdapter<>(TampilSiswa.this, android.R.layout.simple_spinner_item, listSekolah);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TampilSiswa.this, android.R.layout.simple_spinner_item, gender);

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
                        paketAdapter = new ArrayAdapter<>(TampilSiswa.this, android.R.layout.simple_spinner_item, listPaket);
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