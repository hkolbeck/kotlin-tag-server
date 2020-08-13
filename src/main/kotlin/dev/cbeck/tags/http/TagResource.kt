package dev.cbeck.tags.http;

import dev.cbeck.proto.Request
import dev.cbeck.proto.Response
import dev.cbeck.tags.TagStorage
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.*

import javax.ws.rs.core.MediaType

@Singleton
@Path("/api")
class TagResource @Inject constructor (private var tagStorage: TagStorage) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tags")
    fun serve(
            request: Request
    ): Response {
        if (request.timestamp <= 0 && request.add.size + request.remove.size > 0) {
            throw BadRequestException("'timestamp' must be provided and greater than 0 if any tags are being added or removed")
        }

        if (request.user == "") {
            throw BadRequestException("'user' must be provided and not empty")
        }

        val resultTags = tagStorage.modifyTags(request.user, request.add, request.remove, request.timestamp)
        return Response(resultTags.toList())
    }
}
