<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:amr="http://sbrf.ru/NCP/AMRLIRT/"
				xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateLGD/Data"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:cal="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/"
				xmlns:rq="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRq/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/AMRLiRT/xml/SrvCalcLGDData.xml')/rsd:data"/>
				<xsl:variable name="linkedTag" select="./soap-env:Body//rq:comments"/>
				<xsl:call-template name="RsBody">
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
		<rsd:errorMessage>
			<xsl:value-of select="."/>
		</rsd:errorMessage>
	</xsl:template>


	<!--Transform main XML-->
	<xsl:template name="RsBody">
		<!--Get params-->
		<xsl:param name="response"/>
		<xsl:param name="data"/>
		<!-- - - - - - - - -->
		<amr:calculateLGDRs>
			<cal:errorCode><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/></cal:errorCode>
			<!--Optional:-->
			<cal:errorMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/></cal:errorMessage>
			<cal:crmId><xsl:value-of select="//cal:crmId"/></cal:crmId>
			<cal:lgdType><xsl:value-of select="//cal:lgdType"/></cal:lgdType>
			<cal:lgdDate><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgdDate"/></cal:lgdDate>
			<!--Optional:-->
			<cal:pd><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:pd"/></cal:pd>
			<cal:lgd><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgd"/></cal:lgd>
			<cal:ead><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ead"/></cal:ead>
			<cal:sum><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:sum"/></cal:sum>
			<!--Optional:-->
			<cal:elPercent><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:elPercent"/></cal:elPercent>
			<!--Optional:-->
			<cal:el><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:el"/></cal:el>
			<cal:totalValue><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalValue"/></cal:totalValue>
			<cal:totalColValueLgd><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueLgd"/></cal:totalColValueLgd>
			<cal:totalColValueEad><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueEad"/></cal:totalColValueEad>
			<!--Optional:-->
			<cal:listOfCollateral>
				<!--Zero or more repetitions:-->
				<xsl:for-each select="$data/rsd:response[@name=$response]//rsd:listOfCollateral/rsd:collateral">
				<cal:collateral>
					<cal:crmId><xsl:value-of select="rsd:crmId"/></cal:crmId>
					<cal:collType><xsl:value-of select="rsd:collType"/></cal:collType>
					<cal:returnRate><xsl:value-of select="rsd:returnRate"/></cal:returnRate>
					<cal:discountRate><xsl:value-of select="rsd:discountRate"/></cal:discountRate>
					<cal:collValueEad><xsl:value-of select="rsd:collValueEad"/></cal:collValueEad>
					<cal:collValueLgd><xsl:value-of select="rsd:collValueLgd"/></cal:collValueLgd>
				</cal:collateral>
				</xsl:for-each>
			</cal:listOfCollateral>
		</amr:calculateLGDRs>
	</xsl:template>

</xsl:stylesheet>





