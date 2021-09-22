import android.net.TrafficStats
import android.os.Handler
import android.os.Looper
import com.usabilla.sdk.ubform.net.http.UsabillaHttpClient
import com.usabilla.sdk.ubform.net.http.UsabillaHttpListener
import com.usabilla.sdk.ubform.net.http.UsabillaHttpRequest
import com.usabilla.sdk.ubform.net.http.UsabillaHttpResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.HashMap

class CustomHttpClient : UsabillaHttpClient {

    private val client = OkHttpClient()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun execute(request: UsabillaHttpRequest, listener: UsabillaHttpListener) {
        val builder = Request.Builder().url(request.url)
        addHeadersToRequest(request, builder)
        addBodyToRequest(request, builder)
        TrafficStats.setThreadStatsTag(1)
        val callback = OkHttpAsynchronousCallback(listener)
        client.newCall(builder.build()).enqueue(callback)
    }

    private fun addHeadersToRequest(request: UsabillaHttpRequest, builder: Request.Builder) {
        request.headers?.let {
            it.keys.forEach { key ->
                it[key]?.let { header -> builder.addHeader(key, header) }
            }
        }
    }

    private fun addBodyToRequest(request: UsabillaHttpRequest, builder: Request.Builder) {
        if (request.method == "POST" || request.method == "PATCH") {
            val content = request.body ?: "application/json; charset=utf-8"
            val body = content.toRequestBody()
            if (request.method == "POST") {
                builder.post(body)
                return
            }
            builder.patch(body)
        }
    }

    inner class OkHttpAsynchronousCallback(private val listener: UsabillaHttpListener) : Callback {

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

    internal class CustomUsabillaHttpResponse(response: Response?, e: IOException?) :
        UsabillaHttpResponse {
        override var statusCode: Int? = null
        override var headers: Map<String, String>? = null
        override var body: String? = null
        override var error: String? = null

        init {
            run {
                response?.let { res ->
                    res.body?.use {
                        body = it.string()
                    }
                    error = res.message
                    val tempHeaders = HashMap<String, String>()
                    val responseHeaders = res.headers
                    for (i in 0 until responseHeaders.size) {
                        tempHeaders[responseHeaders.name(i)] = responseHeaders.value(i)
                    }
                    headers = tempHeaders
                    statusCode = response.code
                    return@run
                }
                error = e?.localizedMessage ?: "Failure custom http client. Response is null"
            }
        }
    }
}
