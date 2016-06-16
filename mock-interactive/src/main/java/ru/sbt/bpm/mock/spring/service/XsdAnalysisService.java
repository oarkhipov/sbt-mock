package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

	private static final String XPATH_SCHEMA = "//*[local-name()='schema']/@targetNamespace";

	public Set<String> getNamespaceFromXSD () throws IOException, SaxonApiException {
		Set<String> setXsdNamespace = new TreeSet<String>(new Comparator<String>() {
			@Override
			public int compare (String o1, String o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return -1;
				if (o2 == null)
					return 1;
				return o1.compareTo(o2);
			}
		});
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			log.info("====================================================================");
			log.info("");
			log.info(String.format("      READ XSD FILES FOR SYSTEM: %s", systemName));
			log.info("");
			log.info("====================================================================");
			for (File xsdFile : map.get(systemName)) {
				log.info("Read file: " + xsdFile.getName());
				// FIXME если файл сохранен в кодировке "UTF-8 with BOM" выдается ошибка чтения файла
				XdmValue xdmValue = XpathUtils.evaluateXpath(readFile(xsdFile), XPATH_SCHEMA);
				// FIXME подумать над исправлением данного куска кода
				if (xdmValue.size() == 0)
					continue;
				XdmNode xdmNode = (XdmNode) xdmValue;
				String namespace = xdmNode.getStringValue();
				log.info(String.format("Namespace: %s", namespace));
				setXsdNamespace.add(namespace);
			}
		}
		return setXsdNamespace;
	}

	private String readFile(File file) throws IOException {
		return FileUtils.readFileToString(file);
	}

	/**
	 * Получение списка xsd файлов по системе
	 * @return map<система, список xsd файлов>
	 * @throws IOException
	 */
	private Map<String, List<File>> getXsdFilesFromSystems() throws IOException {
		Map<String, List<File>> map = new HashMap<String, List<File>>();
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			String systemName = system.getSystemName();
			log.info("====================================================================");
			log.info("");
			log.info(String.format("              GET XSD FILES FOR SYSTEM: %s", systemName));
			log.info("");
			log.info("====================================================================");
			List<File> filesFromDir = getFilesFromDir(dataFileService.getSystemXsdDirectoryResource(systemName).getFile().getCanonicalFile());
			log.info(String.format("Files: %s", filesFromDir));
			map.put(systemName, filesFromDir);
		}
		return map;
	}

	/**
	 * Получение списка xsd фалов для системы
	 * @param dir - директория xsd файлов для системы
	 * @return список xsd файлов системы
	 */
	private List<File> getFilesFromDir(File dir) {
		List<File> listFiles = new ArrayList<File>();
		for (File fileEntry : dir.listFiles())
			if (fileEntry.isDirectory())
				listFiles.addAll(getFilesFromDir(fileEntry));
			else
				listFiles.add(fileEntry);
		return listFiles;
	}
}
