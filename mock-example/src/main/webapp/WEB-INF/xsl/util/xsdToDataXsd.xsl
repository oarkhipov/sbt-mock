<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:dt="http://sbrf.ru/commonTypes/dataTypes/"
                xmlns:st="http://sbrf.ru/commonTypes/simpleTypes/">
    <!-- xslt чтобы превратить xsd веб сервиса в data xsd - нашу xsd, которой должно соответствовать xml с данными.
     Представляет собой копипаст объявления с заменой неймспейсов-->

    <!-- файл с темплейтом для soap header'а -->
    <xsl:import href="NCPSoapRqHeaderXSLTTemplate.xsl"/>

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="entryPointName" select="replace(xsd:schema/@targetNamespace,'^.+/(\w+)(/[0-9\.]+)?/$','$1')"/>
    <!-- TODO выбрать этот параметр более надежным способом -->


    <xsl:template match="xsd:schema">
        <xsl:variable name="targetNS" select="concat(./@targetNamespace,'Data/')"/><!-- неймспейс, в котором будут содрежатся наши данные. Получается припиской к стандратному неймспейсу субпути /Data/ -->
        <xsl:variable name="CommonTypesNSAlias" select="local-name(namespace::*[contains(.,'CommonTypes')])"/> <!-- алиас для xsd библиотеки типов CommonTypes. нужен потому что отличается от файла к файлу -->
        <xsl:variable name="CommonTypesNS" select="namespace::*[contains(.,'CommonTypes')]"/> <!-- неймспейс для xsd библиотеки типов CommonTypes -->
        <xsl:element name="xsd:schema">
            <xsl:namespace name="" select="$targetNS"/>
            <xsl:namespace name="st" select="'http://sbrf.ru/commonTypes/simpleTypes/'"/>
            <xsl:namespace name="dt" select="'http://sbrf.ru/commonTypes/dataTypes/'"/>
            <xsl:if test="$CommonTypesNS!=''">
                <xsl:namespace name="{$CommonTypesNSAlias}" select="$CommonTypesNS"/>
            </xsl:if>
            <xsl:attribute name="targetNamespace">
                <xsl:value-of select="$targetNS"/>
            </xsl:attribute>
            <xsl:attribute name="elementFormDefault">qualified</xsl:attribute>


            <xsl:apply-templates select="./xsd:import"/>

            <!-- Заголовок из импортированного файла NCPSoapRqHeaderXSLTTemplate.xsl -->
            <xsl:call-template name="NCPHeaderForXSD"/>

            <!-- корень данных -->
            <xsl:element name="xsd:element">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="type">dataValue</xsl:attribute>
            </xsl:element>

            <!-- контейнер ответов/запросов -->
            <xsl:element name="xsd:complexType">
                <xsl:attribute name="name">dataValue</xsl:attribute>
                <xsl:element name="xsd:sequence">
                    <xsl:element name="xsd:element">
                        <xsl:attribute name="name">
                            <xsl:choose>
                                <!-- различаем запрос от ответа -->
                                <!--TODO вполне может быть такое, что запрос отличается каким-нибудь другим суффиком, или не имеет его вовсе. Нужно сделать параметр, которые если задатиь напрямую говорит что это запрос, а не ответ -->
                                <xsl:when test="contains(./@targetNamespace,'Rq')">
                                    <xsl:text>request</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>response</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="type">
                            <xsl:value-of select="$entryPointName"/>
                             <!--Более крутой способ только для XPATH 2.0 <xsl:value-of select="replace(./@targetNamespace,'^.+/(\w+)(/[0-9\.]+)?/$','$1')"/>-->
                        </xsl:attribute>
                        <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                        <xsl:element name="xsd:annotation">
                            <xsl:element name="xsd:documentation">Ответ на запрос</xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>


            <xsl:apply-templates select="./xsd:complexType">
                <xsl:with-param name="entryPointName" select="$entryPointName"/>
            </xsl:apply-templates>

        </xsl:element>
    </xsl:template>

    <!-- переносит если есть импорты из исходной xsd -->
    <xsl:template match="xsd:import">
        <xsl:element name="xsd:import">
            <xsl:attribute name="namespace">
                <xsl:value-of select="./@namespace"/>
            </xsl:attribute>
            <xsl:attribute name="schemaLocation">
                <xsl:text>../../../xsd/CRM/</xsl:text><xsl:value-of select="./@schemaLocation"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexType">
        <xsl:param name="entryPointName"/>
        <xsl:copy copy-namespaces="no" >
            <xsl:copy-of select="@*"/>
            <xsl:choose>
                <xsl:when test="./@name=$entryPointName">
                    <xsl:for-each select="*">
                        <xsl:apply-templates select=".">
                            <xsl:with-param name="entryPointName" select="$entryPointName"/>
                        </xsl:apply-templates>
                    </xsl:for-each>
                    <xsl:element name="xsd:attribute">
                        <xsl:attribute name="name">name</xsl:attribute>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="./*" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[name(parent::*)='xsd:complexType']" name="complexType">
        <xsl:param name="entryPointName"/>
        <xsl:copy copy-namespaces="no">
            <xsl:if test="name(.)='xsd:sequence'">
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">SoapHeader</xsl:attribute>
                    <xsl:attribute name="type">Header</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates select="./*" mode="copy"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@* | node()" mode="copy">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*[local-name()!='type' and local-name()!='base']"  mode="copy"/>
            <xsl:if test="@type">
                <!--TODO replace неправилен - нужно вставить регулярку на проверку типов из тех что есть в импортах -->
                <xsl:attribute name="type"><xsl:value-of select="replace(@type,'tns:','')"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@base">
                <!--TODO replace неправилен - нужно вставить регулярку на проверку типов из тех что есть в импортах -->
                <xsl:attribute name="base"><xsl:value-of select="replace(@base,'tns:','')"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="node()"  mode="copy"/>
        </xsl:copy>
    </xsl:template>
    




</xsl:stylesheet>