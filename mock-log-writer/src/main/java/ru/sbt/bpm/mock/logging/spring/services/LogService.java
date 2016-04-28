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

    final QLogsEntity logsEntity = QLogsEntity.logsEntity;

    @Autowired
    LogsRepository logRepository;

    public long getLogsDataBaseSize() {
        return logRepository.count();
    }

    public Iterable<LogsEntity> getLogs(LogsApiEntity apiEntity) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Sort> sorts = new ArrayList<Sort>();
        for (LogsApiColumnEntity logsApiColumnEntity : apiEntity.getLogsApiColumnEntities()) {
            if (logsApiColumnEntity.getData().equals("ts")) {
                handleOtherColumn(apiEntity, logsApiColumnEntity, sorts);
            }
            if (logsApiColumnEntity.getData().equals("protocol")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.protocol, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("systemName")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.systemName, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("integrationPointName")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.integrationPointName, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("fullEndpoint")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.fullEndpoint, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("shortEndpoint")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.shortEndpoint, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("messageState")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.messageState, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("messagePreview")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.messagePreview, predicates, sorts);
            }
            if (logsApiColumnEntity.getData().equals("message")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.message, predicates, sorts);
            }
        }
        int pageSize = apiEntity.getLength();
        int page = apiEntity.getStart()/ pageSize;
        Sort joinedSort = SortUtils.allOf(sorts);
        Predicate joinedPredicate = ExpressionUtils.allOf(predicates);
        PageRequest pageRequest = new PageRequest(page, pageSize, joinedSort);

        return logRepository.findAll(joinedPredicate, pageRequest);
    }

    private void handleOtherColumn(LogsApiEntity jsonEntity, LogsApiColumnEntity logsApiColumnEntity, List<Sort> sorts) {
        handleOrdering(jsonEntity, logsApiColumnEntity,sorts);
    }

    private void handleStringColumn(LogsApiEntity jsonEntity, final LogsApiColumnEntity logsApiColumnEntity, StringPath entityObject, List<Predicate> predicates, List<Sort> sorts) {
        if (logsApiColumnEntity.isSearchable()) {
            if (logsApiColumnEntity.isSearchRegex()) {
                predicates.add(entityObject.matches(logsApiColumnEntity.getSearchValue()));
            } else {
                predicates.add(entityObject.eq(logsApiColumnEntity.getSearchValue()));
            }
        }
        handleOrdering(jsonEntity, logsApiColumnEntity, sorts);
    }

    private void handleOrdering(LogsApiEntity jsonEntity, final LogsApiColumnEntity logsApiColumnEntity, List<Sort> sorts) {
        //TODO replace searching from columns to search from orders
        if (logsApiColumnEntity.isOrderable()) {
            LogsApiOrderEntity logsApiOrderEntity = CollectionUtils.find(jsonEntity.getLogsApiOrderEntities(),
                    new org.apache.commons.collections4.Predicate<LogsApiOrderEntity>() {
                        @Override
                        public boolean evaluate(LogsApiOrderEntity logsJsonOrderEntity) {
                            return logsJsonOrderEntity.getColumnNum() == logsApiColumnEntity.getNum();
                        }
                    });
            Sort.Direction direction = Sort.Direction.fromString(logsApiOrderEntity.getDirection().toString());
            sorts.add(new Sort(direction, logsApiColumnEntity.getData()));
        }
    }

    public void write(LogsEntity entity) {
        logRepository.save(entity);
    }

}
