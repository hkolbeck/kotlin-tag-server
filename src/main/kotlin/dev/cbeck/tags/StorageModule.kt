package dev.cbeck.tags

import com.google.inject.AbstractModule


class StorageModule : AbstractModule() {
    override fun configure() {
        bind(TagStorage::class.java).to(InMemoryTagStorage::class.java)
    }
}