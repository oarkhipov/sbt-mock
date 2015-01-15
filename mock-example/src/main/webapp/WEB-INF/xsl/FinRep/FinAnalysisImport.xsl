<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/"
                xmlns="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/"
                xmlns:amrct="http://sbrf.ru/NCP/AMRLIRT/CommonTypes/"
                xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/Data/"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/ASFO/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="//soap-env:Body/*/*[1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/FinRep/xml/FinAnalysisImportData.xml'"/>
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
                  <xsl:with-param name="operation-name" select="string('FinAnalysisImportResponse')"/>
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
            <xsl:call-template name="FinAnalysisImportResponse">
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

   <xsl:template match="rsd:actualRequirements">
      <tns:actualRequirements>
         <tns:creditRiskPerDebtor>
            <xsl:value-of select="./rsd:creditRiskPerDebtor"/>
         </tns:creditRiskPerDebtor>
         <tns:creditRiskForRelatedDebtorGroup>
            <xsl:value-of select="./rsd:creditRiskForRelatedDebtorGroup"/>
         </tns:creditRiskForRelatedDebtorGroup>
         <tns:creditRiskPerLegalEntityInsider>
            <xsl:value-of select="./rsd:creditRiskPerLegalEntityInsider"/>
         </tns:creditRiskPerLegalEntityInsider>
         <tns:economicGroupRiskFactor>
            <xsl:value-of select="./rsd:economicGroupRiskFactor"/>
         </tns:economicGroupRiskFactor>
      </tns:actualRequirements>
   </xsl:template>

   <xsl:template match="rsd:actualRequirementsInTotal">
      <tns:actualRequirementsInTotal>
         <tns:creditRiskPerDebtor>
            <xsl:value-of select="./rsd:creditRiskPerDebtor"/>
         </tns:creditRiskPerDebtor>
         <tns:creditRiskForRelatedDebtorGroup>
            <xsl:value-of select="./rsd:creditRiskForRelatedDebtorGroup"/>
         </tns:creditRiskForRelatedDebtorGroup>
         <tns:creditRiskPerLegalEntityInsider>
            <xsl:value-of select="./rsd:creditRiskPerLegalEntityInsider"/>
         </tns:creditRiskPerLegalEntityInsider>
         <tns:economicGroupRiskFactor>
            <xsl:value-of select="./rsd:economicGroupRiskFactor"/>
         </tns:economicGroupRiskFactor>
      </tns:actualRequirementsInTotal>
   </xsl:template>

   <xsl:template match="rsd:fieldInfo">
      <tns:fieldInfo>
         <tns:mainField>
            <xsl:value-of select="./rsd:mainField"/>
         </tns:mainField>
         <tns:mainBusiness>
            <xsl:value-of select="./rsd:mainBusiness"/>
         </tns:mainBusiness>
      </tns:fieldInfo>
   </xsl:template>

   <xsl:template match="rsd:competitor">
      <tns:competitor>
         <tns:advantages>
            <xsl:value-of select="./rsd:advantages"/>
         </tns:advantages>
         <tns:disadvantages>
            <xsl:value-of select="./rsd:disadvantages"/>
         </tns:disadvantages>
         <tns:revenue>
            <xsl:value-of select="./rsd:revenue"/>
         </tns:revenue>
         <tns:ebitda>
            <xsl:value-of select="./rsd:ebitda"/>
         </tns:ebitda>
         <tns:debt>
            <xsl:value-of select="./rsd:debt"/>
         </tns:debt>
         <tns:assets>
            <xsl:value-of select="./rsd:assets"/>
         </tns:assets>
         <tns:equity>
            <xsl:value-of select="./rsd:equity"/>
         </tns:equity>
         <tns:ebitdaRevenue>
            <xsl:value-of select="./rsd:ebitdaRevenue"/>
         </tns:ebitdaRevenue>
         <tns:debtEbitda>
            <xsl:value-of select="./rsd:debtEbitda"/>
         </tns:debtEbitda>
         <tns:debtEquity>
            <xsl:value-of select="./rsd:debtEquity"/>
         </tns:debtEquity>
      </tns:competitor>
   </xsl:template>

   <xsl:template match="rsd:borrower">
      <tns:borrower>
         <tns:advantages>
            <xsl:value-of select="./rsd:advantages"/>
         </tns:advantages>
         <tns:disadvantages>
            <xsl:value-of select="./rsd:disadvantages"/>
         </tns:disadvantages>
         <tns:revenue>
            <xsl:value-of select="./rsd:revenue"/>
         </tns:revenue>
         <tns:ebitda>
            <xsl:value-of select="./rsd:ebitda"/>
         </tns:ebitda>
         <tns:debt>
            <xsl:value-of select="./rsd:debt"/>
         </tns:debt>
         <tns:assets>
            <xsl:value-of select="./rsd:assets"/>
         </tns:assets>
         <tns:equity>
            <xsl:value-of select="./rsd:equity"/>
         </tns:equity>
         <tns:ebitdaRevenue>
            <xsl:value-of select="./rsd:ebitdaRevenue"/>
         </tns:ebitdaRevenue>
         <tns:debtEbitda>
            <xsl:value-of select="./rsd:debtEbitda"/>
         </tns:debtEbitda>
         <tns:debtEquity>
            <xsl:value-of select="./rsd:debtEquity"/>
         </tns:debtEquity>
      </tns:borrower>
   </xsl:template>

   <xsl:template match="rsd:listOfCompetitor">
      <tns:listOfCompetitor>
         <xsl:apply-templates select="./rsd:competitor"/>
      </tns:listOfCompetitor>
   </xsl:template>

   <xsl:template match="rsd:market">
      <tns:market>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:posidionAndShare>
            <xsl:value-of select="./rsd:posidionAndShare"/>
         </tns:posidionAndShare>
         <tns:description>
            <xsl:value-of select="./rsd:description"/>
         </tns:description>
         <tns:lastFinReportQuarter>
            <xsl:value-of select="./rsd:lastFinReportQuarter"/>
         </tns:lastFinReportQuarter>
         <xsl:apply-templates select="./rsd:borrower"/>
         <xsl:apply-templates select="./rsd:listOfCompetitor"/>
      </tns:market>
   </xsl:template>

   <xsl:template match="rsd:marketPositionAndTrends">
      <tns:marketPositionAndTrends>
         <tns:trends>
            <xsl:value-of select="./rsd:trends"/>
         </tns:trends>
      </tns:marketPositionAndTrends>
   </xsl:template>

   <xsl:template match="rsd:supplier">
      <tns:supplier>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:isCgParticipant>
            <xsl:value-of select="./rsd:isCgParticipant"/>
         </tns:isCgParticipant>
         <tns:conditions>
            <xsl:value-of select="./rsd:conditions"/>
         </tns:conditions>
         <tns:productsAndTrends>
            <xsl:value-of select="./rsd:productsAndTrends"/>
         </tns:productsAndTrends>
      </tns:supplier>
   </xsl:template>

   <xsl:template match="rsd:buyer">
      <tns:buyer>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:isCgParticipant>
            <xsl:value-of select="./rsd:isCgParticipant"/>
         </tns:isCgParticipant>
         <tns:conditions>
            <xsl:value-of select="./rsd:conditions"/>
         </tns:conditions>
         <tns:productsAndTrends>
            <xsl:value-of select="./rsd:productsAndTrends"/>
         </tns:productsAndTrends>
      </tns:buyer>
   </xsl:template>

   <xsl:template match="rsd:business">
      <tns:business>
         <tns:suppliersConcentrationLevel>
            <xsl:value-of select="./rsd:suppliersConcentrationLevel"/>
         </tns:suppliersConcentrationLevel>
         <tns:buyersConcentrationLevel>
            <xsl:value-of select="./rsd:buyersConcentrationLevel"/>
         </tns:buyersConcentrationLevel>
         <tns:dependenceComment>
            <xsl:value-of select="./rsd:dependenceComment"/>
         </tns:dependenceComment>
      </tns:business>
   </xsl:template>

   <xsl:template name="FinAnalysisImportResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:FinAnalysisImportResponse">
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:actualRequirements"/>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:actualRequirementsInTotal"/>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonNFRS">
            <tns:finIndexChangesReasonNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonNFRS"/>
            </tns:finIndexChangesReasonNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentNFRS">
            <tns:finPerformanceCommentNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentNFRS"/>
            </tns:finPerformanceCommentNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonIFRS">
            <tns:finIndexChangesReasonIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonIFRS"/>
            </tns:finIndexChangesReasonIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentIFRS">
            <tns:finPerformanceCommentIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentIFRS"/>
            </tns:finPerformanceCommentIFRS>
         </xsl:if>
         <tns:creditHistoryConclusions>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:creditHistoryConclusions"/>
         </tns:creditHistoryConclusions>
         <tns:offBalanceSheetLiabilities>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:offBalanceSheetLiabilities"/>
         </tns:offBalanceSheetLiabilities>
         <tns:liabilitiesDynamicsComment>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilitiesDynamicsComment"/>
         </tns:liabilitiesDynamicsComment>
         <tns:turnoversComment>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:turnoversComment"/>
         </tns:turnoversComment>
         <tns:revenueExpensesStressAnalysis>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:revenueExpensesStressAnalysis"/>
         </tns:revenueExpensesStressAnalysis>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:structuredDealsStressAnalysis">
            <tns:structuredDealsStressAnalysis>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:structuredDealsStressAnalysis"/>
            </tns:structuredDealsStressAnalysis>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:fieldInfo"/>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:marketPositionAndTrends"/>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:business"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
