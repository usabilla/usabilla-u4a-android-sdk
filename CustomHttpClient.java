import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.usabilla.sdk.ubform.net.http.UsabillaHttpClient;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpListener;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpRequest;
import com.usabilla.sdk.ubform.net.http.UsabillaHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
* This class is meant as a crude example of a custom http client that can be passed to 
* our SDK in case you would like to handle the connectivity on your own.
*
* It's based on the library OkHttp and it needs to extend from our interface UsabillaHttpClient
* which requires to implement the method execute(UsabillaHttpRequest usabillaRequest, UsabillaHttpListener listener)
*
* The parameters passed in the signature are:
* - usabillaRequest: The network request created from our SDK that you need to map to your client for execution.
* - listener: The listener to be triggered in case the request succeeds or not
*/

public class CustomHttpClient implements UsabillaHttpClient {

    private OkHttpClient client = new OkHttpClient();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

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
                final String header = usabillaRequest.getHeaders().get(key);
                if (header != null) {
                    builder.addHeader(key, header);
                }
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
        public void onFailure(@NonNull Call call, @NonNull final IOException e) {
            final CustomUsabillaHttpResponse customResponse = new CustomUsabillaHttpResponse(e);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFailure(customResponse);
                }
            });
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) {
            final CustomUsabillaHttpResponse customResponse = new CustomUsabillaHttpResponse(response);
            mainHandler.post(new Runnable() {
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

    class CustomUsabillaHttpResponse implements UsabillaHttpResponse {
        private String body;
        private String error;
        private Map<String, String> headers;
        private Integer statusCode;

        CustomUsabillaHttpResponse(IOException e) {
            init(null, e);
        }

        CustomUsabillaHttpResponse(Response response) {
            init(response, null);
        }

        private void init(Response response, IOException e) {
            if (response != null) {
                try {
                    final ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        body = responseBody.string();
                        responseBody.close();
                    }
                } catch (IOException e1) {
                    Log.i("CustomHttpClient", "Custom http client response failed to read body with exception: " + e1.getLocalizedMessage());
                }
                error = response.message();
                headers = new HashMap<>();
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    headers.put(responseHeaders.name(i), responseHeaders.value(i));
                }
                statusCode = response.code();
            } else if (e != null) {
                error = e.getLocalizedMessage();
            }
        }

        @Nullable
        @Override
        public String getBody() {
            return body;
        }

        @Nullable
        @Override
        public String getError() {
            return error;
        }

        @Nullable
        @Override
        public Map<String, String> getHeaders() {
            return headers;
        }

        @Nullable
        @Override
        public Integer getStatusCode() {
            return statusCode;
        }
    }
}
