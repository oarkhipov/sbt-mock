package ru.sbt.bpm.mock.spring.utils;


        import org.springframework.integration.annotation.MessageEndpoint;
        import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author Mark Fisher
 */
@MessageEndpoint
public class DemoBean {

    @ServiceActivator
    public String upperCase(String input) {
        return "JMS response: " + input.toUpperCase();
    }

    @ServiceActivator
    public String out(String input){
        System.out.println("!!!!!!!!!!!!!!!!!: "+input); return "+++++: "+input;
    }

    @ServiceActivator
    public void out1(String input){
        System.out.println("!!!!!!!!!!!!!!!!!: "+input);
    }
}

