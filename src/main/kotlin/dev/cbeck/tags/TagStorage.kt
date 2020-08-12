package dev.cbeck.tags


interface TagStorage {
    fun modifyTags(
        user: String,
        add: List<String>,
        remove: List<String>,
        opTimestamp: Long
    ): Set<String>
}