package com.example.openweatherapitest.network

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import com.example.openweatherapitest.BuildConfig
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import java.lang.IllegalStateException
import java.lang.reflect.Type

class CentralRequestManager {
    companion object {

        private var requestQueue: RequestQueue = RequestQueue(
            NoCache(),
            BasicNetwork(HurlStack())
        ).apply { start() }

        fun <T: Any> fetchObject(baseUrl: String, params: Map<String, String>?, responseReturnType: Class<T>, requestTag: String): Single<T> {
            return Single.create { emitter ->
                val listener = VolleyResultListener(emitter)
                val request = VolleyRequest(
                    responseConcreteType = responseReturnType,
                    method = Request.Method.POST,
                    url = parseUrl(baseUrl, params),
                    listener = listener,
                ).apply {
                    retryPolicy = DefaultRetryPolicy(0, 0, 0f)
                    this.tag = requestTag
                }

                requestQueue.add(request)
            }
        }

        private fun parseUrl(baseUrl: String, params: Map<String, String>?): String {
            return baseUrl.plus(
                params
                    ?.takeIf {
                        it.isNotEmpty()
                    }
                    ?.let {
                        "?" + it.entries
                            .joinToString("&") { entry -> "${entry.key}=${entry.value}" }
                    }
                    .orEmpty()
            )
        }

        fun clearRequestQueue(){
            this.requestQueue.cancelAll{ true }
        }

        fun clearRequestQueue(requestTag: String){
            this.requestQueue.cancelAll{ it.tag == requestTag }
        }
    }



}

private class VolleyRequest<T : Any>(
    private val responseConcreteType: Type,
    method: Int,
    url: String,
    val listener: VolleyResultListener<T>
): Request<T>(
    method,
    url,
    listener
) {
    private val TAG = VolleyRequest::class.java.simpleName

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        val responseString = response?.let { String(it.data) }
        if (BuildConfig.DEBUG) Log.d(TAG, "parseNetworkResponse: response => $responseString")
        val parsedObject = Gson().fromJson<T>(responseString, this.responseConcreteType)
        if (BuildConfig.DEBUG) Log.d(TAG, "parseNetworkResponse: parsing SUCCESS")
        return Response.success(parsedObject, null)
    }

    override fun deliverResponse(response: T) {
        if (BuildConfig.DEBUG) Log.d(TAG, "deliverResponse")
        this.listener.onResponse(response)
    }

    override fun deliverError(error: VolleyError?) {
        if (BuildConfig.DEBUG) Log.d(TAG, "deliverError")
        this.listener.onErrorResponse(error)
    }

}

private class VolleyResultListener<T : Any>(private val emitter: SingleEmitter<T>): Response.Listener<T>, Response.ErrorListener {

    private val TAG = VolleyResultListener::class.java.simpleName

    override fun onResponse(response: T) {
        if (BuildConfig.DEBUG) Log.d(TAG, "deliverError")
        if (!emitter.isDisposed) {
            emitter.onSuccess(response)
        }
    }

    override fun onErrorResponse(error: VolleyError?) {
        if (!emitter.isDisposed) {
            if (BuildConfig.DEBUG) Log.d(TAG, "onErrorResponse - sent")
            emitter.onError(error ?: IllegalStateException("API call failed and VolleyError is null"))
        } else {
            if (BuildConfig.DEBUG) Log.e(TAG, "onErrorResponse - not sent sent (emitter disposed)")
        }
    }

}
