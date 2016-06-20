package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 20.06.2016.
 */


@Slf4j
@Service
public class XsdElementsAnalysisService {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	MockConfigContainer configContainer;

	@Autowired
	DataFileService dataFileService;

	private static final String xPath = "//*[local-name()='schema']/@*";

	public void getElements() throws IOException, SaxonApiException {
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			log.info("====================================================================");
			log.info("");
			log.info(String.format("      READ XSD FILES FOR SYSTEM: %s", systemName));
			log.info("");
			log.info("====================================================================");
			for (File xsdFile : map.get(systemName)) {
				String inputXml = readFileWithoutBOM(xsdFile);
				XdmValue xdmValue = XpathUtils.evaluateXpath(inputXml, xPath);
				System.out.println("Size of xdm valeu: " + xdmValue.size());
				for (int i = 0; i < xdmValue.size(); i++) {
					System.out.println(xdmValue.itemAt(i).getStringValue());
				}
			}
		}
	}

	private String readFileWithoutBOM(File file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(file), false)));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line);
		return sb.toString();
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
