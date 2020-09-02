import android.net.TrafficStats
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.usabilla.sdk.ubform.net.http.UsabillaHttpClient
import com.usabilla.sdk.ubform.net.http.UsabillaHttpListener
import com.usabilla.sdk.ubform.net.http.UsabillaHttpRequest
import com.usabilla.sdk.ubform.net.http.UsabillaHttpResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.jetbrains.annotations.Nullable
import java.io.IOException
import java.util.HashMap

class CustomHttpClient : UsabillaHttpClient {

    private val client = OkHttpClient()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun execute(usabillaRequest: UsabillaHttpRequest, listener: UsabillaHttpListener) {
        val builder = Request.Builder().url(usabillaRequest.url)
        addHeadersToRequest(usabillaRequest, builder)
        addBodyToRequest(usabillaRequest, builder)
        val request = builder.build()
        TrafficStats.setThreadStatsTag(1)
        val callback = OkHttpAsynchronousCallback(listener)
        client.newCall(request).enqueue(callback)
    }

    private fun addHeadersToRequest(usabillaRequest: UsabillaHttpRequest, builder: Request.Builder) {
        usabillaRequest.headers?.let {
            it.keys.forEach { key ->
                it[key]?.let { header -> builder.addHeader(key, header) }
            }
        }
    }

    private fun addBodyToRequest(usabillaRequest: UsabillaHttpRequest,
                                 builder: Request.Builder) {
        if (usabillaRequest.method == "POST" || usabillaRequest.method == "PATCH") {
            val type = MediaType.parse("application/json; charset=utf-8")
            val content = usabillaRequest.body ?: ""
            val body = RequestBody.create(type, content)
            if (usabillaRequest.method == "POST") {
                builder.post(body)
                return
            }
            builder.patch(body)
        }
    }

    internal inner class OkHttpAsynchronousCallback(private val listener: UsabillaHttpListener) : Callback {

        override fun onFailure(call: Call, e: IOException) {
            val customResponse = CustomUsabillaHttpResponse(e)
            mainHandler.post { listener.onFailure(customResponse) }
        }

        override fun onResponse(call: Call, response: Response) {
            val customResponse = CustomUsabillaHttpResponse(response)
            mainHandler.post {
                if (response.isSuccessful) {
                    listener.onSuccess(customResponse)
                } else {
                    listener.onFailure(customResponse)
                }
            }
        }
    }

    internal class CustomUsabillaHttpResponse : UsabillaHttpResponse {
        @get:Nullable
        override var body: String? = null
            private set

        @get:Nullable
        override var error: String? = null
            private set

        @get:Nullable
        override var headers: MutableMap<String, String>? = null
            private set

        @get:Nullable
        override var statusCode: Int? = null
            private set

        constructor(e: IOException?) {
            init(null, e)
        }

        constructor(response: Response?) {
            init(response, null)
        }

        private fun init(response: Response?, e: IOException?) {
            response?.let { res ->
                try {
                    res.body()?.let {
                        body = it.string()
                        it.close()
                    }
                } catch (e1: IOException) {
                    Log.i("CustomHttpClient", "Custom http client response failed to read body with exception: " + e1.localizedMessage)
                }
                error = res.message()
                val tempHeaders = HashMap<String, String>()
                val responseHeaders = res.headers()
                for (i in 0 until responseHeaders.size()) {
                    tempHeaders[responseHeaders.name(i)] = responseHeaders.value(i)
                }
                headers = tempHeaders
                statusCode = response.code()
                return
            }
            error = e?.localizedMessage ?: "Failure custom http client. Response is null"
        }
    }
}
