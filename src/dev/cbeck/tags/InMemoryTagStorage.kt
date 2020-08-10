package dev.cbeck.tags

import java.lang.IllegalArgumentException
import java.util.concurrent.ConcurrentHashMap


class InMemoryTagStorage : TagStorage {
    private val backing = ConcurrentHashMap<String, ConcurrentHashMap<String, Pair<Long, Boolean>>>()

    override fun modifyTags(
        user: String,
        add: List<String>,
        remove: List<String>,
        opTimestamp: Long
    ): Set<String> {
        val userMap = backing.computeIfAbsent(user) { ConcurrentHashMap() }

        val addableSet = mutableSetOf<String>()
        addableSet.addAll(add)
        addableSet.removeAll(remove)

        addableSet.forEach { tag -> userMap.compute(tag, mapCompute(true, opTimestamp)) }
        remove.forEach { tag -> userMap.compute(tag, mapCompute(false, opTimestamp)) }

        return userMap.filter { it.value.second }.map { it.key }.toSet()
    }

    private fun mapCompute(isAdd: Boolean, opTimestamp: Long): (String, Pair<Long, Boolean>?) -> Pair<Long, Boolean>? {
        return { _: String, existing: Pair<Long, Boolean>? ->
            existing?.let { if (it.first > opTimestamp) it else null } ?: Pair(opTimestamp, isAdd)
        }
    }
}