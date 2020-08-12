package dev.cbeck.tags

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Guice
import com.google.inject.Stage
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
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
        val conf = configuration ?: throw RuntimeException("Null configuration passed into run()")
        val env = environment ?: throw RuntimeException("Null configuration passed into run()")

        val injector = Guice.createInjector(Stage.PRODUCTION, StorageModule())

        env.jersey().register(injector.getInstance(TagResource::class.java))
        env.healthChecks().register("tag-storage", injector.getInstance(TagStorageHealthcheck::class.java))

        env.lifecycle().manage(InMemoryTagStorage())
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            TagServer().run(*args)
        }
    }
}
