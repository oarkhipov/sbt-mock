<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/calculateRatingRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:AMRLiRT="http://sbrf.ru/NCP/AMRLIRT/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Header' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='user-id' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/AMRLiRT/xml/CalculateRatingData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('calculateRatingRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="calculateRatingRs">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:CalculatedFactor--><xsl:template match="rsd:calculatedFactor">
      <tns:calculatedFactor><!--xsd:element - Inside--><tns:code>
            <xsl:value-of select="./rsd:code"/>
         </tns:code>
         <!--xsd:element - Inside--><tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <!--xsd:element - Inside--><tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:calculatedFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:ResultRating--><xsl:template match="rsd:resultRating">
      <tns:resultRating><!--xsd:element - Inside--><tns:isPrimary>
            <xsl:value-of select="./rsd:isPrimary"/>
         </tns:isPrimary>
         <!--xsd:element - Inside--><tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <!--xsd:element - Inside--><tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
         <!--xsd:element - Inside--><xsl:if test="./rsd:type">
            <tns:type>
               <xsl:value-of select="./rsd:type"/>
            </tns:type>
         </xsl:if>
      </tns:resultRating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:Return--><xsl:template match="rsd:return">
      <tns:return><!--xsd:element - Inside--><tns:errorCode>
            <xsl:value-of select="./rsd:errorCode"/>
         </tns:errorCode>
         <!--xsd:element - Inside--><xsl:if test="./rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="./rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside ListOfResultRating--><xsl:apply-templates select="./rsd:listOfResultRating"/>
         <!-- xsd:element[$typesList] - Inside ListOfCalculatedFactor--><xsl:apply-templates select="./rsd:listOfCalculatedFactor"/>
         <!-- xsd:element[$typesList] - Inside ListOfAddParameter--><xsl:apply-templates select="./rsd:listOfAddParameter"/>
      </tns:return>
   </xsl:template>

   <!--xsd:complexType - template :CalcRatingResponse--><!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:AddParameter--><xsl:template match="rsd:addParameter">
      <tns:addParameter><!--xsd:element - Inside--><tns:order>
            <xsl:value-of select="./rsd:order"/>
         </tns:order>
         <!--xsd:element - Inside--><tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <!--xsd:element - Inside--><tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:addParameter>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:ListOfAddParameter--><xsl:template match="rsd:listOfAddParameter">
      <tns:listOfAddParameter><!-- xsd:element[$typesList] - Inside AddParameter--><xsl:apply-templates select="./rsd:addParameter"/>
      </tns:listOfAddParameter>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:ListOfResultRating--><xsl:template match="rsd:listOfResultRating">
      <tns:listOfResultRating><!-- xsd:element[$typesList] - Inside ResultRating--><xsl:apply-templates select="./rsd:resultRating"/>
      </tns:listOfResultRating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/:ListOfCalculatedFactor--><xsl:template match="rsd:listOfCalculatedFactor">
      <tns:listOfCalculatedFactor><!-- xsd:element[$typesList] - Inside CalculatedFactor--><xsl:apply-templates select="./rsd:calculatedFactor"/>
      </tns:listOfCalculatedFactor>
   </xsl:template>

   <!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/AMRLIRT/CalculateRatingRs/-http://sbrf.ru/NCP/AMRLIRT/--><xsl:template name="calculateRatingRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="AMRLiRT:calculateRatingRs"><!-- xsd:element[$typesList] - Inside Return--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:return"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
