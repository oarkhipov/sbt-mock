<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:amr="http://sbrf.ru/NCP/AMRLIRT/"
				xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/Data"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:cal="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/"
				xmlns:rq="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRq/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/AMRLiRT/xml/CalculateDebtCapacityData.xml')/rsd:data"/>
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
		<amr:calculateDCRs>
			<cal:errorCode><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/></cal:errorCode>
			<!--Optional:-->
			<cal:errorMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/></cal:errorMessage>
			<cal:crmId><xsl:value-of select="//cal:crmId"/></cal:crmId>
			<!--Optional:-->
			<cal:rmk><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmk"/></cal:rmk>
			<cal:debtCapacity><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacity"/></cal:debtCapacity>
			<!--Optional:-->
			<cal:rmkInDealCurrency><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkInDealCurrency"/></cal:rmkInDealCurrency>
			<cal:debtCapacityInDealCurrency><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityInDealCurrency"/></cal:debtCapacityInDealCurrency>
			<!--Optional:-->
			<cal:rmkForNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkForNextYear"/></cal:rmkForNextYear>
			<!--Optional:-->
			<cal:debtCapacityForNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityForNextYear"/></cal:debtCapacityForNextYear>
			<cal:listOfAddParameter>
				<!--Zero or more repetitions:-->
				<xsl:for-each select="$data/rsd:response[@name=$response]//rsd:listOfAddParameter/rsd:addParameter">
				<cal:addParameter>
					<!--Optional:-->
					<cal:order><xsl:value-of select="rsd:order"/></cal:order>
					<cal:name><xsl:value-of select="rsd:name"/></cal:name>
					<cal:value><xsl:value-of select="rsd:value"/></cal:value>
				</cal:addParameter>
				</xsl:for-each>
			</cal:listOfAddParameter>
			<!--Optional:-->
			<cal:amMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:amMessage"/></cal:amMessage>
		</amr:calculateDCRs>
	</xsl:template>

</xsl:stylesheet>





