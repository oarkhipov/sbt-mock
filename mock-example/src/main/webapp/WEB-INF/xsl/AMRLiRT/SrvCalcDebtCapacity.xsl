<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:amr="http://sbrf.ru/NCP/AMRLIRT/"
				xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/Data"
				xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
				xmlns:cal="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/"
				xmlns:rq="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRq/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
	
	<xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
	<xsl:param name="id" select="null"/>
	<xsl:param name="defaultId" select="string('5f4e83ab38514920b55f3eaa1dc378ad')"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:apply-templates select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/AMRLiRT/xml/SrvCalcDebtCapacityData.xml')/rsd:data"/>
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

	<xsl:template match="soap-env:Header">
		<soap-env:Header>
			<!--<soap-env:message-id><xsl:value-of select="concat(./soap-env:message-id,substring('5f4e83ab38514920b55f3eaa1dc378ad', 1 div mot(./soap-env:message-id)))"/></soap-env:message-id>-->
			<soap-env:message-id>
				<xsl:choose>
					<xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
					<xsl:when test="./soap-env:message-id"><xsl:value-of select="./soap-env:message-id"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
			</xsl:choose>
			</soap-env:message-id>
			<soap-env:request-time><xsl:value-of select="$timestamp"/></soap-env:request-time>
			<soap-env:operation-name>calculateDC</soap-env:operation-name>
		</soap-env:Header>
	</xsl:template>


	<!--Fill tags with data from data.xml (0..1)-->
	<xsl:template match="rsd:errorMessage">
		<rsd:errorMessage>
			<xsl:value-of select="."/>
		</rsd:errorMessage>
	</xsl:template>


	<xsl:template match="rsd:addParameter">
		<cal:addParameter>
			<!--Optional:-->
			<cal:order><xsl:value-of select="rsd:order"/></cal:order>
			<cal:name><xsl:value-of select="rsd:name"/></cal:name>
			<cal:value><xsl:value-of select="rsd:value"/></cal:value>
		</cal:addParameter>
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
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
				<cal:errorMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/></cal:errorMessage>
			</xsl:if>
			<cal:crmId><xsl:value-of select="//cal:crmId"/></cal:crmId>
			<!--Optional:-->
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:rmk">
				<cal:rmk><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmk"/></cal:rmk>
			</xsl:if>
			<cal:debtCapacity><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacity"/></cal:debtCapacity>
			<!--Optional:-->
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:rmkInDealCurrency">
				<cal:rmkInDealCurrency><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkInDealCurrency"/></cal:rmkInDealCurrency>
			</xsl:if>
			<cal:debtCapacityInDealCurrency><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityInDealCurrency"/></cal:debtCapacityInDealCurrency>
			<!--Optional:-->
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:rmkForNextYear">
				<cal:rmkForNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkForNextYear"/></cal:rmkForNextYear>
			</xsl:if>
			<!--Optional:-->
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:debtCapacityForNextYear">
				<cal:debtCapacityForNextYear><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityForNextYear"/></cal:debtCapacityForNextYear>
			</xsl:if>
			<cal:listOfAddParameter>
				<!--Zero or more repetitions:-->
				<xsl:apply-templates select="$data/rsd:response[@name=$response]//rsd:listOfAddParameter/rsd:addParameter"/>
			</cal:listOfAddParameter>
			<!--Optional:-->
			<xsl:if test="$data/rsd:response[@name=$response]/rsd:amMessage">
				<cal:amMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:amMessage"/></cal:amMessage>
			</xsl:if>
		</amr:calculateDCRs>
	</xsl:template>

</xsl:stylesheet>





