package jhp.monitoring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "timeLogs") @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Record {

    @Id
    @Column(name = "record_id")
    private String id;
    private String action;
    private String memo;

    @OneToMany(mappedBy = "record")
    private List<TimeLog> timeLogs = new ArrayList<>();

    @Builder
    public Record(String id, String action, String memo) {
        this.id = id;
        this.action = action;
        this.memo = memo;
    }
}
