/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbt.bpm.mock.service.ChannelService;


/**
 *
 * @author sbt-barinov-sv
 */
@Controller
public class ChannelController {
    public static final String PARAM_NAME="object";
    @Autowired
    private ChannelService service;    
    
    @RequestMapping("/channel/")
    public String getChannels(Model model) {
        model.addAttribute("list", service.getChannels());
        return "form";
    }
    
    @RequestMapping(value="/channel/{name}/", method = RequestMethod.GET) 
    public String getChannel(@PathVariable("name") String name, Model model) {
        model.addAttribute(PARAM_NAME, "insert value to send there");

        int count = service.getPayloadsCount(name);
        List<String> list = new ArrayList<String>(count);
        list.add("clear");
        for(int i=0;i<count;i++) 
            list.add(String.valueOf(i));
        model.addAttribute("list", list);
        return "form";
    }
    @RequestMapping(value="/channel/{name}/", method = RequestMethod.POST) 
    public String sendMessage(
            @PathVariable("name") String name, 
            @RequestParam(PARAM_NAME) String msg, 
            Model model)    {
        service.sendMessage(name, msg);
        return getChannel(name, model);
    }
    @RequestMapping(value="/channel/{name}/clear/") 
    public String clearMessages(@PathVariable("name") String name, Model model) {
        service.clearMessages(name);
        return "redirect:../";
    }
    @RequestMapping(value="/channel/{name}/{index}/")
    public String getPayload(
            @PathVariable("name") String name, 
            @PathVariable("index") int index, 
            Model model) {
        model.addAttribute(PARAM_NAME,service.getPayload(name, index));
        return "form";
    }
}
