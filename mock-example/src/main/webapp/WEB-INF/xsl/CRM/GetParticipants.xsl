<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
	>
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

	<!-- - - - - - - - - - - - - - - - - DATA - - - - - - - - - - - - - - - - - - - - - - -->
	Prepare data and section of data XML
	<!--<xsl:template match="soap-env:Envelope">-->
		<!--<xsl:element name="soap-env:Envelope">-->
			<!--<xsl:copy-of select="soap-env:Header"/>-->
			<!--<soap-env:Body>-->
				<!--<xsl:variable name="data" select="document('../../data/xml/CRM/GetParticipants.xml')/data"/>-->
				<!--&lt;!&ndash;Link to field&ndash;&gt;-->
				<!--<xsl:variable name="linkedTag" select="./soap-env:Body/rq:createTaskRq/rq:comment"/>-->
				<!--&lt;!&ndash;Call template for main xml&ndash;&gt;-->
				<!--<xsl:call-template name="GetParticipants">-->
					<!--<xsl:with-param name="data" select="$data"/>-->
					<!--<xsl:with-param name="response">-->
						<!--<xsl:choose>-->
							<!--<xsl:when test="count($data/response[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>-->
							<!--<xsl:otherwise>default</xsl:otherwise>-->
						<!--</xsl:choose>-->
					<!--</xsl:with-param>-->
				<!--</xsl:call-template>-->
			<!--</soap-env:Body>-->
		<!--</xsl:element>-->
	<!--</xsl:template>-->

	<!-- - - - - - - - - - - - - - - - - CONTAINERS - - - - - - - - - - - - - - - - - - - - - - -->
	<!--Fill container with data from data.xml-->
	<!--<xsl:template match="response">-->
		<!--<xsl:apply-templates select="errorMessage"/>-->
	<!--</xsl:template>-->

	<!-- - - - - - - - - - - - - - - - - TAGS - - - - - - - - - - - - - - - - - - - - - - -->
	<!--Fill tags with data from data.xml-->
	<!--<xsl:template match="errorMessage">-->
		<!--<ns2:errorMessage>-->
			<!--<xsl:value-of select="."/>-->
		<!--</ns2:errorMessage>-->
	<!--</xsl:template>-->

	<!-- - - - - - - - - - - - - - - - - MAIN XML WITH DIRECT MAPPINGS - - - - - - - - - - - - - - - - - - - - - - -->

	<xsl:template name="GetParticipants">
		<!--Get params-->
		<xsl:param name="response"/>
		<xsl:param name="data"/>
		<!-- - - - - - - - -->
		<crm:prtspRs xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:rs="http://sbrf.ru/NCP/CRM/GetParticipantsRs/">
			<rs:id>string</rs:id>
			<rs:requestType>string</rs:requestType>
			<rs:participantsGroup>
				<!--Zero or more repetitions:-->
				<rs:participants>
					<rs:participantID>string</rs:participantID>
					<rs:type>string</rs:type>
					<rs:isBorrower>false</rs:isBorrower>
					<rs:clientType>string</rs:clientType>
					<rs:name>string</rs:name>
					<rs:resident>true</rs:resident>
					<!--Zero or more repetitions:-->
					<rs:address>
						<rs:type>string</rs:type>
						<rs:city>string</rs:city>
						<rs:street>string</rs:street>
						<rs:building>string</rs:building>
						<rs:block>string</rs:block>
						<rs:phone>string</rs:phone>
						<rs:email>string</rs:email>
					</rs:address>
					<!--Zero or more repetitions:-->
					<rs:share>
						<rs:ownershipID>string</rs:ownershipID>
						<rs:share>1.0</rs:share>
						<rs:periodOfOwnerhip>string</rs:periodOfOwnerhip>
					</rs:share>
					<rs:consolidatedGroupID>string</rs:consolidatedGroupID>
					<rs:linkedBorrowersGroupID>string</rs:linkedBorrowersGroupID>
					<!--Optional:-->
					<rs:industry>string</rs:industry>
					<rs:isProblemClient>true</rs:isProblemClient>
					<!--Zero or more repetitions:-->
					<rs:rating>
						<rs:ratingID>string</rs:ratingID>
						<rs:ratingValue>1.0</rs:ratingValue>
						<rs:ratingType>string</rs:ratingType>
						<rs:ratingModel>string</rs:ratingModel>
						<rs:ratingCalculatedDate>2014-09-19</rs:ratingCalculatedDate>
						<rs:ratingCalculatedRole>string</rs:ratingCalculatedRole>
						<rs:ratingCalculatedBy>string</rs:ratingCalculatedBy>
						<rs:ratingApprovalDate>2006-08-19+04:00</rs:ratingApprovalDate>
						<rs:ratingApprovedBy>string</rs:ratingApprovedBy>
					</rs:rating>
					<rs:roleInConsolidatedGroup>string</rs:roleInConsolidatedGroup>
				</rs:participants>
				<rs:groupType>string</rs:groupType>
				<!--Optional:-->
				<rs:groupInfo>
					<rs:id>string</rs:id>
					<rs:label>string</rs:label>
					<rs:status>string</rs:status>
					<rs:updateDate>2009-05-16</rs:updateDate>
					<rs:approvalDate>2003-08-09+04:00</rs:approvalDate>
					<!--Zero or more repetitions:-->
					<rs:ratings>
						<rs:ratingID>string</rs:ratingID>
						<rs:ratingValue>1.0</rs:ratingValue>
						<rs:ratingType>string</rs:ratingType>
						<rs:ratingModel>string</rs:ratingModel>
						<rs:ratingCalculatedDate>2012-09-13+04:00</rs:ratingCalculatedDate>
						<rs:ratingCalculatedRole>string</rs:ratingCalculatedRole>
						<rs:ratingCalculatedBy>string</rs:ratingCalculatedBy>
						<rs:ratingApprovalDate>2009-10-14</rs:ratingApprovalDate>
						<rs:ratingApprovedBy>string</rs:ratingApprovedBy>
					</rs:ratings>
					<rs:topLevelGroupName>string</rs:topLevelGroupName>
				</rs:groupInfo>
			</rs:participantsGroup>
			<rs:operationStatus>
				<rs:errorCode>string</rs:errorCode>
				<!--Optional:-->
				<rs:errorMessage>string</rs:errorMessage>
			</rs:operationStatus>
		</crm:prtspRs>
	</xsl:template>

</xsl:stylesheet>