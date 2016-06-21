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
import java.util.*;

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

	// xPath
	private static final String XPATH_TARGET_NAMESPACE       = "//*[local-name()='schema']/@targetNamespace";
	private static final String XPATH_ELEMENT_ATTRIBUTE_NAME = "//*[local-name()='element']/@name";
	private static final String XPATH_ELEMENT_ATTRIBUTE_REF  = "//*[local-name()='element']/@ref";

	// String comparator for map and set
	private static final Comparator<String> STRING_COMPARATOR = new Comparator<String>() {
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
	};

	public Map<String, Set<String>> getElementsFromXsd() throws IOException, SaxonApiException {
		Map<String, Set<String>> mapElementsByNamespace = new TreeMap<String, Set<String>>(STRING_COMPARATOR);
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			log.info("====================================================================");
			log.info("");
			log.info(String.format("      READ XSD FILES FOR SYSTEM: %s", systemName));
			log.info("");
			log.info("====================================================================");
			for (File xsdFile : map.get(systemName)) {
				String inputXml = readFileWithoutBOM(xsdFile);
				mapElementsByNamespace.putAll(getNamespaceByxPath(inputXml, XPATH_TARGET_NAMESPACE, XPATH_ELEMENT_ATTRIBUTE_NAME));
			}
		}
		return mapElementsByNamespace;
	}

	private Set<String> getElementFromXsd(String inputXml, String xPath) throws SaxonApiException {
		Set<String> set = new TreeSet<String>(STRING_COMPARATOR);
		XdmValue xdmValue = XpathUtils.evaluateXpath(inputXml, xPath);
		for (int i = 0; i < xdmValue.size(); i++) {
			String element = xdmValue.itemAt(i).getStringValue();
			set.add(element);
			log.info(String.format("Element name: %s", element));
		}
		return set;
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 * @param inputXml
	 * @param xPathNamespace
	 * @param xPathElement
	 * @throws SaxonApiException
	 */
	private Map<String, Set<String>> getNamespaceByxPath (String inputXml, String xPathNamespace, String xPathElement) throws SaxonApiException {
		Map<String, Set<String>> map = new TreeMap<String, Set<String>>(STRING_COMPARATOR);
		String namespace = "";
		XdmValue xdmValue = XpathUtils.evaluateXpath(inputXml, xPathNamespace);
		for (int i = 0; i < xdmValue.size(); i++) {
			namespace = xdmValue.itemAt(i).getStringValue();
			log.info(String.format("Namespace: %s", namespace));
			map.put(namespace, getElementFromXsd(inputXml, xPathElement));
		}
		return map;
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
