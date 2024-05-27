package nl.groenveldict.gamez.whoamiservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WhoAmIServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateNewGame() {
		String requestBody =
				"""
				{
					"userIdsPlaying" : [1,2,3,4,5,6]
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/game/new")
				.then()
				.log().body()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("playerDetails.userId", Matchers.hasItems(1, 2, 3, 4, 5, 6))
				.body("playerDetails.opponentId", Matchers.hasItems(1, 2, 3, 4, 5, 6));
	}
}
