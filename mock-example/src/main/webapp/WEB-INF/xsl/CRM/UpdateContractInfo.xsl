<xsl:stylesheet xmlns:ct="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10/Data"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:param name="dataFileName" select="'../../data/CRM/xml/ExportContractInfoData.xml'"/>
    <!-- Данный параметр пока имеет значение null, так как не понятно по какому тегу нужно выбирать ответ на сообщение -->
    <xsl:param name="name" select="null"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="UpdateContractInfoTemplate">
                <xsl:with-param name="data" select="$data"/>
                <xsl:with-param name="request">
                    <xsl:choose>
                        <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1">
                            <xsl:value-of select="$linkedTag"/>
                        </xsl:when>
                        <xsl:otherwise>default</xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="UpdateContractInfoTemplate">
        <xsl:param name="data"/>
        <xsl:param name="request"/>
        <xsl:element name="ns1:RqUID">
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RqUID"/>
        </xsl:element>
        <xsl:element name="ns1:RqTm">
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RqTm"/>
        </xsl:element>
        <xsl:element name="ns1:SPName">
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:SPName"/>
        </xsl:element>
        <xsl:element name="ns1:SystemId">
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:SystemId"/>
        </xsl:element>
        <xsl:element name="ns1:OperationName">
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:OperationName"/>
        </xsl:element>
        <xsl:element name="ns2:ContractInfo">
            <xsl:element name="ns2:ContractID">
                <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ContractInfo/rsd:ContractID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfError"/>
        </xsl:element>
        <xsl:if test="$data/rsd:request[@name=$request]/rsd:ErrorCode">
            <xsl:element name="ns1:ErrorCode">
                <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ErrorCode"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:request[@name=$request]/rsd:ErrorMessage">
            <xsl:element name="ns1:ErrorMessage">
                <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ErrorMessage"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:request[@name=$request]/rsd:ErrorDetails">
            <xsl:element name="ns1:ErrorDetails">
                <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ErrorDetails"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>