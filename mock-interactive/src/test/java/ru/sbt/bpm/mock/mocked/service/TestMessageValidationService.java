package ru.sbt.bpm.mock.mocked.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 02.06.2016.
 */
@Slf4j
@Service
@Qualifier("messageValidationService")
public class TestMessageValidationService extends MessageValidationService {


    @Override
    public void init() throws IOException, SAXException {

    }

    @Override
    public List<String> validate(String xml, String systemName) {
        return new ArrayList<String>();
    }

    @Override
    public void initValidator(ru.sbt.bpm.mock.config.entities.System system) throws IOException, SAXException {
        super.initValidator(system);
    }
}
