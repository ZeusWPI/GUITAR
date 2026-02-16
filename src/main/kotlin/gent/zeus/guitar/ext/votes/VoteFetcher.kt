package gent.zeus.guitar.ext.votes

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.Environment
import gent.zeus.guitar.HttpResponse
import gent.zeus.guitar.ServerError
import gent.zeus.guitar.data.Track
import gent.zeus.guitar.ext.ModelFiller
import gent.zeus.guitar.httpRequestIntoObj
import org.springframework.web.client.body


class VoteFetcher : ModelFiller<Track> {
    /**
     * json object for ZODOM api
     */
    data class VoteCount(val songId: String, val votesFor: Int, val votesAgainst: Int)

    override fun fetchInto(musicModel: Track): DataResult<Track> {
        if (Environment.ZODOM_API_URL.isEmpty()) return DataResult.Ok(
            musicModel.copy(
                votesFor = 0,
                votesAgainst = 0,
            )
        );

        val id = musicModel.spotifyId

        return when (val response = httpRequestIntoObj<VoteCount>("${Environment.ZODOM_API_URL}/vote_count/$id")) {
            is HttpResponse.Error -> DataResult.Error(
                ServerError("error fetching votes", response.body)
            )

            is HttpResponse.Ok -> DataResult.Ok(
                musicModel.copy(
                    votesFor = response.body.votesFor,
                    votesAgainst = response.body.votesAgainst
                )
            )
        }
    }
}