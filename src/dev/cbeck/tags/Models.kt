package dev.cbeck.tags

data class Request(val user: String, val addTags: List<String>, val rmTags: List<String>)

data class Response(val tags: List<String>)
