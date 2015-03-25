package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/

@XStreamAlias("dependency")
public class Dependency {

    @XStreamAlias("xsdFile")
    private String aXsdFile;

    public String getaXsdFile() {
        return aXsdFile;
    }

    public void setaXsdFile(String aXsdFile) {
        this.aXsdFile = aXsdFile;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "aXsdFile='" + aXsdFile + '\'' +
                '}';
    }
}
