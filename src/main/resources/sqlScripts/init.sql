create table track
(
    track_id   varchar primary key,
    spotify_id varchar unique,
    name       varchar,
    album      varchar,
    length     int,             -- length in ms
    play_share double precision -- share of song's own length that is has been played in the kelder
);

create table track_artist
(
    track_id  varchar,
    artist_id varchar,

    foreign key (track_id) references track (track_id)
);

create table artist
(
    artist_id varchar primary key,
    spotifyId varchar unique,
    name      varchar
);

create table artist_genre
(
    artist_id varchar,
    genre     varchar,

    primary key (artist_id, genre),
    foreign key (artist_id) references artist (artist_id)
);
