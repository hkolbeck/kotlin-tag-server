package dev.cbeck.tags

import com.google.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
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

        fun mapCompute(isAdd: Boolean): (String, Pair<Long, Boolean>?) -> Pair<Long, Boolean>? =
                { _: String, existing: Pair<Long, Boolean>? ->
                    existing?.let { if (it.first > opTimestamp) it else null } ?: Pair(opTimestamp, isAdd)
                }

        addableSet.forEach { tag -> userMap.compute(tag, mapCompute(true)) }
        remove.forEach { tag -> userMap.compute(tag, mapCompute(false)) }

        return userMap.filter { it.value.second }.map { it.key }.toSet()
    }
}