package dev.cbeck.tags.pgsql

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate


interface TagDao {

    @SqlUpdate("""
        CREATE TABLE IF NOT EXISTS user_tags (
            user_name VARCHAR,
            tag VARCHAR, 
            present BOOLEAN,
            ts BIGINT,
            
            PRIMARY KEY (user_name, tag)
        )
    """)
    fun makeTable()

    @SqlQuery("""
        SELECT tag
        FROM user_tags
        WHERE user_name = :user_name
        AND present
    """)
    fun getTags(
            @Bind("user_name") user: String
    ): Set<String>

    @SqlBatch("""
        INSERT INTO user_tags (user_name, tag, present, ts)
        VALUES (:user_name, :tag, :present, :timestamp)
        ON CONFLICT (user_name, tag) WHERE :timestamp >= ts
        DO UPDATE SET present = :present AND ts = :timestamp
    """)
    fun updateTags(
            @Bind("tag") tags: List<String>,
            @Bind("present") ops: Iterator<Boolean>,
            @Bind("user_name") user: String,
            @Bind("timestamp") timestamp: Long
    ): Set<String>

}