<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


    <xsl:template name="KKMBSOAPNS">http://sbrf.ru/prpc/kkmb/crm/Header/req/10/</xsl:template>

    <xsl:template name="KKMBnamespaces">
        <xsl:namespace name="ct" select="'http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10'"/>
    </xsl:template>

    <!--относительный путь к файлу с шаблоном заголовка (к этому файлу), чтобы втсавить в xsl-->
    <xsl:template name="KKMBTemplatePath">../util/KKMBHeaderTemplate.xsl</xsl:template>

    <xsl:template name="KKMBSoapHeader"
                  xmlns:msg="http://sbrf.ru/prpc/kkmb/crm/Header/req/10">
        <xsl:param name="RqUID" select="'String36_123456789012345678901234567'"/>
        <xsl:param name="RqTm" select="'2014-12-16T17:55:06.410'"/>
        <xsl:param name="SPName" select="''"/>
        <xsl:param name="SystemId" select="''"/>
        <xsl:param name="OperationName" select="'operation-name'"/>
        <xsl:element name="msg:RqUID"><xsl:value-of select="$RqUID"/></xsl:element>
        <xsl:element name="msg:RqTm"><xsl:value-of select="$RqTm"/></xsl:element>
        <xsl:element name="msg:SPName"><xsl:value-of select="$SPName"/></xsl:element>
        <xsl:element name="msg:SystemId"><xsl:value-of select="$SystemId"/></xsl:element>
        <xsl:element name="msg:OperationName"><xsl:value-of select="$OperationName"/></xsl:element>
    </xsl:template>

    <xsl:template name="KKMBHeaderForXSD" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <xsl:element name="xsd:complexType">
            <xsl:namespace name="ct" select="'http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10'"/>
            <xsl:attribute name="name">Header</xsl:attribute>
            <xsl:element name="xsd:annotation">
                <xsl:element name="xsd:documentation">Заголовок сообщения</xsl:element>
            </xsl:element>
            <xsd:sequence>
                <xsd:element name="RqUID" type="ct:String36"  minOccurs="0"/>
                <xsd:element name="RqTm" type="dateTime"  minOccurs="0"/>
                <xsd:element name="SPName" type="ct:String50" minOccurs="0"/>
                <xsd:element name="OperationName" type="ct:String50" minOccurs="0"/>
                <xsd:element name="SystemId" type="ct:String255" minOccurs="0" maxOccurs="unbounded"/>
            </xsd:sequence>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать список параметров, необходимых для заголовка-->
    <xsl:template name="KKMBxslTeplateHeaderDeclaration">
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">RqUID</xsl:attribute>
            <xsl:attribute name="select">String36_123456789012345678901234567</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">RqTm</xsl:attribute>
            <xsl:attribute name="select">string('2014-12-16T17:55:06.410')</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">SPName</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">system-id</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать необходимый заголовок-->
    <xsl:template name="KKMBxslTeplateDeclaration"
                  xmlns:msg="http://sbrf.ru/prpc/kkmb/crm/Header/req/10">
        <xsl:param name="type"/>
        <xsl:param name="operationName"/>
        <xsl:element name="xsl:element">
            <xsl:namespace name="msg" select="'http://sbrf.ru/prpc/kkmb/crm/Header/req/10'"/>
            <xsl:attribute name="name">msg:Message</xsl:attribute>
            <xsl:element name="xsl:call-template">
                <xsl:attribute name="name">KKMBHeader</xsl:attribute>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">response</xsl:attribute>
                    <xsl:element name="xsl:choose">
                        <xsl:element name="xsl:when">
                            <xsl:attribute name="test">count($data/rsd:<xsl:value-of select="$type"/>[@name=$linkedTag])=1</xsl:attribute>
                            <xsl:element name="xsl:value-of">
                                <xsl:attribute name="select">$linkedTag</xsl:attribute>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="xsl:otherwise">default</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">RqUID</xsl:attribute>
                    <xsl:attribute name="select">$RqUID</xsl:attribute>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">RqTm</xsl:attribute>
                    <xsl:attribute name="select">$RqTm</xsl:attribute>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">SPName</xsl:attribute>
                    <xsl:attribute name="select">$SPName</xsl:attribute>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">OperationName</xsl:attribute>
                    <xsl:attribute name="select">string('<xsl:value-of select="$operationName"/>')</xsl:attribute>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">system-id</xsl:attribute>
                    <xsl:attribute name="select">$system-id</xsl:attribute>
                </xsl:element>
            </xsl:element>
            <xsl:element name="xsl:call-template">
                <xsl:attribute name="name"><xsl:value-of select="$operationName"/></xsl:attribute>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name">data</xsl:attribute>
                    <xsl:attribute name="select">$data</xsl:attribute>
                </xsl:element>
                <xsl:element name="xsl:with-param">
                    <xsl:attribute name="name"><xsl:value-of select="$type"/></xsl:attribute>
                    <xsl:element name="xsl:choose">
                        <xsl:element name="xsl:when">
                            <xsl:attribute name="test">count($data/rsd:<xsl:value-of select="$type"/>[@name=$linkedTag])=1</xsl:attribute>
                            <xsl:element name="xsl:value-of">
                                <xsl:attribute name="select">$linkedTag</xsl:attribute>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="xsl:otherwise">default</xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>