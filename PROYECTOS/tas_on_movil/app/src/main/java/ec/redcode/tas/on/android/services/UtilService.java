package ec.redcode.tas.on.android.services;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import ec.redcode.tas.on.android.Constants;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by mauchilan on 3/20/18.
 */

public class UtilService {

    public static final MediaType TEXT_PLAIN = MediaType.parse("text; charset=utf-8");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Integer READ_TIME_OUT = 80;
    private static final Integer WRITE_TIME_OUT = 30;
    /**
     * Set your max limit here= 30;
     **/
    private static final Integer MAX_RETRY_INTENT = 2;
    private static final TimeUnit UNIT_TIME = TimeUnit.SECONDS;
    private OkHttpClient client = getBuilder();

    /**
     * @param url
     * @return
     * @throws IOException
     */
    protected String get(String url) throws IOException {
        Log.d("ENLACE  ",""+url);
        Request request = new Request.Builder()
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BEARER + " " + Constants.user.getToken())
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @param token
     * @return
     * @throws IOException
     */
    protected String get(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BEARER + " " + token)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @param requestType
     * @param token
     * @return
     * @throws IOException
     */
    protected String get(String url, String requestType, String token) throws IOException {
        Request request = new Request.Builder()
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BEARER + " " + token)
                .url(url + requestType)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    protected String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BEARER + " " + Constants.user.getToken())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    protected String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BEARER + " " + Constants.user.getToken())
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected String postNoAuth(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @return
     * @throws IOException
     */
    protected String getBasic(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BASIC + " " + Constants.HEADER_PUBLIC)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @return
     * @throws IOException
     */
    protected String postBasic(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_BASIC + " " + Constants.HEADER_PUBLIC)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public OkHttpClient getBuilder() {
        final Builder b = new Builder();
        b.connectTimeout(READ_TIME_OUT, UNIT_TIME);
        b.readTimeout(READ_TIME_OUT, UNIT_TIME);
        b.writeTimeout(WRITE_TIME_OUT, UNIT_TIME);
        b.retryOnConnectionFailure(true);
        b.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                // try the request
                Response response = chain.proceed(request);

                int tryCount = 0;

                String body = getBufferBody(response);

                if (!response.isSuccessful()) {
                    if (!body.contains("message") || !body.contains("developerMessage")) {
                        while (tryCount < MAX_RETRY_INTENT) {
                            tryCount++;
                            Log.w("Intercept", " Request failed - " + tryCount);

                            incrementConecction(b);

                            // retry the request
                            response = chain.proceed(request);
                        }
                    }
                }
                // otherwise just pass the original response on
                return response;
            }
        });
        return b.build();
    }

    private String getBufferBody(Response response) throws IOException {
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }

    private void incrementConecction(Builder b) {
        int rto = READ_TIME_OUT;
        int wto = READ_TIME_OUT;

        rto = (int) (rto + (b.build().readTimeoutMillis() * 0.001));
        wto = (int) (wto + (b.build().writeTimeoutMillis() * 0.001));
        b.connectTimeout(rto, UNIT_TIME);
        b.readTimeout(rto, UNIT_TIME);
        b.writeTimeout(wto, UNIT_TIME);
        Log.i("ADD TIME:", "");
        Log.i("RTO: ", String.valueOf(rto));
        Log.i("WTO: ", String.valueOf(wto));
    }
}
