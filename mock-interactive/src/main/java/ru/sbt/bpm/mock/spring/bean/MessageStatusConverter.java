package ru.sbt.bpm.mock.spring.bean;

import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.bean.enums.MessageStatus;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

/**
 * @author sbt-bochev-as on 25.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class MessageStatusConverter {
    public static MessageStatus convert(MockMessage mockMessage, MessageType messageType) {
        if (mockMessage.getSystem()==null || mockMessage.getIntegrationPoint()==null) {
            return MessageStatus.RCVPE;
        }
        if (!mockMessage.isSendMessage()) {
            return MessageStatus.NANS;
        }
        if (mockMessage.isFaultMessage()) {
            return (messageType == MessageType.RS) != mockMessage.getIntegrationPoint().isDriver()? MessageStatus.SNTVE: MessageStatus.RCVVE;
        }
        return (messageType == MessageType.RS) != mockMessage.getIntegrationPoint().isDriver()? MessageStatus.SNT: MessageStatus.RCV;
    }
}
