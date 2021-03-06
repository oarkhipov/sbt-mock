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

package ru.sbt.bpm.mock.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.pojo.LogResponseDataEntity;
import ru.sbt.bpm.mock.logging.pojo.LogResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class LogControllerResponseBuilder {

    private LogsApiEntity logsApiEntity;
    private Iterable<LogsEntity> logsEntities;
    private long dataBaseSize;
    private long filteredRecordsSize;

    public LogControllerResponseBuilder() {
    }

    public LogControllerResponseBuilder withApiEntity(LogsApiEntity logsApiEntity) {
        this.logsApiEntity = logsApiEntity;
        return this;
    }

    public LogControllerResponseBuilder withLogsQueryEntities(Iterable<LogsEntity> entities) {
        logsEntities = entities;
        return this;
    }

    public LogControllerResponseBuilder withDataBaseSize(long dataBaseSize) {
        this.dataBaseSize = dataBaseSize;
        return this;
    }

    public String build() {
        if (logsApiEntity != null && logsEntities != null) {
            List<LogResponseDataEntity> dataEntities = new ArrayList<LogResponseDataEntity>();
            for (LogsEntity logsEntity : logsEntities) {
                dataEntities.add(new LogResponseDataEntity(logsEntity));
            }
            LogResponseEntity logResponseEntity = new LogResponseEntity();
            LogResponseDataEntity[] dataArray = dataEntities.toArray(new LogResponseDataEntity[dataEntities.size()]);
            logResponseEntity.setData(dataArray);
            logResponseEntity.setDraw(logsApiEntity.getRequestNum());
            logResponseEntity.setRecordsFiltered(filteredRecordsSize);
            logResponseEntity.setRecordsTotal(dataBaseSize);

            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            return gson.toJson(logResponseEntity);
        } else {
            return null;
        }
    }

    public LogControllerResponseBuilder withFilteredRecordsCount(Long recordsCount) {
        filteredRecordsSize = recordsCount;
        return this;
    }
}
