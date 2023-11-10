package com.aceventura.voicerecoder.Retrofit.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;
    private final Map<String, String> mParams;

    private static final String BOUNDARY = "----------VolleyUploadBoundary";

    public MultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
        this.mParams = new HashMap<>();
        mHeaders.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    }

    public void addHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    public void addJsonPart(String key, JSONObject value) {
        mParams.put(key, value.toString());
    }

    public void addFilePart(String key, File file) {
        mParams.put(key, file.getAbsolutePath());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            dos.writeBytes("--" + BOUNDARY + "--" + "\r\n");
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String key, String value) throws IOException {
        dataOutputStream.writeBytes("--" + BOUNDARY + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n");
        dataOutputStream.writeBytes("\r\n");
        dataOutputStream.writeBytes(value + "\r\n");
    }
}