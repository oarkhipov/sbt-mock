<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:crmct="http://sbrf.ru/NCP/CRM/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/CRM/CreateTaskRs/1.02/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/CreateTaskRs/1.02/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="//soap:Body/*//*[local-name()='comment'][1]/text()"/>
   <xsl:param name="dataFileName" select="'../../data/CRM/xml/CreateTaskData.xml'"/>
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
         <xsl:choose>
            <xsl:when test="soap:Header">
               <xsl:copy-of select="soap:Header"/>
            </xsl:when>
            <xsl:otherwise>
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
                  <xsl:with-param name="operation-name" select="string('createTaskRs')"/>
                  <xsl:with-param name="correlation-id" select="$correlation-id"/>
                  <xsl:with-param name="eis-name" select="$eis-name"/>
                  <xsl:with-param name="system-id" select="$system-id"/>
                  <xsl:with-param name="operation-version" select="$operation-version"/>
                  <xsl:with-param name="user-id" select="$user-id"/>
                  <xsl:with-param name="user-name" select="$user-name"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
         <soap:Body>
            <xsl:call-template name="CreateTaskRs">
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

   <xsl:template name="CreateTaskRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:createTaskRs">
         <tns:contractID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:contractID"/>
         </tns:contractID>
         <tns:contractBPMID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:contractBPMID"/>
         </tns:contractBPMID>
         <tns:comment>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:comment"/>
         </tns:comment>
         <tns:requestType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:requestType"/>
         </tns:requestType>
         <tns:responsiblePersonID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:responsiblePersonID"/>
         </tns:responsiblePersonID>
         <tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
