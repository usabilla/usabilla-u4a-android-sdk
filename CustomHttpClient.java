import android.app.Activity;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.security.ProviderInstaller;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpClient;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpListener;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpRequest;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;

/**
 * This class is meant as a crude example of a custom http client that can be passed to
 * our SDK in case you would like to handle the connectivity on your own.
 *
 * It's based on the library OkHttp and it needs to extend from our interface UsabillaHttpClient
 * which requires to implement the method execute(UsabillaHttpRequest usabillaRequest, UsabillaHttpListener listener)
 */
public class CustomHttpClient implements UsabillaHttpClient {

    private OkHttpClient client;
    private final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.RESTRICTED_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();
    private final OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
            .connectionSpecs(Collections.singletonList(spec));

    private WeakReference<Activity> activityReference;

    CustomHttpClient(Activity activity) {
        activityReference = new WeakReference(activity);

        /**
         * This is only required for phones running Android API 19 since they do not have TLS1.2
         * enabled by default, and our SDK has it as a requirement to perform network connections
         */
        ProviderInstaller.installIfNeededAsync(activity.getBaseContext(), new ProviderInstaller.ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {
                try {
                    builder.sslSocketFactory(new TlsSocketFactory());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client = builder.build();
            }

            @Override
            public void onProviderInstallFailed(int i, Intent intent) {
            }
        });
    }

    @Override
    public void execute(@NonNull UsabillaHttpRequest usabillaRequest, @NonNull UsabillaHttpListener listener) {
        final Request.Builder builder = new Request.Builder().url(usabillaRequest.getUrl());
        addHeadersToRequest(usabillaRequest, builder);
        addBodyToRequest(usabillaRequest, builder);
        final Request request = builder.build();

        TrafficStats.setThreadStatsTag(1);
        final OkHttpAsynchronousCallback callback = new OkHttpAsynchronousCallback(listener);
        client.newCall(request).enqueue(callback);
    }

    private void addHeadersToRequest(@NonNull UsabillaHttpRequest usabillaRequest,
                                     @NonNull Request.Builder builder) {
        if (usabillaRequest.getHeaders() != null) {
            for (String key : usabillaRequest.getHeaders().keySet()) {
                builder.addHeader(key, usabillaRequest.getHeaders().get(key));
            }
        }
    }

    private void addBodyToRequest(@NonNull UsabillaHttpRequest usabillaRequest,
                                  @NonNull Request.Builder builder) {
        if (usabillaRequest.getMethod().equals("POST") || usabillaRequest.getMethod().equals("PATCH")) {
            final MediaType type = MediaType.parse("application/json; charset=utf-8");
            String content = "";
            if (usabillaRequest.getBody() != null) {
                content = usabillaRequest.getBody();
            }
            final RequestBody body = RequestBody.create(type, content);
            if (usabillaRequest.getMethod().equals("POST")) {
                builder.post(body);
                return;
            }
            builder.patch(body);
        }
    }

    class OkHttpAsynchronousCallback implements Callback {

        private UsabillaHttpListener listener;

        OkHttpAsynchronousCallback(@NonNull UsabillaHttpListener listener) {
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            activityReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onFailure(new UsabillaHttpResponse() {
                        @Nullable
                        @Override
                        public Integer getStatusCode() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public Map<String, String> getHeaders() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String getBody() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String getError() {
                            return e.getLocalizedMessage();
                        }
                    });
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) {
            final UsabillaHttpResponse customResponse = new UsabillaHttpResponse() {
                @Nullable
                @Override
                public Integer getStatusCode() {
                    return response.code();
                }

                @Nullable
                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();
                    final Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        headers.put(responseHeaders.name(i), responseHeaders.value(i));
                    }
                    return headers;
                }

                @Nullable
                @Override
                public String getBody() {
                    try {
                        String returnValue = "";
                        final ResponseBody body = response.body();
                        if (body != null) {
                            returnValue = body.string();
                            body.close();
                        }
                        return returnValue;
                    } catch (IOException e) {
                        Log.i("CustomHttpClient", "Response in the custom HttpClient (status 200) failed to read body with exception: " + e.getLocalizedMessage());
                    }
                    return null;
                }

                @Nullable
                @Override
                public String getError() {
                    return response.message();
                }
            };

            activityReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful()) {
                        listener.onSuccess(customResponse);
                    } else {
                        listener.onFailure(customResponse);
                    }
                }
            });
        }
    }
}
