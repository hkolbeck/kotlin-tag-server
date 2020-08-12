package dev.cbeck.tags

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import pbandk.Message
import java.io.IOException
import java.lang.RuntimeException

class TagServer : Application<TagConfiguration>() {
    override fun initialize(bootstrap: Bootstrap<TagConfiguration>?) {
        bootstrap?.objectMapper?.registerModule(KotlinModule())
        bootstrap?.objectMapper?.registerModule(PBSerDeModule())
    }

    override fun getName(): String {
        return "tag-server"
    }

    override fun run(configuration: TagConfiguration?, environment: Environment?) {
//        val conf = configuration ?: throw RuntimeException("Null configuration passed into run()")
        val env = environment ?: throw RuntimeException("Null configuration passed into run()")
        println("in run")
//        val injector = Guice.createInjector(Stage.PRODUCTION)

        val tagStorage = InMemoryTagStorage()
        env.jersey().register(TagResource(tagStorage))
        env.healthChecks().register("tag-storage", TagStorageHealthcheck(tagStorage))

//        env.lifecycle().manage(InMemoryTagStorage())
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            TagServer().run(*args)
        }
    }
}
