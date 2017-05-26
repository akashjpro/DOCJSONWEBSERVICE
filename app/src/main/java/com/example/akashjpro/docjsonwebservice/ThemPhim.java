package com.example.akashjpro.docjsonwebservice;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ThemPhim extends AppCompatActivity {

    EditText edtTenPhim, edtTheLoai, edtQuocGia;
    Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phim);

        addcontrols();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenPhim.getText().toString().trim();
                String loai = edtTheLoai.getText().toString().trim();
                String quocGia= edtQuocGia.getText().toString().trim();
                if(ten.isEmpty() || loai.isEmpty() || quocGia.isEmpty()){
                    Toast.makeText(ThemPhim.this, "Thông tin chưa đầy đủ", Toast.LENGTH_SHORT).show();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new InertData().execute("http://akashjpro.esy.es/insert.php");
                        }
                    });
                }
            }
        });


    }

    private class  InertData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (s){
                case "0": Toast.makeText(ThemPhim.this, "Thông tin chưa đầy đủ", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                            Toast.makeText(ThemPhim.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ThemPhim.this, MainActivity.class);
                            startActivity(intent);
                    break;
                case "2": Toast.makeText(ThemPhim.this, "Lỗi thêm", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private String postData(String link){
        HttpURLConnection connect;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error!";
        }
        try {
            // cấu hình HttpURLConnection
            connect = (HttpURLConnection)url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("POST");

            // Gán tham số vào URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("tenPhim", edtTenPhim.getText().toString().trim())
                    .appendQueryParameter("theLoai", edtTheLoai.getText().toString().trim())
                    .appendQueryParameter("quocGia", edtQuocGia.getText().toString().trim());
            String query = builder.build().getEncodedQuery();

            // Mở kết nối gửi dữ liệu
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connect.connect();

        } catch (IOException e1) {
            e1.printStackTrace();
            return "Error!";
        }
        try {
            int response_code = connect.getResponseCode();

            // kiểm tra kết nối ok
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Đọc nội dung trả về
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }else{
                return "Error!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        } finally {
            connect.disconnect();
        }
    }

    private void addcontrols() {
        btnThem = (Button) findViewById(R.id.buttonThem);

        edtTenPhim = (EditText) findViewById(R.id.editTextTenPhim);
        edtTheLoai = (EditText) findViewById(R.id.editTextTheLoai);
        edtQuocGia = (EditText) findViewById(R.id.editTextQuocGia);
    }
}
