package ru.sbt.bpm.mock.logging;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.entities.QLogsEntity;
import ru.sbt.bpm.mock.logging.repository.LogsRepository;
import ru.sbt.bpm.mock.logging.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration({"classpath:test-spring-config.xml"})
public class DatabaseTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    LogsRepository logsRepository;

    public void fillData() {
        logsRepository.save(new LogsEntity(
                "someProtocol",
                "systemName",
                "integrationPointName",
                "someFullEndpointName",
                "someShortEndpointName",
                "someMessageState",
                "mes preview",
                "message content"));
    }

    @Test
    public void FindAllTest() throws Exception {
        fillData();
        Iterable<LogsEntity> entities = logsRepository.findAll();
        int count = 0;
        for (LogsEntity logsEntity : entities) {
            System.out.println(logsEntity);
            count++;
        }
        assertTrue(count == 1);
    }

    @Test
    public void FindByShortEndpointName() throws Exception {
        fillData();
        List<LogsEntity> shortEndpointName = logsRepository.findByShortEndpoint("someShortEndpointName");
        assertTrue(shortEndpointName.size() == 1);
    }

    @Test
    public void FindByBooleanExpression() throws Exception {
        fillData();
        QLogsEntity logsEntity = QLogsEntity.logsEntity;
        Iterable<LogsEntity> entities = logsRepository.findAll(
                logsEntity.protocol.equalsIgnoreCase("someProtocol").and(logsEntity.systemName.equalsIgnoreCase("systemName"))
        );
        int count = 0;
        for (LogsEntity entity : entities) {
            System.out.println(entity);
            count++;
        }
        assertTrue(count == 1);
    }

    @Test
    public void FindByPredicate() throws Exception {
        fillData();
        final QLogsEntity logsEntity = QLogsEntity.logsEntity;
        List<Predicate> predicates = new ArrayList<Predicate>() {{
            add(logsEntity.protocol.equalsIgnoreCase("someProtocol"));
            add(logsEntity.systemName.equalsIgnoreCase("systemName"));
        }};
        Predicate where = ExpressionUtils.allOf(predicates);
        Iterable<LogsEntity> entities = logsRepository.findAll(where,logsEntity.ts.desc());
        int count = 0;
        for (LogsEntity entity : entities) {
            System.out.println(entity);
            count++;
        }
        assertTrue(count == 1);
    }

    @Test
    public void FindPageByPredicate() {
        for (int i = 0; i < 10; i++) {
            fillData();
        }
        final QLogsEntity logsEntity = QLogsEntity.logsEntity;
        List<Predicate> predicates = new ArrayList<Predicate>() {{
            add(logsEntity.protocol.equalsIgnoreCase("someProtocol"));
            add(logsEntity.systemName.equalsIgnoreCase("systemName"));
        }};
        List<Sort>sortingList = new ArrayList<Sort>() {{
            add(new Sort(Sort.Direction.ASC,"systemName"));
            add(new Sort(Sort.Direction.DESC,"ts"));
        }};
        Predicate where = ExpressionUtils.allOf(predicates);
        Sort sort = SortUtils.allOf(sortingList);
        Iterable<LogsEntity> entities = logsRepository.findAll(where,new PageRequest(0,7, sort));
        int count = 0;
        for (LogsEntity entity : entities) {
            System.out.println(entity);
            count++;
        }
        assertTrue(count == 7);
    }
}
