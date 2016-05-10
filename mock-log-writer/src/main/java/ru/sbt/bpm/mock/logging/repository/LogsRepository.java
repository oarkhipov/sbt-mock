package ru.sbt.bpm.mock.logging.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public interface LogsRepository extends PagingAndSortingRepository<LogsEntity, Timestamp>, QueryDslPredicateExecutor<LogsEntity> {

    List<LogsEntity> findByShortEndpoint(String shortEndpoint);

    LogsEntity findByTs(Timestamp timestamp);
}
