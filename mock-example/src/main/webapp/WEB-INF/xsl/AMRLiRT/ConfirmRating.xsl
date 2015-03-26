<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/ConfirmRatingRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/ConfirmRatingRs/confirmRatingRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:AMRLiRT="http://sbrf.ru/NCP/AMRLIRT/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='confirmRatingRq' and namespace-uri()='http://sbrf.ru/NCP/AMRLIRT/']/*[local-name()='siebelMessage' and namespace-uri()='http://sbrf.ru/NCP/AMRLIRT/ConfirmRatingRq/']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/AMRLiRT/xml/ConfirmRatingData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('confirmRatingRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="confirmRatingRs">
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

   <xsl:template match="rsd:addParameter">
      <tns:addParameter>
         <tns:order>
            <xsl:value-of select="./rsd:order"/>
         </tns:order>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:addParameter>
   </xsl:template>

   <xsl:template match="rsd:listOfAddParameter">
      <tns:listOfAddParameter>
         <xsl:apply-templates select="./rsd:addParameter"/>
      </tns:listOfAddParameter>
   </xsl:template>

   <xsl:template match="rsd:return">
      <tns:return>
         <tns:errorCode>
            <xsl:value-of select="./rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="./rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="./rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <xsl:apply-templates select="./rsd:listOfAddParameter"/>
      </tns:return>
   </xsl:template>

   <xsl:template name="confirmRatingRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="AMRLiRT:confirmRatingRs">
			      <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:return"/>
		    </xsl:element>
   </xsl:template>
</xsl:stylesheet>
