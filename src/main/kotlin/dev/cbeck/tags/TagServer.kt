package dev.cbeck.tags

import com.google.inject.Guice
import com.google.inject.Stage
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import java.lang.RuntimeException

class TagServer : Application<TagConfiguration>() {
    override fun initialize(bootstrap: Bootstrap<TagConfiguration>?) {
        super.initialize(bootstrap)
    }

    override fun getName(): String {
        return "tag-server"
    }

    override fun run(configuration: TagConfiguration?, environment: Environment?) {
//        val conf = configuration ?: throw RuntimeException("Null configuration passed into run()")
        val env = environment ?: throw RuntimeException("Null configuration passed into run()")
        println("in run")
//        val injector = Guice.createInjector(Stage.PRODUCTION)

        env.jersey().register(TagResource(InMemoryTagStorage()))

//        env.lifecycle().manage(InMemoryTagStorage())
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            TagServer().run(*args)
        }
    }
}
