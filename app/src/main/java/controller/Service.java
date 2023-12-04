package controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AgriculturalDisease;
import model.DataPart;
import model.Url;
import security.HttpsTrustManager;

public class Service {

    private RequestQueue _queue;
    private StringRequest mStringRequest;
    private Context _context;
    private JSONArray _jsonArray;
    private Url url;



    public Service(Context context){
        HttpsTrustManager.allowAllSSL();
        _context=context;
        _queue= Volley.newRequestQueue(_context);
        url=new Url();
    }

    public void getType(Spinner spinner, List<String> spinnerId){
        StringRequest myRequest = new StringRequest(Request.Method.GET, url.getType,
                response -> {
                    try{
                        JSONArray jsonarray = new JSONArray(response);
                        List<String> spinnerArray =  new ArrayList<String>();
                        for (int i=0; i < jsonarray.length(); i++) {
                            //sSystem.out.println(jsonarray.getJSONObject(i));
                            spinnerId.add(jsonarray.getJSONObject(i).getString("id"));
                            spinnerArray.add(jsonarray.getJSONObject(i).getString("name"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        //System.out.println(_jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(_context, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        _queue.add(myRequest);
    }

    public void getAgriculturalProduct(Spinner spinner, List<String> spinnerId,int id){
        StringRequest myRequest = new StringRequest(Request.Method.GET, url.getAgriculturalProduct+id,
                response -> {
                    try{
                        JSONArray jsonarray = new JSONArray(response);
                        List<String> spinnerArray =  new ArrayList<String>();
                        for (int i=0; i < jsonarray.length(); i++) {
                            System.out.println(jsonarray.getJSONObject(i));
                            spinnerId.add(jsonarray.getJSONObject(i).getString("id"));
                            spinnerArray.add(jsonarray.getJSONObject(i).getString("name"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        //System.out.println(_jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(_context, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        _queue.add(myRequest);
    }

    public void getDisease(Spinner spinner, List<String> spinnerId,int id){
        StringRequest myRequest = new StringRequest(Request.Method.GET, url.getDisease+id,
                response -> {
                    try{
                        JSONArray jsonarray = new JSONArray(response);
                        List<String> spinnerArray =  new ArrayList<String>();
                        for (int i=0; i < jsonarray.length(); i++) {
                            System.out.println(jsonarray.getJSONObject(i));
                            spinnerId.add(jsonarray.getJSONObject(i).getString("id"));
                            spinnerArray.add(jsonarray.getJSONObject(i).getString("name"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        //System.out.println(_jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(_context, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        _queue.add(myRequest);
    }

    //Upload image
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void uploadBitmap(final Bitmap bitmap, AgriculturalDisease agriculturalDisease) {
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url.sendImage,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            //System.out.println("Response-----------------");
                            //System.out.println(response.toString());
                            System.out.println(obj.getString("fileName"));
                            Toast.makeText(_context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            PostProduct(agriculturalDisease);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //System.out.println("Error");
                            //System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(_context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("tags", tags);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(_context).add(volleyMultipartRequest);
    }


    public void PostProduct(AgriculturalDisease agriculturalDisease) {

         JsonObjectRequest _jsonObjectRequest = null;



        try {
            JSONObject _jsonObject = new JSONObject();
            _jsonObject.put("AgriculturalProductId",agriculturalDisease.getAgriculturalProductId());
            _jsonObject.put("DiseaseId",agriculturalDisease.getDiseaseId());
            _jsonObject.put("Not",agriculturalDisease.getNot());

            _jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.postDisease, _jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println(response.getString("id"));
                                String message =response.getString("id");
                                Toast.makeText(_context, "" + message, Toast.LENGTH_SHORT).show();
                                //Dialog progressDialog;
                                //progressDialog;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(_context, "Hastalık kayıt edilemedi", Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _queue = Volley.newRequestQueue(_context);
        _queue.add(_jsonObjectRequest);

    }


}
