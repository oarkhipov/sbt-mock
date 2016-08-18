-dontshrink
-optimizations !class/marking/final

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keepclasseswithmembers public class * { public *;}

-keep public class * {
    public java.lang.String concatStrings(java.lang.String[]);
}