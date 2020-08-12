package dev.cbeck.tags.pgsql

import dev.cbeck.tags.TagStorage
import javax.inject.Inject


class PostgresTagStorage @Inject constructor(var tagDao: TagDao) : TagStorage {
    override fun modifyTags(user: String, add: List<String>, remove: List<String>, opTimestamp: Long): Set<String> {
        val tags = mutableListOf<String>()
        val ops = mutableListOf<Boolean>()

        add.forEach {
            tags.add(it)
            ops.add(true)
        }

        remove.forEach {
            tags.add(it)
            ops.add(false)
        }

        tagDao.updateTags(tags, ops.iterator(), user, opTimestamp)

        return tagDao.getTags(user)
    }
}