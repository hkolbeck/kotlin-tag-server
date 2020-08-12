package dev.cbeck.tags

import com.codahale.metrics.health.HealthCheck


class TagStorageHealthcheck(val tagStorage: TagStorage) : HealthCheck() {
    override fun check(): Result = tagStorage.healthCheck()?.let { Result.unhealthy(it) } ?: Result.healthy()
}