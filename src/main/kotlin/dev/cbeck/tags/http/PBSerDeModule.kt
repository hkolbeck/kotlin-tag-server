package dev.cbeck.tags.http

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import pbandk.Message
import java.io.IOException


class PBSerDeModule: SimpleModule() {
    init {
        addSerializer(PbandkJsonSerializer())
    }

    internal class PbandkJsonSerializer : StdSerializer<Message<*>>(Message::class.java) {
        @Throws(IOException::class)
        override fun serialize(value: Message<*>, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeRaw(value.jsonMarshal())
        }
    }
}