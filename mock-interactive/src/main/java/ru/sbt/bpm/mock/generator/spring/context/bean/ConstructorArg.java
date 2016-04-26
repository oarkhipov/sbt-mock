package ru.sbt.bpm.mock.generator.spring.context.bean;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sbt-bochev-as on 12.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstructorArg {
    @XStreamAsAttribute
    String value;
}
