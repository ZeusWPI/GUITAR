set dotenv-load := true

default:
    just --list

run:
    ./gradlew bootRun

build-container version="":
    podman build -t git.zeus.gent/zukebox/guitar:latest .
    [ -n "{{version}}" ] && podman tag git.zeus.gent/zukebox/guitar:latest guitar:{{version}} || :
    
build-and-publish version="": (build-container version)
    podman push git.zeus.gent/zukebox/guitar:latest

