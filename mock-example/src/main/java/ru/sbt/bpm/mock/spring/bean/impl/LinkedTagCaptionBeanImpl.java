package ru.sbt.bpm.mock.spring.bean.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.LinkedTag;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.spring.bean.LinkedTagCaption;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-bochev-as on 08.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class LinkedTagCaptionBeanImpl implements LinkedTagCaption{

//    <System_InterationPointName, linkedTagCaption>
    private Map<String, String> captionMap;

    @Autowired
    MockConfigContainer configContainer;

    /**
     * Инициализация карты <System_InterationPointName, linkedTagCaption>
     */
    @PostConstruct
    public void init() {
        captionMap = new HashMap<String, String>();

        MockConfig mockConfig = configContainer.getConfig();
        for (SystemTag system : mockConfig.getListOfSystems()) {
            for (IntegrationPoint integrationPoint : system.getListIntegrationPoint()) {
                List<String> integrationPointCaptionList = new ArrayList<String>();
                if(integrationPoint.getLinkedTagSequence()!=null) {
                    for (LinkedTag linkedTag : integrationPoint.getLinkedTagSequence().getListOfLinkedTags()) {
                        integrationPointCaptionList.add(linkedTag.getTag());
                    }

                    String captionString = integrationPointCaptionList.toString();

//                Used so strange replace code because of Large size of Guava lib for using canonical join function
//                1.5Mb library - are you serious?
                    captionString = captionString.substring(1,captionString.length()-1).replace(", ","/");

                    captionMap.put(system.getSystemName() + "_" + integrationPoint.getIntegrationPointName(),
                            captionString);
                }
            }

        }
    }

    /**
     * Возвращает linked tag по имени точки интеграции
     *
     * @param SystemIntegrationPointName название точки интеграции <System_InterationPointName, linkedTagCaption>
     * @return linkedTag без namespace
     */
    @Override
    public String getCaption(String SystemIntegrationPointName) {
        return captionMap.get(SystemIntegrationPointName);
    }
}
