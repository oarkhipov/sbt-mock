package ru.sbt.bpm.mock.spring.logging;

import org.testng.annotations.Test;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class LogControllerResponseBuilderTest {

    @Test
    public void testBuild() throws Exception {
        LogsApiEntity logsApiEntity = new LogsApiEntity();
        logsApiEntity.setRequestNum(1);
        ArrayList<LogsEntity> entities = new ArrayList<LogsEntity>();
        entities.add(new LogsEntity("protocolName1", "systemName1", "integrationPointName1", "fullEndpointName1", "shortEndpointName1", "messageState1", "messagePreview1", "message1"));
        String response = new LogControllerResponseBuilder().withLogsQueryEntities(entities).withApiEntity(logsApiEntity).withDataBaseSize(50).build();
        String expectedResult = "{\"draw\":1,\"recordsTotal\":50,\"recordsFiltered\":1,\"data\":[{\"ts\":\"2016-04-28 12:09:50.162\",\"protocol\":\"protocolName1\",\"systemName\":\"systemName1\",\"integrationPointName\":\"integrationPointName1\",\"fullEndpointName\":\"fullEndpointName1\",\"shortEndpointName\":\"shortEndpointName1\",\"messageState\":\"messageState1\",\"messagePreview\":\"messagePreview1\"}]}";
        expectedResult = expectedResult.replaceAll("\"ts\":\".*?\"","\"ts\":\"someTs\"");
        response = response.replaceAll("\"ts\":\".*?\"","\"ts\":\"someTs\"");
        assertEquals(response, expectedResult);
    }
}