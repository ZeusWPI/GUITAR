package gent.zeus.guitar.votes

import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.ZODOM_API_URL
import org.springframework.http.MediaType
import org.springframework.web.client.body

class VoteFetcher {
    data class VoteCount(val songId: String, val votesFor: Int, val votesAgainst: Int)

    fun getVotes(spotifyId: String): VoteCount {
        return REST_CLIENT.get()
            .uri("${ZODOM_API_URL}/vote_count/$spotifyId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<VoteCount>()
            ?: run {
                Logging.log.error("error fetching votes for ${spotifyId}: response body was null")
                VoteCount(spotifyId, 0, 0)
            }
    }
}