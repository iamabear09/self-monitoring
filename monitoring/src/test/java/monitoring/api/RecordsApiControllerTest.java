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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
        Integer durationMinutes = 30;

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
        resultActions.andExpect(status().isOk())
                .andDo(print());

        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);

        Long timeId = 1L;
        Long recordId = 1L;
        RecordDto expected = new RecordDto(recordId, action, memo, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("Record 조회 요청 - 성공")
    void getRecord() throws Exception{

        //given
        Long recordId = 1L;

        //when
        ResultActions resultActions
                = mockMvc.perform(get("/api/records/{id}", recordId));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);

        //expected mock date
        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;
        String action = "운동";
        String memo = "헬스장";

        RecordDto expected = new RecordDto(recordId, action, memo, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
        assertThat(response).isEqualTo(expected);
    }


    @Test
    @DisplayName("여러 Record 조회 & 검색 요청 - 성공")
    void getRecords() throws Exception{

        //given
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>() {{
            add("action", "운동");
            add("memo", "헬스장");
            add("date", LocalDate.of(2024, 1, 10).toString());
            add("time", LocalTime.of(11, 10).toString());
        }};


        //when
        ResultActions resultActions = mockMvc.perform(get("/api/records")
                .queryParams(queryParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        SearchRecordsResponseDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), SearchRecordsResponseDto.class);

        //mock data1
        Long recordId1 = 1L;
        String action1 = "운동";
        String meno1 = "헬스장";
        Long timeId1 = 1L;
        LocalDate date1 = LocalDate.of(2024, 1, 13);
        LocalTime startTime1 = LocalTime.of(13, 10);
        Integer durationMinutes1 = 60;

        List<RecordDto.Time> timeRecords1 = List.of(new RecordDto.Time(timeId1, date1, startTime1, durationMinutes1));
        RecordDto recordDto1 = new RecordDto(recordId1, action1, meno1, timeRecords1);

        //mock data2
        Long recordId2 = 2L;
        String action2 = "공부";
        String meno2 = "API 설계";

        Long timeId2 = 2L;
        LocalDate date2 = LocalDate.of(2024, 1, 11);
        LocalTime startTime2 = LocalTime.of(13, 10);
        Integer durationMinutes2 = 60;
        List<RecordDto.Time> timeRecords2 = List.of(new RecordDto.Time(timeId2, date2, startTime2, durationMinutes2));
        RecordDto recordDto2 = new RecordDto(recordId2, action2, meno2, timeRecords2);


        assertThat(response).isEqualTo(new SearchRecordsResponseDto(List.of(recordDto1, recordDto2)));
    }


    @Test
    @DisplayName("Patch 를 통한 Record 수정 요청 - 성공")
    void updateRecordsByPatch() throws Exception {

        //given
        Long id = 1L;
        String action = "운동";
        String memo = "헬스장";

        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;

        List<PatchUpdateRecordRequestDto.Time> timeRecords = List.of(PatchUpdateRecordRequestDto.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        PatchUpdateRecordRequestDto request = PatchUpdateRecordRequestDto.builder()
                .action(action)
                .memo(memo)
                .timeRecords(timeRecords)
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

        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);

        Long timeId = 1L;
        RecordDto expected = new RecordDto(id, action, memo, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));

        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("Put 을 통한 Record 수정 요청 - 성공")
    void updateRecordsByPut() throws Exception {

        //given
        Long id = 1L;
        String action = "운동";
        String memo = "헬스장";

        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;

        List<PutUpdateRecordRequestDto.Time> timeRecords = List.of(PutUpdateRecordRequestDto.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        PutUpdateRecordRequestDto request = PutUpdateRecordRequestDto.builder()
                .action(action)
                .memo(memo)
                .timeRecords(timeRecords)
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


        //mock data
        RecordDto.Time time1 = new RecordDto.Time(5L, LocalDate.of(2024, 1, 1), LocalTime.of(10, 10), 30);
        RecordDto.Time time2 = new RecordDto.Time(6L, LocalDate.of(2024, 1, 1), LocalTime.of(11, 10), 60);

        RecordDto affectedRecord = new RecordDto(11L, "운동", "벤치프레스", List.of(time1, time2));
        List<Long> deleteRecordsIds = List.of(10L, 11L, 12L);

        Long timeId = 1L;
        RecordDto updatedRecords = new RecordDto(id, action, memo, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
        PutUpdateRecordResponseDto expected = new PutUpdateRecordResponseDto(updatedRecords, deleteRecordsIds, List.of(affectedRecord));

        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("Record 삭제 요청 - 성공")
    void deleteRecord() throws Exception {
        //given
        Long recordId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/records/{id}", recordId));


        //then
        resultActions.andExpect(status().isOk());
        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);

        //mock data
        String action = "운동";
        String meno = "헬스장";

        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;

        RecordDto expected = new RecordDto(recordId, action, meno, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
        assertThat(response).isEqualTo(expected);
    }

}
