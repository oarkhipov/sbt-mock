package ru.sbt.bpm.mock.spring.controllers.api;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-zhdanov-an on 20.09.2016.
 */
@Controller
public class SchemaController {

    @Autowired
    private DataFileService dataFileService;


    @ResponseBody
    @RequestMapping(value = "/api/schema/{systemName}/files/list/", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String getSchemaFileNamesList(
            @PathVariable("systemName") String systemName) throws Exception {

        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();


        try {
            ArrayList<String> schemaFileNames = new ArrayList<String>();

            int parentDirPathLength = dataFileService.getSystemXsdDirectoryFile(systemName).getAbsolutePath().length();

            for (File file : dataFileService.getSchemaFileList(systemName)) {
                schemaFileNames.add(file.getAbsolutePath().substring(parentDirPathLength + 1));
            }

            map.put("data", schemaFileNames);

        } catch (Exception e) {
            return e.getMessage();
            //map.put("error", e.getMessage());
            //ajaxObject.setError(e);
        }


        return new Gson().toJson(map);

    }

    @ResponseBody
    @RequestMapping(value = "/api/schema/{systemName}/files/content/", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String getXSDFileContent(
            @PathVariable("systemName") String systemName,
            @RequestParam(required = true) String fileName) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();

        try {

            String fileContent = FileUtils.readFileToString(dataFileService.getXsdFile(systemName, fileName));
            map.put("data", fileContent);

        } catch (Exception e) {
            map.put("error", e.getMessage());
        }


        return new Gson().toJson(map);

    }


}
