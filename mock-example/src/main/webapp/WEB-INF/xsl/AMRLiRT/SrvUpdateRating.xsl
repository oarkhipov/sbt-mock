<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:amr="http://sbrf.ru/NCP/AMRLIRT/"
				xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CorrectRating/Data"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:cor="http://sbrf.ru/NCP/AMRLIRT/CorrectRatingRs/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/AMRLiRT/xml/CorrectRatingData.xml')/rsd:data"/>
				<xsl:variable name="linkedTag" select="./soap-env:Body//cor:errorMessage"/>
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
		<amr:correctRatingRs>
			<cor:return>
				<cor:errorCode><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/></cor:errorCode>
				<!--Optional:-->
				<cor:errorMessage><xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/></cor:errorMessage>
				<!--Optional:-->
				<cor:listOfAddParameter>
					<!--Zero or more repetitions:-->
					<xsl:for-each select="$data//rsd:listOfAddParameter/rsd:addParameter">
					<cor:addParameter>
						<cor:order><xsl:value-of select="rsd:order"/></cor:order>
						<cor:name><xsl:value-of select="rsd:name"/></cor:name>
						<cor:value><xsl:value-of select="rsd:value"/></cor:value>
					</cor:addParameter>
					</xsl:for-each>
				</cor:listOfAddParameter>
			</cor:return>
		</amr:correctRatingRs>
	</xsl:template>

</xsl:stylesheet>





