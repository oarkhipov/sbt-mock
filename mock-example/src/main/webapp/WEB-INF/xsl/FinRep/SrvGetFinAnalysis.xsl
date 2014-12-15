<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:rq="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRq/"
				xmlns:rs="http://sbrf.ru/NCP/ASFO/GetFinAnalysisRs/"
				xmlns:rsd="http://sbrf.ru/NCP/ASFO/GetFinAnalysis/Data"
				xmlns:crm="http://sbrf.ru/NCP/ASFO/">

	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/FinRep/xml/SrvGetFinAnalysis.xml')/rsd:data"/>
				<xsl:variable name="linkedTag" select="./soap-env:Body/crm:getFinAnalysisRq/rq:comment"/>
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
		<crm:getFinAnalysisRs>
			<rs:actualRequirements>
				<rs:creditRiskPerDebtor><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:actualRequirements/rsd:creditRiskPerDebtor"/></rs:creditRiskPerDebtor>
				<rs:creditRiskForRelatedDebtorGroup><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:creditRiskForRelatedDebtorGroup"/></rs:creditRiskForRelatedDebtorGroup>
				<rs:creditRiskPerLegalEntityInsider><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:creditRiskPerLegalEntityInsider"/></rs:creditRiskPerLegalEntityInsider>
				<rs:economicGroupRiskFactor><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:economicGroupRiskFactor"/></rs:economicGroupRiskFactor>
			</rs:actualRequirements>
			<rs:actualRequirementsInTotal>
				<rs:creditRiskPerDebtor><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:creditRiskPerDebtor"/></rs:creditRiskPerDebtor>
				<rs:creditRiskForRelatedDebtorGroup><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:creditRiskForRelatedDebtorGroup"/></rs:creditRiskForRelatedDebtorGroup>
				<rs:creditRiskPerLegalEntityInsider><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:creditRiskPerLegalEntityInsider"/></rs:creditRiskPerLegalEntityInsider>
				<rs:economicGroupRiskFactor><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:economicGroupRiskFactor"/></rs:economicGroupRiskFactor>
			</rs:actualRequirementsInTotal>
			<!--Optional:-->
			<rs:finIndexChangesReasonNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonNFRS"/></rs:finIndexChangesReasonNFRS>
			<!--Optional:-->
			<rs:finPerformanceCommentNFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentNFRS"/></rs:finPerformanceCommentNFRS>
			<!--Optional:-->
			<rs:finIndexChangesReasonIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finIndexChangesReasonIFRS"/></rs:finIndexChangesReasonIFRS>
			<!--Optional:-->
			<rs:finPerformanceCommentIFRS><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:finPerformanceCommentIFRS"/></rs:finPerformanceCommentIFRS>
			<rs:creditHistoryConclusions><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:creditHistoryConclusions"/></rs:creditHistoryConclusions>
			<rs:offBalanceSheetLiabilities><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:offBalanceSheetLiabilities"/></rs:offBalanceSheetLiabilities>
			<rs:liabilitiesDynamicsComment><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:liabilitiesDynamicsComment"/></rs:liabilitiesDynamicsComment>
			<rs:turnoversComment><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:turnoversComment"/></rs:turnoversComment>
			<rs:revenueExpensesStressAnalysis><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:revenueExpensesStressAnalysis"/></rs:revenueExpensesStressAnalysis>
			<!--Optional:-->
			<rs:structuredDealsStressAnalysis><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:structuredDealsStressAnalysis"/></rs:structuredDealsStressAnalysis>
			<rs:fieldInfo>
				<rs:mainField><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:mainField"/></rs:mainField>
				<rs:mainBusiness><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:mainBusiness"/></rs:mainBusiness>
			</rs:fieldInfo>
			<rs:marketPositionAndTrends>
				<xsl:for-each select="$data/rsd:response[@name=$response]//rsd:marketPositionAndTrends/rsd:trends">
					<rs:trends><xsl:value-of select="."/></rs:trends>
				</xsl:for-each>
			</rs:marketPositionAndTrends>
			<rs:business>
				<rs:suppliersConcentrationLevel><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:suppliersConcentrationLevel"/></rs:suppliersConcentrationLevel>
				<rs:buyersConcentrationLevel><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:buyersConcentrationLevel"/></rs:buyersConcentrationLevel>
				<rs:dependenceComment><xsl:value-of select="$data/rsd:response[@name=$response]//rsd:dependenceComment"/></rs:dependenceComment>
			</rs:business>
		</crm:getFinAnalysisRs>
	</xsl:template>

</xsl:stylesheet>