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

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	// xPath
	private static final String LOCAL_NAME_SCHEMA_TARGET_NAMESPACE = "//*[local-name()='schema']/@targetNamespace";
	private static final String LOCAL_NAME_IMPORT_NAMESPACE        = "//*[local-name()='import']/@namespace";
	private static final String XPATH_ELEMENT_ATTRIBUTE_NAME = "//*[local-name()='element']/@name";
	private static final String XPATH_ELEMENT_ATTRIBUTE_REF  = "//*[local-name()='element']/@ref";

	// RegExp
	private static final Pattern NAMESPACE_PATTERN        = Pattern.compile("xmlns:(.*?)=(\".*?\")");
	private static final Pattern NAMESPACE_URL_PATTERN    = Pattern.compile("(http:.+)(?=\"[^\"]*$)");
	private static final Pattern NAMESPACE_PREFIX_PATTERN = Pattern.compile("([^:]+)(?=\\=[^=]*$)");

	public static final Comparator<String> STRING_COMPARATOR = new Comparator<String>() {
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

	// Maps
	private Map<String, Set<String>> mapNamespacesByRegExp = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> mapNamespacesByXpath  = new HashMap<String, Set<String>>();
	private Map<String, Map<String, Set<String>>> mapOfElements = new HashMap<String, Map<String, Set<String>>>();


	public void reInit () throws IOException, SaxonApiException {
		mapNamespacesByRegExp.clear();
		mapNamespacesByXpath.clear();
		mapOfElements.clear();
		init();
	}

	@PostConstruct
	private void init () throws IOException, SaxonApiException {
		getNamespaceFromXSDByRegExp();
		getNamespaceFromXSDByxPath();
		getElementsFromXsd();
	}

	public Set<String> getNamespaceFromXsdByRegExpForSystem (String systemName) {
		return mapNamespacesByRegExp.get(systemName);
	}

	public Set<String> getNamespaceFromXsdByXpathForSystem (String systemName) {
		return mapNamespacesByXpath.get(systemName);
	}

	public Set<String> getElementsForNamespace(String systemName, String namespaceName) {
		return mapOfElements.get(systemName).get(namespaceName);
	}

	private void getElementsFromXsd () throws IOException, SaxonApiException {
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			Map<String, Set<String>> mapElementsByNamespace = new HashMap<String, Set<String>>();
			printLog(systemName);
			for (File xsdFile : map.get(systemName)) {
				String inputXml = readFileWithoutBOM(xsdFile);
				mapElementsByNamespace.putAll(getNamespaceByxPath(inputXml, LOCAL_NAME_SCHEMA_TARGET_NAMESPACE, XPATH_ELEMENT_ATTRIBUTE_NAME));
			}
			mapOfElements.put(systemName, mapElementsByNamespace);
		}
	}

	private Set<String> getElementFromXsd (String inputXml, String xPath) throws SaxonApiException {
		Set<String> set      = new TreeSet<String>(STRING_COMPARATOR);
		XdmValue    xdmValue = XpathUtils.evaluateXpath(inputXml, xPath);
		for (int i = 0; i < xdmValue.size(); i++) {
			String element = xdmValue.itemAt(i).getStringValue();
			set.add(element);
			log.debug(String.format("Element name: %s", element));
		}
		return set;
	}

	private void getNamespaceFromXSDByRegExp () throws IOException {
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			Set<String>             setXsdNamespaces = new TreeSet<String>(STRING_COMPARATOR);
			printLog(systemName);
			for (File xsdFile : map.get(systemName)) {
				log.debug("Read file: " + xsdFile.getName());
				setXsdNamespaces.addAll(getNamespaceByRegExp(readFileWithoutBOM(xsdFile)));
			}
			mapNamespacesByRegExp.put(systemName, setXsdNamespaces);
		}
	}

	private void printLog (String systemName) {
		log.debug("====================================================================");
		log.debug("");
		log.debug(String.format("      READ XSD FILES FOR SYSTEM: %s", systemName));
		log.debug("");
		log.debug("====================================================================");
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 *
	 * @param inputXml
	 * @param xPathNamespace
	 * @param xPathElement
	 * @throws SaxonApiException
	 */
	private Map<String, Set<String>> getNamespaceByxPath (String inputXml, String xPathNamespace, String xPathElement)
			throws SaxonApiException {
		Map<String, Set<String>> map       = new TreeMap<String, Set<String>>(STRING_COMPARATOR);
		String                   namespace = "";
		XdmValue                 xdmValue  = XpathUtils.evaluateXpath(inputXml, xPathNamespace);
		for (int i = 0; i < xdmValue.size(); i++) {
			namespace = xdmValue.itemAt(i).getStringValue();
			log.info(String.format("Namespace: %s", namespace));
			map.put(namespace, getElementFromXsd(inputXml, xPathElement));
		}
		return map;
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 * @param inputXml
	 */
	private Set<String> getNamespaceByRegExp (String inputXml) {
		Set<String> set = new TreeSet<String>(STRING_COMPARATOR);
		Matcher matcherNamespace = getSubstringByRegExp(NAMESPACE_PATTERN, inputXml);
		while (matcherNamespace.find()) {
			Matcher matcherNamespaceURL = getSubstringByRegExp(NAMESPACE_URL_PATTERN, matcherNamespace.group());
			while (matcherNamespaceURL.find())
				set.add(matcherNamespaceURL.group());
		}
		return set;
	}

	/**
	 * Получение подстроки по регулянорму выражению
	 * @param pattern шаблон для получение подстроки
	 * @param string строка, из которой проводится извлечение
	 * @return все совпадение по шаблону
	 */
	private Matcher getSubstringByRegExp(Pattern pattern, String string) {
		return pattern.matcher(string);
	}

		private void getNamespaceFromXSDByxPath () throws IOException, SaxonApiException {
		Map<String, List<File>> map = getXsdFilesFromSystems();
		for (String systemName : map.keySet()) {
			Set<String> setXsdNamespace = new TreeSet<String>(STRING_COMPARATOR);
			printLog(systemName);
			for (File xsdFile : map.get(systemName)) {
				log.info("Read file: " + xsdFile.getName());
				String inputXml = readFileWithoutBOM(xsdFile);
				// Проходим по target
				setXsdNamespace.addAll(getNamespaceByxPath(inputXml, LOCAL_NAME_SCHEMA_TARGET_NAMESPACE));

				// Проходим по import
				setXsdNamespace.addAll(getNamespaceByxPath(inputXml, LOCAL_NAME_IMPORT_NAMESPACE));
			}
			mapNamespacesByXpath.put(systemName, setXsdNamespace);
		}
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 * @param inputXml
	 * @param xPath
	 * @return
	 * @throws SaxonApiException
	 */
	private Set<String> getNamespaceByxPath (String inputXml, String xPath) throws SaxonApiException {
		XdmValue xdmValue = XpathUtils.evaluateXpath(inputXml, xPath);
		Set<String> set = new TreeSet<String>(STRING_COMPARATOR);
		for (int i = 0; i < xdmValue.size(); i++) {
			String namespace = xdmValue.itemAt(i).getStringValue();
			log.debug(String.format("Namespace: %s", namespace));
			set.add(namespace);
		}
		return set;
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
			printLog(systemName);
			List<File> filesFromDir = getFilesFromDir(dataFileService.getSystemXsdDirectoryResource(systemName).getFile().getCanonicalFile());
			log.debug(String.format("Files: %s", filesFromDir));
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
