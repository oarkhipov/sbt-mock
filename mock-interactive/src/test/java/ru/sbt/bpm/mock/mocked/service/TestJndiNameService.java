package ru.sbt.bpm.mock.mocked.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.service.JndiNameService;

/**
 * @author sbt-bochev-as on 09.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Service
@Qualifier("jndiNameService")
public class TestJndiNameService extends JndiNameService {
    @Override
    public boolean isExist(String name) {
        return true;
    }
}
