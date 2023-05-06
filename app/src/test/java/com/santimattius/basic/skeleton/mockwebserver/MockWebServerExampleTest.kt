package com.santimattius.basic.skeleton.mockwebserver

import com.santimattius.basic.skeleton.core.networking.RetrofitServiceCreator
import com.santimattius.basic.skeleton.tools.helpers.JsonLoader
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class MockWebServerExampleTest {
    private val jsonLoader = JsonLoader()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: ExampleService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toUri().toString()
        val serviceCreator = RetrofitServiceCreator(baseUrl)
        service = serviceCreator.create<ExampleService>()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPlatform() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonLoader.load("hello.json"))

        mockWebServer.enqueue(response)

        runBlocking {
            val result = service.platform()
            assertThat(result.platform, equalTo("Android"))
        }
    }
}