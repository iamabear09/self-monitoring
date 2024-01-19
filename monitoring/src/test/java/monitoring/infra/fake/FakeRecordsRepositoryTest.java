package monitoring.infra.fake;

import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FakeRecordsRepositoryTest {

    FakeRecordsRepository fakeRecordsRepository = new FakeRecordsRepository();

    @Test
    @DisplayName("Record 를 저장할 수 있다.")
    void saveRecord() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        //when
        Record saveRecord = fakeRecordsRepository.save(record);
        Record findRecord = fakeRecordsRepository.findById(saveRecord.getRecordId())
                .orElseThrow(() -> new RuntimeException("조회 실패"));

        //then
        Assertions.assertThat(saveRecord).isEqualTo(findRecord);
    }

}