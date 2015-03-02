<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mock="http://sbrf.ru/mockService"><!--TODO заменить mock на namespace конфига -->


    <!--************Функции****************-->
    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace($string, '^([\w_]+:)?([\w_]+)$', '$2')"/>
    </xsl:function>

    <xsl:function name="mock:getNamespaceAlias">
        <!--Взять из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'tns'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace($string, '^(([\w_]+):)?([\w_]+)$', '$2')"/>
    </xsl:function>

    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки конеретное имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData', но 'otherns:ClientReferenceData' в 'otherns:ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:param name="NSAliasToRemove"/>
        <xsl:value-of select="replace($string, concat('^(',$NSAliasToRemove,':)?([\w_]+)$'), '$2')"/>
    </xsl:function>

    <xsl:function name="mock:addDataToNamespaceUrl">
        <!--Добавить в урл суффикс '/Data/'. Например 'http://www.w3.org/1999/XSL/Transform' в 'http://www.w3.org/1999/XSL/Transform/Data/'-->
        <xsl:param name="url"/>
        <xsl:param name="operationName"/>
        <xsl:value-of select="replace($url, concat('^(.+[^/])(/',$operationName,')?(/)?$'), concat('$1/',$operationName,'/Data/'))"/>
    </xsl:function>

    <!-- определяем алиас урла. Пока не могу придумать, что еще может пригодиться, кроме xsd -->
    <xsl:function name="mock:getAliasOfUrl">
        <xsl:param name="url"/>
        <xsl:choose>
            <xsl:when test="$url='http://www.w3.org/2001/XMLSchema'">xsd:</xsl:when>
        </xsl:choose>
    </xsl:function>


</xsl:stylesheet>