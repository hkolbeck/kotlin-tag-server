package dev.cbeck.tags

import com.google.inject.AbstractModule
import dev.cbeck.tags.pgsql.PostgresTagStorage
import dev.cbeck.tags.pgsql.TagDao
import org.jdbi.v3.core.Jdbi


class StorageModule(val jdbi: Jdbi) : AbstractModule() {
    override fun configure() {
        bind(TagDao::class.java).toInstance(jdbi.onDemand(TagDao::class.java))
        bind(TagStorage::class.java).to(PostgresTagStorage::class.java)
    }
}