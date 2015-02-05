<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mock="http://sbrf.ru/mockService">

    <!--Файл нужен чтобы не плодить логику в рабочих xsl.
    У разных систем бывают разные заголовки. В данный момент реализованна два разных типа заголовков.
    Если вставить их разное наименование в xslt-код, то он будет слишком перегружен.
    Поэтому весь код по созданию заголовков перенсен в соответсвующие теплейты.
    В xsl с кодом мы вызываем нужный теплейт с параметром headerType, и данный файл сам определяет какой шаблон взять

    Если будет необходимость добавить новый заголовок, то нужно:
     1) создать xsl файл и добавить его сюда в include'ы
     2) придумать ему буквенный код (как например 'KD4' или 'NCP') по которому этот заголовок будет выбираться.
     3) прописать выбор этого заголовка в функции getHeaderTypeByHeaderNamespace из класса getHeaderTypeByHeaderNamespace в зависисмости от какого-нибудь параметра из конфига. Лучше всего использовать параметр headerNamespace
     4) реализовать все темплейы, вызов которых идет в этом файле по аналогии-->

    <xsl:include href="NCPSoapRqHeaderXSLTTemplate.xsl"/>
    <xsl:include href="KD4SoapHeaderTemplate.xsl"/>
    <xsl:include href="KD4SoapHeaderTemplate.xsl"/>

    <!--ID сообщения, которое будет использоваться по умолчанию, если не задано параметром или в дата-файле-->
    <xsl:param name="defaultId" select="string('defaultId')"/>


    <!--Возвращает всего одну строку - неймспейс заголовка-->
    <xsl:function name="mock:SOAPNS">
        <xsl:param name="headerType"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPSOAPNS">
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="KD4SOAPNS">
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:function>

    <!--пример заголовка так, чтобы он выглядел как в настоящем сообщении-->
    <xsl:template name="exampleHeader">
        <xsl:param name="headerType"/>
        <xsl:param name="operation-name"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPHeader">
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

    <!--часть xslt для создания xsl - для объявления необходимых переменных. Все, что здесь записанно будет в начасле файла-->
    <xsl:template name="xslTeplateHeaderDeclaration">
        <xsl:param name="headerType"/>
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPxslTeplateHeaderDeclaration">
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="KD4xslTeplateHeaderDeclaration">
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!--часть xslt для создания xsl - для объявления создания непосредственно тела сообщения-->
    <xsl:template name="xslTeplateDeclaration">
        <xsl:param name="headerType"/>
        <xsl:param name="operationName"/> <!-- имя темплейта, который будет вызван через call-template в теле сообщения -->
        <xsl:param name="type"/> <!-- response или request -->
        <xsl:choose>
            <xsl:when test="$headerType='NCP'">
                <xsl:call-template name="NCPxslTeplateDeclaration">
                    <xsl:with-param name="operationName" select="$operationName"/>
                    <xsl:with-param name="type" select="$type"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$headerType='KD4'">
                <xsl:call-template name="KD4xslTeplateDeclaration">
                    <xsl:with-param name="operationName" select="$operationName"/>
                    <xsl:with-param name="type" select="$type"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>