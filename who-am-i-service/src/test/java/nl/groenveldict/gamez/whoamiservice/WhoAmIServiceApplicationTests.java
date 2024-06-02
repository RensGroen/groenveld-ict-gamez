package nl.groenveldict.gamez.whoamiservice;

import io.restassured.RestAssured;
import nl.groenveldict.gamez.whoamiservice.domain.Game;
import nl.groenveldict.gamez.whoamiservice.repository.GameRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WhoAmIServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	private final GameRepository gameRepository;

	@Autowired
	public WhoAmIServiceApplicationTests(GameRepository gr){
		this.gameRepository = gr;
	}

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
				.post("api/game")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("playerDetails.userId", Matchers.hasItems(1, 2, 3, 4, 5, 6))
				.body("playerDetails.opponentId", Matchers.hasItems(1, 2, 3, 4, 5, 6));
	}

	@Test
	void updateExistingGameShouldHandleNonExistingGameCorrectly() {
		String requestBody =
				"""
				{
					"id" : "nonExistingId",
					"playerDetails" : [{"userId": 1, "opponentId": 2, "person": "King Kong"}]
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.put("api/game")
				.then()
				.statusCode(404);
	}

	@Test
	void updateExistingGameShouldUpdateExistingGameCorrectly() {
		String requestBody =
				"""
				{
					"userIdsPlaying" : [1,2]
				}
				""";

		String id = RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/game")
				.then()
				.extract()
				.path("id");

		requestBody =
				"""
				{
					"id" : "${id}",
					"playerDetails" : [{"userId": 1, "opponentId": 2, "person": "King Kong"}, {"userId": 2, "opponentId": 1, "person": "Linus Torvalds"}]
				}
				""".replace("${id}", id);

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.put("api/game");

		Optional<Game> s = gameRepository.findById(id);

		Assertions.assertTrue(s.isPresent());
		Assertions.assertEquals("King Kong",s.get().getPlayerDetails().get(0).getPerson());
		Assertions.assertEquals("Linus Torvalds",s.get().getPlayerDetails().get(1).getPerson());
		Assertions.assertFalse(s.get().isEnded());
	}

	@Test
	void finishExistingGameShouldHandleNonExistingGameCorrectly() {
		String requestBody =
				"""
				{
					"id" : "nonExistingId",
					"userIdLost" : 1
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.put("api/game")
				.then()
				.statusCode(404);
	}

	@Test
	void finishExistingGameShouldUpdateExistingGameCorrectly() {
		String requestBody =
				"""
				{
					"userIdsPlaying" : [1,2]
				}
				""";

		String id = RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/game")
				.then()
				.extract()
				.path("id");

		requestBody =
				"""
				{
					"id" : "${id}",
					"playerDetails" : [{"userId": 1, "opponentId": 2, "person": "King Kong"}, {"userId": 2, "opponentId": 1, "person": "Linus Torvalds"}]
				}
				""".replace("${id}", id);

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.put("api/game");

		requestBody =
				"""
				{
					"id" : "${id}",
					"userIdLost" : 2
				}
				""".replace("${id}", id);

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.put("api/game/finish");

		Optional<Game> s = gameRepository.findById(id);

		Assertions.assertTrue(s.isPresent());
		Assertions.assertEquals(2, s.get().getUserIdLost());
		Assertions.assertTrue(s.get().isEnded());
	}
}
