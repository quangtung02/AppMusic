package app.alone.com.appmusic.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.alone.com.appmusic.main.MainApplication;
import app.alone.com.appmusic.network.api.ApiBase;

/**
 * Created by ducthuan on 1/5/16.
 */
public class TvApi {

    private static final String DATA = "DATA";
    private static final String CODE = "Code";
    private static final String MESSAGE = "Message";


    protected <T> String getListDataObject(String method, final Map<String, String> paras,
                                           final ListObjectRequestListener<T> listener, final Class<T> _class) {
        StringRequest request = new StringRequest(Request.Method.POST, MainApplication.baseUrl + method,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Log.e("response","response:" + response);
                            JSONObject reader = new JSONObject(response);
                            if (reader.getInt(CODE) == 1) {
                                ArrayList<T> listData = new ArrayList<>();
                                JSONArray jsonArray = reader.getJSONArray(DATA);
                                String message = reader.optString(MESSAGE);
                                Gson g = new Gson();
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        listData.add(g.fromJson(jsonArray.getString(i), _class));
                                    }
                                } catch (JSONException e) {
                                    message = e.getMessage();
                                } catch (JsonSyntaxException e) {
                                    message = e.getMessage();
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    message = e.getMessage();
                                    e.printStackTrace();
                                }
                                if (listener != null) {
                                    listener.onFinshRequestListObject(true, message, listData);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onFinshRequestListObject(false, null, null);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paras;
            }
        };
        MainApplication.getsInstance().getRequestQueue().add(request);
        return method;
    }

    public interface ListObjectRequestListener<T> extends
            ApiBase.BaseAPIRequestListener {
        void onFinshRequestListObject(boolean isSuccess, String message,
                                      ArrayList<T> listObject);
    }
}
