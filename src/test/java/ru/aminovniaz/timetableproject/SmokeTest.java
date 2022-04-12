package ru.aminovniaz.testtask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aminovniaz.testtask.controller.EventController;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class SmokeTest {

	@Autowired
	private EventController eventController;

	@Test
	void contextLoads() {
		assertThat(eventController, is(notNullValue()));
	}

}
