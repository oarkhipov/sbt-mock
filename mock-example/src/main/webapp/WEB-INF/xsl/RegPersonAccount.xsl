<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:mqhd="http://sbrf.ru/prpc/mq/headers"
	xmlns:bbmo="http://sbrf.ru/prpc/bbmo/10"
	xmlns:bbmo-types="http://sbrf.ru/prpc/bbmo/commonTypes/10"
	>
	<xsl:template  match="/" >
		<soap-env:Envelope>
			<soap-env:Header>
				<mqhd:AsyncHeader>
					<mqhd:message-id><xsl:value-of select="/soap-env:Envelope/soap-env:Header/*/mqhd:message-id/text()"/></mqhd:message-id>
					<mqhd:request-time><xsl:value-of select="/soap-env:Envelope/soap-env:Header/*/mqhd:request-time/text()"/></mqhd:request-time>
					<mqhd:operation-name>SrvRegisterLegalPersonAccountOperationApplication</mqhd:operation-name>
					<mqhd:user-id><xsl:value-of select="/soap-env:Envelope/soap-env:Header/*/mqhd:user-id/text()"/></mqhd:user-id>
					<mqhd:user-name><xsl:value-of select="/soap-env:Envelope/soap-env:Header/*/mqhd:user-name/text()"/></mqhd:user-name>
				</mqhd:AsyncHeader>
			</soap-env:Header>
			<soap-env:Body>
				<bbmo:SrvRegisterLegalPersonApplicationRs >
					<bbmo-types:ResultCode>0</bbmo-types:ResultCode>
					<bbmo:URL>https://uras-webseal1.honduras.sbrf.ru/BBMO/prweb/SUDIRServlet?pyActivity=Sberbank-FW-.StartProcess&amp;InsKey=11+20131107T154046.228+GMTAOR-408594846</bbmo:URL>
				</bbmo:SrvRegisterLegalPersonApplicationRs>
			</soap-env:Body>
		</soap-env:Envelope>
	</xsl:template>
</xsl:stylesheet>