package ru.sbt.bpm.mock.product.ready;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.JMSValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.SOAPValidationService;

import java.io.IOException;

/**
 * Created by sbt-hodakovskiy-da on 02.06.2016.
 */
@Slf4j
@Service
@Primary
public class TestMessageValidationService extends MessageValidationService {

	public TestMessageValidationService(DataFileService dataFileService, MockConfigContainer configContainer, JMSValidationService jmsMessageValidationService, SOAPValidationService soapValidationService) {
		super(dataFileService, configContainer, jmsMessageValidationService, soapValidationService);
	}

	@Override
	protected void init () throws IOException, SAXException {

	}

	@Override
	public void initValidator (ru.sbt.bpm.mock.config.entities.System system) throws IOException, SAXException {
		super.initValidator(system);
	}
}
