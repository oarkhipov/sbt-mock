package ru.sbt.bpm.mock.config.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sbt-bochev-as on 27.07.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CdataValue {
}
