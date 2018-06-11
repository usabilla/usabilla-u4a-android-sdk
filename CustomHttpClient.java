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
    private WeakReference<Activity> activityReference;

    CustomHttpClient(Activity activity) {
        activityReference = new WeakReference(activity);
    }

    @Override
    public void execute(@NotNull UsabillaHttpRequest usabillaRequest, @NotNull UsabillaHttpListener listener) {
        final Request.Builder builder = new Request.Builder().url(usabillaRequest.getUrl());
        addHeadersToRequest(usabillaRequest, builder);
        addBodyToRequest(usabillaRequest, builder);
        TrafficStats.setThreadStatsTag(1);

        final Request request = builder.build();
        final OkHttpAsynchronousCallback callback = new OkHttpAsynchronousCallback(listener);
        client.newCall(request).enqueue(callback);
    }

    private void addHeadersToRequest(@NotNull UsabillaHttpRequest usabillaRequest,
                                     @NotNull Request.Builder builder) {
        if (usabillaRequest.getHeaders() != null) {
            for (String key : usabillaRequest.getHeaders().keySet()) {
                builder.addHeader(key, usabillaRequest.getHeaders().get(key));
            }
        }
    }

    private void addBodyToRequest(@NotNull UsabillaHttpRequest usabillaRequest,
                                  @NotNull Request.Builder builder) {
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

        OkHttpAsynchronousCallback(@NotNull UsabillaHttpListener listener) {
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            final UsabillaHttpResponse customResponse = new UsabillaHttpResponse() {
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
            };
            activityReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        listener.onFailure(customResponse);
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
                        Log.i("CustomHttpClient", "Custom http client response failed to read body with exception: " + e.getLocalizedMessage());
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
