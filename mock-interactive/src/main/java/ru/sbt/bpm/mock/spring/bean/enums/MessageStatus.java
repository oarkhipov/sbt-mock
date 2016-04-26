package ru.sbt.bpm.mock.spring.bean.enums;

/**
 * @author sbt-bochev-as on 25.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public enum MessageStatus {
    RCV, //Received
    RCVVE, //Validation Error on received message
    SNT, //Sent
    SNTVE, //Validation Error on sending message
    NANS //No Answer required
}
