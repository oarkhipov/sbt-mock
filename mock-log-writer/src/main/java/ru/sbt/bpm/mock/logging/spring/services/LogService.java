package ru.sbt.bpm.mock.logging.spring.services;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.StringPath;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.logging.entities.*;
import ru.sbt.bpm.mock.logging.repository.LogsRepository;
import ru.sbt.bpm.mock.logging.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 22.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class LogService {

    @Autowired
    LogsRepository logRepository;

    public Iterable<LogsEntity> getLogs(LogsJsonEntity jsonEntity) {
        final QLogsEntity logsEntity = QLogsEntity.logsEntity;
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Sort> sorts = new ArrayList<Sort>();
        for (LogsJsonColumnEntity logsJsonColumnEntity : jsonEntity.getLogsJsonColumnEntities()) {
            if (logsJsonColumnEntity.getData().equals("ts")) {
                handleOtherColumn(jsonEntity, logsJsonColumnEntity, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("protocol")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.protocol, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("systemName")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.systemName, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("integrationPointName")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.integrationPointName, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("fullEndpoint")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.fullEndpoint, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("shortEndpoint")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.shortEndpoint, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("messageState")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.messageState, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("messagePreview")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.messagePreview, predicates, sorts);
            }
            if (logsJsonColumnEntity.getData().equals("message")) {
                handleStringColumn(jsonEntity, logsJsonColumnEntity, logsEntity.message, predicates, sorts);
            }
        }
        int pageSize = jsonEntity.getLength();
        int page = jsonEntity.getStart()/ pageSize;
        Sort joinedSort = SortUtils.allOf(sorts);
        Predicate joinedPredicate = ExpressionUtils.allOf(predicates);
        PageRequest pageRequest = new PageRequest(page, pageSize, joinedSort);

        return logRepository.findAll(joinedPredicate, pageRequest);
    }

    private void handleOtherColumn(LogsJsonEntity jsonEntity, LogsJsonColumnEntity logsJsonColumnEntity, List<Sort> sorts) {
        handleOrdering(jsonEntity,logsJsonColumnEntity,sorts);
    }

    private void handleStringColumn(LogsJsonEntity jsonEntity, final LogsJsonColumnEntity logsJsonColumnEntity, StringPath entityObject, List<Predicate> predicates, List<Sort> sorts) {
        if (logsJsonColumnEntity.isSearchable()) {
            if (logsJsonColumnEntity.isSearchRegex()) {
                predicates.add(entityObject.matches(logsJsonColumnEntity.getSearchValue()));
            } else {
                predicates.add(entityObject.eq(logsJsonColumnEntity.getSearchValue()));
            }
        }
        handleOrdering(jsonEntity, logsJsonColumnEntity, sorts);
    }

    private void handleOrdering(LogsJsonEntity jsonEntity, final LogsJsonColumnEntity logsJsonColumnEntity, List<Sort> sorts) {
        if (logsJsonColumnEntity.isOrderable()) {
            LogsJsonOrderEntity logsJsonOrderEntity = CollectionUtils.find(jsonEntity.getLogsJsonOrderEntities(),
                    new org.apache.commons.collections4.Predicate<LogsJsonOrderEntity>() {
                        @Override
                        public boolean evaluate(LogsJsonOrderEntity logsJsonOrderEntity) {
                            return logsJsonOrderEntity.getColumnNum() == logsJsonColumnEntity.getNum();
                        }
                    });
            Sort.Direction direction = Sort.Direction.fromString(logsJsonOrderEntity.getDirection().toString());
            sorts.add(new Sort(direction, logsJsonColumnEntity.getData()));
        }
    }

    public void write(LogsEntity entity) {
        logRepository.save(entity);
    }

}
