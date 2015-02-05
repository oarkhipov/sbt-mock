<xsl:stylesheet xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/GetParticipant/resp/10/Data"
                xmlns:tns="http://sbrf.ru/prpc/kkmb/crm/GetParticipant/resp/10"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <!-- опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output omit-xml-declaration="yes" method="xml" indent="yes"/>

    <xsl:param name="name" select="//ns1:ParticipantInfo/*//*[local-name()='ContractID'][1]/text()"/>
    <xsl:param name="dataFileName" select="'../../data/CRM/xml/GetParticipantInfoResponseData.xml'"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
        <xsl:call-template name="PrtsRs">
            <xsl:with-param name="data" select="$data"/>
            <xsl:with-param name="response">
                <xsl:choose>
                    <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1">
                        <xsl:value-of select="$linkedTag"/>
                    </xsl:when>
                    <xsl:otherwise>default</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template match="rsd:ParticipantInfo">
        <ns1:ParticipantInfo xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/GetParticipant/resp/10">
            <xsl:element name="ns1:ContractID">
                <xsl:value-of select="./rsd:ContractID"/>
            </xsl:element>
            <ns2:Participant>
                <ns2:ListOfParticipantFL>
                    <xsl:if test="./ns2:Participant/ns2:ListOfParticipantFL/ns2:ParticipantFL">
                        <xsl:apply-templates select="./ns2:Participant/ns2:ListOfParticipantFL/ns2:ParticipantFL"/>
                    </xsl:if>
                </ns2:ListOfParticipantFL>
                <ns2:ListOfParticipantUL>
                    <xsl:apply-templates select="./ns2:Participant/ns2:ListOfParticipantUL/ns2:ParticipantUL"/>
                </ns2:ListOfParticipantUL>
            </ns2:Participant>
            <ns2:GSL>
                <ns2:ListOfPartGSLFL>
                    <xsl:if test="./ns2:GSL/ns2:ListOfPartGSLFL/ns2:PartGSLFL">
                        <xsl:apply-templates select="./ns2:GSL/ns2:ListOfPartGSLFL/ns2:PartGSLFL"/>
                    </xsl:if>
                </ns2:ListOfPartGSLFL>
                <ns2:ListOfPartGSLUL>
                    <xsl:if test="./ns2:GSL/ns2:ListOfPartGSLUL/ns2:PartGSLUL">
                        <xsl:apply-templates select="./ns2:GSL/ns2:ListOfPartGSLUL/ns2:PartGSLUL"/>
                    </xsl:if>
                </ns2:ListOfPartGSLUL>
            </ns2:GSL>
            <ns1:ListOfGSZ>
                <xsl:if test="./rsd:ListOfGSZ/ns2:GSZ">
                    <xsl:apply-templates select="./rsd:ListOfGSZ/ns2:GSZ"/>
                </xsl:if>
            </ns1:ListOfGSZ>
            <ns1:ListOfKG>
                <xsl:if test="./rsd:ListOfKG/ns2:KG">
                    <xsl:apply-templates select="./rsd:ListOfKG/ns2:KG"/>
                </xsl:if>
            </ns1:ListOfKG>
            <ns1:ListOfLegalPerson>
                <xsl:if test="./rsd:ListOfLegalPerson/ns2:LegalPerson">
                    <xsl:apply-templates select="./rsd:ListOfLegalPerson/ns2:LegalPerson"/>
                </xsl:if>
            </ns1:ListOfLegalPerson>
            <ns1:ListOfNaturalPerson>
                <xsl:if test="./rsd:ListOfNaturalPerson/ns2:NaturalPerson">
                    <xsl:apply-templates select="./rsd:ListOfNaturalPerson/ns2:NaturalPerson"/>
                </xsl:if>
            </ns1:ListOfNaturalPerson>
            <ns1:ListOfError/>
        </ns1:ParticipantInfo>
    </xsl:template>

    <xsl:template name="PrtsRs">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:RqUID">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqUID"/>
        </xsl:element>
        <xsl:element name="ns1:RqTm">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqTm"/>
        </xsl:element>
        <xsl:element name="ns1:SPName">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SPName"/>
        </xsl:element>
        <xsl:element name="ns1:SystemId">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SystemId"/>
        </xsl:element>
        <xsl:element name="ns1:OperationName">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:OperationName"/>
        </xsl:element>
        <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:ParticipantInfo"/>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorCode">
            <xsl:element name="ns1:ErrorCode">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ParticipantInfo/rsd:ErrorCode"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorMessage">
            <xsl:element name="ns1:ErrorMessage">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ParticipantInfo/rsd:ErrorMessage"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorDetails">
            <xsl:element name="ns1:ErrorDetails">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ParticipantInfo/rsd:ErrorDetails"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template match="ns2:ParticipantFL">
        <xsl:element name="ns2:ParticipantFL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusFL">
                <xsl:if test="./ns2:ListOfStatusFL/ns2:StatusFL">
                    <xsl:apply-templates select="./ns2:ListOfStatusFL/ns2:StatusFL"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleFL">
                <xsl:value-of select="./ns2:RoleFL"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:StatusUL">
        <xsl:element name="ns2:StatusUL">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ParticipantUL">
        <xsl:element name="ns2:ParticipantUL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusUL">
                <xsl:if test="./ns2:ListOfStatusUL/ns2:StatusUL">
                    <xsl:apply-templates select="./ns2:ListOfStatusUL/ns2:StatusUL"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleUL">
                <xsl:value-of select="./ns2:RoleUL"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:StatusFL">
        <xsl:element name="ns2:StatusFL">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:PartGSLFL">
        <xsl:element name="ns2:PartGSLFL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusFL">
                <xsl:if test="./ns2:ListOfStatusFL/ns2:StatusFL">
                    <xsl:apply-templates select="./ns2:ListOfStatusFL/ns2:StatusFL"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleFL">
                <xsl:value-of select="./ns2:RoleFL"/>
            </xsl:element>
            <xsl:element name="ns2:CommentOnInclusion">
                <xsl:value-of select="./ns2:CommentOnInclusion"/>
            </xsl:element>
            <xsl:element name="ns2:Stake">
                <xsl:value-of select="./ns2:Stake"/>
            </xsl:element>
            <xsl:if test="./ns2:CommentGSLFL">
                <xsl:element name="ns2:CommentGSLFL">
                    <xsl:value-of select="./ns2:CommentGSLFL"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:PartGSLUL">
        <xsl:element name="ns2:PartGSLUL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusUL">
                <xsl:if test="./ns2:ListOfStatusUL/ns2:StatusUL">
                    <xsl:element name="ns2:StatusUL">
                        <xsl:value-of select="./ns2:ListOfStatusUL/ns2:StatusUL"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleUL">
                <xsl:value-of select="./ns2:RoleUL"/>
            </xsl:element>
            <xsl:element name="ns2:CommentOnInclusion">
                <xsl:value-of select="./ns2:CommentOnInclusion"/>
            </xsl:element>
            <xsl:element name="ns2:Stake">
                <xsl:value-of select="./ns2:Stake"/>
            </xsl:element>
            <xsl:if test="./ns2:CommentGSLFL">
                <xsl:element name="ns2:CommentGSLUL">
                    <xsl:value-of select="./ns2:CommentGSLUL"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:GSZ">
        <xsl:element name="ns2:GSZ">
            <xsl:element name="ns2:GSZ_ID">
                <xsl:value-of select="./ns2:GSZ_ID"/>
            </xsl:element>
            <xsl:element name="ns2:NameGSZ">
                <xsl:value-of select="./ns2:NameGSZ"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberGSZFL">
                <xsl:if test="./ns2:ListOfMemberGSZFL/ns2:ClientID">
                    <xsl:apply-templates select="./ns2:ListOfMemberGSZFL/ns2:ClientID"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberGSZUL">
                <xsl:if test="./ns2:ListOfMemberGSZUL/ns2:ClientID">
                    <xsl:apply-templates select="./ns2:ListOfMemberGSZUL/ns2:ClientID"/>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:KG">
        <xsl:element name="ns2:KG">
            <xsl:element name="ns2:KG_ID">
                <xsl:value-of select="./ns2:KG_ID"/>
            </xsl:element>
            <xsl:element name="ns2:NameKG">
                <xsl:value-of select="./ns2:NameKG"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberKGFL">
                <xsl:if test="./ns2:ListOfMemberKGFL/ns2:ClientID">
                    <xsl:apply-templates select="./ns2:ListOfMemberKGFL/ns2:ClientID"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberKGUL">
                <xsl:if test="./ns2:ListOfMemberKGUL/ns2:ClientID">
                    <xsl:apply-templates select="./ns2:ListOfMemberKGUL/ns2:ClientID"/>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ClientID">
        <xsl:element name="ns2:ClientID">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:LegalPerson">
        <xsl:element name="ns2:LegalPerson">
            <xsl:element name="ns2:LegalPersonID">
                <xsl:value-of select="./ns2:LegalPersonID"/>
            </xsl:element>
            <xsl:element name="ns2:NaturalPersonID">
                <xsl:value-of select="./ns2:NaturalPersonID"/>
            </xsl:element>
            <xsl:if test="./ns2:SubDivision">
                <xsl:element name="ns2:SubDivision">
                    <xsl:value-of select="./ns2:SubDivision"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:StoppingList">
                <xsl:value-of select="./ns2:StoppingList"/>
            </xsl:element>
            <xsl:element name="ns2:OPF">
                <xsl:value-of select="./ns2:OPF"/>
            </xsl:element>
            <xsl:element name="ns2:OrganizationName">
                <xsl:value-of select="./ns2:OrganizationName"/>
            </xsl:element>
            <xsl:element name="ns2:INN">
                <xsl:value-of select="./ns2:INN"/>
            </xsl:element>
            <xsl:element name="ns2:KPP">
                <xsl:value-of select="./ns2:KPP"/>
            </xsl:element>
            <xsl:if test="./ns2:OGRN">
                <xsl:element name="ns2:OGRN">
                    <xsl:value-of select="./ns2:OGRN"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:Industry">
                <xsl:value-of select="./ns2:Industry"/>
            </xsl:element>
            <xsl:if test="./ns2:ProblemGroup">
                <xsl:element name="ns2:ProblemGroup">
                    <xsl:value-of select="./ns2:ProblemGroup"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:OKPO">
                <xsl:element name="ns2:OKPO">
                    <xsl:value-of select="./ns2:OKPO"/>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates select="./ns2:ListOfAddressInfoUL"/>
            <xsl:element name="ns2:ListOfRegistrationInfo">
                <xsl:if test="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo">
                    <xsl:element name="ns2:RegistrationInfo">
                        <xsl:element name="ns2:RegistrationDate">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationDate"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationAuthority">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationAuthority"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationSertificateSeries">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationSertificateSeries"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationSertificateDate">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationSertificateDate"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ListOfAddressInfoUL">
        <xsl:element name="ns2:ListOfAddressInfoUL">
            <xsl:for-each select="./ns2:AddressInfoUL">
                <xsl:element name="ns2:AddressInfoUL">
                    <xsl:element name="ns2:AddressTypeUL">
                        <xsl:value-of select="./ns2:AddressTypeUL"/>
                    </xsl:element>
                    <xsl:element name="ns2:City">
                        <xsl:value-of select="./ns2:City"/>
                    </xsl:element>
                    <xsl:element name="ns2:Street">
                        <xsl:value-of select="./ns2:Street"/>
                    </xsl:element>
                    <xsl:element name="ns2:House">
                        <xsl:value-of select="./ns2:House"/>
                    </xsl:element>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:NaturalPerson">
        <xsl:element name="ns2:NaturalPerson">
            <xsl:element name="ns2:NaturalPersonID">
                <xsl:value-of select="./ns2:NaturalPersonID"/>
            </xsl:element>
            <xsl:if test="./ns2:SubDivision">
                <xsl:element name="ns2:SubDivision">
                    <xsl:value-of select="./ns2:SubDivision"/>
                </xsl:element>
                <xsl:element name="ns2:LastName">
                    <xsl:value-of select="./ns2:LastName"/>
                </xsl:element>
                <xsl:element name="ns2:FirstName">
                    <xsl:value-of select="./ns2:FirstName"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:MiddleName">
                <xsl:element name="ns2:MiddleName">
                    <xsl:value-of select="./ns2:MiddleName"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:DateOfBirth">
                <xsl:element name="ns2:DateOfBirth">
                    <xsl:value-of select="./ns2:DateOfBirth"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:PlaceOfBirth">
                <xsl:element name="ns2:PlaceOfBirth">
                    <xsl:value-of select="./ns2:PlaceOfBirth"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Gender">
                <xsl:element name="ns2:Gender">
                    <xsl:value-of select="./ns2:Gender"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfDocument">
                <xsl:if test="./ns2:ListOfDocument/ns2:Document">
                    <xsl:element name="ns2:Document">
                        <xsl:element name="ns2:DocumentTypeCode">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentTypeCode"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentType">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentType"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentSeries">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentSeries"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentName">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentName"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueDate">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueDate"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueAuthorityCode">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueAuthorityCode"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueAuthority">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueAuthority"/>
                        </xsl:element>
                        <xsl:element name="ns2:Primary">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:Primary"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:apply-templates select="./ns2:ListOfAddressInfoFL"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ListOfAddressInfoFL">
        <xsl:element name="ns2:ListOfAddressInfoFL">
            <xsl:for-each select="./ns2:AddressInfoFL">
                <xsl:element name="ns2:AddressInfoFL">
                    <xsl:element name="ns2:AddressTypeFL">
                        <xsl:value-of select="./ns2:AddressTypeFL"/>
                    </xsl:element>
                    <xsl:if test="./ns2:City">
                        <xsl:element name="ns2:City">
                            <xsl:value-of select="./ns2:City"/>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="./ns2:Street">
                        <xsl:element name="ns2:Street">
                            <xsl:value-of select="./ns2:Street"/>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="./ns2:House">
                        <xsl:element name="ns2:House">
                            <xsl:value-of select="./ns2:House"/>
                        </xsl:element>
                    </xsl:if>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>