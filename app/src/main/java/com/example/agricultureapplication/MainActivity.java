package com.example.agricultureapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.SystemDefaultCredentialsProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import controller.ApiConfig;
import controller.AppConfig;
import controller.Service;
import model.AgriculturalDisease;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity  {

    private ImageView imageView;
    private Button buttonSave;
    private Bitmap bitmap;
    public static final int RequestPermissionCode = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static final String TAG = MainActivity.class.getName();

    private Service _service;
    private AgriculturalDisease _agriculturalDisease;


    private Spinner spinnerType;
    private List<String> spinnerTypeId;

    private Spinner spinnerProduct;
    private List<String> spinnerProductId;

    private Spinner spinnerDisease;
    private List<String> spinnerDiseaseId;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinnerType=findViewById(R.id.spinnerType);
        spinnerProduct=findViewById(R.id.spinnerProduct);
        spinnerDisease=findViewById(R.id.spinnerDisease);

        _service=new Service(MainActivity.this);
        _agriculturalDisease=new AgriculturalDisease();

        spinnerTypeId=new ArrayList<String>();

        spinnerDiseaseId=new ArrayList<String>();

        imageView=findViewById(R.id.imageView);
        buttonSave=findViewById(R.id.buttonSave);

        //Give camera permission
        EnableRuntimePermission();
        //Receiver
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        Bundle bundle= result.getData().getExtras();
                        bitmap = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(bitmap);

                        //UploadImage(bitmap);
                        //uploadFile(bitmap);
                        //uploadNewBitmap(bitmap);
                        //_service.uploadBitmap(bitmap);

                    }
                }catch (Exception e){

                    System.out.println(e.getMessage());
                }

            }
        });

        //set data to spinnerType
        _service.getType(spinnerType,spinnerTypeId);
        //_service.getAgriculturalProduct(spinnerProduct,spinnerProductId,1);

        //Image ClickListener
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

        //Button ClickListener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tıklandı");
                System.out.println("-------------");
               // _service.getType(spinner,spinnerId);

                int productId= Integer.parseInt(spinnerProductId.get((int) spinnerProduct.getSelectedItemId()));
                int diseaseId= Integer.parseInt(spinnerDiseaseId.get((int) spinnerDisease.getSelectedItemId()));

                _agriculturalDisease.setAgriculturalProductId(productId);
                _agriculturalDisease.setDiseaseId(diseaseId);
               System.out.println(_agriculturalDisease.getDiseaseId());
               System.out.println(_agriculturalDisease.getAgriculturalProductId());
                //_service.PostProduct();
               _service.uploadBitmap(bitmap,_agriculturalDisease);


            }
        });




        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println("Type Seçildi");
                System.out.println(spinnerTypeId.toString());
                spinnerProductId=new ArrayList<String>();
                _service.getAgriculturalProduct(spinnerProduct,spinnerProductId, Integer.parseInt(spinnerTypeId.get(position)));
                _service.getDisease(spinnerDisease,spinnerDiseaseId,Integer.parseInt(spinnerTypeId.get(position)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                System.out.println("Seçilmedi");
            }
        });

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println("Product Seçildi");



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                System.out.println("Seçilmedi");
            }
        });
    }



    // Camera
    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app",Toast.LENGTH_LONG).show();
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
   //Camera End


}