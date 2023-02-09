package com.example.agricultureapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicReference;

import security.HttpsTrustManager;

public class MainActivity<requestQueue> extends AppCompatActivity {

    private ImageView imageView;
    private Button buttonSave;
    public static final int RequestPermissionCode = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://192.168.1.245:45455/api/Type";
    private static final String TAG = MainActivity.class.getName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        buttonSave=findViewById(R.id.buttonSave);

        //Instantiate the RequestQueue.
        RequestQueue queue= Volley.newRequestQueue(this);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        String _url="https://192.168.1.245:45455/api/Type";

        //String url="https://jsonplaceholder.typicode.com/todos/";

        //Give camera permission
        EnableRuntimePermission();

        //Receiver
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        Bundle bundle= result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(bitmap);
                    }
                }catch (Exception e){

                    System.out.println(e.getMessage());
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Caller
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!=null){
                        activityResultLauncher.launch(intent);

                    }else{
                        Toast.makeText(MainActivity.this,"There is no app that support this section",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){

                    System.out.println(e.getMessage());
                }


            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tıklandı");

                HttpsTrustManager.allowAllSSL();
                StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try{
                                System.out.println(response.toString());
                                JSONArray jsonarray = new JSONArray(response);

                                for (int i=0; i < jsonarray.length(); i++) {
                                    System.out.println(jsonarray.getJSONObject(i));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
                );
                //RequestQueue requestQueue = Volley.newRequestQueue(this);
                queue.add(myRequest);
                //sendAndRequestResponse();



            }
        });


    }



    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void sendAndRequestResponse() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("Error :" + error.getMessage());
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void test(){
        // Request a string response from the provided URL.
        mRequestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), ""+status+message, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            System.out.println("Catch Error");
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                        System.out.println("Error Listener");
                        //System.out.println(error.getMessage());
                    }
                });
        mRequestQueue.add(jsonObjectRequest);

         //Add the request to the RequestQueue.
        //queue.add(stringRequest);
    }


}