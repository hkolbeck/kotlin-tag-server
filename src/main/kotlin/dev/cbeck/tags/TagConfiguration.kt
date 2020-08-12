package dev.cbeck.tags

import io.dropwizard.Configuration
import javax.validation.constraints.NotEmpty


class TagConfiguration() : Configuration() {

    @NotEmpty
    var garbage: String = ""
}