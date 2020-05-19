# IFImon Server SoPra FS2020

![Pokémon Top Trumps](./logo.png)

Client-repository: https://github.com/Team-Heroket/IFImon-webclient

Play here: https://sopra-fs20-group-20-client.herokuapp.com/

## Introduction

We want to provide an online experience for playing a [Pokémon](https://en.wikipedia.org/wiki/Pok%C3%A9mon) themed [Top-Trumps](https://en.wikipedia.org/wiki/Top_Trumps) game.

This project was made in the scope of a University exercise and is not intended for commercial use, since we don't own the rights to any media used in this game.

## Technologies

- Spring Boot as Server.

- [PokéAPI ](https://pokeapi.co/) to retrieve the Pokémon information.

- We use [pokemonshowdown](https://play.pokemonshowdown.com/audio/cries) for our sfx.

- Gradle for dependency management.

- To communicate with the client we use the REST-pattern.

#### Postman

We highly recommend to use [Postman](https://www.getpostman.com) in order to test our API Endpoints and get a feel for them. You can use our Postman-dump from [here](./postman_dump.json).

## High-level components

Our main-class that runs the application is called [Application.java](./src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java).

Our folderstructure looks like this

- constant
  
  - Are used for our enumerations-classes

- controller
  
  - Handles the requests. Split into classes concerning the Card, Game and User.

- objects
  
  - Helper-classes

- service
  
  - Most of our game logic. We used the state-pattern for the GameService.

Constants are used by all classes. Controller calls the Service and Service uses some helpers from Objects and access the repositories.

Our tests imitate this structure.

## Launch & Deployment

The launch and deployment routine does not differ from the SoPra template, as we stuck to those tools and frameworks. We added some extra libraries to the gradle dependencies though.

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure **Java 13** is installed on your system.

1. File -> Open... -> IFImon-server
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

- MAC OS X: `./gradlew`
- Linux: `./gradlew`
- Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Usage
When using the application there are a Few things to keep in mind
### API Ratelimits
- Heroku Rate Limit: 4800 Requests/h/IP
- PokéAPI Rate Limit: 100 Requests/h/IP

Since we generate our cards with information from the PokéAPI, we have a security measure that delays the requests when getting close to the limit.
So when starting a game with a lot of cards that are not cached yet, be aware of that timeout and stay patient.

## Roadmap

Future features could be:

- Adding the 8th generation of Pokémon since PokéAPI doesn't provide it fully yet.

- Realtime in-game chat.

- Friendslist for chat and game-invites.

## Authors and acknowledgement

- David Steiger
  
- Dominik Frauchiger
  
- Tim Moser

## License

This project is licensed under [Apache License 2.0](./LICENSE).
