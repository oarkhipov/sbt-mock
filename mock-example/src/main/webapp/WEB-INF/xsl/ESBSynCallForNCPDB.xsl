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
					<xsl:when test="/soap-env:Envelope/soap-env:Header/esbhd:AsyncHeader/esbhd:operation-name='SaveDeal'">
						<xsl:call-template name="SaveDeal" />
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
		<ns1:createTaskRs xmlns:ns1="http://sbrf.ru/NCP/CRM/" xmlns:ns2="http://sbrf.ru/NCP/CRM/CreateTaskRs/">
			<ns2:errorCode>0</ns2:errorCode>
			<ns2:errorMessage>Success</ns2:errorMessage>
			<ns2:contractInfo>
				<ns2:ContractID>GDF567GF</ns2:ContractID>
				<ns2:ContractBPMID>D-123</ns2:ContractBPMID>
				<ns2:Status>New</ns2:Status>
				<ns2:Comment>Some note</ns2:Comment>
				<ns2:RequestType>Согласование сделки с клиентом</ns2:RequestType>
				<ns2:FullNameOfResponsiblePerson>Иванов Иван Семёнович</ns2:FullNameOfResponsiblePerson>
			</ns2:contractInfo>
		</ns1:createTaskRs>
	</xsl:template>
	
 	<xsl:template name="SaveDeal">
        <crm:saveDealRs xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:ur="http://sbrf.ru/NCP/CRM/SaveDealRs/"/>
    </xsl:template>
	
</xsl:stylesheet>