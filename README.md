###Technologies Used
• Java17
• Junit/Mockito
• Maven
• Spring boot 2.6.6
• Webflux
• docker

## How to run this?
```bash
$ git clone https://github.com/prateek687/musify.git
$ cd musify
$ mvn clean install
$ mvn spring-boot:run

  access http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e

//dockerize

// create a docker image
$ docker-compose build
// run it
$ docker-compose up

  access http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e
```
