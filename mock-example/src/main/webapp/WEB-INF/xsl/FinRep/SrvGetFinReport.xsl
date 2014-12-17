<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:rq="http://sbrf.ru/NCP/ASFO/GetFinReportRq/"
				xmlns:rs="http://sbrf.ru/NCP/ASFO/GetFinReportRs/"
				xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetFinReport/Data"
				xmlns:crm="http://sbrf.ru/NCP/ASFO/">

	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/FinRep/xml/SrvGetFinReportData.xml')/rsd:data"/>
				<xsl:variable name="linkedTag" select="./soap-env:Body/crm:GetFinReportRq/rq:comment"/>
				<xsl:call-template name="FinAnalysis">
					<xsl:with-param name="data" select="$data"/>
					<xsl:with-param name="response">
						<xsl:choose>
							<xsl:when test="count($data/rsd:response[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
							<xsl:otherwise>default</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</soap-env:Body>
		</xsl:element>
	</xsl:template>


	<!--Fill tags with data from data.xml (0..1)-->
	<xsl:template match="rsd:errorMessage">
		<rs:errorMessage>
			<xsl:value-of select="."/>
		</rs:errorMessage>
	</xsl:template>

	<!--Transform main XML-->
	<xsl:template name="FinAnalysis">
		<!--Get params-->
		<xsl:param name="response"/>
		<xsl:param name="data"/>
		<!-- - - - - - - - -->
		<crm:getFinAnalysisRq>
			<rs:nonCurrentAssetsNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsNFRS"/></rs:nonCurrentAssetsNFRS>
			<rs:currentAssetsNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentAssetsNFRS"/></rs:currentAssetsNFRS>
			<rs:nonCurrentAssetsIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:nonCurrentAssetsIFRS"/></rs:nonCurrentAssetsIFRS>
			<rs:currentAssetsIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentAssetsIFRS"/></rs:currentAssetsIFRS>
			<rs:longTermDebtNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:longTermDebtNFRS"/></rs:longTermDebtNFRS>
			<rs:shortTermDebtNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:shortTermDebtNFRS"/></rs:shortTermDebtNFRS>
			<rs:longTermDebtIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:longTermDebtIFRS"/></rs:longTermDebtIFRS>
			<rs:shortTermDebtIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:shortTermDebtIFRS"/></rs:shortTermDebtIFRS>
			<rs:ebitdaNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ebitdaNFRS"/></rs:ebitdaNFRS>
			<rs:numQuarterNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:numQuarterNFRS"/></rs:numQuarterNFRS>
			<rs:ebitdaIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ebitdaIFRS"/></rs:ebitdaIFRS>
			<rs:numQuarterIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:numQuarterIFRS"/></rs:numQuarterIFRS>
			<rs:currencyCode><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyCode"/></rs:currencyCode>
			<rs:currencyCodeForFR><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyCodeForFR"/></rs:currencyCodeForFR>
			<rs:currencyRate><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currencyRate"/></rs:currencyRate>
			<rs:futureCurrencyRate><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:futureCurrencyRate"/></rs:futureCurrencyRate>
			<rs:currentCurrencyRate><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentCurrencyRate"/></rs:currentCurrencyRate>
			<rs:purpose><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:purpose"/></rs:purpose>
			<rs:currentYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:currentYear"/></rs:currentYear>
			<rs:govDebt><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:govDebt"/></rs:govDebt>
			<rs:creditGovDebt><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:creditGovDebt"/></rs:creditGovDebt>
			<rs:credit><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:credit"/></rs:credit>
			<rs:liabilities><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilities"/></rs:liabilities>
			<rs:liabilitiesNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilitiesNextYear"/></rs:liabilitiesNextYear>
			<rs:income><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:income"/></rs:income>
			<rs:incomeNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:incomeNextYear"/></rs:incomeNextYear>
			<rs:plannedCredit><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:plannedCredit"/></rs:plannedCredit>
			<rs:plannedCreditNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:plannedCreditNextYear"/></rs:plannedCreditNextYear>
		</crm:getFinAnalysisRq>
	</xsl:template>

</xsl:stylesheet>