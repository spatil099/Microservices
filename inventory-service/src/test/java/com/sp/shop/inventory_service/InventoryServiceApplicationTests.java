package com.sp.shop.inventory_service;

import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsRestAssuredConfigurationCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.1");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup (){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}
	@Test
	void checkIfInStock_TrueScenario() {
		Boolean result = RestAssured.given()
				.when()
				.get("/api/inventory?skuCode=iphone_15&quantity=100")
				.then()
				.log().all()
				.statusCode(200)
				.extract()
				.response()
				.as(Boolean.class);

		Assert.assertTrue(result);
	}

	@Test
	void checkIfInStock_FalseScenario() {
		Boolean result = RestAssured.given()
				.when()
				.get("/api/inventory?skuCode=iphone_15&quantity=700")
				.then()
				.log().all()
				.statusCode(200)
				.extract()
				.response()
				.as(Boolean.class);

		Assert.assertFalse(result);
	}

}
