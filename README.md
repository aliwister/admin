
logstash -f C:\work\_merchant-data\logstash_import_csv.config


# shop
mvnw package -Pprod -DskipTests
heroku deploy:jar target/shop-0.0.1-SNAPSHOT.jar --app face-shop-api

This application was generated using JHipster 6.1.2, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.1.2](https://www.jhipster.tech/documentation-archive/v6.1.2).

## Development

To start your application in the dev profile, simply run:

    ./mvnw

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the shop application for production, run:

    ./mvnw -Pprod clean verify

To ensure everything worked, run:

    java -jar target/*.jar

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean verify

## Testing

To launch your application's tests, run:

    ./mvnw verify

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

or

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.1.2 archive]: https://www.jhipster.tech/documentation-archive/v6.1.2
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.1.2/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.1.2/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.1.2/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.1.2/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.1.2/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.1.2/setting-up-ci/



mvn deploy:deploy-file -Dfile=./lib/paapi5-java-sdk-1.0.0.jar -DgroupId=com.amazonservices -DartifactId=paapi5 -Dversion=1.0 -Dpackaging=jar -Durl=https://mymavenrepo.com/repo/xFZB9BHHw4UacjoRlVOe/
mvn deploy:deploy-file -Dfile=./lib/MWSProductsServiceJavaClientLibrary-2013-08-01.jar -DgroupId=com.amazonservices -DartifactId=mws-products -Dversion=1.0 -Dpackaging=jar -Durl=https://mymavenrepo.com/repo/xFZB9BHHw4UacjoRlVOe/
mvn deploy:deploy-file -Dfile=./lib/MWSClientJavaRuntime-1.0.jar -DgroupId=com.amazonservices -DartifactId=mws-client -Dversion=1.0 -Dpackaging=jar -Durl=https://mymavenrepo.com/repo/xFZB9BHHw4UacjoRlVOe/
