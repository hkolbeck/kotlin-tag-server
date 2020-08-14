package dev.cbeck.tags

import dev.cbeck.tags.pgsql.TagDao
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric as randStr
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import pl.domzal.junit.docker.rule.DockerRule
import pl.domzal.junit.docker.rule.WaitFor.logMessage


class TagDaoTest : BaseTest() {

    private val jdbi: Jdbi
    private val dao: TagDao
    private val tableDestructor: TableDestructor

    init {
        val exposedContainerPort = pgRule.getExposedContainerPort("5432")
        jdbi = Jdbi.create("jdbc:postgresql://localhost:$exposedContainerPort/postgres?user=postgres&password=$pass")
        jdbi.installPlugin(SqlObjectPlugin())

        dao = jdbi.onDemand(TagDao::class.java)
        tableDestructor = jdbi.onDemand(TableDestructor::class.java)
    }

    @Before
    fun cleanTable() {
        tableDestructor.dropUserTable()
        dao.makeTable()
    }

    @Test
    fun testRoundtrip() {
        val user = randStr(10)
        val tag = randStr(15)
        dao.updateTags(listOf(tag), listOf(true), user, System.currentTimeMillis())

        val result = dao.getTags(user)

        assertEquals(setOf(tag), result)
    }

    @Test
    fun testAddThenDelete() {
        val user = randStr(10)
        val tag = randStr(15)
        val firstOpTimestamp = System.currentTimeMillis()
        dao.updateTags(listOf(tag), listOf(true), user, firstOpTimestamp)
        dao.updateTags(listOf(tag), listOf(false), user, firstOpTimestamp + 1)

        val result = dao.getTags(user)
        assertEquals(setOf<String>(), result)
    }

    @Test
    fun testOutOfOrderDelete() {
        val user = randStr(10)
        val tag = randStr(15)
        val firstOpTimestamp = System.currentTimeMillis()
        dao.updateTags(listOf(tag), listOf(true), user, firstOpTimestamp)
        dao.updateTags(listOf(tag), listOf(false), user, firstOpTimestamp - 1)

        val result = dao.getTags(user)
        assertEquals(setOf(tag), result)
    }

    companion object {
        const val pass = "lolsecurity"

        @ClassRule
        @JvmField
        val pgRule: DockerRule = DockerRule.builder()
                .imageName("postgres:latest")
                .env("POSTGRES_PASSWORD", pass)
                .expose("5432")
                .waitFor(logMessage("database system is ready to accept connections"))
                .build()
    }
}