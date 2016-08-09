package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author SBT-Bochev-AS on 01.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Data
public class MainConfig {

    @XStreamAlias("checksum")
    private String configChecksum;

    @XStreamAlias("driverTimeout")
    private String driverTimeout;

    @XStreamAlias("maxLogsCount")
    private Integer maxLogsCount;

    @XStreamAlias("validationEnabled")
    private Boolean validationEnabled;
}
