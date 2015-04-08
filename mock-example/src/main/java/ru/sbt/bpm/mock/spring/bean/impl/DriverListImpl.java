package ru.sbt.bpm.mock.spring.bean.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.spring.bean.DriverList;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class DriverListImpl implements DriverList {

    private List<String> list;

    @Autowired
    MockConfigContainer configContainer;

    @PostConstruct
    public void init() {
        list = new ArrayList<String>();

        MockConfig mockConfig = configContainer.getConfig();

        for (SystemTag system : mockConfig.getSystems().getListOfSystems()) {
            for (IntegrationPoint integrationPoint : system.getListIntegrationPoint()) {
                String type = integrationPoint.getIntegrationPointType();
                if (type.equals("Driver")) {
                    list.add(system.getSystemName() + "_" + integrationPoint.getIntegrationPointName());
                }
            }
        }
    }

    @Override
    public List<String> getList() {
        return list;
    }
}
