package ru.aminovniaz.testtask.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.aminovniaz.testtask.dto.HumanDto;
import ru.aminovniaz.testtask.model.Interval;
import ru.aminovniaz.testtask.service.HumanService;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("HumanController is working when")
public class HumanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HumanService humanService;

    @BeforeEach
    public void setUp() {
        when(humanService.getAllHuman()).thenReturn(List.of(
                HumanDto.builder().name("Vasya").login("vasek@mail.ru").events(List.of("Meeting 1", "Meeting 2")).build(),
                HumanDto.builder().name("Sasha").login("sanya@list.ru").events(List.of("Meeting 3")).build()
        ));

        when(humanService.getHumanById(2L)).thenReturn(HumanDto.builder()
                .name("Dima").login("dimasick@mail.ru").events(Collections.emptyList()).build());

        when((humanService.getCommonGaps(List.of("Ilya", "Rafael", "Ernest")))).thenReturn(List.of(
                Interval.builder().start(LocalTime.of(0, 0)).finish(LocalTime.of(10, 0)).build(),
                Interval.builder().start(LocalTime.of(11, 0)).finish(LocalTime.of(16, 30)).build(),
                Interval.builder().start(LocalTime.of(18, 0)).finish(LocalTime.of(0, 0)).build()
        ));
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getAllHuman() is working")
    class GetAllHumanTest {
        @Test
        public void return_two_human() throws Exception {
            mockMvc.perform(get("/humans"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name", is("Vasya")))
                    .andExpect(jsonPath("$[0].login", is("vasek@mail.ru")))
                    .andExpect(jsonPath("$[0].events", is(List.of("Meeting 1", "Meeting 2"))))
                    .andExpect(jsonPath("$[1].name", is("Sasha")))
                    .andExpect(jsonPath("$[1].login", is("sanya@list.ru")))
                    .andExpect(jsonPath("$[1].events", is(List.of("Meeting 3"))));
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getHumanById() is working")
    class GetHumanByIdTest {
        @Test
        public void return_dima() throws Exception {
            mockMvc.perform(get("/humans/2"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dima")))
                    .andExpect(jsonPath("$.login", is("dimasick@mail.ru")))
                    .andExpect(jsonPath("$.events", is(Collections.emptyList())));
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getCommonGaps() is working")
    class GetCommonGapsTest {
        @Test
        public void return_three_intervals() throws Exception {
            mockMvc.perform(post("/humans/common-gaps")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[\n" +
                            "  \"Ilya\",\n" +
                            "  \"Rafael\",\n" +
                            "  \"Ernest\"\n" +
                            "]"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].start", is("00:00:00")))
                    .andExpect(jsonPath("$[0].finish", is("10:00:00")))
                    .andExpect(jsonPath("$[1].start", is("11:00:00")))
                    .andExpect(jsonPath("$[1].finish", is("16:30:00")))
                    .andExpect(jsonPath("$[2].start", is("18:00:00")))
                    .andExpect(jsonPath("$[2].finish", is("00:00:00")));
        }
    }
}
