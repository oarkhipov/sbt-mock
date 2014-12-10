<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:crm="http://sbrf.ru/NCP/CRM/"
	xmlns:rq="http://sbrf.ru/NCP/CRM/GetParticipantsRq/"
	xmlns:rs="http://sbrf.ru/NCP/CRM/GetParticipantsRs/"
	xmlns:rsd="http://sbrf.ru/NCP/CRM/GetParticipantsRs/Data/"
	>
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

	<!-- - - - - - - - - - - - - - - - - DATA - - - - - - - - - - - - - - - - - - - - - - -->
	<!--Prepare data and section of data XML-->
	<xsl:template match="soap:Envelope">
		<xsl:element name="soap:Envelope">
			<xsl:copy-of select="soap:Header"/>
			<soap:Body>
				<xsl:variable name="data" select="document('../../data/CRM/xml/GetParticipantsData.xml')/rsd:data"/>
				<!--Link to field-->
				<xsl:variable name="linkedTag" select="./soap:Body/crm:prtspRq/rq:comment"/>
				<!--Call template for main xml-->
				<xsl:call-template name="GetParticipants">
					<xsl:with-param name="data" select="$data"/>
					<xsl:with-param name="response">
						<xsl:choose>
							<xsl:when test="count($data/response[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
							<xsl:otherwise>default</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</soap:Body>
		</xsl:element>
	</xsl:template>

	<!-- - - - BLOCKS - - - -->
	<xsl:template match="rsd:ratings">
		<!--Zero or more repetitions:-->
		<rs:ratings>
			<rs:ratingID>
				<xsl:value-of select="rsd:ratingID"/>
			</rs:ratingID>
			<rs:ratingValue>
				<xsl:value-of select="rsd:ratingValue"/>
			</rs:ratingValue>
			<rs:ratingType>
				<xsl:value-of select="rsd:ratingType"/>
			</rs:ratingType>
			<rs:ratingModel>
				<xsl:value-of select="rsd:ratingModel"/>
			</rs:ratingModel>
			<rs:ratingCalculatedDate>
				<xsl:value-of select="rsd:ratingCalculatedDate"/>
			</rs:ratingCalculatedDate>
			<rs:ratingCalculatedRole>
				<xsl:value-of select="rsd:ratingCalculatedRole"/>
			</rs:ratingCalculatedRole>
			<rs:ratingCalculatedBy>
				<xsl:value-of select="rsd:ratingCalculatedBy"/>
			</rs:ratingCalculatedBy>
			<rs:ratingApprovalDate>
				<xsl:value-of select="rsd:ratingApprovalDate"/>
			</rs:ratingApprovalDate>
			<rs:ratingApprovedBy>
				<xsl:value-of select="rsd:ratingApprovedBy"/>
			</rs:ratingApprovedBy>
		</rs:ratings>
	</xsl:template> <!--DONE-->

	<xsl:template match="rsd:rating">
		<!--Zero or more repetitions:-->
		<rs:rating>
			<rs:ratingID>
				<xsl:value-of select="rsd:ratingID"/>
			</rs:ratingID>
			<rs:ratingValue>
				<xsl:value-of select="rsd:ratingValue"/>
			</rs:ratingValue>
			<rs:ratingType>
				<xsl:value-of select="rsd:ratingType"/>
			</rs:ratingType>
			<rs:ratingModel>
				<xsl:value-of select="rsd:ratingModel"/>
			</rs:ratingModel>
			<rs:ratingCalculatedDate>
				<xsl:value-of select="rsd:ratingCalculatedDate"/>
			</rs:ratingCalculatedDate>
			<rs:ratingCalculatedRole>
				<xsl:value-of select="rsd:ratingCalculatedRole"/>
			</rs:ratingCalculatedRole>
			<rs:ratingCalculatedBy>
				<xsl:value-of select="rsd:ratingCalculatedBy"/>
			</rs:ratingCalculatedBy>
			<rs:ratingApprovalDate>
				<xsl:value-of select="rsd:ratingApprovalDate"/>
			</rs:ratingApprovalDate>
			<rs:ratingApprovedBy>
				<xsl:value-of select="rsd:ratingApprovedBy"/>
			</rs:ratingApprovedBy>
		</rs:rating>
	</xsl:template> <!--DONE-->

	<xsl:template match="rsd:share">
		<!--Zero or more repetitions:-->
		<rs:share>
			<rs:ownershipID>
				<xsl:value-of select="rsd:ownershipID"/>
			</rs:ownershipID>
			<rs:share>
				<xsl:value-of select="rsd:share"/>
			</rs:share>
			<rs:periodOfOwnerhip>
				<xsl:value-of select="rsd:periodOfOwnerhip"/>
			</rs:periodOfOwnerhip>
		</rs:share>
	</xsl:template> <!--DONE-->

	<xsl:template match="rsd:address">
		<!--Zero or more repetitions:-->
		<rs:address>
			<rs:type>
				<xsl:value-of select="rsd:type"/>
			</rs:type>
			<rs:city>
				<xsl:value-of select="rsd:city"/>
			</rs:city>
			<rs:street>
				<xsl:value-of select="rsd:street"/>
			</rs:street>
			<rs:building>
				<xsl:value-of select="rsd:building"/>
			</rs:building>
			<rs:block>
				<xsl:value-of select="rsd:block"/>
			</rs:block>
			<rs:phone>
				<xsl:value-of select="rsd:phone"/>
			</rs:phone>
			<rs:email>
				<xsl:value-of select="rsd:email"/>
			</rs:email>
		</rs:address>
	</xsl:template> <!--DONE-->

	<xsl:template match="rsd:groupInfo">
		<!--Optional:-->
		<rs:groupInfo>
			<rs:id>
				<xsl:value-of select="rsd:id"/>
			</rs:id>
			<rs:label>
				<xsl:value-of select="rsd:label"/>
			</rs:label>
			<rs:status>
				<xsl:value-of select="rsd:status"/>
			</rs:status>
			<rs:updateDate>
				<xsl:value-of select="rsd:updateDate"/>
			</rs:updateDate>
			<rs:approvalDate>
				<xsl:value-of select="rsd:approvalDate"/>
			</rs:approvalDate>
			<xsl:apply-templates select="rsd:ratings"/>
			<rs:topLevelGroupName>
				<xsl:value-of select="rsd:topLevelGroupName"/>
			</rs:topLevelGroupName>
		</rs:groupInfo>
	</xsl:template> <!--DONE-->
	
	<xsl:template match="rsd:participants">
		<!--Zero or more repetitions:-->
		<rs:participants>
			<rs:participantID>
				<xsl:value-of select="rsd:participantID"/>
			</rs:participantID>
			<rs:type>
				<xsl:value-of select="rsd:type"/>
			</rs:type>
			<rs:isBorrower>
				<xsl:value-of select="rsd:isBorrower"/>
			</rs:isBorrower>
			<rs:clientType>
				<xsl:value-of select="rsd:clientType"/>
			</rs:clientType>
			<rs:name>
				<xsl:value-of select="rsd:name"/>
			</rs:name>
			<rs:resident>
				<xsl:value-of select="rsd:resident"/>
			</rs:resident>
			<xsl:apply-templates select="rsd:address"/>
			<xsl:apply-templates select="rsd:share"/>
			<rs:consolidatedGroupID>
				<xsl:value-of select="rsd:consolidatedGroupID"/>
			</rs:consolidatedGroupID>
			<rs:linkedBorrowersGroupID>
				<xsl:value-of select="rsd:linkedBorrowersGroupID"/>
			</rs:linkedBorrowersGroupID>
			<!--Optional:-->
			<xsl:apply-templates select="rsd:industry"/>
			<rs:isProblemClient>
				<xsl:value-of select="rsd:isProblemClient"/>
			</rs:isProblemClient>
			<xsl:apply-templates select="rsd:rating"/>
			<rs:roleInConsolidatedGroup>
				<xsl:value-of select="rsd:roleInConsolidatedGroup"/>
			</rs:roleInConsolidatedGroup>
		</rs:participants>
	</xsl:template> <!--DONE-->

	<xsl:template match="rsd:participantsGroup">
		<rs:participantsGroup>
			<xsl:apply-templates select="rsd:participants"/>
			<rs:groupType>
				<xsl:value-of select="rsd:groupType"/>
			</rs:groupType>
			<xsl:apply-templates select="rsd:groupInfo"/>
		</rs:participantsGroup>
	</xsl:template> <!--DONE-->
	<!-- - - - - - - - - - - - - - - - - TAGS - - - - - - - - - - - - - - - - - - - - - - -->
	<xsl:template match="rsd:industry">
		<!--Optional:-->
		<rs:industry>
			<xsl:value-of select="."/>
		</rs:industry>
	</xsl:template>
	<!--Fill tags with data from data.xml (0..1)-->
	<xsl:template match="rsd:errorMessage">
		<rs:errorMessage>
			<xsl:value-of select="."/>
		</rs:errorMessage>
	</xsl:template>

	<!-- - - - - - - - - - - - - - - - - MAIN XML WITH DIRECT MAPPINGS - - - - - - - - - - - - - - - - - - - - - - -->

	<xsl:template name="GetParticipants">
		<!--Get params-->
		<xsl:param name="response"/>
		<xsl:param name="data"/>
		<!-- - - - - - - - -->
		<crm:prtspRs xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:rs="http://sbrf.ru/NCP/CRM/GetParticipantsRs/">
			<rs:id>
				<xsl:value-of select="./soap:Body/crm:prtspRq/rq:id"/>
			</rs:id>
			<rs:requestType>
				<xsl:value-of select="./soap:Body/crm:prtspRq/rq:requestType"/>
			</rs:requestType>
			<xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:participantsGroup"/>
			<rs:operationStatus>
				<rs:errorCode>
					<xsl:value-of select="$data/rsd:response[@name=$response]/rsd:operationStatus/rsd:errorCode"/>
				</rs:errorCode>
				<!--Optional:-->
				<xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:operationStatus/rsd:errorMessage"/>
			</rs:operationStatus>
		</crm:prtspRs>
	</xsl:template>

</xsl:stylesheet>