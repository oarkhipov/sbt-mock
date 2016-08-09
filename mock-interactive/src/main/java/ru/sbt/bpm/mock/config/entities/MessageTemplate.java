package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;
import ru.sbt.bpm.mock.config.serialization.CdataValue;

import java.util.UUID;

/**
 * @author sbt-bochev-as on 28.07.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Data
@XStreamAlias("messageTemplate")
public class MessageTemplate {

    @XStreamAlias("id")
    private UUID templateId;

    @XStreamAlias("caption")
    private String caption;

    @XStreamAlias("dispatcherType")
    private DispatcherTypes dispatcherType;

    @CdataValue
    @XStreamAlias("dispatcherExpression")
    private String dispatcherExpression;

    @XStreamAlias("regexGroups")
    private String regexGroups;

    @XStreamAlias("value")
    private String value;

    public MessageTemplate() {
        templateId = UUID.randomUUID();
    }

    public MessageTemplate(String caption, DispatcherTypes dispatcherType, String dispatcherExpression, String regexGroups, String value) {
        templateId = UUID.randomUUID();
        this.caption = caption;
        this.dispatcherType = dispatcherType;
        this.dispatcherExpression = dispatcherExpression;
        this.regexGroups = regexGroups;
        this.value = value;
    }
}
