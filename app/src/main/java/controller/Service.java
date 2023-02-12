package controller;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agricultureapplication.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import security.HttpsTrustManager;

public class Service {

    private RequestQueue _queue;
    private StringRequest mStringRequest;
    private String url = "https://192.168.1.245:45455/api/Type";
    private Context _context;
    private JSONArray _jsonArray;




    public Service(Context context){
        HttpsTrustManager.allowAllSSL();
        _context=context;
        _queue= Volley.newRequestQueue(_context);
    }

    public void getType(Spinner spinner, List<String> spinnerId){
        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONArray jsonarray = new JSONArray(response);
                        List<String> spinnerArray =  new ArrayList<String>();
                        for (int i=0; i < jsonarray.length(); i++) {
                            //System.out.println(jsonarray.getJSONObject(i));
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
}
