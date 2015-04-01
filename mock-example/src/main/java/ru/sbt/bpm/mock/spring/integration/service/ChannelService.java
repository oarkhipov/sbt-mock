/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.spring.integration.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.message.GenericMessage;

/**
 *
 * @author sbt-barinov-sv
 */
public class ChannelService {
    
    @Autowired
    private ApplicationContext appContext;
    
    private final Map<String, List<Message>> messageMap = new HashMap<String, List<Message>>();
    
    public void setSubscribeChannels(Collection<SubscribableChannel> channels) {
        for(SubscribableChannel channel:channels) {
            String channelName = findBeanName(channel);
            final List<Message> messageList = new LinkedList<Message>();
            channel.subscribe(new MessageHandler() {
                public void handleMessage(Message<?> msg) throws MessagingException {
                    messageList.add(msg);
                }
            });
            messageMap.put(channelName, messageList);            
        }
    }
    
    private String findBeanName(Object obj) {
        for(Map.Entry<String, ? extends Object> entry:appContext.getBeansOfType(obj.getClass()).entrySet()) {
            if(obj.equals(entry.getValue()))
                return entry.getKey();
        }
        throw new IllegalArgumentException("Bean "+obj.toString()+" not found in ApplicationContext");
    }
    
    public Collection<String> getChannels() {
        return Arrays.asList(appContext.getBeanNamesForType(SubscribableChannel.class));
    }
    
    public void sendMessage(String channelName, String payload) {
        SubscribableChannel channel = appContext.getBean(channelName, SubscribableChannel.class);
        Message msg = new GenericMessage(payload);
        channel.send(msg);
    }
    
    public void clearMessages(String channelName) {
        List msgList = messageMap.get(channelName);
        if(msgList!=null) msgList.clear();
    }
    
    public int getPayloadsCount(String channelName) {
        List<Message> messages = messageMap.get(channelName);
        if(messages == null) return 0;
        else return messages.size();
    }
    
    public String getPayload(String channelName, int index) {
        List<Message> messages = messageMap.get(channelName);
        if(messages == null) return "log is empty";
        if(messages.size() <= index) return "not found "+index+"msg in list";
        return String.valueOf(messages.get(index).getPayload());   
    }
}
