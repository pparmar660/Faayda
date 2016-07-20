package com.faayda.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.faayda.R;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.utils.ConnectionDetector;
import com.faayda.utils.Constants;

import java.util.Map;

/**
 * Created by Aashutosh @ vinove on 7/1/2015.
 */
public final class NetworkManager {

    private RequestQueue requestQueue;
    private Constants.SERVICE_TYPE type;
    private Map<String, String> parameters;
    private OnWebServiceResult onWebServiceResult;
    private ConnectionDetector connectionDetector;
    private ProgressDialog dialog;
    private Context mContext;
    private boolean showDialog = true;

   /* public NetworkManager(Context mContext, OnWebServiceResult webServiceResult){
        this(mContext, webServiceResult, Constants.SERVICE_TYPE.DEFAULT);
        connectionDetector=new ConnectionDetector(mContext);
    }*/

    public NetworkManager(Context mContext, Map<String, String> params, OnWebServiceResult webServiceResult) {
        this(mContext, params, webServiceResult, Constants.SERVICE_TYPE.DEFAULT);
    }

    /*public NetworkManager(Context mContext, OnWebServiceResult webServiceResult, Constants.SERVICE_TYPE type){
        requestQueue = Volley.newRequestQueue(mContext);
        System.out.println("REQUEST : " + requestQueue);

        this.type = type;
        this.onWebServiceResult = webServiceResult;
        connectionDetector=new ConnectionDetector(mContext);
        this.mContext=mContext;
    }*/


    public NetworkManager(Context mContext, Map<String, String> params, OnWebServiceResult webServiceResult, Constants.SERVICE_TYPE type, boolean showDialog) {
        requestQueue = Volley.newRequestQueue(mContext);
        System.out.println("REQUEST : " + type.toString() + " = " + params);
        this.showDialog = showDialog;
        this.parameters = params;
        this.type = type;
        this.onWebServiceResult = webServiceResult;
        connectionDetector = new ConnectionDetector(mContext);
        this.mContext = mContext;
    }

    public NetworkManager(Context mContext, Map<String, String> params, OnWebServiceResult webServiceResult, Constants.SERVICE_TYPE type) {
        requestQueue = Volley.newRequestQueue(mContext);
        System.out.println("REQUEST : " + type.toString() + " = " + params);

        this.parameters = params;
        this.type = type;
        this.onWebServiceResult = webServiceResult;
        connectionDetector = new ConnectionDetector(mContext);
        this.mContext = mContext;
    }

    public void callWebService(String url) {
        if (!connectionDetector.isConnectingToInternet()) {
            Toast.makeText(mContext, mContext.getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            return;
        }
        if (showDialog) {
            dialog = new ProgressDialog(mContext, R.style.MyTheme);
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            dialog.show();
        }

        final StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null) {
                    Toast.makeText(mContext, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                onWebServiceResult.onWebServiceResult(response, type);
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.dismiss();
                System.out.println("Response : " + type.toString() + " = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onWebServiceResult.onWebServiceResult(error.getMessage(), Constants.SERVICE_TYPE.ERROR);
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
}
