package monitoring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import monitoring.api.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest
class RecordsApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Record 생성 요청 - 성공")
    void createRecords() throws Exception {

        //given
        String action = "공부";
        String memo = "API 설계";

        LocalDate date = LocalDate.of(2024, 1, 12);
        LocalTime startTime = LocalTime.of(11, 10);
        Long durationMinutes = 30L;

        List<CreateRecordRequestDto.Time> timeRecords = List.of(CreateRecordRequestDto.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        CreateRecordRequestDto request = CreateRecordRequestDto.builder()
                .action(action)
                .memo(memo)
                .timeRecords(timeRecords)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        CreateRecordResponseDto response
                = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), CreateRecordResponseDto.class);

        assertThat(response.getRecordId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Record 조회 요청 - 성공")
    void getRecord() throws Exception{
        //given
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;
        String action = "운동";
        String meno = "헬스장";

        RecordDto recordDto = RecordDto.builder()
                .action(action)
                .memo(meno)
                .build();


        //when
        ResultActions resultActions
                = mockMvc.perform(get("/api/records/{id}", id));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);
        RecordDto expected =
                RecordDto.builder()
                .recordId(id)
                .action(action)
                .memo(meno)
                .build();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("Patch 를 통한 Record 수정 요청 - 성공")
    void updateRecordsByPatch() throws Exception {


        //given
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;
        String action = "운동";
        String memo = "헬스장";

        PatchUpdateRecordRequestDto request = PatchUpdateRecordRequestDto.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .action(action)
                .memo(memo)
                .build();


        //when
        ResultActions resultActions
                = mockMvc.perform(patch("/api/records/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        PatchUpdateRecordResponseDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), PatchUpdateRecordResponseDto.class);
        PatchUpdateRecordResponseDto expected =
                PatchUpdateRecordResponseDto.builder()
                .recordId(id)
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .action(action)
                .memo(memo)
                .build();

        assertThat(response).isEqualTo(expected);
    }


    @Test
    @DisplayName("Put 을 통한 Record 수정 요청 - 성공")
    void updateRecordsByPut() throws Exception {


        //given
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;
        String action = "운동";
        String memo = "헬스장";

        PutUpdateRecordResponseDto request = PutUpdateRecordResponseDto.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .action(action)
                .memo(memo)
                .build();


        //when
        ResultActions resultActions
                = mockMvc.perform(put("/api/records/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        PutUpdateRecordResponseDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), PutUpdateRecordResponseDto.class);
        PutUpdateRecordResponseDto expected =
                PutUpdateRecordResponseDto.builder()
                        .recordId(id)
                        .date(date)
                        .startTime(startTime)
                        .durationMinutes(durationMinutes)
                        .action(action)
                        .memo(memo)
                        .build();

        assertThat(response).isEqualTo(expected);
    }
}
