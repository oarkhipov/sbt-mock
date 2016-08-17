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

package ru.sbt.bpm.mock.spring.logging;

import ru.sbt.bpm.mock.logging.entities.LogsApiColumnEntity;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsApiOrderEntity;
import ru.sbt.bpm.mock.logging.enums.OrderDirection;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author sbt-bochev-as on 27.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class LogApiEntityBuilder {


    private SortedMap<String, String[]> httpRequestParams;
    private LogsApiEntity entity;

    public LogApiEntityBuilder() {
    }

    public LogApiEntityBuilder with(Map<String, String[]> httpRequestParams) {
        this.httpRequestParams = new TreeMap<String, String[]>(httpRequestParams);
        return this;
    }

    public LogsApiEntity build() {
        if (httpRequestParams == null) {
            throw new IllegalStateException("No parameters specified!");
        }
        entity = new LogsApiEntity();

        entity.setRequestNum(Integer.parseInt(httpRequestParams.get("draw")[0]));
        entity.setStart(Integer.parseInt(httpRequestParams.get("start")[0]));
        entity.setLength(Integer.parseInt(httpRequestParams.get("length")[0]));

        handleColumnsParameters();
        handleOrderParameters();
        handleSearchParameters();
        return entity;
    }

    private void handleSearchParameters() {
        if (httpRequestParams.subMap("search","search"+Character.MAX_VALUE).keySet().size()!=0) {
            entity.setSearchValue(httpRequestParams.get("search[value]")[0]);
            entity.setSearchRegex(Boolean.parseBoolean(httpRequestParams.get("search[regex]")[0]));
        }
    }

    private void handleOrderParameters() {
        SortedMap<String, String[]> orderMap = httpRequestParams.subMap("order", "order" + Character.MAX_VALUE);
        assert orderMap.size()%2==0;
        int ordersCount = orderMap.size()/2;
        ArrayList<LogsApiOrderEntity> logsApiOrderEntities = new ArrayList<LogsApiOrderEntity>();
        for (int i = 0; i < ordersCount; i++) {
            LogsApiOrderEntity orderEntity = new LogsApiOrderEntity();
            orderEntity.setColumnNum(Integer.parseInt(orderMap.get("order[" + i + "][column]")[0]));
            orderEntity.setDirection(OrderDirection.valueOf(orderMap.get("order[" + i + "][dir]")[0].toUpperCase()));
            logsApiOrderEntities.add(orderEntity);
        }
        entity.setLogsApiOrderEntities(logsApiOrderEntities);
    }


    private void handleColumnsParameters() {
        SortedMap<String, String[]> columnsMap = httpRequestParams.subMap("columns", "columns" + Character.MAX_VALUE);
        assert columnsMap.size()%6==0;
        ArrayList<LogsApiColumnEntity> logsApiColumnEntities = new ArrayList<LogsApiColumnEntity>();
        int columnsCount = columnsMap.size()/6;
        for (int i = 0; i < columnsCount; i++) {
            LogsApiColumnEntity columnEntity = new LogsApiColumnEntity();
            columnEntity.setNum(i);
            columnEntity.setData(columnsMap.get("columns[" + i + "][data]")[0]);
            columnEntity.setName(columnsMap.get("columns[" + i + "][name]")[0]);
            columnEntity.setSearchable(Boolean.parseBoolean(columnsMap.get("columns[" + i + "][searchable]")[0]));
            columnEntity.setOrderable(Boolean.parseBoolean(columnsMap.get("columns[" + i + "][orderable]")[0]));

            columnEntity.setSearchValue(columnsMap.get("columns[" + i + "][search][value]")[0]);
            columnEntity.setSearchRegex(Boolean.parseBoolean(columnsMap.get("columns[" + i + "][search][regex]")[0]));
            logsApiColumnEntities.add(columnEntity);
        }
        entity.setLogsApiColumnEntities(logsApiColumnEntities);
    }
}
