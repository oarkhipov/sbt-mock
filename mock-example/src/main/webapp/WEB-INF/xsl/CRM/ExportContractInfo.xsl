<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:ct="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:tns="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:param name="dataFileName" select="'../../data/CRM/xml/ExportContractInfoResponseData.xml'"/>

    <xsl:template match="tns:Message">

    </xsl:template>
</xsl:stylesheet>