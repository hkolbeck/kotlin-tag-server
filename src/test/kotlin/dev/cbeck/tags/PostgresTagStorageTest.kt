package dev.cbeck.tags

import com.nhaarman.mockitokotlin2.*
import dev.cbeck.tags.pgsql.PostgresTagStorage
import dev.cbeck.tags.pgsql.TagDao
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric as randStr


class PostgresTagStorageTest : BaseTest() {

    @Inject
    lateinit var tagDao: TagDao

    @Inject
    lateinit var postgresTagStorage: PostgresTagStorage

    private var opResult: Set<String> = setOf()

    @Before
    fun before() {
        reset(tagDao)

        opResult = setOf(RandomStringUtils.randomAlphanumeric(10))

        whenever(tagDao.getTags(any())).thenReturn(opResult)
    }

    @Test
    fun testNoOps() {
        val user = randStr(10)
        val result = postgresTagStorage.modifyTags(user, listOf(), listOf(), System.currentTimeMillis())

        assertEquals(opResult, result)
        verify(tagDao, times(1)).getTags(eq(user))
        verifyNoMoreInteractions(tagDao)
    }

    @Test
    fun testOps() {
        val user = randStr(10)
        val add = listOf(randStr(10))
        val remove = listOf(randStr(10))
        val opTimestamp = System.currentTimeMillis()
        val result = postgresTagStorage.modifyTags(user, add, remove, opTimestamp)

        assertEquals(opResult, result)
        verify(tagDao, times(1)).updateTags(eq(add + remove), eq(listOf(true, false)), eq(user), eq(opTimestamp))
        verify(tagDao, times(1)).getTags(eq(user))
        verifyNoMoreInteractions(tagDao)
    }
}