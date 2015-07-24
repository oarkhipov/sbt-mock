<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mock="http://sbrf.ru/mockService">


    <!--************Функции****************-->
    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="count($string)>1"><xsl:value-of select="replace($string[1], '^([\w_]+:)?([\w_]+)$', '$2')"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="replace($string, '^([\w_]+:)?([\w_]+)$', '$2')"/></xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="mock:getNamespaceAlias">
        <!--Взять из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'tns'-->
        <!--TODO надо бы передалть, чтобы это возвращало бы массив -->
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="count($string)>1"><xsl:value-of select="replace($string[1], '^(([\w_]+):)?([\w_]+)$', '$2')"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="replace($string, '^(([\w_]+):)?([\w_]+)$', '$2')"/></xsl:otherwise>
        </xsl:choose>
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

    <!-- определяем алиас урла. Пока не могу придумать, что еще может пригодиться, кроме xsd -->
    <xsl:function name="mock:addAliasToName">
        <xsl:param name="alias"/>
        <xsl:param name="name"/>
        <xsl:choose>
            <xsl:when test="contains($name,':')"><xsl:value-of select="$name"/></xsl:when>
            <xsl:when test="string-length($alias)>0"><xsl:value-of select="concat($alias,$name)"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="concat('tns:',$name)"/></xsl:otherwise>
        </xsl:choose>
    </xsl:function>


</xsl:stylesheet>