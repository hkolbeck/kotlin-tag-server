package dev.cbeck.tags.http;

import dev.cbeck.proto.Request
import dev.cbeck.proto.Response
import dev.cbeck.tags.TagStorage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.*

import javax.ws.rs.core.MediaType

@Singleton
@Path("/api")
class TagResource @Inject constructor(private val tagStorage: TagStorage) {

    @Operation(
            summary = "Add, remove, and fetch tags for a user",
            description = "Accepts a user, timestamp, a set of tags to add, a set of tags to remove. Performs the " +
                    "requested operations and returns the resulting tags on the user.",
            responses = [
                ApiResponse(
                        description = "The resulting tags",
                        content = [Content(schema = Schema(implementation = Response::class))]
                ),
                ApiResponse(responseCode = "400", description = "Missing or empty user, or invalid timestamp")
            ]
    )
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
