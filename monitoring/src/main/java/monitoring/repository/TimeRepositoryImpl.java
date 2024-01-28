package monitoring.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import monitoring.domain.QRecord_Time;
import monitoring.domain.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static monitoring.domain.QRecord.record;
import static monitoring.domain.QRecord_Time.*;

public class TimeRepositoryImpl implements TimeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TimeRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Record.Time> search(TimeSearchCond condition) {
        return jpaQueryFactory
                .select(time)
                .from(time)
                .join(time.record, record).fetchJoin()
                .where(actionEq(condition.getAction()),
                        dateEqAndBetweenTime(condition.getDate(), condition.getTime()))
                .fetch();
    }

    private BooleanExpression actionEq(String actionCond) {
        return actionCond != null ? time.record.action.containsIgnoreCase(actionCond) : null;
    }

    private BooleanExpression dateEqAndBetweenTime(LocalDate dateCond, LocalTime timeCond) {
        if (dateCond == null) return null;
        BooleanExpression dataCond = time.date.eq(dateCond);

        return timeCond != null ? dataCond.and(time.startTime.loe(timeCond)
                .and(time.endTime.goe(timeCond))): dataCond;
    }
}
