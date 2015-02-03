<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/"
                xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/ASFO/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="//soap:Body/*//*[local-name()='dealId'][1]/text()"/>
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
            <xsl:with-param name="operation-name" select="string('getFinAnalysisRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="getFinAnalysisRs">
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

   <xsl:template match="rsd:marketPositionAndTrends">
      <tns:marketPositionAndTrends>
         <tns:trends>
            <xsl:value-of select="./rsd:trends"/>
         </tns:trends>
      </tns:marketPositionAndTrends>
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

   <xsl:template name="getFinAnalysisRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:getFinAnalysisRs">
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
