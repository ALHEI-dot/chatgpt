# Real Serene Seasons — Connector build (Minecraft 1.21.1)

Fabric mod build intended to run on NeoForge 21.1.x through Sinytra Connector.

Target setup:
- Minecraft 1.21.1
- NeoForge 21.1.265
- Sinytra Connector 2.0.0-beta.17+1.21.1
- Serene Seasons NeoForge 10.1.0.9
- Java 21

The project keeps Fabric entrypoints and Fabric API calls so Connector can bridge them. It is compiled against the matching Fabric Serene Seasons 10.1.0.0 API artifact; the runtime installation is expected to provide the NeoForge Serene Seasons 10.1.0.9 mod.

## Build

A GitHub Actions workflow builds on every push to `main` and uploads the JAR as an artifact. Locally, with Java 21 and Gradle 8.10.2:

```sh
gradle clean build
```

The JAR is written to `build/libs/`.

## Configuration

Install Mod Menu and Cloth Config on the client if Connector supports them in your pack. The mod exposes the six calendar settings through its Mod Menu config screen. The TOML config remains available at `config/real_serene_seasons.toml`.

## Compatibility note

Connector compatibility is not guaranteed merely by compiling the Fabric mod. The resulting artifact must be tested in the exact NeoForge pack, especially the Fabric networking bridge and Serene Seasons API linkage. Check the GitHub Actions build result first, then test client startup and a dedicated-server join.

Original project source: https://github.com/cintreck/real-serene-seasons (CC0-1.0).
