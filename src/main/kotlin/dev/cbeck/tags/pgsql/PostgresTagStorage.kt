package dev.cbeck.tags

import javax.inject.Inject


class PostgresTagStorage @Inject constructor() : TagStorage {
    override fun modifyTags(user: String, add: List<String>, remove: List<String>, opTimestamp: Long): Set<String> {
        TODO("Not yet implemented")
    }

    override fun healthCheck(): String? {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

}