package com.example.inputdatasiswa;

public class konfigurasi {
    public static final String URL_ADD="https://crudsiswa.000webhostapp.com/tambahSiswa.php";
    public static final String URL_GET_ALL = "https://crudsiswa.000webhostapp.com/tampilSemuaSiswa.php";
    public static final String URL_GET_STUDENT = "https://crudsiswa.000webhostapp.com/tampilSiswa.php?id=";
    public static final String URL_UPDATE_STUDENT= "https://crudsiswa.000webhostapp.com/updateSiswa.php";
    public static final String URL_DELETE_STUDENT = "https://crudsiswa.000webhostapp.com/hapusSiswa.php?id=";

    public static final String GET_SCHOOL = "https://crudsiswa.000webhostapp.com/ambilSekolah.php";
    public static final String GET_PACKAGE = "https://crudsiswa.000webhostapp.com/ambilPaket.php";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_SISWA_NOMOR_INDUK = "nomor_induk";
    public static final String KEY_SISWA_NAMA = "nama";
    public static final String KEY_SISWA_JENIS_KELAMIN = "jenis_kelamin";
    public static final String KEY_SISWA_TEMPAT_LAHIR = "tempat_lahir";
    public static final String KEY_SISWA_TANGGAL_LAHIR = "tanggal_lahir";
    public static final String KEY_SISWA_SEKOLAH_ASAL = "sekolah_asal";
    public static final String KEY_SISWA_ALAMAT = "alamat";
    public static final String KEY_SISWA_NAMA_WALI = "nama_wali";
    public static final String KEY_SISWA_NOMOR_TELP = "nomor_telp";
    public static final String KEY_SISWA_PAKET = "paket";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "nomor_induk";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JENIS_KELAMIN = "jenis_kelamin";
    public static final String TAG_TEMPAT_LAHIR = "tempat_lahir";
    public static final String TAG_TANGGAL_LAHIR = "tanggal_lahir";
    public static final String TAG_SEKOLAH_ASAL = "sekolah_asal";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NAMA_WALI = "nama_wali";
    public static final String TAG_NOMOR_TELP = "nomor_telp";
    public static final String TAG_PAKET = "paket";


    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String STUDENT_ID = "nomor_induk";
}
