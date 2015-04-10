<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/"
                xmlns:rsd="http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/importRatingRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/FinRep/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='importRatingRq' and namespace-uri()='http://sbrf.ru/NCP/FinRep/']/*[local-name()='entityType' and namespace-uri()='http://sbrf.ru/NCP/FinRep/ImportRatingRq/1.00/']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/FinRep/xml/importRatingData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('importRatingRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="importRatingRs">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:Employee--><xsl:template match="rsd:employeeForAjustment">
      <tns:employeeForAjustment><!--xsd:element - Inside--><tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <!--xsd:element - Inside--><tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <!--xsd:element - Inside--><tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <!--xsd:element - Inside--><tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <!--xsd:element - Inside--><tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <!--xsd:element - Inside--><xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <!--xsd:element - Inside--><tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForAjustment>
   </xsl:template>

   <xsl:template match="rsd:employeeForCalc">
      <tns:employeeForCalc><!--xsd:element - Inside--><tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <!--xsd:element - Inside--><tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <!--xsd:element - Inside--><tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <!--xsd:element - Inside--><tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <!--xsd:element - Inside--><tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <!--xsd:element - Inside--><xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <!--xsd:element - Inside--><tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForCalc>
   </xsl:template>

   <xsl:template match="rsd:employeeForApprove">
      <tns:employeeForApprove><!--xsd:element - Inside--><tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <!--xsd:element - Inside--><tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <!--xsd:element - Inside--><tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <!--xsd:element - Inside--><tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <!--xsd:element - Inside--><tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <!--xsd:element - Inside--><xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <!--xsd:element - Inside--><tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForApprove>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:RatingAjustment--><xsl:template match="rsd:ratingAjustment">
      <tns:ratingAjustment><!--xsd:element - Inside--><tns:ajustmentDate>
            <xsl:value-of select="./rsd:ajustmentDate"/>
         </tns:ajustmentDate>
         <!--xsd:element - Inside--><tns:ajustedValue>
            <xsl:value-of select="./rsd:ajustedValue"/>
         </tns:ajustedValue>
         <!--xsd:element - Inside--><tns:ajustmetReason>
            <xsl:value-of select="./rsd:ajustmetReason"/>
         </tns:ajustmetReason>
         <!--xsd:element - Inside--><xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
         <!--xsd:element - Inside--><tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <!-- xsd:element[$typesList] - Inside Employee--><xsl:apply-templates select="./rsd:employeeForAjustment"/>
      </tns:ratingAjustment>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfRatingAjustment--><xsl:template match="rsd:listOfRatingAjustment">
      <tns:listOfRatingAjustment><!-- xsd:element[$typesList] - Inside RatingAjustment--><xsl:apply-templates select="./rsd:ratingAjustment"/>
      </tns:listOfRatingAjustment>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:AddParameter--><xsl:template match="rsd:addParameter">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfAddParameter--><xsl:template match="rsd:listOfAddParameter">
      <tns:listOfAddParameter><!-- xsd:element[$typesList] - Inside AddParameter--><xsl:apply-templates select="./rsd:addParameter"/>
      </tns:listOfAddParameter>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:CalculatedFactor--><xsl:template match="rsd:calculatedFactor">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfCalculatedFactor--><xsl:template match="rsd:listOfCalculatedFactor">
      <tns:listOfCalculatedFactor><!-- xsd:element[$typesList] - Inside CalculatedFactor--><xsl:apply-templates select="./rsd:calculatedFactor"/>
      </tns:listOfCalculatedFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ResultRating--><xsl:template match="rsd:resultRating">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfResultRating--><xsl:template match="rsd:listOfResultRating">
      <tns:listOfResultRating><!-- xsd:element[$typesList] - Inside ResultRating--><xsl:apply-templates select="./rsd:resultRating"/>
      </tns:listOfResultRating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:CardinalFactor--><xsl:template match="rsd:cardinalFactor">
      <tns:cardinalFactor><!--xsd:element - Inside--><tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <!--xsd:element - Inside--><tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <!--xsd:element - Inside--><tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <!--xsd:element - Inside--><xsl:if test="./rsd:numValue">
            <tns:numValue>
               <xsl:value-of select="./rsd:numValue"/>
            </tns:numValue>
         </xsl:if>
      </tns:cardinalFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfCardinalFactor--><xsl:template match="rsd:listOfCardinalFactor">
      <tns:listOfCardinalFactor><!-- xsd:element[$typesList] - Inside CardinalFactor--><xsl:apply-templates select="./rsd:cardinalFactor"/>
      </tns:listOfCardinalFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:RatingPeriod--><xsl:template match="rsd:ratingPeriod">
      <tns:ratingPeriod><!--xsd:element - Inside--><xsl:if test="./rsd:reportDate">
            <tns:reportDate>
               <xsl:value-of select="./rsd:reportDate"/>
            </tns:reportDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:period">
            <tns:period>
               <xsl:value-of select="./rsd:period"/>
            </tns:period>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:startDate">
            <tns:startDate>
               <xsl:value-of select="./rsd:startDate"/>
            </tns:startDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:endDate">
            <tns:endDate>
               <xsl:value-of select="./rsd:endDate"/>
            </tns:endDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:type">
            <tns:type>
               <xsl:value-of select="./rsd:type"/>
            </tns:type>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside ListOfCardinalFactor--><xsl:apply-templates select="./rsd:listOfCardinalFactor"/>
      </tns:ratingPeriod>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfRatingPeriod--><xsl:template match="rsd:listOfRatingPeriod">
      <tns:listOfRatingPeriod><!-- xsd:element[$typesList] - Inside RatingPeriod--><xsl:apply-templates select="./rsd:ratingPeriod"/>
      </tns:listOfRatingPeriod>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:SupportFactor--><xsl:template match="rsd:supportFactor">
      <tns:supportFactor><!--xsd:element - Inside--><tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <!--xsd:element - Inside--><tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <!--xsd:element - Inside--><tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <!--xsd:element - Inside--><xsl:if test="./rsd:value">
            <tns:value>
               <xsl:value-of select="./rsd:value"/>
            </tns:value>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:valueIntegrationId">
            <tns:valueIntegrationId>
               <xsl:value-of select="./rsd:valueIntegrationId"/>
            </tns:valueIntegrationId>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:freeValue">
            <tns:freeValue>
               <xsl:value-of select="./rsd:freeValue"/>
            </tns:freeValue>
         </xsl:if>
      </tns:supportFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfSupportFactor--><xsl:template match="rsd:listOfSupportFactor">
      <tns:listOfSupportFactor><!-- xsd:element[$typesList] - Inside SupportFactor--><xsl:apply-templates select="./rsd:supportFactor"/>
      </tns:listOfSupportFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:RatingSupport--><xsl:template match="rsd:ratingSupport">
      <tns:ratingSupport><!--xsd:element - Inside--><tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <!--xsd:element - Inside--><tns:integrationId>
            <xsl:value-of select="./rsd:integrationId"/>
         </tns:integrationId>
         <!-- xsd:element[$typesList] - Inside ListOfSupportFactor--><xsl:apply-templates select="./rsd:listOfSupportFactor"/>
      </tns:ratingSupport>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfRatingSupport--><xsl:template match="rsd:listOfRatingSupport">
      <tns:listOfRatingSupport><!-- xsd:element[$typesList] - Inside RatingSupport--><xsl:apply-templates select="./rsd:ratingSupport"/>
      </tns:listOfRatingSupport>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:FixedFactor--><xsl:template match="rsd:fixedFactor">
      <tns:fixedFactor><!--xsd:element - Inside--><tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <!--xsd:element - Inside--><tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <!--xsd:element - Inside--><tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <!--xsd:element - Inside--><xsl:if test="./rsd:value">
            <tns:value>
               <xsl:value-of select="./rsd:value"/>
            </tns:value>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:valueIntegrationId">
            <tns:valueIntegrationId>
               <xsl:value-of select="./rsd:valueIntegrationId"/>
            </tns:valueIntegrationId>
         </xsl:if>
      </tns:fixedFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfFixedFactor--><xsl:template match="rsd:listOfFixedFactor">
      <tns:listOfFixedFactor><!-- xsd:element[$typesList] - Inside FixedFactor--><xsl:apply-templates select="./rsd:fixedFactor"/>
      </tns:listOfFixedFactor>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:RatingCalc--><xsl:template match="rsd:ratingCalc">
      <tns:ratingCalc><!--xsd:element - Inside--><tns:ratingCalcId>
            <xsl:value-of select="./rsd:ratingCalcId"/>
         </tns:ratingCalcId>
         <!--xsd:element - Inside--><tns:modelName>
            <xsl:value-of select="./rsd:modelName"/>
         </tns:modelName>
         <!--xsd:element - Inside--><tns:modelIntegrationId>
            <xsl:value-of select="./rsd:modelIntegrationId"/>
         </tns:modelIntegrationId>
         <!--xsd:element - Inside--><xsl:if test="./rsd:dateCalc">
            <tns:dateCalc>
               <xsl:value-of select="./rsd:dateCalc"/>
            </tns:dateCalc>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:calcValue">
            <tns:calcValue>
               <xsl:value-of select="./rsd:calcValue"/>
            </tns:calcValue>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside ListOfRatingAjustment--><xsl:apply-templates select="./rsd:listOfRatingAjustment"/>
         <!-- xsd:element[$typesList] - Inside Employee--><xsl:apply-templates select="./rsd:employeeForCalc"/>
         <!-- xsd:element[$typesList] - Inside ListOfResultRating--><xsl:apply-templates select="./rsd:listOfResultRating"/>
         <!-- xsd:element[$typesList] - Inside ListOfCalculatedFactor--><xsl:apply-templates select="./rsd:listOfCalculatedFactor"/>
         <!-- xsd:element[$typesList] - Inside ListOfAddParameter--><xsl:apply-templates select="./rsd:listOfAddParameter"/>
         <!-- xsd:element[$typesList] - Inside ListOfFixedFactor--><xsl:apply-templates select="./rsd:listOfFixedFactor"/>
         <!-- xsd:element[$typesList] - Inside ListOfRatingPeriod--><xsl:apply-templates select="./rsd:listOfRatingPeriod"/>
         <!-- xsd:element[$typesList] - Inside ListOfRatingSupport--><xsl:apply-templates select="./rsd:listOfRatingSupport"/>
      </tns:ratingCalc>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfRatingCalc--><xsl:template match="rsd:listOfRatingCalc">
      <tns:listOfRatingCalc><!-- xsd:element[$typesList] - Inside RatingCalc--><xsl:apply-templates select="./rsd:ratingCalc"/>
      </tns:listOfRatingCalc>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:Rating--><xsl:template match="rsd:rating">
      <tns:rating><!--xsd:element - Inside--><tns:ratingId>
            <xsl:value-of select="./rsd:ratingId"/>
         </tns:ratingId>
         <!--xsd:element - Inside--><xsl:if test="./rsd:finalRatingValue">
            <tns:finalRatingValue>
               <xsl:value-of select="./rsd:finalRatingValue"/>
            </tns:finalRatingValue>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:intermediateRatingValue">
            <tns:intermediateRatingValue>
               <xsl:value-of select="./rsd:intermediateRatingValue"/>
            </tns:intermediateRatingValue>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:ajustedRatingValue">
            <tns:ajustedRatingValue>
               <xsl:value-of select="./rsd:ajustedRatingValue"/>
            </tns:ajustedRatingValue>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:pd">
            <tns:pd>
               <xsl:value-of select="./rsd:pd"/>
            </tns:pd>
         </xsl:if>
         <!--xsd:element - Inside--><tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <!--xsd:element - Inside--><xsl:if test="./rsd:approvalDate">
            <tns:approvalDate>
               <xsl:value-of select="./rsd:approvalDate"/>
            </tns:approvalDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:calculationDate">
            <tns:calculationDate>
               <xsl:value-of select="./rsd:calculationDate"/>
            </tns:calculationDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:ajustmentDate">
            <tns:ajustmentDate>
               <xsl:value-of select="./rsd:ajustmentDate"/>
            </tns:ajustmentDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:modelName">
            <tns:modelName>
               <xsl:value-of select="./rsd:modelName"/>
            </tns:modelName>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:modelIntegrationId">
            <tns:modelIntegrationId>
               <xsl:value-of select="./rsd:modelIntegrationId"/>
            </tns:modelIntegrationId>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:ratingName">
            <tns:ratingName>
               <xsl:value-of select="./rsd:ratingName"/>
            </tns:ratingName>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:ratingType">
            <tns:ratingType>
               <xsl:value-of select="./rsd:ratingType"/>
            </tns:ratingType>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside Employee--><xsl:apply-templates select="./rsd:employeeForApprove"/>
         <!-- xsd:element[$typesList] - Inside ListOfRatingCalc--><xsl:apply-templates select="./rsd:listOfRatingCalc"/>
      </tns:rating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/:ListOfRating--><xsl:template match="rsd:listOfRating">
      <tns:listOfRating><!-- xsd:element[$typesList] - Inside Rating--><xsl:apply-templates select="./rsd:rating"/>
      </tns:listOfRating>
   </xsl:template>

   <!--xsd:complexType - template :ImportRatingResponse--><!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/FinRep/ImportRatingRs/1.03/-http://sbrf.ru/NCP/FinRep/--><xsl:template name="importRatingRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:importRatingRs"><!--xsd:element - Inside--><tns:dealId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:dealId"/>
         </tns:dealId>
         <!--xsd:element - Inside--><tns:entityType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:entityType"/>
         </tns:entityType>
         <!--xsd:element - Inside--><tns:entityId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:entityId"/>
         </tns:entityId>
         <!--xsd:element - Inside--><tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside ListOfRating--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:listOfRating"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
