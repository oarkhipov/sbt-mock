package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 14.06.2016.
 */

@Slf4j
@Service
public class XsdAnalysisService {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	MockConfigContainer configContainer;

	@Autowired
	DataFileService dataFileService;

	public Map<String, Resource> getXsdResources () {
		Map<String, Resource> map = new HashMap<String, Resource>();
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems())
			try {
				System.out.println(system.getSystemName() + " xsd: " + dataFileService.getSystemXsdDirectoryResource(system.getSystemName()).getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		return new HashMap<String, Resource>();
	}
}
