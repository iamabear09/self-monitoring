package jhp.monitoring.service;

import jhp.monitoring.repository.RecordRepository;
import jhp.monitoring.domain.Record;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    RecordRepository recordRepository;

    @InjectMocks
    RecordService recordService;


    //given
    Record recordData1;
    Record mockRecord1;
    @BeforeEach
    void initGiven() {

        String action1 = "공부";
        String memo1 = "Gradle 공부";
        recordData1 = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        String id = "1";
        mockRecord1 = Record.builder()
                .id(id)
                .action(action1)
                .memo(memo1)
                .build();

    }


    @Test
    @DisplayName(" Record 를 저장할 수 있다.")
    void saveRecord() {
        //given
        given(recordRepository.save(eq(recordData1))).willReturn(mockRecord1);

        //when
        Record savedRecord = recordService.create(recordData1);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedRecord.getId()).isEqualTo(mockRecord1.getId());
            softAssertions.assertThat(savedRecord.getAction()).isEqualTo(mockRecord1.getAction());
            softAssertions.assertThat(savedRecord.getMemo()).isEqualTo(mockRecord1.getMemo());
        });

    }

}

