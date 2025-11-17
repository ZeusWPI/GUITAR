package gent.zeus.guitar.votes

import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult
import gent.zeus.guitar.logger
import org.springframework.http.MediaType
import org.springframework.web.client.body


internal object VotesEnv : StartupCheck {
    val ZODOM_API_URL: String? = System.getenv("ZODOM_API_URL")

    override fun checkOnStartup(): StartupCheckResult {
        return StartupCheckResult(
            ZODOM_API_URL != null,
            "ZODOM_API_URL environment variable not set!"
        )
    }
}


class VoteFetcher {
    data class VoteCount(val songId: String, val votesFor: Int, val votesAgainst: Int)

    fun getVotes(spotifyId: String): VoteCount {
        return REST_CLIENT.get()
            .uri("${VotesEnv.ZODOM_API_URL}/vote_count/$spotifyId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<VoteCount>()
            ?: run {
                logger.error("error fetching votes for ${spotifyId}: response body was null")
                VoteCount(spotifyId, 0, 0)
            }
    }
}