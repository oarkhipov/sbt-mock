package ru.sbt.bpm.mock.spring.bean.impl;

import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.bean.MockList;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by sbt-bochev-as on 06.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class MockListImpl extends MockDriverList implements MockList {
    private List<String> list;

    @PostConstruct
    public void init() {
        list = initList("Mock");
    }

    @Override
    public List<String> getList() {
        return list;
    }
}
