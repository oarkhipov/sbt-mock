package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@Service
public class JndiNameService {

    public boolean isExist(String name) {
        try {
            Context cnt = new InitialContext();
            Object tmp = cnt.lookup(name);
            return tmp != null;
        } catch (NamingException e) {
            //No need to print stack trace
            log.error(String.format("JNDI name %s does not exist!", name));
            return false;
        }
    }
}
