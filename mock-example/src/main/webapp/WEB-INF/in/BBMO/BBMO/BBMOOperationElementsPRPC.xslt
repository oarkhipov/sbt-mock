<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:st="http://sbrf.ru/prpc/bbmo/simpleTypes/10" xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10">
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="/xs:schema">
	
		<schema xmlns="http://www.w3.org/2001/XMLSchema" xmlns:st="http://sbrf.ru/prpc/bbmo/simpleTypes/10" xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10" xmlns:dt="http://sbrf.ru/prpc/bbmo/dataTypes/10" xmlns:tns="http://sbrf.ru/prpc/bbmo/10" targetNamespace="http://sbrf.ru/prpc/bbmo/10" elementFormDefault="qualified">
			<import namespace="http://sbrf.ru/prpc/bbmo/dataTypes/10" schemaLocation="./commonTypes/dataTypes.xsd"/>
			<import namespace="http://sbrf.ru/prpc/bbmo/simpleTypes/10" schemaLocation="./commonTypes/simpleTypes.xsd"/>
			<import namespace="http://sbrf.ru/prpc/bbmo/commonTypes/10" schemaLocation="./commonTypes/commonTypes.xsd"/>
		
			<xsl:comment>	
				<xsl:text>========================================</xsl:text>
			</xsl:comment>
			<xsl:comment>
				<xsl:text>Elements from schema ./BBMOOperationElements.xsd</xsl:text>
			</xsl:comment>
			<xsl:comment>	
				<xsl:text>========================================</xsl:text>
			</xsl:comment>
		
			<xsl:for-each select="xs:element|comment()">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			
			<xsl:for-each select="xs:include">
			
				<xsl:variable name="doc" select="document(./@schemaLocation)"/>
				
				<xsl:comment>	
					<xsl:text>========================================</xsl:text>
				</xsl:comment>				
				<xsl:comment>
					<xsl:text>Elements from schema </xsl:text>
					<xsl:value-of select="./@schemaLocation"/>
				</xsl:comment>
				<xsl:comment>	
					<xsl:text>========================================</xsl:text>
				</xsl:comment>		
				
				<xsl:for-each select="$doc/xs:schema/xs:element">
					<xsl:copy-of select="."/>
				</xsl:for-each>
				
			</xsl:for-each>
			
		</schema>
		
	</xsl:template>
	
</xsl:stylesheet>
