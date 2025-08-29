package gent.zeus.guitar.storage

import gent.zeus.guitar.data.Track
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import java.sql.DriverManager


private val dbConnection = DriverManager.getConnection(
    System.getenv("DB_URL") + "?createDatabaseIfNotExist=true",
    "guitar",
    System.getenv("DB_PASSWORD"),
)


internal object Database {
    object TrackDao {
        val storeStatement = dbConnection.prepareStatement(
            """
            INSERT INTO track
        """.trimIndent()
        )

        fun store(track: Track) {

        }
    }
}


fun initializeDatabase() {
    ScriptUtils.executeSqlScript(dbConnection, ClassPathResource("sqlScripts/init.sql"))
}

