<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/SrvSendBasicDocumentInformationRs/Data/"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:VIVAT="http://sbrf.ru/prpc/bbmo/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mq="http://sbrf.ru/prpc/mq/headers"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']/*[local-name()='Body' and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']/*[local-name()='SrvSendBasicDocumentInformationRq' and namespace-uri()='http://sbrf.ru/prpc/bbmo/10']/*[local-name()='BasicDocumentInformation' and namespace-uri()='http://sbrf.ru/prpc/bbmo/10']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/VIVAT/xml/SrvSendBasicDocumentInformationData.xml'"/>
   <xsl:param name="request-time" select="string('2014-12-16T17:55:06.410')"/>
   <xsl:param name="kd4header" select="''"/>
   <xsl:param name="message-id" select="''"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="''"/>
   <xsl:param name="eis-name" select="''"/>
   <xsl:param name="system-id" select="''"/>
   <xsl:param name="operation-version" select="''"/>
   <xsl:param name="user-id" select="''"/>
   <xsl:param name="user-name" select="''"/>
   <xsl:param name="proc-inst-tb" select="''"/>

   <xsl:template match="soap:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap:Envelope">
         <xsl:call-template name="KD4Header">
            <xsl:with-param name="response">
               <xsl:choose>
                  <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="request-time" select="$request-time"/>
            <xsl:with-param name="message-id" select="$message-id"/>
            <xsl:with-param name="operation-name"
                            select="string('SrvSendBasicDocumentInformation')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
            <xsl:with-param name="kd4header" select="$kd4header"/>
            <xsl:with-param name="proc-inst-tb" select="$proc-inst-tb"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="SrvSendBasicDocumentInformation">
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

   <xsl:template match="rsd:SrvSendBasicDocumentInformationRs">
      <tns:SrvSendBasicDocumentInformationRs>
         <xsl:if test="./rsd:URL">
            <tns:URL>
               <xsl:value-of select="./rsd:URL"/>
            </tns:URL>
         </xsl:if>
      </tns:SrvSendBasicDocumentInformationRs>
   </xsl:template>

   <xsl:template match="rsd:ErrorDetails">
      <ct:ErrorDetails xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10">
         <ct:ErrorMessage>
            <xsl:value-of select="./rsd:ErrorMessage"/>
         </ct:ErrorMessage>
         <xsl:apply-templates select="./rsd:FormatError"/>
      </ct:ErrorDetails>
   </xsl:template>

   <xsl:template match="rsd:FormatError">
      <ct:FormatError xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10">
         <ct:FieldName>
            <xsl:value-of select="./rsd:FieldName"/>
         </ct:FieldName>
         <ct:ErrorDescription>
            <xsl:value-of select="./rsd:ErrorDescription"/>
         </ct:ErrorDescription>
      </ct:FormatError>
   </xsl:template>

   <xsl:template match="rsd:ExtensionData">
      <ct:ExtensionData xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10"/>
   </xsl:template>

   <xsl:template name="SrvSendBasicDocumentInformation">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvSendBasicDocumentInformationRs">
         <ct:ResultCode xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ResultCode"/>
         </ct:ResultCode>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:ErrorDetails"/>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:ExtensionData"/>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:URL">
            <tns:URL>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:URL"/><xsl:value-of select="/soap:Envelope/soap:Header/mq:AsyncHeader/mq:user-id/text()"/>
            </tns:URL>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
