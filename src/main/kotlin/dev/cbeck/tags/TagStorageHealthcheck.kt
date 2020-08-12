package dev.cbeck.tags

import com.codahale.metrics.health.HealthCheck
import javax.inject.Inject


class TagStorageHealthcheck @Inject constructor (var tagStorage: TagStorage) : HealthCheck() {
    override fun check(): Result = tagStorage.healthCheck()?.let { Result.unhealthy(it) } ?: Result.healthy()
}