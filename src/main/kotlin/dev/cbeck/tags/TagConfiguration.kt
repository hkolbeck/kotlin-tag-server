package dev.cbeck.tags

import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty


class TagConfiguration() : Configuration() {

    @NotEmpty
    var garbage: String = ""
}