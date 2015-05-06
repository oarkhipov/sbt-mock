package ru.sbt.bpm.mock.spring.bean.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-bochev-as on 06.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
public abstract class MockDriverList {

    @Autowired
    MockConfigContainer configContainer;

    public List<String> initList(String typeConst) {
        List<String> list = new ArrayList<String>();

        MockConfig mockConfig = configContainer.getConfig();

        for (SystemTag system : mockConfig.getSystems().getListOfSystems()) {
            for (IntegrationPoint integrationPoint : system.getListIntegrationPoint()) {
                String type = integrationPoint.getIntegrationPointType();
                if (type.equals(typeConst)) {
                    list.add(system.getSystemName() + "_" + integrationPoint.getIntegrationPointName());
                }
            }
        }
        return list;
    }
}
