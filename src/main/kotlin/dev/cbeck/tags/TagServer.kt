package dev.cbeck.tags

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Guice
import com.google.inject.Stage
import dev.cbeck.tags.http.PBSerDeModule
import dev.cbeck.tags.http.TagResource
import io.dropwizard.Application
import io.dropwizard.jdbi3.JdbiFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import java.lang.RuntimeException

class TagServer : Application<TagConfiguration>() {
    override fun initialize(bootstrap: Bootstrap<TagConfiguration>) {
        bootstrap.objectMapper?.registerModule(KotlinModule())
        bootstrap.objectMapper?.registerModule(PBSerDeModule())
    }

    override fun getName(): String = "tag-server"

    override fun run(conf: TagConfiguration, env: Environment) {
        val jdbiFactory = JdbiFactory()
        val jdbi = jdbiFactory.build(env, conf.dataSourceFactory, "postgresql")

        val injector = Guice.createInjector(Stage.PRODUCTION, StorageModule(jdbi))

        env.jersey().register(injector.getInstance(TagResource::class.java))
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            TagServer().run(*args)
        }
    }
}
