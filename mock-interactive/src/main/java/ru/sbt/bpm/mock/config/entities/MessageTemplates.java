package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import ru.sbt.bpm.mock.config.container.MovableList;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author sbt-bochev-as on 28.07.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Data
@XStreamAlias("messageTemplates")
public class MessageTemplates {
    @XStreamImplicit(itemFieldName = "messageTemplate")
    private MovableList<MessageTemplate> messageTemplateList = initList();

    public static MovableList<MessageTemplate> initList() {
        return new MovableList<MessageTemplate>();
    }

    public MessageTemplate findMessageTemplateByUUID(UUID templateUuid) {
        for (MessageTemplate messageTemplate : messageTemplateList) {
            if (messageTemplate.getTemplateId().equals(templateUuid)) {
                return messageTemplate;
            }
        }
        throw new NoSuchElementException("No template with UUID [" + templateUuid.toString() + "]");
    }

    public MovableList<MessageTemplate> getMessageTemplateList() {
        if (messageTemplateList == null) {
            messageTemplateList = initList();
        }
        return messageTemplateList;
    }

    public List<MessageTemplate> getSequenceTemplateList() {
        LinkedList<MessageTemplate> sequenceTemplates = new LinkedList<MessageTemplate>();
        for (MessageTemplate messageTemplate : messageTemplateList) {
            if (messageTemplate.getDispatcherType().equals(DispatcherTypes.SEQUENCE)) {
                sequenceTemplates.add(messageTemplate);
            }
        }
        return sequenceTemplates;
    }
}
