package com.santimattius.basic.skeleton.core.networking

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleInterceptorTest {

    private val interceptor = ExampleInterceptor()

    @Test
    fun `intercept adds Custom header to the request`() {
        val request = Request.Builder().url("https://www.example.com/api/platform").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()

        val requestSlot = slot<Request>()
        val chain = mockk<Interceptor.Chain> {
            every { this@mockk.request() } returns request
            every { proceed(capture(requestSlot)) } returns response
        }

        interceptor.intercept(chain)

        assertEquals("Say Hello", requestSlot.captured.header("Custom"))
        verify(exactly = 1) { chain.proceed(any()) }
    }

    @Test
    fun `intercept preserves the original request url`() {
        val request = Request.Builder().url("https://www.example.com/api/platform").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()

        val requestSlot = slot<Request>()
        val chain = mockk<Interceptor.Chain> {
            every { this@mockk.request() } returns request
            every { proceed(capture(requestSlot)) } returns response
        }

        interceptor.intercept(chain)

        assertEquals(request.url, requestSlot.captured.url)
    }
}
