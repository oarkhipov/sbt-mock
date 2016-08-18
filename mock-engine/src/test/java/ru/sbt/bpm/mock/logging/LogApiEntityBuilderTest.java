package ru.sbt.bpm.mock.logging;

import org.testng.annotations.Test;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company"," SBT - Moscow
 */
public class LogApiEntityBuilderTest {

    @Test
    public void testBuild() throws Exception {
        Map<String, String[]> httpRequestParams = new HashMap<String, String[]>();

        httpRequestParams.put("draw", new String[]{"2"});
        httpRequestParams.put("columns[0][data]", new String[]{"ts"});
        httpRequestParams.put("columns[0][name]", new String[]{""});
        httpRequestParams.put("columns[0][searchable]", new String[]{"false"});
        httpRequestParams.put("columns[0][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[0][search][value]", new String[]{""});
        httpRequestParams.put("columns[0][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[1][data]", new String[]{"protocol"});
        httpRequestParams.put("columns[1][name]", new String[]{""});
        httpRequestParams.put("columns[1][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[1][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[1][search][value]", new String[]{""});
        httpRequestParams.put("columns[1][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[2][data]", new String[]{"systemName"});
        httpRequestParams.put("columns[2][name]", new String[]{""});
        httpRequestParams.put("columns[2][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[2][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[2][search][value]", new String[]{""});
        httpRequestParams.put("columns[2][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[3][data]", new String[]{"integrationPointName"});
        httpRequestParams.put("columns[3][name]", new String[]{""});
        httpRequestParams.put("columns[3][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[3][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[3][search][value]", new String[]{""});
        httpRequestParams.put("columns[3][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[4][data]", new String[]{"fullEndpoint"});
        httpRequestParams.put("columns[4][name]", new String[]{""});
        httpRequestParams.put("columns[4][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[4][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[4][search][value]", new String[]{""});
        httpRequestParams.put("columns[4][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[5][data]", new String[]{"shortEndpoint"});
        httpRequestParams.put("columns[5][name]", new String[]{""});
        httpRequestParams.put("columns[5][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[5][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[5][search][value]", new String[]{""});
        httpRequestParams.put("columns[5][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[6][data]", new String[]{"messageState"});
        httpRequestParams.put("columns[6][name]", new String[]{""});
        httpRequestParams.put("columns[6][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[6][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[6][search][value]", new String[]{""});
        httpRequestParams.put("columns[6][search][regex]", new String[]{"false"});
        httpRequestParams.put("columns[7][data]", new String[]{"messagePreview"});
        httpRequestParams.put("columns[7][name]", new String[]{""});
        httpRequestParams.put("columns[7][searchable]", new String[]{"true"});
        httpRequestParams.put("columns[7][orderable]", new String[]{"true"});
        httpRequestParams.put("columns[7][search][value]", new String[]{""});
        httpRequestParams.put("columns[7][search][regex]", new String[]{"false"});
        httpRequestParams.put("order[0][column]", new String[]{"0"});
        httpRequestParams.put("order[0][dir]", new String[]{"asc"});
        httpRequestParams.put("start", new String[]{"0"});
        httpRequestParams.put("length", new String[]{"10"});
        httpRequestParams.put("search[value]", new String[]{"a"});
        httpRequestParams.put("search[regex]", new String[]{"false"});
        httpRequestParams.put("_", new String[]{"1461684078911"});

        LogsApiEntity builtEntity = new LogApiEntityBuilder().with(httpRequestParams).build();
        assertEquals(builtEntity.getLogsApiColumnEntities().size(),8);
        assertEquals(builtEntity.getLogsApiOrderEntities().size(),1);
    }
}