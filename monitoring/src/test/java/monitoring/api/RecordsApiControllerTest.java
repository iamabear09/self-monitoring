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
        Long recordId = 1L;

        //when
        ResultActions resultActions
                = mockMvc.perform(get("/api/records/{id}", recordId));

        //then

        //expected mock date
        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;

        List<RecordDto.Time> timeRecords = List.of(RecordDto.Time.builder()
                .timeId(timeId)
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        String action = "운동";
        String meno = "헬스장";


        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        RecordDto response = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), RecordDto.class);
        RecordDto expected = RecordDto.builder()
                        .recordId(recordId)
                        .action(action)
                        .memo(meno)
                        .timeRecords(timeRecords)
                        .build();

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

        //expected mock data1
        Long recordId1 = 1L;
        String action1 = "운동";
        String meno1 = "헬스장";

        Long timeId1 = 1L;
        LocalDate date1 = LocalDate.of(2024, 1, 13);
        LocalTime startTime1 = LocalTime.of(13, 10);
        Long durationMinutes1 = 60L;

        List<RecordDto.Time> timeRecords1 = List.of(RecordDto.Time.builder()
                .timeId(timeId1)
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build());

        RecordDto recordDto1 = RecordDto.builder()
                .recordId(recordId1)
                .action(action1)
                .memo(meno1)
                .timeRecords(timeRecords1)
                .build();

        //expected mock data2
        Long recordId2 = 2L;
        String action2 = "공부";
        String meno2 = "API 설계";

        Long timeId2 = 2L;
        LocalDate date2 = LocalDate.of(2024, 1, 11);
        LocalTime startTime2 = LocalTime.of(13, 10);
        Long durationMinutes2 = 60L;

        List<RecordDto.Time> timeRecords2 = List.of(RecordDto.Time.builder()
                .timeId(timeId2)
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build());

        RecordDto recordDto2 = RecordDto.builder()
                .recordId(recordId2)
                .action(action2)
                .memo(meno2)
                .timeRecords(timeRecords2)
                .build();

        SearchRecordsResponseDto expected = SearchRecordsResponseDto.builder()
                .records(List.of(recordDto1, recordDto2))
                .build();

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        SearchRecordsResponseDto response = objectMapper.readValue(contentAsString, SearchRecordsResponseDto.class);

        assertThat(response).isEqualTo(expected);
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
        Long durationMinutes = 60L;

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
        List<RecordDto.Time> expectedTimeRecords = request.getTimeRecords().stream()
                .map(time -> RecordDto.Time.builder()
                        .timeId(timeId)
                        .date(time.getDate())
                        .startTime(time.getStartTime())
                        .durationMinutes(time.getDurationMinutes())
                        .build())
                .toList();
        RecordDto expected = RecordDto.builder()
                .recordId(id)
                .action(action)
                .memo(memo)
                .timeRecords(expectedTimeRecords)
                .build();

        assertThat(response).isEqualTo(expected);
    }


    @Test
    @DisplayName("Patch 를 통한 Record 특정 필드 수정 요청 - 성공")
    void updatePartOfRecordsByPatch() throws Exception {

        //given
        Long id = 1L;
//        String action = "운동";
//        String memo = "헬스장";

        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;

        List<PatchUpdateRecordRequestDto.Time> timeRecords = List.of(PatchUpdateRecordRequestDto.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        PatchUpdateRecordRequestDto request = PatchUpdateRecordRequestDto.builder()
//                .action(action)
//                .memo(memo)
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
        List<RecordDto.Time> expectedTimeRecords = request.getTimeRecords().stream()
                .map(time -> RecordDto.Time.builder()
                        .timeId(timeId)
                        .date(time.getDate())
                        .startTime(time.getStartTime())
                        .durationMinutes(time.getDurationMinutes())
                        .build())
                .toList();
        RecordDto expected = RecordDto.builder()
                .recordId(id)
//                .action(action)
//                .memo(memo)
                .timeRecords(expectedTimeRecords)
                .build();

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
        Long durationMinutes = 60L;

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
        RecordDto.Time time1 = RecordDto.Time.builder()
                .timeId(5L)
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 10))
                .durationMinutes(30L)
                .build();

        RecordDto.Time time2 = RecordDto.Time.builder()
                .timeId(6L)
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(11, 10))
                .durationMinutes(30L)
                .build();

        RecordDto affectedRecord = RecordDto.builder()
                .recordId(id)
                .action("운동")
                .memo("벤치프레스")
                .timeRecords(List.of(time1, time2))
                .build();

        Long timeId = 1L;
        List<RecordDto.Time> expectedTimeRecords = request.getTimeRecords().stream()
                .map(time -> RecordDto.Time.builder()
                        .timeId(timeId)
                        .date(time.getDate())
                        .startTime(time.getStartTime())
                        .durationMinutes(time.getDurationMinutes())
                        .build())
                .toList();
        RecordDto updatedRecord = RecordDto.builder()
                .recordId(id)
                .action(action)
                .memo(memo)
                .timeRecords(expectedTimeRecords)
                .build();


        List<Long> deleteRecordsIds = List.of(10L, 11L, 12L);

        PutUpdateRecordResponseDto expected = PutUpdateRecordResponseDto.builder()
                .updatedRecord(updatedRecord)
                .deleteRecordsIds(deleteRecordsIds)
                .affectedRecords(List.of(affectedRecord))
                .build();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("Record 삭제 요청 - 성공")
    void deleteRecord() throws Exception {
        //given
        Long recordId = 1L;

        //when & then
        mockMvc.perform(delete("/api/records/{id}", recordId))
                .andExpect(status().isNoContent());
    }

}
