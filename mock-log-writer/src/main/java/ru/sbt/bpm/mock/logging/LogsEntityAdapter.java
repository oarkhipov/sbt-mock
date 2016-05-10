package ru.sbt.bpm.mock.logging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.StringEscapeUtils;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;

import java.lang.reflect.Type;

/**
 * @author sbt-bochev-as on 05.05.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class LogsEntityAdapter implements JsonSerializer<LogsEntity> {
    @Override
    public JsonElement serialize(LogsEntity entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ts", entity.getTs().toString());
        jsonObject.addProperty("protocol", entity.getProtocol());
        jsonObject.addProperty("systemName", entity.getSystemName());
        jsonObject.addProperty("integrationPointName", entity.getIntegrationPointName());
        jsonObject.addProperty("fullEndpoint", entity.getFullEndpoint());
        jsonObject.addProperty("shortEndpoint", entity.getShortEndpoint());
        jsonObject.addProperty("messageState", entity.getMessageState());
        jsonObject.addProperty("messagePreview", entity.getMessagePreview());
        jsonObject.addProperty("message", StringEscapeUtils.escapeHtml4(entity.getMessage()));
        return jsonObject;
    }
}
