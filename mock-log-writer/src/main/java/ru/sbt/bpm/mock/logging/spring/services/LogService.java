/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.logging.spring.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.StringPath;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.logging.LogsEntityAdapter;
import ru.sbt.bpm.mock.logging.entities.*;
import ru.sbt.bpm.mock.logging.repository.LogsRepository;
import ru.sbt.bpm.mock.logging.utils.SortUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 22.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@Service
public class LogService {

    private final QLogsEntity logsEntity = QLogsEntity.logsEntity;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(LogsEntity.class, new LogsEntityAdapter()).setPrettyPrinting().create();

    @Autowired
    LogsRepository logRepository;

    @Setter
    Integer maxRowsCount;

    public long getLogsDatabaseSize() {
        return logRepository.count();
    }

    public Tuple2<List<LogsEntity>, Long> getLogs(LogsApiEntity apiEntity) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Sort> sorts = new ArrayList<Sort>();

        handleColumnPredicates(apiEntity, predicates, sorts);
        handleOrderingPredicates(apiEntity, sorts);
        handleFullSearch(apiEntity, predicates);

        int pageSize = apiEntity.getLength();
        int page = apiEntity.getStart() / pageSize;
        Sort joinedSort = SortUtils.allOf(sorts);
        Predicate joinedPredicate = ExpressionUtils.allOf(predicates);
        PageRequest pageRequest = new PageRequest(page, pageSize, joinedSort);
        log.debug("Search rows with predicate: " + joinedPredicate);
        return Tuple.of(logRepository.findAll(joinedPredicate, pageRequest).getContent(), logRepository.count(joinedPredicate));
    }

    private void handleFullSearch(LogsApiEntity logsApiEntity, List<Predicate> predicates) {
        String searchValue = logsApiEntity.getSearchValue();
        if (searchValue != null && !searchValue.isEmpty()) {
            List<Predicate> fullSearchPredicates = new ArrayList<Predicate>();
            for (String searchValuePart : searchValue.split("\\|")) {
                if (logsApiEntity.isSearchRegex()) {
                    fullSearchPredicates.add(logsEntity.transactionId.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.protocol.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.systemName.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.integrationPointName.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.fullEndpoint.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.shortEndpoint.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.messageState.containsIgnoreCase(searchValuePart));
                    fullSearchPredicates.add(logsEntity.message.containsIgnoreCase(searchValuePart));
                } else {
                    fullSearchPredicates.add(logsEntity.transactionId.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.protocol.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.systemName.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.integrationPointName.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.fullEndpoint.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.shortEndpoint.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.messageState.eq(searchValuePart));
                    fullSearchPredicates.add(logsEntity.message.eq(searchValuePart));
                }
            }
            predicates.add(ExpressionUtils.anyOf(fullSearchPredicates));
        }
    }

    private void handleColumnPredicates(LogsApiEntity apiEntity, List<Predicate> predicates, List<Sort> sorts) {
        for (LogsApiColumnEntity logsApiColumnEntity : apiEntity.getLogsApiColumnEntities()) {
//            if (logsApiColumnEntity.getData().equals("ts")) {
//                // do nothing
//            }
            if (logsApiColumnEntity.getData().equals("transactionId")) {
                handleStringColumn(apiEntity, logsApiColumnEntity, logsEntity.transactionId, predicates, sorts);
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
    }


    private void handleStringColumn(LogsApiEntity jsonEntity, final LogsApiColumnEntity logsApiColumnEntity, StringPath entityObject, List<Predicate> predicates, List<Sort> sorts) {
        if (logsApiColumnEntity.isSearchable()) {
            String searchValue = logsApiColumnEntity.getSearchValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                if (logsApiColumnEntity.isSearchRegex()) {
                    log.debug("replace regex with like for H2! Fix it if  DB change is planning");
                    searchValue = searchValue.replace("$", "%").replace("^", "%");
                    predicates.add(entityObject.matches(searchValue));
                } else {
                    predicates.add(entityObject.eq(searchValue));
                }
            }
        }
        handleOrderingPredicates(jsonEntity, sorts);
    }

    private void handleOrderingPredicates(LogsApiEntity jsonEntity, List<Sort> sorts) {

        for (LogsApiOrderEntity apiOrderEntity : jsonEntity.getLogsApiOrderEntities()) {
            final int apiOrderEntityColumnNum = apiOrderEntity.getColumnNum();
            Sort.Direction direction = Sort.Direction.fromString(apiOrderEntity.getDirection().toString());

            LogsApiColumnEntity logsApiColumnEntity = CollectionUtils.find(jsonEntity.getLogsApiColumnEntities(), new org.apache.commons.collections4.Predicate<LogsApiColumnEntity>() {
                @Override
                public boolean evaluate(LogsApiColumnEntity logsApiColumnEntity) {
                    return logsApiColumnEntity.getNum() == apiOrderEntityColumnNum;
                }
            });
            sorts.add(new Sort(direction, logsApiColumnEntity.getData()));
        }
    }

    public void write(LogsEntity entity) {
        logRepository.save(entity);
        log.info("Log saved [" + entity.getSystemName() + "]");
        fitToLimit();
    }

    private void fitToLimit() {
        if (maxRowsCount != null) {
            boolean cleaningFinished = false;
            Page<LogsEntity> overLimitItems = getOverLimitItemsPage();

            while (!cleaningFinished) {
                logRepository.delete(overLimitItems.getContent());
                overLimitItems = getOverLimitItemsPage();

                if (overLimitItems.getContent().size() == 0) {
                    cleaningFinished = true;
                }

            }

        }
    }

    private Page<LogsEntity> getOverLimitItemsPage() {
        return logRepository.findAll(new PageRequest(1, maxRowsCount, new Sort(Sort.Direction.DESC, "ts")));
    }

    public String getTopLogs(int rowNumbers, String systemName, String integrationPointName) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (systemName != null && !systemName.isEmpty()) {
            predicates.add(logsEntity.systemName.matches(systemName));
        }
        if (integrationPointName != null && !integrationPointName.isEmpty()) {
            predicates.add(logsEntity.integrationPointName.matches(integrationPointName));
        }
        Page<LogsEntity> entities = logRepository.findAll(ExpressionUtils.allOf(predicates), new PageRequest(0, rowNumbers, new Sort(Sort.Direction.DESC, "ts")));
        List<LogsEntity> logsEntities = IteratorUtils.toList(entities.iterator());
        return gson.toJson(logsEntities);
    }

    public String getMessageByTs(String stringTimestamp) {
        Timestamp timestamp = Timestamp.valueOf(stringTimestamp);
        return logRepository.findByTs(timestamp).getMessage();
    }
}
