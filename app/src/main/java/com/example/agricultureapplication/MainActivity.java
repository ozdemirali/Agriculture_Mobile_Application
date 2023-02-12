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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import controller.Service;

public class MainActivity extends AppCompatActivity  {

    private ImageView imageView;
    private Button buttonSave;
    public static final int RequestPermissionCode = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static final String TAG = MainActivity.class.getName();

    private Service _service;
    private Spinner spinner;
    private  List<String> spinnerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner=findViewById(R.id.spinnerDisease);
        // Spinner click listener




        _service=new Service(MainActivity.this);
        spinnerId=new ArrayList<String>();

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
                System.out.println("-------------");
                _service.getType(spinner,spinnerId);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println("Seçildi");
                System.out.println(spinner.getSelectedItem());
                System.out.println(spinnerId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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

}