package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/

@XStreamAlias("dependency")
@ToString
public class Dependency {

    @XStreamAlias("xsdFile")
    @Getter
    @Setter
    private String xsdFile;
}
