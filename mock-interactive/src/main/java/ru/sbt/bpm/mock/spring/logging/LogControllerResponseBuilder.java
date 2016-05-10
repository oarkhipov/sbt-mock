package ru.sbt.bpm.mock.spring.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.spring.logging.pojo.LogResponseDataEntity;
import ru.sbt.bpm.mock.spring.logging.pojo.LogResponseEntity;

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
        if (logsApiEntity!= null && logsEntities != null) {
            List<LogResponseDataEntity> dataEntities = new ArrayList<LogResponseDataEntity>();
            for (LogsEntity logsEntity : logsEntities) {
                dataEntities.add(new LogResponseDataEntity(logsEntity));
            }
            LogResponseEntity logResponseEntity = new LogResponseEntity();
            LogResponseDataEntity[] dataArray = dataEntities.toArray(new LogResponseDataEntity[dataEntities.size()]);
            logResponseEntity.setData(dataArray);
            logResponseEntity.setDraw(logsApiEntity.getRequestNum());
            logResponseEntity.setRecordsFiltered(dataEntities.size());
            logResponseEntity.setRecordsTotal(dataBaseSize);

            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String response = gson.toJson(logResponseEntity);
            return response;
        } else {
            return null;
        }
    }
}
