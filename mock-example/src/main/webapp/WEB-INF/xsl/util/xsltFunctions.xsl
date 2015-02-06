<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mock="http://sbrf.ru/mockService"><!--TODO заменить mock на namespace конфига -->


    <!--************Функции****************-->
    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace(mock:unArray($string), '^([\w_]+:)?([\w_]+)$', '$2')"/>
    </xsl:function>

    <xsl:function name="mock:unArray">
        <!--выташить строку из массива, если она там. Например '("tns:calculateResponse", "xs:anyType")' в 'tns:calculateResponse'-->
        <!--TODO подумать как сделать так, чтобы вся цепочка работала с этими массивами-->
        <xsl:param name="array"/>
        <xsl:value-of select="subsequence($array,1,1)"/>
    </xsl:function>

    <xsl:function name="mock:getNamespaceAlias">
        <!--Взять из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'tns'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace(mock:unArray($string), '^(([\w_]+):)?([\w_]+)$  ', '$2')"/>
    </xsl:function>

    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки конеретное имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData', но 'otherns:ClientReferenceData' в 'otherns:ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:param name="NSAliasToRemove"/>
        <xsl:value-of select="replace(mock:unArray($string), concat('^(',$NSAliasToRemove,':)?([\w_]+)$'), '$2')"/>
    </xsl:function>

    <xsl:function name="mock:addDataToNamespaceUrl">
        <!--Добавить в урл суффикс '/Data/'. Например 'http://www.w3.org/1999/XSL/Transform' в 'http://www.w3.org/1999/XSL/Transform/Data/'-->
        <xsl:param name="url"/>
        <xsl:value-of select="replace($url, '^(.+[^/])(/)?$', '$1/Data/')"/>
    </xsl:function>


</xsl:stylesheet>