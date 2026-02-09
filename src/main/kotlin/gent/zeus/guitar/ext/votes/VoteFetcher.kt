package gent.zeus.guitar.ext.votes

import gent.zeus.guitar.Environment
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.logger
import org.springframework.http.MediaType
import org.springframework.web.client.body


class VoteFetcher {  // TODO extend model filler interface
    data class VoteCount(val songId: String, val votesFor: Int, val votesAgainst: Int)

    fun getVotes(spotifyId: String): VoteCount {
        return REST_CLIENT.get()
            .uri("${Environment.ZODOM_API_URL}/vote_count/$spotifyId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<VoteCount>()
            ?: run {
                logger.error("error fetching votes for ${spotifyId}: response body was null")
                VoteCount(spotifyId, 0, 0)
            }
    }
}