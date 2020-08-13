package dev.cbeck.tags

import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Before
import org.junit.Ignore
import java.net.ServerSocket


@Ignore
abstract class BaseTest {

    private val injector: Injector = Guice.createInjector(TestModule())

    @Before
    fun setUp() {
        injector.injectMembers(this)
    }

    companion object {
        fun getOpenPort(): Int {
            val serverSocket = ServerSocket(0)
            serverSocket.reuseAddress = true
            serverSocket.close()

            return serverSocket.localPort
        }
    }
}