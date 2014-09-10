/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbt.bpm.mock.service.TransformService;
/**
 *
 * @author sbt-barinov-sv
 */
@Controller
@RequestMapping("/transform")
public class TransformController {
    public static final String XSL="xsl";
    @Autowired
    private TransformService service;
    
    public void setService(TransformService service) {
        this.service = service;
    }
    
    @RequestMapping(value="/transform")
    public String list(Model model) {
        model.addAttribute("list", service.keySet());
        return "list";
    }
    
    @RequestMapping(value="/transform/{name}", method=RequestMethod.GET)
    public String get(@PathVariable("name") String name, Model model) {
        model.addAttribute(XSL, service.getXSL(name));
        return "form";
    }
    @RequestMapping(value="/transform/{name}", method=RequestMethod.POST)
    public String post(
            @PathVariable("name") String name, 
            @RequestParam(XSL) String xsl,
            ModelMap model) {
        service.putXSL(name, xsl);
        return "form";
    }
    
}
