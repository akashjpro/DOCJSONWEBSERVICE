package com.example.akashjpro.docjsonwebservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvPhim;
    ArrayList<Phim> mangPhim;
    PhimAdapter phimAdapter;
    Button btnThemPhim, btnUpdate;
    String idPhim = "";
    ProgressBar prBarLoad;
    EditText edtTen, edtLoai, edtQuocGia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        mangPhim = new ArrayList<>();
        phimAdapter = new PhimAdapter(this, R.layout.item_phim, mangPhim);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new READ_JON_WEB_SERVICE().execute("http://akashjpro.esy.es/demo1.php");
            }
        });

        btnThemPhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemPhim.class);
                startActivity(intent);
            }
        });

        lvPhim.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               xacNhanXoa(mangPhim.get(position).getTenPhim());
                idPhim = String.valueOf(mangPhim.get(position).getId());
                return false;
            }
        });

        lvPhim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtTen.setText(mangPhim.get(position).getTenPhim());
                edtLoai.setText(mangPhim.get(position).getTheLoai());
                edtQuocGia.setText(mangPhim.get(position).getQuocGia());
                idPhim = String.valueOf(mangPhim.get(position).getId());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new UdateData().execute("http://akashjpro.esy.es/update.php");
                    }
                });
            }
        });
    }

    private void addControls() {
        lvPhim      = (ListView) findViewById(R.id.lvPhim);

        btnThemPhim = (Button) findViewById(R.id.btnThemPhim);
        btnUpdate   = (Button) findViewById(R.id.btnUpadatePhim);

        prBarLoad   = (ProgressBar) findViewById(R.id.progressBar);

        edtTen      = (EditText) findViewById(R.id.editTextTen);
        edtLoai      = (EditText) findViewById(R.id.editTextLoai);
        edtQuocGia      = (EditText) findViewById(R.id.editTextQuocGia1);


    }

    private class READ_JON_WEB_SERVICE extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mangPhim.clear();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject objectPhim = jsonArray.getJSONObject(i);
                    mangPhim.add(new Phim(
                            objectPhim.getInt("id"),
                            objectPhim.getString("tenPhim"),
                            objectPhim.getString("theLoai"),
                            objectPhim.getString("quocGia")
                    ));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            phimAdapter.notifyDataSetChanged();
            lvPhim.setAdapter(phimAdapter);

            prBarLoad.setVisibility(View.INVISIBLE);
        }
    }



    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    private class DELETE_PHIM extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "1":
                    {
                        Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new READ_JON_WEB_SERVICE().execute("http://akashjpro.esy.es/demo1.php");
                            }
                        });
                    }
                    break;
                case "0":
                    Toast.makeText(MainActivity.this, "khong nhan duoc id", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                    break;
            }

            //Toast.makeText(MainActivity.this, "id= "+ s, Toast.LENGTH_SHORT).show();

        }
    }

    private void xacNhanXoa(String ten){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xac nhan xoa");
        builder.setMessage("Ban co chac chan xoa khong"+ ten + "khong?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DELETE_PHIM().execute("http://akashjpro.esy.es/delete.php");
                    }
                });
            }
        });
        builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private class UdateData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            return postDataUpdate(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "1":
                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new READ_JON_WEB_SERVICE().execute("http://akashjpro.esy.es/demo1.php");
                        }
                    });
                    break;
                case "0":
                    Toast.makeText(MainActivity.this, "khong nhan duoc id", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(MainActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
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
                    .appendQueryParameter("id", idPhim.toString().trim());
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

    private String postDataUpdate(String link){
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
                    .appendQueryParameter("id", idPhim)
                    .appendQueryParameter("tenPhim", edtTen.getText().toString().trim())
                    .appendQueryParameter("theLoai", edtLoai.getText().toString().trim())
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
}
