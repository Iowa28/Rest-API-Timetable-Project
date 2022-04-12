package ru.aminovniaz.testtask.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.aminovniaz.testtask.dto.EventDto;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.service.EventService;

import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("EventController is working when")
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        when(eventService.getAllEvents()).thenReturn(Collections.singletonList(EventDto.builder()
                .name("Meeting 1")
                .start(LocalTime.of(9, 30))
                .finish(LocalTime.of(12, 0))
                .members(Collections.emptyList())
                .build()));

        when(eventService.addEventToHuman(9L, HumanDto.builder().name("Alexey").build()))
                .thenReturn(EventDto.builder()
                .name("Meeting 9")
                .start(LocalTime.of(16, 45))
                .finish(LocalTime.of(18, 10))
                .members(Collections.singletonList("Alexey"))
                .build());

        when(eventService.addEventToHumans(9L, Collections.emptyList()))
                .thenThrow(IllegalArgumentException.class);
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getAllEvents() is working")
    class GetAllEventsTest {
        @Test
        public void return_one_event() throws Exception {
            mockMvc.perform(get("/events"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name", is("Meeting 1")))
                    .andExpect(jsonPath("$[0].start", is("09:30:00")))
                    .andExpect(jsonPath("$[0].finish", is("12:00:00")))
                    .andExpect(jsonPath("$[0].members", is(Collections.emptyList())));
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addEventToHuman() is working")
    class AddEventToHumanTest {
        @Test
        public void successful_adding() throws Exception {
            mockMvc.perform(post("/events/human/9")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"name\" : \"Alexey\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Meeting 9")))
                    .andExpect(jsonPath("$.start", is("16:45:00")))
                    .andExpect(jsonPath("$.finish", is("18:10:00")))
                    .andExpect(jsonPath("$.members", is(Collections.singletonList("Alexey"))));
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addEventToHumans() is working")
    class AddEventToHumansTest {
        @Test
        public void empty_list_of_humans() throws Exception {
            mockMvc.perform(post("/events/humans/9")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[]"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
