package dev.cbeck.tags

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import javax.validation.Valid


class TagConfiguration() : Configuration() {

    @Valid
    var dataSourceFactory: DataSourceFactory = DataSourceFactory()

}