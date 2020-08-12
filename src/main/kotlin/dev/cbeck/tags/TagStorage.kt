package dev.cbeck.tags

import io.dropwizard.lifecycle.Managed


interface TagStorage : Managed {
    fun modifyTags(
        user: String,
        add: List<String>,
        remove: List<String>,
        opTimestamp: Long
    ): Set<String>

    fun healthCheck() : String?
}