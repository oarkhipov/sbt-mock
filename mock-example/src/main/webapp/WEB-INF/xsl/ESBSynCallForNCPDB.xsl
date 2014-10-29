<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:esbhd="http://sbrf.ru/prpc/esb/headers/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:choose>
					<xsl:when test="/soap-env:Envelope/soap-env:Header/esbhd:AsyncHeader/esbhd:operation-name='SrvSendApplicationForCBDFProcessing'">
						<xsl:call-template name="SrvSendApplicationForCBDFProcessing" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Header/esbhd:AsyncHeader/esbhd:operation-name='CreateTask'">
						<xsl:call-template name="CreateTask" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Header/esbhd:AsyncHeader/esbhd:operation-name='GetParticipants'">
						<xsl:call-template name="GetParticipants" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="Error"/>
					</xsl:otherwise>
				</xsl:choose>
			</soap-env:Body>
		</xsl:element>
	</xsl:template>
	
		<xsl:template name="Error">
		<ns1:CreateTaskRs xmlns:ns1="http://sbrf.ru/NCP/CRM/">
			<ns1:ErrorCode>1</ns1:ErrorCode>
			<ns1:ErrorMessage>Ошибка в запросе. Не удалось найти ответ с указанным operation-name</ns1:ErrorMessage>
			<ns1:ContractInfo>
				<ns1:ContractID>1</ns1:ContractID>
				<ns1:ContractBPMID>1</ns1:ContractBPMID>
				<ns1:Status>1</ns1:Status>
				<ns1:Comment>1</ns1:Comment>
				<ns1:RequestType>1</ns1:RequestType>
				<ns1:FullNameOfResponsiblePerson>1</ns1:FullNameOfResponsiblePerson>
			</ns1:ContractInfo>
		</ns1:CreateTaskRs>
	</xsl:template>

	<xsl:template name="SrvSendApplicationForCBDFProcessing">
		<ns1:calculateResponse xmlns:ns1="http://service.scoring.sberbank.luxoft.com/">
			<ns1:return>
				<ns1:ErrorCode>0</ns1:ErrorCode>
				<ns1:ErrorMessage>Успех</ns1:ErrorMessage>
				<ns1:ListOfResultRating>
					<ns1:ResultRating>
						<ns1:IsPrimary>N</ns1:IsPrimary>
						<ns1:Name>PDstandalone</ns1:Name>
						<ns1:Value>17</ns1:Value>
						<ns1:Type />
					</ns1:ResultRating>
					<ns1:ResultRating>
						<ns1:IsPrimary>N</ns1:IsPrimary>
						<ns1:Name>PDstandalone_CH</ns1:Name>
						<ns1:Value>16</ns1:Value>
						<ns1:Type />
					</ns1:ResultRating>
					<ns1:ResultRating>
						<ns1:IsPrimary>Y</ns1:IsPrimary>
						<ns1:Name>Rw</ns1:Name>
						<ns1:Value>16</ns1:Value>
						<ns1:Type>Рейтинг</ns1:Type>
					</ns1:ResultRating>
				</ns1:ListOfResultRating>
			</ns1:return>
		</ns1:calculateResponse>
	</xsl:template>
	
	<xsl:template name="CreateTask">
		<ns1:CreateTaskRs xmlns:ns1="http://sbrf.ru/NCP/CRM/">
			<ns1:ErrorCode>0</ns1:ErrorCode>
			<ns1:ErrorMessage>Success</ns1:ErrorMessage>
			<ns1:ContractInfo>
				<ns1:ContractID>GDF567GF</ns1:ContractID>
				<ns1:ContractBPMID>D-123</ns1:ContractBPMID>
				<ns1:Status>New</ns1:Status>
				<ns1:Comment>Some note</ns1:Comment>
				<ns1:RequestType>Согласование сделки с клиентом</ns1:RequestType>
				<ns1:FullNameOfResponsiblePerson>Иванов Иван Семёнович</ns1:FullNameOfResponsiblePerson>
			</ns1:ContractInfo>
		</ns1:CreateTaskRs>
	</xsl:template>
	
	<xsl:template name="GetParticipants">
		<b:prtspRs xmlns:b="http://sbrf.ru/NCP/CRM/GetParticipants/" >
			<b:participantsInfo>
				<b:participant>
					<b:participantID>String</b:participantID>
					<b:type>String</b:type>
					<b:clientType>String</b:clientType>
					<b:name>String</b:name>
					<b:role>String</b:role>
					<b:role>String</b:role>
					<b:address>
						<b:type>String</b:type>
						<b:city>String</b:city>
						<b:street>String</b:street>
						<b:building>String</b:building>
						<b:block>String</b:block>
						<b:phone>String</b:phone>
						<b:email>String</b:email>
					</b:address>
					<b:address>
						<b:type>String</b:type>
						<b:city>String</b:city>
						<b:street>String</b:street>
						<b:building>String</b:building>
						<b:block>String</b:block>
						<b:phone>String</b:phone>
						<b:email>String</b:email>
					</b:address>
					<b:share>
						<b:ownershipID>String</b:ownershipID>
						<b:periodOfOwnerhip>String</b:periodOfOwnerhip>
						<b:comment>String</b:comment>
					</b:share>
					<b:share>
						<b:ownershipID>String</b:ownershipID>
						<b:periodOfOwnerhip>String</b:periodOfOwnerhip>
						<b:comment>String</b:comment>
					</b:share>
					<b:consolidatedGroupID>String</b:consolidatedGroupID>
					<b:linkedBorrowersGroupID>String</b:linkedBorrowersGroupID>
					<b:industry>String</b:industry>
					<b:subDivision>String</b:subDivision>
					<b:isInStopList>1</b:isInStopList>
					<b:primaryBusiness>String</b:primaryBusiness>
					<b:isProblemClient>1</b:isProblemClient>
					<b:liability>
						<b:contractType>String</b:contractType>
					</b:liability>
					<b:liability>
						<b:contractType>String</b:contractType>
					</b:liability>
					<b:financialAnalysis>1</b:financialAnalysis>
					<b:conclusion>String</b:conclusion>
					<b:cashForecast>1</b:cashForecast>
					<b:forecastFormat>String</b:forecastFormat>
					<b:calculationUnit>String</b:calculationUnit>
					<b:justification>String</b:justification>
					<b:finalConclusion>String</b:finalConclusion>
					<b:rating>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:rating>
					<b:rating>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:rating>
					<b:roleInConsolidatedGroup>String</b:roleInConsolidatedGroup>
					<b:financialData>
						<b:revenue>3.1415926535897932384626433832795</b:revenue>
						<b:type>String</b:type>
						<b:EBITDA>3.1415926535897932384626433832795</b:EBITDA>
						<b:debt>3.1415926535897932384626433832795</b:debt>
						<b:assets>3.1415926535897932384626433832795</b:assets>
						<b:capital>3.1415926535897932384626433832795</b:capital>
						<b:EBITDAToRevenue>3.14159265358979</b:EBITDAToRevenue>
						<b:debtToEBITDA>3.14159265358979</b:debtToEBITDA>
						<b:debtToCapital>3.14159265358979</b:debtToCapital>
					</b:financialData>
				</b:participant>
				<b:participant>
					<b:participantID>String</b:participantID>
					<b:type>String</b:type>
					<b:clientType>String</b:clientType>
					<b:name>String</b:name>
					<b:role>String</b:role>
					<b:role>String</b:role>
					<b:address>
						<b:type>String</b:type>
						<b:city>String</b:city>
						<b:street>String</b:street>
						<b:building>String</b:building>
						<b:block>String</b:block>
						<b:phone>String</b:phone>
						<b:email>String</b:email>
					</b:address>
					<b:address>
						<b:type>String</b:type>
						<b:city>String</b:city>
						<b:street>String</b:street>
						<b:building>String</b:building>
						<b:block>String</b:block>
						<b:phone>String</b:phone>
						<b:email>String</b:email>
					</b:address>
					<b:share>
						<b:ownershipID>String</b:ownershipID>
						<b:periodOfOwnerhip>String</b:periodOfOwnerhip>
						<b:comment>String</b:comment>
					</b:share>
					<b:share>
						<b:ownershipID>String</b:ownershipID>
						<b:periodOfOwnerhip>String</b:periodOfOwnerhip>
						<b:comment>String</b:comment>
					</b:share>
					<b:consolidatedGroupID>String</b:consolidatedGroupID>
					<b:linkedBorrowersGroupID>String</b:linkedBorrowersGroupID>
					<b:industry>String</b:industry>
					<b:subDivision>String</b:subDivision>
					<b:isInStopList>1</b:isInStopList>
					<b:primaryBusiness>String</b:primaryBusiness>
					<b:isProblemClient>1</b:isProblemClient>
					<b:liability>
						<b:contractType>String</b:contractType>
					</b:liability>
					<b:liability>
						<b:contractType>String</b:contractType>
					</b:liability>
					<b:financialAnalysis>1</b:financialAnalysis>
					<b:conclusion>String</b:conclusion>
					<b:cashForecast>1</b:cashForecast>
					<b:forecastFormat>String</b:forecastFormat>
					<b:calculationUnit>String</b:calculationUnit>
					<b:justification>String</b:justification>
					<b:finalConclusion>String</b:finalConclusion>
					<b:rating>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:rating>
					<b:rating>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:rating>
					<b:roleInConsolidatedGroup>String</b:roleInConsolidatedGroup>
					<b:financialData>
						<b:revenue>3.1415926535897932384626433832795</b:revenue>
						<b:type>String</b:type>
						<b:EBITDA>3.1415926535897932384626433832795</b:EBITDA>
						<b:debt>3.1415926535897932384626433832795</b:debt>
						<b:assets>3.1415926535897932384626433832795</b:assets>
						<b:capital>3.1415926535897932384626433832795</b:capital>
						<b:EBITDAToRevenue>3.14159265358979</b:EBITDAToRevenue>
						<b:debtToEBITDA>3.14159265358979</b:debtToEBITDA>
						<b:debtToCapital>3.14159265358979</b:debtToCapital>
					</b:financialData>
				</b:participant>
				<b:consolidatedGroup>
					<b:id>String</b:id>
					<b:label>String</b:label>
					<b:status>String</b:status>
					<b:updateDate>2001-12-17T09:30:47-05:00</b:updateDate>
					<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
					<b:ratings>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:ratings>
					<b:ratings>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:ratings>
					<b:topLevelGroupName>String</b:topLevelGroupName>
				</b:consolidatedGroup>
				<b:linkedBorrowersGroup>
					<b:id>String</b:id>
					<b:label>String</b:label>
					<b:status>String</b:status>
					<b:updateDate>2001-12-17T09:30:47-05:00</b:updateDate>
					<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
					<b:ratings>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:ratings>
					<b:ratings>
						<b:ratingID>String</b:ratingID>
						<b:type>String</b:type>
						<b:value>3.14159265358979</b:value>
						<b:qualitativeRating>String</b:qualitativeRating>
						<b:supportRating>String</b:supportRating>
						<b:calculated>1</b:calculated>
						<b:calculatedDate>2001-12-17T09:30:47-05:00</b:calculatedDate>
						<b:approvalDate>2001-12-17T09:30:47-05:00</b:approvalDate>
						<b:endDate>2001-12-17T09:30:47-05:00</b:endDate>
						<b:ownerID>String</b:ownerID>
					</b:ratings>
					<b:topLevelGroupName>String</b:topLevelGroupName>
				</b:linkedBorrowersGroup>
				<b:dealID>String</b:dealID>
				<b:industryInfo>
					<b:industryID>String</b:industryID>
					<b:industry>String</b:industry>
					<b:markets>
						<b:marketID>String</b:marketID>
						<b:description>String</b:description>
						<b:marketShare>3.14159265358979</b:marketShare>
					</b:markets>
					<b:markets>
						<b:marketID>String</b:marketID>
						<b:description>String</b:description>
						<b:marketShare>3.14159265358979</b:marketShare>
					</b:markets>
				</b:industryInfo>
			</b:participantsInfo>
			<b:operationStatus>
				<b:errorCode>0</b:errorCode>
				<b:errorMessage>Result message</b:errorMessage>
			</b:operationStatus>
		</b:prtspRs>
	</xsl:template>

</xsl:stylesheet>