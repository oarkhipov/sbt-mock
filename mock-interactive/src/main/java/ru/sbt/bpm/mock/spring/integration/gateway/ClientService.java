package ru.sbt.bpm.mock.spring.integration.gateway;

import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

/**
 * Created by sbt-bochev-as on 17.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public interface  ClientService {

    /**
     * Entry to the messaging system. All invocations to this method will be intercepted and sent to the SI "system entry" gateway
     *
     * @param request
     */
    String send(String request);

    MockMessage sendMockMessage(String request);
}
