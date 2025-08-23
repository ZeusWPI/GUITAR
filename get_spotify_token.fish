set SPOTIFY_TOKEN (xhs post https://accounts.spotify.com/api/token content-type:application/x-www-form-urlencoded --raw "grant_type=client_credentials&client_id=$SPOTIFY_CLIENT_ID&client_secret=$SPOTIFY_CLIENT_SECRET" | jq .access_token | sed 's/"//g')

