package monitoring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(RecordsApiController.class)
class RecordsApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Records 생성 요청 성공")
    void createRecords() throws Exception {

        //given
        LocalDate date = LocalDate.of(2024, 1, 12);
        LocalTime startTime = LocalTime.of(11, 10);
        Long durationMinutes = 30L;
        String action = "공부";
        String memo = "API 설계";

        CreateRecordsRequestDto request = CreateRecordsRequestDto.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .action(action)
                .memo(memo)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        CreateRecordsResponseDto response
                = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CreateRecordsResponseDto.class);

        Assertions.assertThat(response.getId()).isEqualTo(1L);
    }
}
