package org.acme;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This sample is based on our official tutorials:
 * <ul>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/quickstart">Gatling quickstart tutorial</a>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/advanced">Gatling advanced tutorial</a>
 * </ul>
 */
public class ComputerDatabaseSimulation extends Simulation {

    // The default url is the localhost, you can pass parameters from the running command, e.g. mvn gatling:test -DbaseUrl="http://localhost:8090"
    String baseUrl = System.getProperty("baseUrl", "https://www.example.com/");

    // Number of virtual users being introduced in the simulation
    int nbUsers = Integer.getInteger("users", 10);

    // Time period that the users are added to the simulation
    long myRamp = Long.getLong("ramp", 5);

    // HTTP protocol configuration to simulate requests
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(baseUrl)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    /**
     * Basic scenario definition of simulation with the GreetingResource API
     * It just checks if the status is 200 and then pauses 5 seconds
     */
    ScenarioBuilder scn = scenario("GreetingResource")
            .exec(http("")
                    .get("/")
                    .check(
                            status().is(200)
                    )).pause(5);

    /*
      Simulation setup with user injection for the previous created scenario
      In this case 10 virtual users are gradually introduced over 5-second period
      (Those values are the default, you can pass parameters from the running command, e.g. mvn gatling:test -Dusers=20 -Dramp=10)
    */
    {
        setUp(
                scn.injectOpen(rampUsers(nbUsers).during(myRamp))
        ).protocols(httpProtocol);
    }
}
