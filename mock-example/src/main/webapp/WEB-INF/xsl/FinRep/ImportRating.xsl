<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/ASFO/GetRatingsAndFactorsRs/"
                xmlns="http://sbrf.ru/NCP/ASFO/GetRatingsAndFactorsRs/"
                xmlns:amrct="http://sbrf.ru/NCP/AMRLIRT/CommonTypes/"
                xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetRatingsAndFactorsRs/Data/"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/ASFO/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="all"/>
   <xsl:param name="dataFileName"
              select="'../../data/FinRep/xml/ImportRatingData.xml'"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="soap-env:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap-env:Envelope">
         <xsl:choose>
            <xsl:when test="soap-env:Header">
               <xsl:copy-of select="soap-env:Header"/>
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
                  <xsl:with-param name="operation-name" select="string('ImportRatingResponse')"/>
                  <xsl:with-param name="correlation-id" select="$correlation-id"/>
                  <xsl:with-param name="eis-name" select="$eis-name"/>
                  <xsl:with-param name="system-id" select="$system-id"/>
                  <xsl:with-param name="operation-version" select="$operation-version"/>
                  <xsl:with-param name="user-id" select="$user-id"/>
                  <xsl:with-param name="user-name" select="$user-name"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
         <soap-env:Body>
            <xsl:call-template name="ImportRatingResponse">
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
         </soap-env:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template match="rsd:employeeForAjustment">
      <tns:employeeForAjustment>
         <tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForAjustment>
   </xsl:template>

   <xsl:template match="rsd:employeeForCalc">
      <tns:employeeForCalc>
         <tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForCalc>
   </xsl:template>

   <xsl:template match="rsd:employeeForApprove">
      <tns:employeeForApprove>
         <tns:login>
            <xsl:value-of select="./rsd:login"/>
         </tns:login>
         <tns:lastName>
            <xsl:value-of select="./rsd:lastName"/>
         </tns:lastName>
         <tns:firstName>
            <xsl:value-of select="./rsd:firstName"/>
         </tns:firstName>
         <xsl:if test="./rsd:middleName">
            <tns:middleName>
               <xsl:value-of select="./rsd:middleName"/>
            </tns:middleName>
         </xsl:if>
         <tns:division>
            <xsl:value-of select="./rsd:division"/>
         </tns:division>
         <tns:divisionCode>
            <xsl:value-of select="./rsd:divisionCode"/>
         </tns:divisionCode>
         <xsl:if test="./rsd:jobTitle">
            <tns:jobTitle>
               <xsl:value-of select="./rsd:jobTitle"/>
            </tns:jobTitle>
         </xsl:if>
         <tns:role>
            <xsl:value-of select="./rsd:role"/>
         </tns:role>
      </tns:employeeForApprove>
   </xsl:template>

   <xsl:template match="rsd:ratingAjustment">
      <tns:ratingAjustment>
         <tns:ajustmentDate>
            <xsl:value-of select="./rsd:ajustmentDate"/>
         </tns:ajustmentDate>
         <tns:ajustedValue>
            <xsl:value-of select="./rsd:ajustedValue"/>
         </tns:ajustedValue>
         <tns:ajustmetReason>
            <xsl:value-of select="./rsd:ajustmetReason"/>
         </tns:ajustmetReason>
         <xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
         <tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <xsl:apply-templates select="./rsd:employeeForAjustment"/>
      </tns:ratingAjustment>
   </xsl:template>

   <xsl:template match="rsd:listOfRatingAjustment">
      <tns:listOfRatingAjustment>
         <xsl:apply-templates select="./rsd:ratingAjustment"/>
      </tns:listOfRatingAjustment>
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

   <xsl:template match="rsd:calculatedFactor">
      <tns:calculatedFactor>
         <tns:code>
            <xsl:value-of select="./rsd:code"/>
         </tns:code>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:calculatedFactor>
   </xsl:template>

   <xsl:template match="rsd:listOfCalculatedFactor">
      <tns:listOfCalculatedFactor>
         <xsl:apply-templates select="./rsd:calculatedFactor"/>
      </tns:listOfCalculatedFactor>
   </xsl:template>

   <xsl:template match="rsd:resultRating">
      <tns:resultRating>
         <tns:isPrimary>
            <xsl:value-of select="./rsd:isPrimary"/>
         </tns:isPrimary>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
         <xsl:if test="./rsd:type">
            <tns:type>
               <xsl:value-of select="./rsd:type"/>
            </tns:type>
         </xsl:if>
      </tns:resultRating>
   </xsl:template>

   <xsl:template match="rsd:listOfResultRating">
      <tns:listOfResultRating>
         <xsl:apply-templates select="./rsd:resultRating"/>
      </tns:listOfResultRating>
   </xsl:template>

   <xsl:template match="rsd:cardinalFactor">
      <tns:cardinalFactor>
         <tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <xsl:if test="./rsd:numValue">
            <tns:numValue>
               <xsl:value-of select="./rsd:numValue"/>
            </tns:numValue>
         </xsl:if>
      </tns:cardinalFactor>
   </xsl:template>

   <xsl:template match="rsd:listOfCardinalFactor">
      <tns:listOfCardinalFactor>
         <xsl:apply-templates select="./rsd:cardinalFactor"/>
      </tns:listOfCardinalFactor>
   </xsl:template>

   <xsl:template match="rsd:ratingPeriod">
      <tns:ratingPeriod>
         <xsl:if test="./rsd:reportDate">
            <tns:reportDate>
               <xsl:value-of select="./rsd:reportDate"/>
            </tns:reportDate>
         </xsl:if>
         <xsl:if test="./rsd:period">
            <tns:period>
               <xsl:value-of select="./rsd:period"/>
            </tns:period>
         </xsl:if>
         <xsl:if test="./rsd:startDate">
            <tns:startDate>
               <xsl:value-of select="./rsd:startDate"/>
            </tns:startDate>
         </xsl:if>
         <xsl:if test="./rsd:endDate">
            <tns:endDate>
               <xsl:value-of select="./rsd:endDate"/>
            </tns:endDate>
         </xsl:if>
         <xsl:if test="./rsd:type">
            <tns:type>
               <xsl:value-of select="./rsd:type"/>
            </tns:type>
         </xsl:if>
         <xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
         <xsl:apply-templates select="./rsd:listOfCardinalFactor"/>
      </tns:ratingPeriod>
   </xsl:template>

   <xsl:template match="rsd:listOfRatingPeriod">
      <tns:listOfRatingPeriod>
         <xsl:apply-templates select="./rsd:ratingPeriod"/>
      </tns:listOfRatingPeriod>
   </xsl:template>

   <xsl:template match="rsd:supportFactor">
      <tns:supportFactor>
         <tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <xsl:if test="./rsd:value">
            <tns:value>
               <xsl:value-of select="./rsd:value"/>
            </tns:value>
         </xsl:if>
         <xsl:if test="./rsd:valueIntegrationId">
            <tns:valueIntegrationId>
               <xsl:value-of select="./rsd:valueIntegrationId"/>
            </tns:valueIntegrationId>
         </xsl:if>
         <xsl:if test="./rsd:freeValue">
            <tns:freeValue>
               <xsl:value-of select="./rsd:freeValue"/>
            </tns:freeValue>
         </xsl:if>
      </tns:supportFactor>
   </xsl:template>

   <xsl:template match="rsd:listOfSupportFactor">
      <tns:listOfSupportFactor>
         <xsl:apply-templates select="./rsd:supportFactor"/>
      </tns:listOfSupportFactor>
   </xsl:template>

   <xsl:template match="rsd:ratingSupport">
      <tns:ratingSupport>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:integrationId>
            <xsl:value-of select="./rsd:integrationId"/>
         </tns:integrationId>
         <xsl:apply-templates select="./rsd:listOfSupportFactor"/>
      </tns:ratingSupport>
   </xsl:template>

   <xsl:template match="rsd:listOfRatingSupport">
      <tns:listOfRatingSupport>
         <xsl:apply-templates select="./rsd:ratingSupport"/>
      </tns:listOfRatingSupport>
   </xsl:template>

   <xsl:template match="rsd:fixedFactor">
      <tns:fixedFactor>
         <tns:factor>
            <xsl:value-of select="./rsd:factor"/>
         </tns:factor>
         <tns:factorIntegrationId>
            <xsl:value-of select="./rsd:factorIntegrationId"/>
         </tns:factorIntegrationId>
         <tns:group>
            <xsl:value-of select="./rsd:group"/>
         </tns:group>
         <xsl:if test="./rsd:value">
            <tns:value>
               <xsl:value-of select="./rsd:value"/>
            </tns:value>
         </xsl:if>
         <xsl:if test="./rsd:valueIntegrationId">
            <tns:valueIntegrationId>
               <xsl:value-of select="./rsd:valueIntegrationId"/>
            </tns:valueIntegrationId>
         </xsl:if>
      </tns:fixedFactor>
   </xsl:template>

   <xsl:template match="rsd:listOfFixedFactor">
      <tns:listOfFixedFactor>
         <xsl:apply-templates select="./rsd:fixedFactor"/>
      </tns:listOfFixedFactor>
   </xsl:template>

   <xsl:template match="rsd:ratingCalc">
      <tns:ratingCalc>
         <tns:ratingCalcId>
            <xsl:value-of select="./rsd:ratingCalcId"/>
         </tns:ratingCalcId>
         <tns:modelName>
            <xsl:value-of select="./rsd:modelName"/>
         </tns:modelName>
         <tns:modelIntegrationId>
            <xsl:value-of select="./rsd:modelIntegrationId"/>
         </tns:modelIntegrationId>
         <xsl:if test="./rsd:dateCalc">
            <tns:dateCalc>
               <xsl:value-of select="./rsd:dateCalc"/>
            </tns:dateCalc>
         </xsl:if>
         <xsl:if test="./rsd:calcValue">
            <tns:calcValue>
               <xsl:value-of select="./rsd:calcValue"/>
            </tns:calcValue>
         </xsl:if>
         <xsl:apply-templates select="./rsd:listOfRatingAjustment"/>
         <xsl:apply-templates select="./rsd:employeeForCalc"/>
         <xsl:apply-templates select="./rsd:listOfResultRating"/>
         <xsl:apply-templates select="./rsd:listOfCalculatedFactor"/>
         <xsl:apply-templates select="./rsd:listOfFixedFactor"/>
         <xsl:apply-templates select="./rsd:listOfRatingPeriod"/>
         <xsl:apply-templates select="./rsd:listOfRatingSupport"/>
      </tns:ratingCalc>
   </xsl:template>

   <xsl:template match="rsd:listOfRatingCalc">
      <tns:listOfRatingCalc>
         <xsl:apply-templates select="./rsd:ratingCalc"/>
      </tns:listOfRatingCalc>
   </xsl:template>

   <xsl:template match="rsd:rating">
      <tns:rating>
         <tns:ratingId>
            <xsl:value-of select="./rsd:ratingId"/>
         </tns:ratingId>
         <xsl:if test="./rsd:finalRatingValue">
            <tns:finalRatingValue>
               <xsl:value-of select="./rsd:finalRatingValue"/>
            </tns:finalRatingValue>
         </xsl:if>
         <xsl:if test="./rsd:intermediateRatingValue">
            <tns:intermediateRatingValue>
               <xsl:value-of select="./rsd:intermediateRatingValue"/>
            </tns:intermediateRatingValue>
         </xsl:if>
         <xsl:if test="./rsd:ajustedRatingValue">
            <tns:ajustedRatingValue>
               <xsl:value-of select="./rsd:ajustedRatingValue"/>
            </tns:ajustedRatingValue>
         </xsl:if>
         <xsl:if test="./rsd:pd">
            <tns:pd>
               <xsl:value-of select="./rsd:pd"/>
            </tns:pd>
         </xsl:if>
         <tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <xsl:if test="./rsd:approvalDate">
            <tns:approvalDate>
               <xsl:value-of select="./rsd:approvalDate"/>
            </tns:approvalDate>
         </xsl:if>
         <xsl:if test="./rsd:calculationDate">
            <tns:calculationDate>
               <xsl:value-of select="./rsd:calculationDate"/>
            </tns:calculationDate>
         </xsl:if>
         <xsl:if test="./rsd:ajustmentDate">
            <tns:ajustmentDate>
               <xsl:value-of select="./rsd:ajustmentDate"/>
            </tns:ajustmentDate>
         </xsl:if>
         <tns:modelName>
            <xsl:value-of select="./rsd:modelName"/>
         </tns:modelName>
         <tns:modelIntegrationId>
            <xsl:value-of select="./rsd:modelIntegrationId"/>
         </tns:modelIntegrationId>
         <tns:ratingName>
            <xsl:value-of select="./rsd:ratingName"/>
         </tns:ratingName>
         <tns:ratingType>
            <xsl:value-of select="./rsd:ratingType"/>
         </tns:ratingType>
         <xsl:apply-templates select="./rsd:employeeForApprove"/>
         <xsl:apply-templates select="./rsd:listOfRatingCalc"/>
      </tns:rating>
   </xsl:template>

   <xsl:template match="rsd:listOfRating">
      <tns:listOfRating>
         <xsl:apply-templates select="./rsd:rating"/>
      </tns:listOfRating>
   </xsl:template>

   <xsl:template name="ImportRatingResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:ImportRatingResponse">
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:listOfRating"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
