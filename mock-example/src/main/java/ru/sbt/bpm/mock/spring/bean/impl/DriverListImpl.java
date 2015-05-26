package ru.sbt.bpm.mock.spring.bean.impl;

import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.bean.DriverList;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class DriverListImpl extends MockDriverList implements DriverList {
    private List<String> list;

    /**
     * Инициализация списка драйверов
     */
    @PostConstruct
    public void init() {
        list = initList("Driver");
    }

    /**
     * Получение списка драйверов
     *
     * @return список названий
     */
    @Override
    public List<String> getList() {
        return list;
    }
}
