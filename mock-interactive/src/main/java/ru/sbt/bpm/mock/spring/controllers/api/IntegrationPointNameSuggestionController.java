package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.IntegrationPointNameSuggestionService;

/**
 * @author sbt-bochev-as on 18.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Controller
public class IntegrationPointNameSuggestionController {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    IntegrationPointNameSuggestionService suggestionService;

    @ResponseBody
    @RequestMapping(value = "/api/{systemName}/suggestIpName/")
    public String suggestIntegrationPointName(
            @PathVariable("systemName") String systemName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (String name : suggestionService.suggestName(configContainer.getSystemByName(systemName), true)) {
            stringBuilder.append("\"").append(name).append("\", ");
        }
        int length = stringBuilder.length();
        stringBuilder.replace(length - 2, length, "");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
