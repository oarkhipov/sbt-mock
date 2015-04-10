<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/FinRep/GetExchangeRatesRs/1.02/"
                xmlns:rsd="http://sbrf.ru/NCP/FinRep/GetExchangeRatesRs/1.02/getExchangeRatesRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/FinRep/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='getExchangeRatesRq' and namespace-uri()='http://sbrf.ru/NCP/FinRep/']/*[local-name()='currencyCode' and namespace-uri()='http://sbrf.ru/NCP/FinRep/GetExchangeRatesRq/1.00/']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/FinRep/xml/getExchangeRatesData.xml'"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="soap:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap:Envelope">
         <xsl:call-template name="NCPHeader">
            <xsl:with-param name="response">
               <xsl:choose>
                  <xsl:when test="count(./rsd:response[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="timestamp" select="$timestamp"/>
            <xsl:with-param name="id" select="$id"/>
            <xsl:with-param name="operation-name" select="string('SrvGetExchangeRates')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="getExchangeRatesRs">
               <xsl:with-param name="data" select="$data"/>
               <xsl:with-param name="response">
                  <xsl:choose>
                     <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1">
                        <xsl:value-of select="$linkedTag"/>
                     </xsl:when>
                     <xsl:otherwise>default</xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </soap:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template match="rsd:exchangeRates">
      <tns:exchangeRates>
         <tns:currencyCode>
            <xsl:value-of select="./rsd:currencyCode"/>
         </tns:currencyCode>
         <tns:currencyRate>
            <xsl:value-of select="./rsd:currencyRate"/>
         </tns:currencyRate>
         <tns:currencyDate>
            <xsl:value-of select="./rsd:currencyDate"/>
         </tns:currencyDate>
      </tns:exchangeRates>
   </xsl:template>

   <xsl:template name="getExchangeRatesRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:getExchangeRatesRs">
         <tns:dealID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:dealID"/>
         </tns:dealID>
         <tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:exchangeRates"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
