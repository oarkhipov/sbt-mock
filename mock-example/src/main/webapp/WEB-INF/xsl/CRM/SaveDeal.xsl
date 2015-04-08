<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/SaveDealRs/1.01/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/SaveDealRs/1.01/saveDealRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='saveDealRq' and namespace-uri()='http://sbrf.ru/NCP/CRM/']/*[local-name()='deal' and namespace-uri()='http://sbrf.ru/NCP/CRM/SaveDealRq/1.15/']/*[local-name()='dealType' and namespace-uri()='http://sbrf.ru/NCP/CRM/SaveDealRq/1.15/']/text()"/>
   <xsl:param name="dataFileName" select="'../../data/CRM/xml/SaveDealData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('saveDealRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="saveDealRs">
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

   <xsl:template match="rsd:operationStatus">
      <tns:operationStatus>
         <tns:errorCode>
            <xsl:value-of select="./rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="./rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="./rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
      </tns:operationStatus>
   </xsl:template>

   <xsl:template name="saveDealRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:saveDealRs">
			      <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:operationStatus"/>
			      <tns:dealID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:dealID"/>
         </tns:dealID>
		    </xsl:element>
   </xsl:template>
</xsl:stylesheet>
