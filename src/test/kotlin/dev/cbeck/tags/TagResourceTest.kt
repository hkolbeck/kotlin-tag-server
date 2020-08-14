package dev.cbeck.tags

import com.nhaarman.mockitokotlin2.*
import dev.cbeck.proto.Request
import dev.cbeck.tags.http.TagResource
import org.junit.Assert.assertEquals
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric as randStr
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.ws.rs.BadRequestException


class TagResourceTest : BaseTest() {

    @Inject
    lateinit var tagStorage: TagStorage

    @Inject
    lateinit var tagResource: TagResource

    private var opResult: Set<String> = setOf()

    @Before
    fun before() {
        reset(tagStorage)

        opResult = setOf(randStr(10))

        whenever(tagStorage.modifyTags(any(), any(), any(), any()))
                .thenReturn(opResult)
    }

    @Test
    fun testAllFieldsPresent() {
        val req = Request(
                user = randStr(10),
                timestamp = System.currentTimeMillis(),
                add = listOf(randStr(10)),
                remove = listOf(randStr(10))
        )

        val resp = tagResource.serve(req)

        verify(tagStorage, times(1)).modifyTags(eq(req.user), eq(req.add), eq(req.remove), eq(req.timestamp))
        assertEquals(opResult, resp.tags.toSet())
    }

    @Test(expected = BadRequestException::class)
    fun testMissingTimestamp() {
        val req = Request(
                user = randStr(10),
                add = listOf(randStr(10)),
                remove = listOf(randStr(10))
        )

        tagResource.serve(req)
    }

    @Test
    fun testMissingTimestampNoOps() {
        val req = Request(
                user = randStr(10)
        )

        val resp = tagResource.serve(req)

        verify(tagStorage, times(1)).modifyTags(eq(req.user), eq(listOf()), eq(listOf()), any())
        assertEquals(opResult, resp.tags.toSet())
    }

    @Test(expected = BadRequestException::class)
    fun testMissingUser() {
        val req = Request(
                timestamp = System.currentTimeMillis(),
                add = listOf(randStr(10)),
                remove = listOf(randStr(10))
        )

        tagResource.serve(req)
    }
}