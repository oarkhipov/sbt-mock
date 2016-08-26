-optimizations !class/marking/final

-dontnote javax.activation.**
-dontnote javax.xml.**
-dontnote org.w3c.dom.**
-dontnote org.xml.sax.**
-dontnote sun.applet.**
-dontnote sun.tools.jar.**
-dontnote com.sun.activation.registries.**
-dontnote org.apache.commons.collections.**

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

#-keepclasseswithmembers public class * { public *;}

-keep public interface * { *; }

-keepdirectories
#-keep @org.springframework.stereotype.Component class *
-keepclassmembers class * {
    @org.springframework.beans.factory.annotation.Autowired *;
    @javax.annotation.PostConstruct *;
}

#Config Classes
-keep public class ru.sbt.bpm.mock.config.MockConfig { public *; }
-keep public class ru.sbt.bpm.mock.config.MockConfigContainer { public *; }
-keep public class ru.sbt.bpm.mock.config.entities.** {
    *;
}
#Services
-keep public class ru.sbt.bpm.mock.spring.bean.MessageAggregator { public *;}
-keep public class ru.sbt.bpm.mock.spring.bean.pojo.MockMessage { public *;}
-keep public class ru.sbt.bpm.mock.spring.service.*Service {
    public *;
    private <fields>;
}
-keep public class ru.sbt.bpm.mock.spring.service.message.* { public *;}
-keep public class ru.sbt.bpm.mock.spring.service.message.validation.exceptions.* { public *;}
-keep public class ru.sbt.bpm.mock.spring.service.message.validation.*Service { public *;}

-keep public class ru.sbt.bpm.mock.spring.bean.ResponseGenerator { public *;}
#Data classes
-keep public class ru.sbt.bpm.mock.config.enums.* { public *;}
-keep public class ru.sbt.bpm.mock.utils.AjaxObject { public *;}
-keep public class ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils { public *;}
-keep public class ru.sbt.bpm.mock.utils.ExceptionUtils { public *;}
-keep public class ru.sbt.bpm.mock.utils.SaveFile { public *;}
-keep public class ru.sbt.bpm.mock.spring.service.message.validation.mockObjects.MockHttpServletResponse { public *;}

#Logging
-keep public class ru.sbt.bpm.mock.logging.** { public *;}

-keep public class ru.sbt.bpm.mock.utils.AjaxObject { private <fields>;}