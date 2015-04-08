<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService" >


    <!--Общие вещи для создания xslt для моков и драйверов. Отдельно запускать этот файл нет смысла-->

    <xsl:function name="mock:typesToImport">
        <xsl:param name="baseElement"/>
        <xsl:variable name="importOnThislevel" select="mock:typesNeedingImport($baseElement)"/>
        <xsl:variable name="nextLevelElements" select="$baseElement | $typesList[@name=$importOnThislevel]"/>
        <xsl:choose>
            <xsl:when test="count($baseElement) &lt; count($nextLevelElements)">
                <xsl:variable name="allInside" select="$nextLevelElements | $typesList[@name=mock:typesToImport($nextLevelElements)]"/>
                <xsl:for-each select="$allInside/@name"><xsl:value-of select="."/></xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$nextLevelElements/@name"><xsl:value-of select="."/></xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="mock:typesNeedingImport">
        <xsl:param name="baseElement"/>
        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias($baseElement//xsd:extension/@base)"/>
        <xsl:variable name="nsAlias" select="mock:getNamespaceAlias($baseElement//xsd:extension/@base)"/>
        <xsl:variable name="ns" select="$baseElement/namespace::*[local-name()=$nsAlias]"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$ns]"/>
        <!--<xsl:variable name="importOnThislevel" select="($baseElement | $extensionElement)//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>-->
        <xsl:variable name="importOnThislevel" select="($baseElement | $extensionElement)//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.)[not(contains(.,':'))]"/>
        <xsl:for-each select="$importOnThislevel"><xsl:value-of select="."/></xsl:for-each>
    </xsl:function>



    <xsl:template match="*[local-name()=$xsdTagsToImport]" mode="base">
        <xsl:variable name="mainElementNSAlias" select="if ($targetNS=$parrentNS) then 'tns' else $systemName"/>
        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias(./(xsd:complexType | xsd:complexContent | xsd:complexType/xsd:complexContent)/xsd:extension/@base)"/>
        <xsl:variable name="usedNsAlias" select="mock:getNamespaceAlias(.//xsd:extension/@base)"/>
        <xsl:variable name="usedNs" select="./namespace::*[local-name()=$usedNsAlias]"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$usedNs]"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>local-name=$xsdTagsToImport base <xsl:value-of select="./local-name()"/> - <xsl:value-of select="./local-name()"/>  <xsl:value-of select="concat($targetNS,'-',$parrentNS)"/></xsl:comment>
        </xsl:if>
        <xsl:choose>
            <!--случай, когда базовый эелемент - это другой тип. Значит в нем больше ничего нет, и мы заново вызываем этот же темплейт на complexType с этим типом-->
            <xsl:when test=".[local-name()='element']/@type">
                <xsl:variable name="type" select="mock:removeNamespaceAlias(./@type)"/>
                <xsl:variable name="baseContainer" select="$typesList[local-name()='complexType'][@name=$type]"/>
                <xsl:if test="$DEBUG">
                    <xsl:comment>type:<xsl:value-of select="$type"/></xsl:comment>
                    <xsl:comment>typelist:<xsl:value-of select="$typesList[local-name()='complexType']/@name"/></xsl:comment>
                </xsl:if>
                <xsl:apply-templates select="$baseContainer" mode="base"/>
            </xsl:when>
            <!--иначе в этом желементе содержится описание типа - расширим его-->
            <xsl:otherwise>
                <xsl:element name="xsl:template">
                    <xsl:attribute name="name"><xsl:value-of select="$operationName"/></xsl:attribute>
                    <xsl:element name="xsl:param">
                        <xsl:attribute name="name"><xsl:value-of select="$type"/></xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:param">
                        <xsl:attribute name="name">data</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:element">
                        <xsl:attribute name="name"><xsl:value-of select="$mainElementNSAlias"/>:<xsl:value-of select="$rootElementName"/></xsl:attribute>
                        <xsl:apply-templates select="$extensionElement/(xsd:sequence
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element)))" mode="Inside">
                            <xsl:with-param name="dataPath" select="concat('$data/rsd:',$type,'[@name=$',$type,']/rsd:')"/>
                            <xsl:with-param name="ns" select="$usedNs"/>
                            <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
                        </xsl:apply-templates>
                        <xsl:apply-templates select="./xsd:sequence/xsd:element
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                            <xsl:with-param name="dataPath" select="concat('$data/rsd:',$type,'[@name=$',$type,']/rsd:')"/>
                        </xsl:apply-templates>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="xsd:complexType" mode="template" priority="2">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:param name="self" select="self::*"/>

        <xsl:variable name="targetElement" select="($typesList/*//xsd:element[mock:removeNamespaceAlias(./@type)=$typeName])[1]"/>
        <xsl:variable name="selfNs" select="$targetElement/ancestor::xsd:schema/@targetNamespace"/>
        <xsl:variable name="selfNsAliasFromFile" select="$targetElement/ancestor::xsd:schema/namespace::*[local-name()=$selfNs]"/>
        <xsl:variable name="selfNsAlais" select="if ($selfNs = $targetNS) then 'tns' else
                                        if (string-length($selfNsAliasFromFile)>0) then $selfNsAliasFromFile else 'ns1'"/>

        <xsl:variable name="ns" select="../@targetNamespace"/>
        <xsl:variable name="nsAliasFromFile" select="$operationXsdSchema/namespace::*[.=$ns][string-length()>0][1]/local-name()"/>
        <xsl:variable name="nsAlias" select="if ($ns = $targetNS) then 'tns' else
                                        if (string-length($nsAliasFromFile)>0) then $nsAliasFromFile else 'ns1'"/>

        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias(./(xsd:complexType | xsd:complexContent | xsd:complexType/xsd:complexContent)/xsd:extension/@base)"/>
        <xsl:variable name="usedExtNsAlias" select="mock:getNamespaceAlias(.//xsd:extension/@base)"/>
        <xsl:variable name="usedExtNs" select="./namespace::*[local-name()=$usedExtNsAlias]"/>
        <xsl:variable name="extNsAlias" select="if ($ns=$usedExtNs) then $nsAlias else $usedExtNsAlias"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$usedExtNs]"/>

        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:complexType - template <xsl:value-of select="$selfNs"/>:<xsl:value-of select="$typeName"/></xsl:comment>
        </xsl:if>

        <!--<xsl:for-each select="$typesList//xsd:element[mock:removeNamespaceAlias(@type, $localTargetNSAlias) = $typeName]/@name">-->
        <xsl:for-each select="$typesList//xsd:element[mock:removeNamespaceAlias(@type) = $typeName]/@name">
            <xsl:element name="xsl:template">
                <xsl:attribute name="match">rsd:<xsl:value-of select="."/></xsl:attribute>
                <xsl:element name="{$selfNsAlais}:{.}"  namespace="{$selfNs}">
                    <xsl:if test="$ns!=''">
                        <xsl:namespace name="{$nsAlias}" select="$ns"/>
                    </xsl:if>
                    <xsl:apply-templates select="$extensionElement/(xsd:sequence
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element)))" mode="Inside">
                        <xsl:with-param name="dataPath" select="'./rsd:'"/>
                        <xsl:with-param name="ns" select="$usedExtNs"/>
                        <xsl:with-param name="nsAlias" select="$extNsAlias"/>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="$self/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                        <xsl:with-param name="dataPath" select="'./rsd:'"/>
                        <xsl:with-param name="ns" select="$ns"/>
                        <xsl:with-param name="nsAlias" select="$nsAlias"/>
                    </xsl:apply-templates>
                </xsl:element>
            </xsl:element>
            <xsl:text>&#xA;&#xA;</xsl:text>
        </xsl:for-each>

    </xsl:template>

    <xsl:template match="xsd:element" mode="template" priority="1">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:variable name="ns" select="../@targetNamespace"/>
        <xsl:variable name="nsAliasFromFile" select="$operationXsdSchema/namespace::*[.=$ns][string-length()>0][1]/local-name()"/>
        <xsl:variable name="nsAlias" select="if ($ns = $targetNS) then 'tns' else
                                        if (string-length($nsAliasFromFile)>0) then $nsAliasFromFile else 'ns1'"/>

        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias(./(xsd:complexType | xsd:complexContent | xsd:complexType/xsd:complexContent)/xsd:extension/@base)"/>
        <xsl:variable name="usedExtNsAlias" select="mock:getNamespaceAlias(.//xsd:extension/@base)"/>
        <xsl:variable name="usedExtNs" select="./namespace::*[local-name()=$usedExtNsAlias]"/>
        <xsl:variable name="extNsAlias" select="if ($ns=$usedExtNs) then $nsAlias else $usedExtNsAlias"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$usedExtNs]"/>

        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element - template</xsl:comment>
        </xsl:if>

        <xsl:element name="xsl:template">
            <xsl:attribute name="match">rsd:<xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:element name="{$nsAlias}:{$typeName}"  namespace="{$ns}">
                <xsl:if test="$ns!=''">
                    <xsl:namespace name="{$nsAlias}" select="$ns"/>
                </xsl:if>
                <xsl:apply-templates select="$extensionElement/(xsd:sequence
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element)))" mode="Inside">
                    <xsl:with-param name="dataPath" select="'./rsd:'"/>
                    <xsl:with-param name="ns" select="$usedExtNs"/>
                    <xsl:with-param name="nsAlias" select="$extNsAlias"/>
                </xsl:apply-templates>
                <xsl:apply-templates select="(./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element)))
                                                | (./xsd:sequence/xsd:element)" mode="Inside">
                    <xsl:with-param name="dataPath" select="'./rsd:'"/>
                    <xsl:with-param name="ns" select="$ns"/>
                    <xsl:with-param name="nsAlias" select="$nsAlias"/>
                </xsl:apply-templates>
            </xsl:element>
        </xsl:element>
        <xsl:text>&#xA;&#xA;</xsl:text>

    </xsl:template>

    <xsl:template match="*" mode="template" priority="0">
        <xsl:comment>template * - <xsl:value-of select="@name"/></xsl:comment>
    </xsl:template>

    <xsl:template match="xsd:element[@name]" mode="Inside" priority="1">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="'tns'"/>

        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element - Inside</xsl:comment>
        </xsl:if>

        <xsl:choose>
            <!--TODO рассмоьтреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="{$nsAlias}:{@name}" namespace="{$ns}">
                        <xsl:if test="$ns!=''">
                            <xsl:namespace name="{$nsAlias}" select="$ns"/>
                        </xsl:if>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="{$nsAlias}:{@name}" namespace="{$ns}">
                    <xsl:if test="$ns!=''">
                        <xsl:namespace name="{$nsAlias}" select="$ns"/>
                    </xsl:if>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[./xsd:complexType/xsd:sequence]" mode="Inside" priority="2">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="'tns'"/>
        <xsl:variable name="elementName" select="@name"/>

        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element[sequence] - Inside</xsl:comment>
        </xsl:if>

        <xsl:choose>
            <!--TODO рассмотреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test"><xsl:value-of select="$dataPath"/><xsl:value-of select="$elementName"/></xsl:attribute>
                    <xsl:element name="{$nsAlias}:{$elementName}" namespace="{$ns}">
                        <xsl:if test="$ns!=''">
                            <xsl:namespace name="{$nsAlias}" select="$ns"/>
                        </xsl:if>
                        <xsl:apply-templates select="./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                            <xsl:with-param name="dataPath" select="replace($dataPath, '^(.+?)/rsd:$', concat('$1/rsd:',$elementName,'/rsd:'))"/><!-- добавляем в конец пути этот элемент и ищем внутри -->
                            <xsl:with-param name="ns" select="$ns"/>
                            <xsl:with-param name="nsAlias" select="$nsAlias"/>
                        </xsl:apply-templates>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="{$nsAlias}:{$elementName}" namespace="{$ns}">
                    <xsl:if test="$ns!=''">
                        <xsl:namespace name="{$nsAlias}" select="$ns"/>
                    </xsl:if>
                    <xsl:apply-templates select="./xsd:complexType/xsd:sequence/xsd:element" mode="Inside">
                        <xsl:with-param name="dataPath" select="replace($dataPath, '^(.+?)/rsd:$', concat('$1/rsd:',$elementName,'/rsd:'))"/><!-- добавляем в конец пути этот элемент и ищем внутри -->
                        <xsl:with-param name="ns" select="$ns"/>
                        <xsl:with-param name="nsAlias" select="$nsAlias"/>
                    </xsl:apply-templates>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[@name and mock:removeNamespaceAlias(./@type)=$typesList/@name]" mode="Inside"  priority="3">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@type, $localTargetNSAlias)"/>
        <xsl:if test="$DEBUG">
            <xsl:comment> xsd:element[$typesList] - Inside <xsl:value-of select="$typeName"/> </xsl:comment>
        </xsl:if>
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="Inside"  priority="4">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="refName" select="mock:removeNamespaceAlias(./@ref)"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element[@ref]- inside <xsl:value-of select="$refName"/> </xsl:comment>
        </xsl:if>
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="$refName"/></xsl:attribute>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>