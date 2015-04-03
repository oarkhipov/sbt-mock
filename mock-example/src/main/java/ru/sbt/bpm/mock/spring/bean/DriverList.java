package ru.sbt.bpm.mock.spring.bean;

import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.MockConfig;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Component
public class DriverList {

    private List<String> list;

    @PostConstruct
    public void init() {
        list = new ArrayList<String>();

    }
}
