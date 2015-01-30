<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="NCPSoapRqHeaderXSLTTemplate.xsl"/>
    <xsl:include href="KD4SoapHeaderTemplate.xsl"/>

    <xsl:param name="defaultId" select="string('defaultId')"/>

    <!--пример заголовка так, чтобы он выглядел как в настоязем сообщении-->
    <xsl:template name="exampleHeader">
        <xsl:param name="headerType"/>
        <xsl:param name="operation-name"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPxslTeplateDeclaration">
                    <xsl:with-param name="operation-name" select="$operation-name"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="KD4SoapHeader">
                    <xsl:with-param name="operation-name" select="$operation-name"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!--часть XSD-схемы для вставки в Data-xsd-->
    <xsl:template name="xsdHeader">
        <xsl:param name="headerType"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPHeaderForXSD">
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="KD4HeaderForXSD">
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="xslTeplateDeclaration">
        <xsl:param name="headerType"/>
        <xsl:param name="operationName"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPxslTeplateDeclaration">
                    <xsl:with-param name="operationName" select="$operationName"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="NCPxslTeplateDeclaration">
                    <xsl:with-param name="operationName" select="$operationName"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>