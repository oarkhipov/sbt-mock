package ru.sbt.bpm.mock.tests;

import com.eviware.soapui.impl.wsdl.support.soap.SoapMessageBuilder;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 22.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class SoapFaultGenerationTest {

    @Test(enabled = false)
    public void soapFaultGenerationTest() throws Exception {
        String fault = SoapMessageBuilder.buildFault("Server", "Error message", SoapVersion.Soap11);
        assertTrue(fault.toLowerCase().contains("faultmessage"), fault);
    }
}
