package org.alberto97.hisenseair.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

interface RequestInterceptor {
    fun interceptRequest(builder: Request.Builder, needsAuthorization: Boolean)
}

abstract class OkHttpAppInterceptor : Interceptor, RequestInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Retrieve custom annotation: Authenticated
        val invocation = original.tag(Invocation::class.java)
        val authenticated = invocation?.method()?.getAnnotation(Authorized::class.java)
        val needsAuthorization = authenticated != null

        val requestBuilder = original.newBuilder()

        interceptRequest(requestBuilder, needsAuthorization)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}