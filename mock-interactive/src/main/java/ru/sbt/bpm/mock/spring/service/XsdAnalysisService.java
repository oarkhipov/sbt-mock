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

	// RegExp
	private static final Pattern NAMESPACE_PATTERN        = Pattern.compile("xmlns:(.*?)=(\".*?\")");
	private static final Pattern NAMESPACE_URL_PATTERN    = Pattern.compile("(http:.+)(?=\"[^\"]*$)");
	private static final Pattern NAMESPACE_PREFIX_PATTERN = Pattern.compile("([^:]+)(?=\\=[^=]*$)");


	public Set<String> getNamespaceFromXSDByRegExp () throws IOException {
		Set<String> setXsdNamespaces = new TreeSet<String>(new Comparator<String>() {
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
				getNamespaceByRegExp(setXsdNamespaces, readFileWithoutBOM(xsdFile));
			}
		}
		return setXsdNamespaces;
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 * @param setXsdNamespace
	 * @param inputXml
	 */
	private void getNamespaceByRegExp (Set<String> setXsdNamespace, String inputXml) {
		Matcher matcherNamespace = getSubstringByRegExp(NAMESPACE_PATTERN, inputXml);
		while (matcherNamespace.find()) {
			Matcher matcherNamespaceURL = getSubstringByRegExp(NAMESPACE_URL_PATTERN, matcherNamespace.group());
			while (matcherNamespaceURL.find())
				setXsdNamespace.add(matcherNamespaceURL.group());
		}
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

	public Set<String> getNamespaceFromXSDByxPath () throws IOException, SaxonApiException {
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
				String inputXml = readFileWithoutBOM(xsdFile);
				// Проходим по target
				getNamespaceByxPath(setXsdNamespace, inputXml, LOCAL_NAME_SCHEMA_TARGET_NAMESPACE);

				// Проходим по import
				getNamespaceByxPath(setXsdNamespace, inputXml, LOCAL_NAME_IMPORT_NAMESPACE);
			}
		}
		return setXsdNamespace;
	}

	/**
	 * Получение namespace из файла xsd по xPath
	 * @param setXsdNamespace
	 * @param inputXml
	 * @param xPath
	 * @throws SaxonApiException
	 */
	private void getNamespaceByxPath (Set<String> setXsdNamespace, String inputXml, String xPath) throws SaxonApiException {
		XdmValue xdmValue = XpathUtils.evaluateXpath(inputXml, xPath);
		for (int i = 0; i < xdmValue.size(); i++) {
			String namespace = xdmValue.itemAt(i).getStringValue();
			log.info(String.format("Namespace: %s", namespace));
			setXsdNamespace.add(namespace);
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
