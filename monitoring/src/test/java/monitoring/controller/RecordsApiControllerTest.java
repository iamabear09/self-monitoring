package monitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import monitoring.controller.request.*;
import monitoring.controller.response.CreateRecordResponse;
import monitoring.controller.response.GetRecordResponse;
import monitoring.controller.response.PutUpdateRecordResponse;
import monitoring.controller.response.SearchRecordResponse;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeLogRepository;
import monitoring.service.RecordTimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class RecordsApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecordTimeService recordTimeService;

    @Autowired
    private TimeLogRepository timeLogRepository;
    @Autowired
    private RecordRepository recordRepository;


    //given
    CreateRecordRequest createRecordRequest1;
    CreateTimeLogRequest createTimeLogRequest1;
    Record record1;
    TimeLog timeLog1InRecord1;

    CreateRecordRequest createRecordRequest2;
    CreateTimeLogRequest createTimeLogRequest2;
    CreateRecordRequest createRecordRequest3;
    CreateTimeLogRequest createTimeLogRequest3;
    CreateRecordRequest createRecordRequest4;
    CreateTimeLogRequest createTimeLogRequest4;
    CreateRecordRequest createRecordRequest5;
    CreateTimeLogRequest createTimeLogRequest5;

    PatchUpdateRecordRequest patchUpdateRecordRequest1;
    PatchUpdateTimeLogRequest patchUpdateTimeLogRequest1;

    PutUpdateRecordRequest putUpdateRecordRequest1;
    PutUpdateTimeLogRequest putUpdateTimeLogRequest1;

    @BeforeEach
    void setUpData() {

        //create request 1
        //2024 .01 .01 09:00 - 10:00
        LocalDate requestData1 = LocalDate.of(2024, 1, 1);
        LocalTime requestStartTime1 = LocalTime.of(9, 0);
        LocalTime requestEndTime1 = LocalTime.of(10, 0);
        createTimeLogRequest1 = CreateTimeLogRequest.builder()
                .date(requestData1)
                .startTime(requestStartTime1)
                .endTime(requestEndTime1)
                .build();
        String requestAction1 = "공부";
        String requestMemo1 = "API 설계";
        createRecordRequest1 = CreateRecordRequest.builder()
                .action(requestAction1)
                .memo(requestMemo1)
                .timeLogRequests(List.of(createTimeLogRequest1))
                .build();

        record1 = Record.builder()
                .action(createRecordRequest1.getAction())
                .memo(createRecordRequest1.getMemo())
                .build();

        timeLog1InRecord1 = TimeLog.builder()
                .date(createTimeLogRequest1.getDate())
                .startTime(createTimeLogRequest1.getStartTime())
                .endTime(createTimeLogRequest1.getEndTime())
                .durationMinutes(Duration.between(createTimeLogRequest1.getStartTime(), createTimeLogRequest1.getEndTime()))
                .record(record1)
                .build();
        record1.setTimeLogs(List.of(timeLog1InRecord1));


        //create request 2
        //2024 .01 .01 10:00 - 11:00
        LocalDate requestData2 = LocalDate.of(2024, 1, 1);
        LocalTime requestStartTime2 = LocalTime.of(10, 0);
        LocalTime requestEndTime2 = LocalTime.of(11, 0);
        createTimeLogRequest2 = CreateTimeLogRequest.builder()
                .date(requestData2)
                .startTime(requestStartTime2)
                .endTime(requestEndTime2)
                .build();
        String requestAction2 = "공부";
        String requestMemo2 = "독서";
        createRecordRequest2 = CreateRecordRequest.builder()
                .action(requestAction2)
                .memo(requestMemo2)
                .timeLogRequests(List.of(createTimeLogRequest2))
                .build();

        //create request 3
        //2024 .01 .01 11:00 - 12:00
        LocalDate requestData3 = LocalDate.of(2024, 1, 1);
        LocalTime requestStartTime3 = LocalTime.of(11, 0);
        LocalTime requestEndTime3 = LocalTime.of(12, 0);
        createTimeLogRequest3 = CreateTimeLogRequest.builder()
                .date(requestData3)
                .startTime(requestStartTime3)
                .endTime(requestEndTime3)
                .build();
        String requestAction3 = "공부";
        String requestMemo3 = "Github Action";
        createRecordRequest3 = CreateRecordRequest.builder()
                .action(requestAction3)
                .memo(requestMemo3)
                .timeLogRequests(List.of(createTimeLogRequest3))
                .build();

        //create request 4
        //2024 .01 .01 09:30 - 10:30
        LocalDate requestData4 = LocalDate.of(2024, 1, 1);
        LocalTime requestStartTime4 = LocalTime.of(9, 30);
        LocalTime requestEndTime4 = LocalTime.of(10, 30);
        createTimeLogRequest4 = CreateTimeLogRequest.builder()
                .date(requestData4)
                .startTime(requestStartTime4)
                .endTime(requestEndTime4)
                .build();
        String requestAction4 = "밥";
        String requestMemo4 = "고등어";
        createRecordRequest4 = CreateRecordRequest.builder()
                .action(requestAction4)
                .memo(requestMemo4)
                .timeLogRequests(List.of(createTimeLogRequest4))
                .build();

        //create request 5
        //2024 .01 .01 09:30 - 10:30
        LocalDate requestData5 = LocalDate.of(2024, 1, 2);
        LocalTime requestStartTime5 = LocalTime.of(9, 30);
        LocalTime requestEndTime5 = LocalTime.of(10, 30);
        createTimeLogRequest5 = CreateTimeLogRequest.builder()
                .date(requestData5)
                .startTime(requestStartTime5)
                .endTime(requestEndTime5)
                .build();
        String requestAction5 = "공부";
        String requestMemo5 = "고등어";
        createRecordRequest5 = CreateRecordRequest.builder()
                .action(requestAction5)
                .memo(requestMemo5)
                .timeLogRequests(List.of(createTimeLogRequest5))
                .build();


        //patch update request 1
        LocalDate patchDate1 = LocalDate.of(2024, 1, 1);
        LocalTime patchStartTime1 = LocalTime.of(9, 30);
        LocalTime patchEndTime1 = LocalTime.of(10, 30);
        patchUpdateTimeLogRequest1 = PatchUpdateTimeLogRequest.builder()
                .date(patchDate1)
                .startTime(patchStartTime1)
                .endTime(patchEndTime1)
                .build();

        String patchAction1 = "수면";
        String patchMemo1 = "낮잠";
        patchUpdateRecordRequest1 = PatchUpdateRecordRequest.builder()
                .action(patchAction1)
                .memo(patchMemo1)
                .timeLogRequests(List.of(patchUpdateTimeLogRequest1))
                .build();

        LocalDate putDate1 = LocalDate.of(2024, 1, 1);
        LocalTime putStartTime1 = LocalTime.of(9, 30);
        LocalTime putEndTime1 = LocalTime.of(10, 30);
        putUpdateTimeLogRequest1 = PutUpdateTimeLogRequest.builder()
                .date(putDate1)
                .startTime(putStartTime1)
                .endTime(putEndTime1)
                .build();

        String putAction1 = "수면";
        String putMemo1 = "낮잠";
        putUpdateRecordRequest1 = PutUpdateRecordRequest.builder()
                .action(putAction1)
                .memo(putMemo1)
                .timeLogRequests(List.of(putUpdateTimeLogRequest1))
                .build();
    }

    @AfterEach
    void cleanDB() {
        timeLogRepository.deleteAllInBatch();
        recordRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("Record 생성 요청 - 성공")
    void createRecords() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRecordRequest1)));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        CreateRecordResponse createRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), CreateRecordResponse.class);

        assertThat(createRecordResponse.getRecordId()).isPositive();
        assertThat(createRecordResponse.getAction()).isEqualTo(createRecordRequest1.getAction());
        assertThat(createRecordResponse.getMemo()).isEqualTo(createRecordRequest1.getMemo());
        assertThat(createRecordResponse.getTimeLogs())
                .hasSize(1)
                .extracting("date", "startTime", "endTime", "durationMinutes")
                .contains(
                        tuple(createTimeLogRequest1.getDate(), createTimeLogRequest1.getStartTime(), createTimeLogRequest1.getEndTime(), 60)
                );
    }

    @Test
    @DisplayName("Record 조회 요청 - 성공")
    void getRecord() throws Exception{

        //given
        Record savedRecord = recordTimeService.create(createRecordRequest1.toRecord());

        //when
        ResultActions resultActions
                = mockMvc.perform(get("/api/records/{id}", savedRecord.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        GetRecordResponse getRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), GetRecordResponse.class);
        GetRecordResponse expectedResponse = GetRecordResponse.from(savedRecord);

        assertThat(getRecordResponse).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("여러 Record 조회 & 검색 요청 - 성공")
    void getRecords() throws Exception{
        //given
        Record record1 = recordTimeService.create(createRecordRequest1.toRecord());
        Record record2 = recordTimeService.create(createRecordRequest2.toRecord());
        Record record3 = recordTimeService.create(createRecordRequest3.toRecord());
        Record record4_noSearched1 = recordTimeService.create(createRecordRequest4.toRecord());
        Record record5_noSearched2 = recordTimeService.create(createRecordRequest5.toRecord());


        //when
        ResultActions resultActions = mockMvc.perform(get("/api/records")
                .queryParam("action", "공부")
                .queryParam("date", LocalDate.of(2024, 1, 1).toString())
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        SearchRecordResponse searchRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), SearchRecordResponse.class);
        SearchRecordResponse expected = SearchRecordResponse.from(List.of(record1, record2, record3));

        assertThat(searchRecordResponse.getRecordsNum()).isEqualTo(expected.getRecordsNum());
        assertThat(searchRecordResponse.getRecords())
                .hasSize(3)
                .containsAll(expected.getRecords());
    }

    @Test
    @DisplayName("Patch 를 통한 Record 수정 요청 - 성공")
    void updateRecordsByPatch() throws Exception {

        //given
        Record record1 = recordTimeService.create(createRecordRequest1.toRecord());


        //when
        ResultActions resultActions
                = mockMvc.perform(patch("/api/records/{id}", record1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchUpdateRecordRequest1)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        GetRecordResponse updateRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), GetRecordResponse.class);

        assertThat(updateRecordResponse.getRecordId()).isEqualTo(record1.getId());
        assertThat(updateRecordResponse.getAction()).isEqualTo(patchUpdateRecordRequest1.getAction());
        assertThat(updateRecordResponse.getMemo()).isEqualTo(patchUpdateRecordRequest1.getMemo());

        assertThat(updateRecordResponse.getTimeLogs())
                .hasSize(1)
                .extracting("date", "startTime", "endTime")
                .contains(
                        tuple(LocalDate.of(2024,1,1), LocalTime.of(9, 30), LocalTime.of(10, 30))
                );
    }


    @Test
    @DisplayName("Put 을 통한 Record 수정 요청 - 성공")
    void updateRecordsByPut() throws Exception {

        //given
        //09 - 10
        Record record1 = recordTimeService.create(createRecordRequest1.toRecord());
        //10- 11
        Record record2 = recordTimeService.create(createRecordRequest2.toRecord());
        //11 - 12  :: update to 09:30 - 10:30
        Record record3 = recordTimeService.create(createRecordRequest3.toRecord());
        //09:30 - 10:30 :: will be deleted
        Record record4 = recordTimeService.create(createRecordRequest4.toRecord());

        //when
        ResultActions resultActions
                = mockMvc.perform(put("/api/records/{id}", record3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putUpdateRecordRequest1)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        PutUpdateRecordResponse putUpdateRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), PutUpdateRecordResponse.class);

        List<GetRecordResponse> changedRecords = putUpdateRecordResponse.getChangedRecords();
        assertThat(changedRecords)
                .hasSize(2)
                .extracting("action", "memo")
                .contains(
                        tuple(record1.getAction(), record1.getMemo()),
                        tuple(record2.getAction(), record2.getMemo())
                );
        assertThat(changedRecords)
                .hasSize(2)
                .flatExtracting("timeLogs")
                .extracting("startTime", "endTime" , "durationMinutes")
                .contains(
                        tuple(LocalTime.of(9, 0), LocalTime.of(9, 30), 30),
                        tuple(LocalTime.of(10, 30), LocalTime.of(11, 0), 30)
                );
        assertThat(putUpdateRecordResponse.getChangedRecordsNum()).isEqualTo(2);


        List<GetRecordResponse> deleteRecords = putUpdateRecordResponse.getDeleteRecords();
        assertThat(deleteRecords)
                .hasSize(1)
                .extracting("action", "memo")
                .contains(
                        tuple(record4.getAction(), record4.getMemo())
                );
        assertThat(putUpdateRecordResponse.getDeletedRecordsNum()).isEqualTo(1);

        GetRecordResponse updatedRecord = putUpdateRecordResponse.getUpdatedRecord();

        assertThat(updatedRecord.getRecordId()).isEqualTo(record3.getId());
        assertThat(updatedRecord.getAction()).isEqualTo(putUpdateRecordRequest1.getAction());
        assertThat(updatedRecord.getMemo()).isEqualTo(putUpdateRecordRequest1.getMemo());
        assertThat(updatedRecord.getTimeLogs())
                .hasSize(1)
                .extracting("startTime", "endTime", "durationMinutes")
                .contains(
                        tuple(LocalTime.of(9, 30), LocalTime.of(10, 30), 60)
                );
    }

    @Test
    @DisplayName("Record 삭제 요청 성공")
    void deleteRecord() throws Exception {
        //given
        Record record1 = recordTimeService.create(createRecordRequest1.toRecord());

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/records/{id}", record1.getId()));

        //then
        resultActions.andExpect(status().isOk());
        GetRecordResponse getRecordResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), GetRecordResponse.class);
        GetRecordResponse expected = GetRecordResponse.from(record1);

        assertThat(getRecordResponse).isEqualTo(expected);
    }
}
