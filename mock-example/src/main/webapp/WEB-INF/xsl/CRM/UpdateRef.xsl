<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/UpdateRefRs/1.02/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateRefRs/1.02/updateRefRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='updateRefRq' and namespace-uri()='http://sbrf.ru/NCP/CRM/']/*[local-name()='referenceItem' and namespace-uri()='http://sbrf.ru/NCP/CRM/UpdateRefRq/1.01/']/text()"/>
   <xsl:param name="dataFileName" select="'../../data/CRM/xml/UpdateRefData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('updateRefRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="updateRefRs">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateRefRs/1.02/:ListOfValue--><xsl:template match="rsd:listOfValue">
      <tns:listOfValue><!--xsd:element - Inside--><tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
         <!--xsd:element - Inside--><tns:valueId>
            <xsl:value-of select="./rsd:valueId"/>
         </tns:valueId>
         <!--xsd:element - Inside--><xsl:if test="./rsd:parentValueId">
            <tns:parentValueId>
               <xsl:value-of select="./rsd:parentValueId"/>
            </tns:parentValueId>
         </xsl:if>
      </tns:listOfValue>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateRefRs/1.02/:ReferenceItem--><xsl:template match="rsd:referenceItem">
      <tns:referenceItem><!--xsd:element - Inside--><tns:referenceid>
            <xsl:value-of select="./rsd:referenceid"/>
         </tns:referenceid>
         <!-- xsd:element[$typesList] - Inside ListOfValue--><xsl:apply-templates select="./rsd:listOfValue"/>
      </tns:referenceItem>
   </xsl:template>

   <!--xsd:complexType - template :UpdateRefRs--><!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/CRM/UpdateRefRs/1.02/-http://sbrf.ru/NCP/CRM/--><xsl:template name="updateRefRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:updateRefRs"><!-- xsd:element[$typesList] - Inside ReferenceItem--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:referenceItem"/>
         <!--xsd:element - Inside--><tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
