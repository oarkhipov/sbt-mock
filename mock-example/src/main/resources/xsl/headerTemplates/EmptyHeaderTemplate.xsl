<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/">

    <xsl:template name="SOAPNS"></xsl:template>

    <xsl:template name="namespaces">
    </xsl:template>

    <!--относительный путь к файлу с шаблоном заголовка (к этому файлу), чтобы втсавить в xsl-->
    <xsl:template name="TemplatePath">../util/SoapRqHeaderXSLTTemplate.xsl</xsl:template>

    <!--пример заголовка так, чтобы он выглядел как в настоязем сообщении-->
    <xsl:template name="HeaderExample">
    </xsl:template>

    <!--часть XSD-схемы для вставки в Data-xsd-->
    <xsl:template name="HeaderForXSD" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать список параметров, необходимых для заголовка-->
    <xsl:template name="xslHeaderDeclaration">
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать необходимый заголовок-->
    <xsl:template name="xslPatternTeplateDeclaration">
    </xsl:template>



</xsl:stylesheet>