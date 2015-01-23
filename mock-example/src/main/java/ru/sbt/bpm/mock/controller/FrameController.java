package ru.sbt.bpm.mock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sbt-bochev-as on 23.01.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class FrameController {

    @RequestMapping(method = RequestMethod.GET, value = "/FrameMock")
    public String getFrame() {
        return "frame";
    }
}