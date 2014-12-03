<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				>

	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:call-template name="CreateTask" />
			</soap-env:Body>
		</xsl:element>
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

</xsl:stylesheet>