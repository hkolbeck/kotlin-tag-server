package dev.cbeck.tags;

import dev.cbeck.proto.Request
import dev.cbeck.proto.Response
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.Consumes

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces
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
        val resultTags = tagStorage.modifyTags(request.user, request.add, request.remove, request.timestamp)
        return Response(resultTags.toList())
    }
}
