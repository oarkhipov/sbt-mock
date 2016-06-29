package ru.sbt.bpm.mock.spring.service;

import com.sun.java.xml.ns.j2Ee.JndiNameType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@Service
public class ValidateQueueService {

    @Autowired
    ApplicationContext appCon;

    public boolean valid(String queue) throws NamingException {
        Boolean isValid = false;
        Object tmp = null;

        try {
            tmp = appCon.getBean(queue);

            String test = tmp.toString();



            if (tmp != null) {
                isValid = true;
                log.info("=========================================================================================");
                log.info("");
                log.info("								WE GET QUEUE NAMED: " + test + " "+ isValid);
                log.info("");
                log.info("=========================================================================================");

            } else {
                isValid = false;
            }
        }catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e){
            log.info("=========================================================================================");
            log.info("");
            log.info("				WE DOESN'T HAVE QUEUE NAMED " + queue + " "+ isValid);
            log.info("");
            log.info("=========================================================================================");

        }
        return isValid;
    }
}
