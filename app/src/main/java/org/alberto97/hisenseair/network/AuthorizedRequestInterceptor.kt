package org.alberto97.hisenseair.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

/**
 * Interceptor used to authorize every request annotated with @Authorize
 */
abstract class AuthorizedRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Retrieve custom annotation: Authorized
        val invocation = original.tag(Invocation::class.java)
        val authenticated = invocation?.method()?.getAnnotation(Authorized::class.java)
        val needsAuthorization = authenticated != null

        val request = if (needsAuthorization)
            authorize(original)
        else
            original

        return chain.proceed(request)
    }

    abstract fun authorize(request: Request): Request
}