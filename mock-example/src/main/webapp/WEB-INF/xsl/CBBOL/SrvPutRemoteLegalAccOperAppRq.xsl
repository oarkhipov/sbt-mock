<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/Data/"
                xmlns:CBBOL="http://sbrf.ru/prpc/bbmo/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="all"/>
   <xsl:param name="request-time" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="kd4header" select="''"/>
   <xsl:param name="message-id" select="''"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="''"/>
   <xsl:param name="eis-name" select="''"/>
   <xsl:param name="system-id" select="''"/>
   <xsl:param name="operation-version" select="''"/>
   <xsl:param name="user-id" select="''"/>
   <xsl:param name="user-name" select="''"/>
   <xsl:param name="proc-inst-tb" select="''"/>

   <xsl:template match="/">
      <xsl:variable name="data" select="//rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" name="soap:Envelope">
         <xsl:call-template name="KD4Header">
            <xsl:with-param name="response">
               <xsl:choose>
                  <xsl:when test="count(./rsd:request[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="request-time" select="$request-time"/>
            <xsl:with-param name="message-id" select="$message-id"/>
            <xsl:with-param name="operation-name" select="string('SrvPutRemoteLegalAccOperAppRq')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
            <xsl:with-param name="kd4header" select="$kd4header"/>
            <xsl:with-param name="proc-inst-tb" select="$proc-inst-tb"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="SrvPutRemoteLegalAccOperAppRq">
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
         </soap:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template match="rsd:SrvPutRemoteLegalAccOperAppRq">
      <tns:SrvPutRemoteLegalAccOperAppRq/>
   </xsl:template>

   <xsl:template match="rsd:LegalPersonApplication">
      <tns:LegalPersonApplication><!--match="xsd:element[@name]"--><xsl:if test="./rsd:ApplicationId">
            <tns:ApplicationId>
               <xsl:value-of select="./rsd:ApplicationId"/>
            </tns:ApplicationId>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OperationType">
            <tns:OperationType>
               <xsl:value-of select="./rsd:OperationType"/>
            </tns:OperationType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientId">
            <tns:ClientId>
               <xsl:value-of select="./rsd:ClientId"/>
            </tns:ClientId>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:TaxId>
            <xsl:value-of select="./rsd:TaxId"/>
         </tns:TaxId>
         <!--match="xsd:element[@name]"--><tns:CustomerFullName>
            <xsl:value-of select="./rsd:CustomerFullName"/>
         </tns:CustomerFullName>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:BankSubdivision"/>
         <!--match="xsd:element[@name]"--><tns:InitDate>
            <xsl:value-of select="./rsd:InitDate"/>
         </tns:InitDate>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementIdentifier">
            <tns:AgreementIdentifier>
               <xsl:value-of select="./rsd:AgreementIdentifier"/>
            </tns:AgreementIdentifier>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementNumber">
            <tns:AgreementNumber>
               <xsl:value-of select="./rsd:AgreementNumber"/>
            </tns:AgreementNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:MainAccountNumber">
            <tns:MainAccountNumber>
               <xsl:value-of select="./rsd:MainAccountNumber"/>
            </tns:MainAccountNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxRegReasonCode">
            <tns:TaxRegReasonCode>
               <xsl:value-of select="./rsd:TaxRegReasonCode"/>
            </tns:TaxRegReasonCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BarCode">
            <tns:BarCode>
               <xsl:value-of select="./rsd:BarCode"/>
            </tns:BarCode>
         </xsl:if>
      </tns:LegalPersonApplication>
   </xsl:template>

   <xsl:template match="rsd:naturalPerson">
      <tns:naturalPerson><!--match="xsd:element[@name]"--><xsl:if test="./rsd:RKOCode">
            <tns:RKOCode>
               <xsl:value-of select="./rsd:RKOCode"/>
            </tns:RKOCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:SeqNumber">
            <tns:SeqNumber>
               <xsl:value-of select="./rsd:SeqNumber"/>
            </tns:SeqNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:RelationToClient>
            <xsl:value-of select="./rsd:RelationToClient"/>
         </tns:RelationToClient>
         <!--match="xsd:element[@name]"--><tns:LastName>
            <xsl:value-of select="./rsd:LastName"/>
         </tns:LastName>
         <!--match="xsd:element[@name]"--><tns:FirstName>
            <xsl:value-of select="./rsd:FirstName"/>
         </tns:FirstName>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:MiddleName">
            <tns:MiddleName>
               <xsl:value-of select="./rsd:MiddleName"/>
            </tns:MiddleName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:LastNameInGenitiveCase">
            <tns:LastNameInGenitiveCase>
               <xsl:value-of select="./rsd:LastNameInGenitiveCase"/>
            </tns:LastNameInGenitiveCase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FirstNameInGenitiveCase">
            <tns:FirstNameInGenitiveCase>
               <xsl:value-of select="./rsd:FirstNameInGenitiveCase"/>
            </tns:FirstNameInGenitiveCase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:MiddleNameInGenitiveCase">
            <tns:MiddleNameInGenitiveCase>
               <xsl:value-of select="./rsd:MiddleNameInGenitiveCase"/>
            </tns:MiddleNameInGenitiveCase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:LastNameInEnglish">
            <tns:LastNameInEnglish>
               <xsl:value-of select="./rsd:LastNameInEnglish"/>
            </tns:LastNameInEnglish>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FirstNameInEnglish">
            <tns:FirstNameInEnglish>
               <xsl:value-of select="./rsd:FirstNameInEnglish"/>
            </tns:FirstNameInEnglish>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:MiddleNameInEnglish">
            <tns:MiddleNameInEnglish>
               <xsl:value-of select="./rsd:MiddleNameInEnglish"/>
            </tns:MiddleNameInEnglish>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Sex">
            <tns:Sex>
               <xsl:value-of select="./rsd:Sex"/>
            </tns:Sex>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BirthDate">
            <tns:BirthDate>
               <xsl:value-of select="./rsd:BirthDate"/>
            </tns:BirthDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BirthPlace">
            <tns:BirthPlace>
               <xsl:value-of select="./rsd:BirthPlace"/>
            </tns:BirthPlace>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Nationality">
            <tns:Nationality>
               <xsl:value-of select="./rsd:Nationality"/>
            </tns:Nationality>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Resident">
            <tns:Resident>
               <xsl:value-of select="./rsd:Resident"/>
            </tns:Resident>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxResidentChangeDate">
            <tns:TaxResidentChangeDate>
               <xsl:value-of select="./rsd:TaxResidentChangeDate"/>
            </tns:TaxResidentChangeDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxId">
            <tns:TaxId>
               <xsl:value-of select="./rsd:TaxId"/>
            </tns:TaxId>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:SocialStatusType">
            <tns:SocialStatusType>
               <xsl:value-of select="./rsd:SocialStatusType"/>
            </tns:SocialStatusType>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:IdentityDocument"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientAddress"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientContact"/>
         <!--match="xsd:element[./xsd:complexType/xsd:sequence]"--><xsl:if test="./rsd:AuthorityBase">
            <tns:AuthorityBase><!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityBase/rsd:DocumentActAuthority">
                  <tns:DocumentActAuthority>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentActAuthority"/>
                  </tns:DocumentActAuthority>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityBase/rsd:DocumentSeriesNumber">
                  <tns:DocumentSeriesNumber>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentSeriesNumber"/>
                  </tns:DocumentSeriesNumber>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityBase/rsd:DocumentDate">
                  <tns:DocumentDate>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentDate"/>
                  </tns:DocumentDate>
               </xsl:if>
            </tns:AuthorityBase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DocumentActAuthority">
            <tns:DocumentActAuthority>
               <xsl:value-of select="./rsd:DocumentActAuthority"/>
            </tns:DocumentActAuthority>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DocumentSeriesNumber">
            <tns:DocumentSeriesNumber>
               <xsl:value-of select="./rsd:DocumentSeriesNumber"/>
            </tns:DocumentSeriesNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DocumentDate">
            <tns:DocumentDate>
               <xsl:value-of select="./rsd:DocumentDate"/>
            </tns:DocumentDate>
         </xsl:if>
         <!--match="xsd:element[./xsd:complexType/xsd:sequence]"--><xsl:if test="./rsd:OfficailInfo">
            <tns:OfficailInfo><!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:BasedOnDocument">
                  <tns:BasedOnDocument>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:BasedOnDocument"/>
                  </tns:BasedOnDocument>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:BasedOnDocumentSeries">
                  <tns:BasedOnDocumentSeries>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:BasedOnDocumentSeries"/>
                  </tns:BasedOnDocumentSeries>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:BasedOnDocumentNumber">
                  <tns:BasedOnDocumentNumber>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:BasedOnDocumentNumber"/>
                  </tns:BasedOnDocumentNumber>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:TrusteeSign">
                  <tns:TrusteeSign>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:TrusteeSign"/>
                  </tns:TrusteeSign>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:OperationType">
                  <tns:OperationType>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:OperationType"/>
                  </tns:OperationType>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:AccountNumber">
                  <tns:AccountNumber>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:AccountNumber"/>
                  </tns:AccountNumber>
               </xsl:if>
               <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OfficailInfo/rsd:Comment">
                  <tns:Comment>
                     <xsl:value-of select="./rsd:OfficailInfo/rsd:Comment"/>
                  </tns:Comment>
               </xsl:if>
            </tns:OfficailInfo>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BasedOnDocument">
            <tns:BasedOnDocument>
               <xsl:value-of select="./rsd:BasedOnDocument"/>
            </tns:BasedOnDocument>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BasedOnDocumentSeries">
            <tns:BasedOnDocumentSeries>
               <xsl:value-of select="./rsd:BasedOnDocumentSeries"/>
            </tns:BasedOnDocumentSeries>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BasedOnDocumentNumber">
            <tns:BasedOnDocumentNumber>
               <xsl:value-of select="./rsd:BasedOnDocumentNumber"/>
            </tns:BasedOnDocumentNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TrusteeSign">
            <tns:TrusteeSign>
               <xsl:value-of select="./rsd:TrusteeSign"/>
            </tns:TrusteeSign>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OperationType">
            <tns:OperationType>
               <xsl:value-of select="./rsd:OperationType"/>
            </tns:OperationType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AccountNumber">
            <tns:AccountNumber>
               <xsl:value-of select="./rsd:AccountNumber"/>
            </tns:AccountNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Comment">
            <tns:Comment>
               <xsl:value-of select="./rsd:Comment"/>
            </tns:Comment>
         </xsl:if>
      </tns:naturalPerson>
   </xsl:template>

   <xsl:template match="rsd:ClientReferenceData">
      <tns:ClientReferenceData><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientInformation"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:naturalPerson"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:LegalPersonBankAccountAgreement"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:SignPatternCard"/>
      </tns:ClientReferenceData>
   </xsl:template>

   <xsl:template match="rsd:ClientInformation">
      <tns:ClientInformation><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:Identifiers"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:SpecialData"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:MainDetails"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:FounderLegalPerson"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:BankSubdivision"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientAddress"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientContact"/>
      </tns:ClientInformation>
   </xsl:template>

   <xsl:template match="rsd:MainDetails">
      <tns:MainDetails><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:Registration"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:TaxInspectorateInfo"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:RosstatCodes"/>
      </tns:MainDetails>
   </xsl:template>

   <xsl:template match="rsd:RosstatCodes">
      <tns:RosstatCodes><!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKATO">
            <tns:OKATO>
               <xsl:value-of select="./rsd:OKATO"/>
            </tns:OKATO>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKPO">
            <tns:OKPO>
               <xsl:value-of select="./rsd:OKPO"/>
            </tns:OKPO>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKFS">
            <tns:OKFS>
               <xsl:value-of select="./rsd:OKFS"/>
            </tns:OKFS>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKOGU">
            <tns:OKOGU>
               <xsl:value-of select="./rsd:OKOGU"/>
            </tns:OKOGU>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKONH">
            <tns:OKONH>
               <xsl:value-of select="./rsd:OKONH"/>
            </tns:OKONH>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKOPF">
            <tns:OKOPF>
               <xsl:value-of select="./rsd:OKOPF"/>
            </tns:OKOPF>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKVED">
            <tns:OKVED>
               <xsl:value-of select="./rsd:OKVED"/>
            </tns:OKVED>
         </xsl:if>
      </tns:RosstatCodes>
   </xsl:template>

   <xsl:template match="rsd:FounderLegalPerson">
      <tns:FounderLegalPerson><!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientName">
            <tns:ClientName>
               <xsl:value-of select="./rsd:ClientName"/>
            </tns:ClientName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:KPP">
            <tns:KPP>
               <xsl:value-of select="./rsd:KPP"/>
            </tns:KPP>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxID">
            <tns:TaxID>
               <xsl:value-of select="./rsd:TaxID"/>
            </tns:TaxID>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxRegistrationDate">
            <tns:TaxRegistrationDate>
               <xsl:value-of select="./rsd:TaxRegistrationDate"/>
            </tns:TaxRegistrationDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxCertificateSeries">
            <tns:TaxCertificateSeries>
               <xsl:value-of select="./rsd:TaxCertificateSeries"/>
            </tns:TaxCertificateSeries>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxCertificateNumber">
            <tns:TaxCertificateNumber>
               <xsl:value-of select="./rsd:TaxCertificateNumber"/>
            </tns:TaxCertificateNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxAuthority">
            <tns:TaxAuthority>
               <xsl:value-of select="./rsd:TaxAuthority"/>
            </tns:TaxAuthority>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRN">
            <tns:OGRN>
               <xsl:value-of select="./rsd:OGRN"/>
            </tns:OGRN>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNRegistrationDate">
            <tns:OGRNRegistrationDate>
               <xsl:value-of select="./rsd:OGRNRegistrationDate"/>
            </tns:OGRNRegistrationDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNSeries">
            <tns:OGRNSeries>
               <xsl:value-of select="./rsd:OGRNSeries"/>
            </tns:OGRNSeries>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNNumber">
            <tns:OGRNNumber>
               <xsl:value-of select="./rsd:OGRNNumber"/>
            </tns:OGRNNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNAuthority">
            <tns:OGRNAuthority>
               <xsl:value-of select="./rsd:OGRNAuthority"/>
            </tns:OGRNAuthority>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientAddress"/>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:LegalCapitalPercent">
            <tns:LegalCapitalPercent>
               <xsl:value-of select="./rsd:LegalCapitalPercent"/>
            </tns:LegalCapitalPercent>
         </xsl:if>
      </tns:FounderLegalPerson>
   </xsl:template>

   <xsl:template match="rsd:TaxInspectorateInfo">
      <tns:TaxInspectorateInfo><!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationSertificateNumber">
            <tns:RegistrationSertificateNumber>
               <xsl:value-of select="./rsd:RegistrationSertificateNumber"/>
            </tns:RegistrationSertificateNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxId">
            <tns:TaxId>
               <xsl:value-of select="./rsd:TaxId"/>
            </tns:TaxId>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxRegReasonCode">
            <tns:TaxRegReasonCode>
               <xsl:value-of select="./rsd:TaxRegReasonCode"/>
            </tns:TaxRegReasonCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:KIO">
            <tns:KIO>
               <xsl:value-of select="./rsd:KIO"/>
            </tns:KIO>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxInspectorate">
            <tns:TaxInspectorate>
               <xsl:value-of select="./rsd:TaxInspectorate"/>
            </tns:TaxInspectorate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationDate">
            <tns:RegistrationDate>
               <xsl:value-of select="./rsd:RegistrationDate"/>
            </tns:RegistrationDate>
         </xsl:if>
      </tns:TaxInspectorateInfo>
   </xsl:template>

   <xsl:template match="rsd:Registration">
      <tns:Registration><!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRN">
            <tns:OGRN>
               <xsl:value-of select="./rsd:OGRN"/>
            </tns:OGRN>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNAssignDate">
            <tns:OGRNAssignDate>
               <xsl:value-of select="./rsd:OGRNAssignDate"/>
            </tns:OGRNAssignDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OGRNIssueDate">
            <tns:OGRNIssueDate>
               <xsl:value-of select="./rsd:OGRNIssueDate"/>
            </tns:OGRNIssueDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:ShortName>
            <xsl:value-of select="./rsd:ShortName"/>
         </tns:ShortName>
         <!--match="xsd:element[@name]"--><tns:FullName>
            <xsl:value-of select="./rsd:FullName"/>
         </tns:FullName>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ShortNameInEnglish">
            <tns:ShortNameInEnglish>
               <xsl:value-of select="./rsd:ShortNameInEnglish"/>
            </tns:ShortNameInEnglish>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FullNameInEnglish">
            <tns:FullNameInEnglish>
               <xsl:value-of select="./rsd:FullNameInEnglish"/>
            </tns:FullNameInEnglish>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationSertificateNumber">
            <tns:RegistrationSertificateNumber>
               <xsl:value-of select="./rsd:RegistrationSertificateNumber"/>
            </tns:RegistrationSertificateNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationSertificateIssueDate">
            <tns:RegistrationSertificateIssueDate>
               <xsl:value-of select="./rsd:RegistrationSertificateIssueDate"/>
            </tns:RegistrationSertificateIssueDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationAuthority">
            <tns:RegistrationAuthority>
               <xsl:value-of select="./rsd:RegistrationAuthority"/>
            </tns:RegistrationAuthority>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:OldShortName>
            <xsl:value-of select="./rsd:OldShortName"/>
         </tns:OldShortName>
         <!--match="xsd:element[@name]"--><tns:OldName>
            <xsl:value-of select="./rsd:OldName"/>
         </tns:OldName>
         <!--match="xsd:element[@name]"--><tns:OldShortNameInEnglish>
            <xsl:value-of select="./rsd:OldShortNameInEnglish"/>
         </tns:OldShortNameInEnglish>
         <!--match="xsd:element[@name]"--><tns:OldNameInEnglish>
            <xsl:value-of select="./rsd:OldNameInEnglish"/>
         </tns:OldNameInEnglish>
         <!--match="xsd:element[@name]"--><tns:TermFrom>
            <xsl:value-of select="./rsd:TermFrom"/>
         </tns:TermFrom>
         <!--match="xsd:element[@name]"--><tns:TermTill>
            <xsl:value-of select="./rsd:TermTill"/>
         </tns:TermTill>
         <!--match="xsd:element[@name]"--><tns:DeclaredFound>
            <xsl:value-of select="./rsd:DeclaredFound"/>
         </tns:DeclaredFound>
         <!--match="xsd:element[@name]"--><tns:PayedFound>
            <xsl:value-of select="./rsd:PayedFound"/>
         </tns:PayedFound>
         <!--match="xsd:element[@name]"--><tns:Control>
            <xsl:value-of select="./rsd:Control"/>
         </tns:Control>
         <!--match="xsd:element[@name]"--><tns:Amount>
            <xsl:value-of select="./rsd:Amount"/>
         </tns:Amount>
         <!--match="xsd:element[@name]"--><tns:YearROI>
            <xsl:value-of select="./rsd:YearROI"/>
         </tns:YearROI>
         <!--match="xsd:element[@name]"--><tns:From>
            <xsl:value-of select="./rsd:From"/>
         </tns:From>
         <!--match="xsd:element[@name]"--><tns:Till>
            <xsl:value-of select="./rsd:Till"/>
         </tns:Till>
         <!--match="xsd:element[@name]"--><tns:License>
            <xsl:value-of select="./rsd:License"/>
         </tns:License>
      </tns:Registration>
   </xsl:template>

   <xsl:template match="rsd:SpecialData">
      <tns:SpecialData><!--match="xsd:element[@name]"--><tns:ClientType>
            <xsl:value-of select="./rsd:ClientType"/>
         </tns:ClientType>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Resident">
            <tns:Resident>
               <xsl:value-of select="./rsd:Resident"/>
            </tns:Resident>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Risk">
            <tns:Risk>
               <xsl:value-of select="./rsd:Risk"/>
            </tns:Risk>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RiskExplanation">
            <tns:RiskExplanation>
               <xsl:value-of select="./rsd:RiskExplanation"/>
            </tns:RiskExplanation>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CountryOfRegistration">
            <tns:CountryOfRegistration>
               <xsl:value-of select="./rsd:CountryOfRegistration"/>
            </tns:CountryOfRegistration>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CountryOfAccreditation">
            <tns:CountryOfAccreditation>
               <xsl:value-of select="./rsd:CountryOfAccreditation"/>
            </tns:CountryOfAccreditation>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RepresentedInCountry">
            <tns:RepresentedInCountry>
               <xsl:value-of select="./rsd:RepresentedInCountry"/>
            </tns:RepresentedInCountry>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorizedCapital">
            <tns:AuthorizedCapital>
               <xsl:value-of select="./rsd:AuthorizedCapital"/>
            </tns:AuthorizedCapital>
         </xsl:if>
      </tns:SpecialData>
   </xsl:template>

   <xsl:template match="rsd:Identifiers">
      <tns:Identifiers><!--match="xsd:element[@name]"--><tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <!--match="xsd:element[@name]"--><tns:State>
            <xsl:value-of select="./rsd:State"/>
         </tns:State>
         <!--match="xsd:element[@name]"--><tns:CreateDate>
            <xsl:value-of select="./rsd:CreateDate"/>
         </tns:CreateDate>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CloseDate">
            <tns:CloseDate>
               <xsl:value-of select="./rsd:CloseDate"/>
            </tns:CloseDate>
         </xsl:if>
      </tns:Identifiers>
   </xsl:template>

   <xsl:template match="rsd:ClientAddress">
      <tns:ClientAddress><!--match="xsd:element[@name]"--><tns:AddressType>
            <xsl:value-of select="./rsd:AddressType"/>
         </tns:AddressType>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:UnnormalizedAddress">
            <tns:UnnormalizedAddress>
               <xsl:value-of select="./rsd:UnnormalizedAddress"/>
            </tns:UnnormalizedAddress>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Country">
            <tns:Country>
               <xsl:value-of select="./rsd:Country"/>
            </tns:Country>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:PostalCode">
            <tns:PostalCode>
               <xsl:value-of select="./rsd:PostalCode"/>
            </tns:PostalCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentificationCode">
            <tns:IdentificationCode>
               <xsl:value-of select="./rsd:IdentificationCode"/>
            </tns:IdentificationCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OKATO">
            <tns:OKATO>
               <xsl:value-of select="./rsd:OKATO"/>
            </tns:OKATO>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegionType">
            <tns:RegionType>
               <xsl:value-of select="./rsd:RegionType"/>
            </tns:RegionType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Region">
            <tns:Region>
               <xsl:value-of select="./rsd:Region"/>
            </tns:Region>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AreaType">
            <tns:AreaType>
               <xsl:value-of select="./rsd:AreaType"/>
            </tns:AreaType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Area">
            <tns:Area>
               <xsl:value-of select="./rsd:Area"/>
            </tns:Area>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CityType">
            <tns:CityType>
               <xsl:value-of select="./rsd:CityType"/>
            </tns:CityType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:City">
            <tns:City>
               <xsl:value-of select="./rsd:City"/>
            </tns:City>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:PlaceType">
            <tns:PlaceType>
               <xsl:value-of select="./rsd:PlaceType"/>
            </tns:PlaceType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Place">
            <tns:Place>
               <xsl:value-of select="./rsd:Place"/>
            </tns:Place>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:StreetType">
            <tns:StreetType>
               <xsl:value-of select="./rsd:StreetType"/>
            </tns:StreetType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Street">
            <tns:Street>
               <xsl:value-of select="./rsd:Street"/>
            </tns:Street>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:House">
            <tns:House>
               <xsl:value-of select="./rsd:House"/>
            </tns:House>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Corpus">
            <tns:Corpus>
               <xsl:value-of select="./rsd:Corpus"/>
            </tns:Corpus>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:Building">
            <tns:Building>
               <xsl:value-of select="./rsd:Building"/>
            </tns:Building>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FlatorOffice">
            <tns:FlatorOffice>
               <xsl:value-of select="./rsd:FlatorOffice"/>
            </tns:FlatorOffice>
         </xsl:if>
      </tns:ClientAddress>
   </xsl:template>

   <xsl:template match="rsd:ClientContact">
      <tns:ClientContact><!--match="xsd:element[@name]"--><tns:ContactCategory>
            <xsl:value-of select="./rsd:ContactCategory"/>
         </tns:ContactCategory>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ContactType">
            <tns:ContactType>
               <xsl:value-of select="./rsd:ContactType"/>
            </tns:ContactType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ContactName">
            <tns:ContactName>
               <xsl:value-of select="./rsd:ContactName"/>
            </tns:ContactName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:ContactData>
            <xsl:value-of select="./rsd:ContactData"/>
         </tns:ContactData>
      </tns:ClientContact>
   </xsl:template>

   <xsl:template match="rsd:LegalPersonBankAccountAgreement">
      <tns:LegalPersonBankAccountAgreement><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:BankSubdivision"/>
         <!--match="xsd:element[@name]"--><tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementIdentifier">
            <tns:AgreementIdentifier>
               <xsl:value-of select="./rsd:AgreementIdentifier"/>
            </tns:AgreementIdentifier>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementNumber">
            <tns:AgreementNumber>
               <xsl:value-of select="./rsd:AgreementNumber"/>
            </tns:AgreementNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:AgreementType>
            <xsl:value-of select="./rsd:AgreementType"/>
         </tns:AgreementType>
         <!--match="xsd:element[@name]"--><tns:AgreementStatus>
            <xsl:value-of select="./rsd:AgreementStatus"/>
         </tns:AgreementStatus>
         <!--match="xsd:element[@name]"--><tns:AgreementIssueDate>
            <xsl:value-of select="./rsd:AgreementIssueDate"/>
         </tns:AgreementIssueDate>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementCancelDate">
            <tns:AgreementCancelDate>
               <xsl:value-of select="./rsd:AgreementCancelDate"/>
            </tns:AgreementCancelDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:Operator>
            <xsl:value-of select="./rsd:Operator"/>
         </tns:Operator>
         <!--match="xsd:element[@name]"--><tns:MainAccountNumber>
            <xsl:value-of select="./rsd:MainAccountNumber"/>
         </tns:MainAccountNumber>
         <!--match="xsd:element[@name]"--><tns:AccountBalance>
            <xsl:value-of select="./rsd:AccountBalance"/>
         </tns:AccountBalance>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ArrestByAccountPresence">
            <tns:ArrestByAccountPresence>
               <xsl:value-of select="./rsd:ArrestByAccountPresence"/>
            </tns:ArrestByAccountPresence>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:RKOAgreementDetails"/>
      </tns:LegalPersonBankAccountAgreement>
   </xsl:template>

   <xsl:template match="rsd:RKOAgreementDetails">
      <tns:RKOAgreementDetails><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:TariffPlan"/>
         <!--match="xsd:element[@name]"--><tns:DetermineTariffs>
            <xsl:value-of select="./rsd:DetermineTariffs"/>
         </tns:DetermineTariffs>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TariffGroup">
            <tns:TariffGroup>
               <xsl:value-of select="./rsd:TariffGroup"/>
            </tns:TariffGroup>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientAccount"/>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:PercentCalculationAlgorithm">
            <tns:PercentCalculationAlgorithm>
               <xsl:value-of select="./rsd:PercentCalculationAlgorithm"/>
            </tns:PercentCalculationAlgorithm>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DeterminePercent">
            <tns:DeterminePercent>
               <xsl:value-of select="./rsd:DeterminePercent"/>
            </tns:DeterminePercent>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:PercentRateGroup">
            <tns:PercentRateGroup>
               <xsl:value-of select="./rsd:PercentRateGroup"/>
            </tns:PercentRateGroup>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DateBeginPercentRate">
            <tns:DateBeginPercentRate>
               <xsl:value-of select="./rsd:DateBeginPercentRate"/>
            </tns:DateBeginPercentRate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DateEndPercentRate">
            <tns:DateEndPercentRate>
               <xsl:value-of select="./rsd:DateEndPercentRate"/>
            </tns:DateEndPercentRate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:PartialRatedPeriodCalculation">
            <tns:PartialRatedPeriodCalculation>
               <xsl:value-of select="./rsd:PartialRatedPeriodCalculation"/>
            </tns:PartialRatedPeriodCalculation>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:CurrentRatedPeriodCalculation>
            <xsl:value-of select="./rsd:CurrentRatedPeriodCalculation"/>
         </tns:CurrentRatedPeriodCalculation>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:OpeningReason">
            <tns:OpeningReason>
               <xsl:value-of select="./rsd:OpeningReason"/>
            </tns:OpeningReason>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementUserType">
            <tns:AgreementUserType>
               <xsl:value-of select="./rsd:AgreementUserType"/>
            </tns:AgreementUserType>
         </xsl:if>
      </tns:RKOAgreementDetails>
   </xsl:template>

   <xsl:template match="rsd:SignPatternCard">
      <tns:SignPatternCard><!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientId">
            <tns:ClientId>
               <xsl:value-of select="./rsd:ClientId"/>
            </tns:ClientId>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:BankSubdivision"/>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementNumber">
            <tns:AgreementNumber>
               <xsl:value-of select="./rsd:AgreementNumber"/>
            </tns:AgreementNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AccountNumber">
            <tns:AccountNumber>
               <xsl:value-of select="./rsd:AccountNumber"/>
            </tns:AccountNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CardType">
            <tns:CardType>
               <xsl:value-of select="./rsd:CardType"/>
            </tns:CardType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RegistrationDate">
            <tns:RegistrationDate>
               <xsl:value-of select="./rsd:RegistrationDate"/>
            </tns:RegistrationDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ActivationDate">
            <tns:ActivationDate>
               <xsl:value-of select="./rsd:ActivationDate"/>
            </tns:ActivationDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CloseDate">
            <tns:CloseDate>
               <xsl:value-of select="./rsd:CloseDate"/>
            </tns:CloseDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:CardRegistartionMethod">
            <tns:CardRegistartionMethod>
               <xsl:value-of select="./rsd:CardRegistartionMethod"/>
            </tns:CardRegistartionMethod>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:Sign"/>
      </tns:SignPatternCard>
   </xsl:template>

   <xsl:template match="rsd:Sign">
      <tns:Sign><!--match="xsd:element[@name]"--><tns:RKONaturalPersonCode>
            <xsl:value-of select="./rsd:RKONaturalPersonCode"/>
         </tns:RKONaturalPersonCode>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:SeqNumber">
            <tns:SeqNumber>
               <xsl:value-of select="./rsd:SeqNumber"/>
            </tns:SeqNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:SignType>
            <xsl:value-of select="./rsd:SignType"/>
         </tns:SignType>
         <!--match="xsd:element[@name]"--><tns:Position>
            <xsl:value-of select="./rsd:Position"/>
         </tns:Position>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityStartDate">
            <tns:AuthorityStartDate>
               <xsl:value-of select="./rsd:AuthorityStartDate"/>
            </tns:AuthorityStartDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityEndDate">
            <tns:AuthorityEndDate>
               <xsl:value-of select="./rsd:AuthorityEndDate"/>
            </tns:AuthorityEndDate>
         </xsl:if>
      </tns:Sign>
   </xsl:template>

   <xsl:template match="rsd:ClientAccount">
      <tns:ClientAccount><!--match="xsd:element[@name]"--><xsl:if test="./rsd:AccountNumber">
            <tns:AccountNumber>
               <xsl:value-of select="./rsd:AccountNumber"/>
            </tns:AccountNumber>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ExternalAttribute">
            <tns:ExternalAttribute>
               <xsl:value-of select="./rsd:ExternalAttribute"/>
            </tns:ExternalAttribute>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:RecipientName">
            <tns:RecipientName>
               <xsl:value-of select="./rsd:RecipientName"/>
            </tns:RecipientName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxID">
            <tns:TaxID>
               <xsl:value-of select="./rsd:TaxID"/>
            </tns:TaxID>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TaxRegReasonCode">
            <tns:TaxRegReasonCode>
               <xsl:value-of select="./rsd:TaxRegReasonCode"/>
            </tns:TaxRegReasonCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BIK">
            <tns:BIK>
               <xsl:value-of select="./rsd:BIK"/>
            </tns:BIK>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:SWIFTCode">
            <tns:SWIFTCode>
               <xsl:value-of select="./rsd:SWIFTCode"/>
            </tns:SWIFTCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClearingCode">
            <tns:ClearingCode>
               <xsl:value-of select="./rsd:ClearingCode"/>
            </tns:ClearingCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BankBeneficiarySWIFTCode">
            <tns:BankBeneficiarySWIFTCode>
               <xsl:value-of select="./rsd:BankBeneficiarySWIFTCode"/>
            </tns:BankBeneficiarySWIFTCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DCode">
            <tns:DCode>
               <xsl:value-of select="./rsd:DCode"/>
            </tns:DCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:BeneficiaryIBANCode">
            <tns:BeneficiaryIBANCode>
               <xsl:value-of select="./rsd:BeneficiaryIBANCode"/>
            </tns:BeneficiaryIBANCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientBeneficiarySWIFTCode">
            <tns:ClientBeneficiarySWIFTCode>
               <xsl:value-of select="./rsd:ClientBeneficiarySWIFTCode"/>
            </tns:ClientBeneficiarySWIFTCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientBeneficiaryName">
            <tns:ClientBeneficiaryName>
               <xsl:value-of select="./rsd:ClientBeneficiaryName"/>
            </tns:ClientBeneficiaryName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientBeneficiaryTaxID">
            <tns:ClientBeneficiaryTaxID>
               <xsl:value-of select="./rsd:ClientBeneficiaryTaxID"/>
            </tns:ClientBeneficiaryTaxID>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientBeneficaryAddress">
            <tns:ClientBeneficaryAddress>
               <xsl:value-of select="./rsd:ClientBeneficaryAddress"/>
            </tns:ClientBeneficaryAddress>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ClientBeneficiaryCountry">
            <tns:ClientBeneficiaryCountry>
               <xsl:value-of select="./rsd:ClientBeneficiaryCountry"/>
            </tns:ClientBeneficiaryCountry>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AccountPurpose">
            <tns:AccountPurpose>
               <xsl:value-of select="./rsd:AccountPurpose"/>
            </tns:AccountPurpose>
         </xsl:if>
      </tns:ClientAccount>
   </xsl:template>

   <xsl:template match="rsd:IdentityDocument">
      <tns:IdentityDocument><!--match="xsd:element[@name]"--><tns:DocumentType>
            <xsl:value-of select="./rsd:DocumentType"/>
         </tns:DocumentType>
         <!--match="xsd:element[@name]"--><tns:DocumentStatus>
            <xsl:value-of select="./rsd:DocumentStatus"/>
         </tns:DocumentStatus>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:DocumentSeries">
            <tns:DocumentSeries>
               <xsl:value-of select="./rsd:DocumentSeries"/>
            </tns:DocumentSeries>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:DocumentNumber>
            <xsl:value-of select="./rsd:DocumentNumber"/>
         </tns:DocumentNumber>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IssueDate">
            <tns:IssueDate>
               <xsl:value-of select="./rsd:IssueDate"/>
            </tns:IssueDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IssueAuthority">
            <tns:IssueAuthority>
               <xsl:value-of select="./rsd:IssueAuthority"/>
            </tns:IssueAuthority>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IssueAuthorityCode">
            <tns:IssueAuthorityCode>
               <xsl:value-of select="./rsd:IssueAuthorityCode"/>
            </tns:IssueAuthorityCode>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ExpirationDate">
            <tns:ExpirationDate>
               <xsl:value-of select="./rsd:ExpirationDate"/>
            </tns:ExpirationDate>
         </xsl:if>
      </tns:IdentityDocument>
   </xsl:template>

   <xsl:template match="rsd:TariffPlan">
      <tns:TariffPlan><!--match="xsd:element[@name]"--><tns:TariffPlanCode>
            <xsl:value-of select="./rsd:TariffPlanCode"/>
         </tns:TariffPlanCode>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TariffPlanStartDate">
            <tns:TariffPlanStartDate>
               <xsl:value-of select="./rsd:TariffPlanStartDate"/>
            </tns:TariffPlanStartDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:TariffPlanEndDate">
            <tns:TariffPlanEndDate>
               <xsl:value-of select="./rsd:TariffPlanEndDate"/>
            </tns:TariffPlanEndDate>
         </xsl:if>
      </tns:TariffPlan>
   </xsl:template>

   <xsl:template match="rsd:BankSubdivision">
      <tns:BankSubdivision><!--match="xsd:element[@name]"--><tns:TB>
            <xsl:value-of select="./rsd:TB"/>
         </tns:TB>
         <!--match="xsd:element[@name]"--><tns:OSB>
            <xsl:value-of select="./rsd:OSB"/>
         </tns:OSB>
         <!--match="xsd:element[@name]"--><tns:VSP>
            <xsl:value-of select="./rsd:VSP"/>
         </tns:VSP>
      </tns:BankSubdivision>
   </xsl:template>

   <xsl:template match="rsd:DocumentsInfo">
      <tns:DocumentsInfo><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:DocumentLinks"/>
      </tns:DocumentsInfo>
   </xsl:template>

   <xsl:template match="rsd:DocumentLinks">
      <tns:DocumentLinks><!--match="xsd:element[@name]"--><xsl:if test="./rsd:DocumentLink">
            <tns:DocumentLink>
               <xsl:value-of select="./rsd:DocumentLink"/>
            </tns:DocumentLink>
         </xsl:if>
      </tns:DocumentLinks>
   </xsl:template>

   <xsl:template name="SrvPutRemoteLegalAccOperAppRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvPutRemoteLegalAccOperAppRq"><!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:RemoteClient">
            <tns:RemoteClient>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RemoteClient"/>
            </tns:RemoteClient>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:RemoteSystemId">
            <tns:RemoteSystemId>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RemoteSystemId"/>
            </tns:RemoteSystemId>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:IsAdditionalAccount">
            <tns:IsAdditionalAccount>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:IsAdditionalAccount"/>
            </tns:IsAdditionalAccount>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:IsBankCardProcessing">
            <tns:IsBankCardProcessing>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:IsBankCardProcessing"/>
            </tns:IsBankCardProcessing>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:ClientReferenceData"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:LegalPersonApplication"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:DocumentsInfo"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
