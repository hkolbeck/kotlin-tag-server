package dev.cbeck.tags

import org.jdbi.v3.sqlobject.statement.SqlUpdate


interface TableDestructor {
    @SqlUpdate("""
        DROP TABLE IF EXISTS user_tags;
    """)
    fun dropUserTable()
}