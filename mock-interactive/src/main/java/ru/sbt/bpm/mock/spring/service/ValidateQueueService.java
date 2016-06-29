package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@Service
public class ValidateQueueService {

    public boolean valid(String queueName) {
        try {
            Context cnt = new InitialContext();
            Object tmp = cnt.lookup(queueName);
            return tmp != null;
        } catch (NamingException e) {
            log.error(String.format("JNDI name %s does not exist!", queueName), e);
            return false;
        }
    }
}
