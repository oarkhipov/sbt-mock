<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/ASFO/GetFinReportRs/"
                xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetFinReportRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:FinRep="http://sbrf.ru/NCP/ASFO/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//soap:Body/*//*[local-name()='finReportType'][1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/FinRep/xml/FinReportImportData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('getFinReportRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="getFinReportRs">
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

   <xsl:template name="getFinReportRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="FinRep:getFinReportRs">
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsNFRS">
            <tns:nonCurrentAssetsNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsNFRS"/>
            </tns:nonCurrentAssetsNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currentAssetsNFRS">
            <tns:currentAssetsNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentAssetsNFRS"/>
            </tns:currentAssetsNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsIFRS">
            <tns:nonCurrentAssetsIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsIFRS"/>
            </tns:nonCurrentAssetsIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currentAssetsIFRS">
            <tns:currentAssetsIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentAssetsIFRS"/>
            </tns:currentAssetsIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:longTermDebtNFRS">
            <tns:longTermDebtNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:longTermDebtNFRS"/>
            </tns:longTermDebtNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:shortTermDebtNFRS">
            <tns:shortTermDebtNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:shortTermDebtNFRS"/>
            </tns:shortTermDebtNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:longTermDebtIFRS">
            <tns:longTermDebtIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:longTermDebtIFRS"/>
            </tns:longTermDebtIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:shortTermDebtIFRS">
            <tns:shortTermDebtIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:shortTermDebtIFRS"/>
            </tns:shortTermDebtIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:ebitdaNFRS">
            <tns:ebitdaNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ebitdaNFRS"/>
            </tns:ebitdaNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:numQuarterNFRS">
            <tns:numQuarterNFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:numQuarterNFRS"/>
            </tns:numQuarterNFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:ebitdaIFRS">
            <tns:ebitdaIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ebitdaIFRS"/>
            </tns:ebitdaIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:numQuarterIFRS">
            <tns:numQuarterIFRS>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:numQuarterIFRS"/>
            </tns:numQuarterIFRS>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currencyCode">
            <tns:currencyCode>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyCode"/>
            </tns:currencyCode>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currencyCodeForFR">
            <tns:currencyCodeForFR>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyCodeForFR"/>
            </tns:currencyCodeForFR>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currencyRate">
            <tns:currencyRate>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyRate"/>
            </tns:currencyRate>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:futureCurrencyRate">
            <tns:futureCurrencyRate>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:futureCurrencyRate"/>
            </tns:futureCurrencyRate>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currentCurrencyRate">
            <tns:currentCurrencyRate>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentCurrencyRate"/>
            </tns:currentCurrencyRate>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:purpose">
            <tns:purpose>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:purpose"/>
            </tns:purpose>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:currentYear">
            <tns:currentYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentYear"/>
            </tns:currentYear>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:govDebt">
            <tns:govDebt>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:govDebt"/>
            </tns:govDebt>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:creditGovDebt">
            <tns:creditGovDebt>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:creditGovDebt"/>
            </tns:creditGovDebt>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:credit">
            <tns:credit>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:credit"/>
            </tns:credit>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:liabilities">
            <tns:liabilities>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilities"/>
            </tns:liabilities>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:liabilitiesNextYear">
            <tns:liabilitiesNextYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilitiesNextYear"/>
            </tns:liabilitiesNextYear>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:income">
            <tns:income>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:income"/>
            </tns:income>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:incomeNextYear">
            <tns:incomeNextYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:incomeNextYear"/>
            </tns:incomeNextYear>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:plannedCredit">
            <tns:plannedCredit>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:plannedCredit"/>
            </tns:plannedCredit>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:plannedCreditNextYear">
            <tns:plannedCreditNextYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:plannedCreditNextYear"/>
            </tns:plannedCreditNextYear>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
