package ru.sbt.bpm.mock.spring.logging;

import org.testng.annotations.Test;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class LogFormControllerResponseBuilderTest {

    @Test
    public void testBuild() throws Exception {
        LogsApiEntity logsApiEntity = new LogsApiEntity();
        logsApiEntity.setRequestNum(1);
        ArrayList<LogsEntity> entities = new ArrayList<LogsEntity>();
        entities.add(new LogsEntity("protocolName1", "systemName1", "integrationPointName1", "fullEndpointName1", "shortEndpointName1", "messageState1", "messagePreview1", "message1"));
        String response = new LogControllerResponseBuilder().withLogsQueryEntities(entities).withApiEntity(logsApiEntity).withDataBaseSize(50).build();

        Pattern pattern = Pattern.compile("\"ts\":.*?\".*?\"");

        String expectedResult = "{\n" +
                "  \"draw\": 1,\n" +
                "  \"recordsTotal\": 50,\n" +
                "  \"recordsFiltered\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"ts\": \"someTs\",\n" +
                "      \"protocol\": \"protocolName1\",\n" +
                "      \"systemName\": \"systemName1\",\n" +
                "      \"integrationPointName\": \"integrationPointName1\",\n" +
                "      \"fullEndpoint\": \"fullEndpointName1\",\n" +
                "      \"shortEndpoint\": \"shortEndpointName1\",\n" +
                "      \"messageState\": \"messageState1\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        response = pattern.matcher(response).replaceAll("\"ts\": \"someTs\"");
        assertEquals(response, expectedResult);
    }
}