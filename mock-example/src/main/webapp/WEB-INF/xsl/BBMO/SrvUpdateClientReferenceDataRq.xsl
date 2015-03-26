<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/Data/"
                xmlns:BBMO="http://sbrf.ru/prpc/bbmo/10"
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
            <xsl:with-param name="operation-name" select="string('SrvUpdateClientReferenceDataRq')"/>
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
            <xsl:call-template name="SrvUpdateClientReferenceDataRq">
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

   <xsl:template match="rsd:SrvUpdateClientReferenceDataRq">
      <tns:SrvUpdateClientReferenceDataRq/>
   </xsl:template>

   <xsl:template match="rsd:ClientReferenceData">
      <tns:ClientReferenceData>
         <xsl:apply-templates select="./rsd:ClientInformation"/>
         <xsl:apply-templates select="./rsd:NaturalPerson"/>
         <xsl:apply-templates select="./rsd:LegalPersonBankAccountAgreement"/>
         <xsl:apply-templates select="./rsd:SignPatternCard"/>
      </tns:ClientReferenceData>
   </xsl:template>

   <xsl:template match="rsd:ClientInformation">
      <tns:ClientInformation>
         <xsl:apply-templates select="./rsd:Identifiers"/>
         <xsl:apply-templates select="./rsd:SpecialData"/>
         <xsl:apply-templates select="./rsd:MainDetails"/>
         <xsl:apply-templates select="./rsd:BankSubdivision"/>
         <xsl:apply-templates select="./rsd:ClientAddress"/>
         <xsl:apply-templates select="./rsd:ClientContact"/>
      </tns:ClientInformation>
   </xsl:template>

   <xsl:template match="rsd:MainDetails">
      <tns:MainDetails>
         <xsl:apply-templates select="./rsd:Registration"/>
         <xsl:apply-templates select="./rsd:TaxInspectorateInfo"/>
         <xsl:apply-templates select="./rsd:RosstatCodes"/>
      </tns:MainDetails>
   </xsl:template>

   <xsl:template match="rsd:RosstatCodes">
      <tns:RosstatCodes>
         <xsl:if test="./rsd:OKATO">
            <tns:OKATO>
               <xsl:value-of select="./rsd:OKATO"/>
            </tns:OKATO>
         </xsl:if>
         <xsl:if test="./rsd:OKPO">
            <tns:OKPO>
               <xsl:value-of select="./rsd:OKPO"/>
            </tns:OKPO>
         </xsl:if>
         <xsl:if test="./rsd:OKFS">
            <tns:OKFS>
               <xsl:value-of select="./rsd:OKFS"/>
            </tns:OKFS>
         </xsl:if>
         <xsl:if test="./rsd:OKOGU">
            <tns:OKOGU>
               <xsl:value-of select="./rsd:OKOGU"/>
            </tns:OKOGU>
         </xsl:if>
         <xsl:if test="./rsd:OKONH">
            <tns:OKONH>
               <xsl:value-of select="./rsd:OKONH"/>
            </tns:OKONH>
         </xsl:if>
         <xsl:if test="./rsd:OKOPF">
            <tns:OKOPF>
               <xsl:value-of select="./rsd:OKOPF"/>
            </tns:OKOPF>
         </xsl:if>
         <xsl:if test="./rsd:OKVED">
            <tns:OKVED>
               <xsl:value-of select="./rsd:OKVED"/>
            </tns:OKVED>
         </xsl:if>
      </tns:RosstatCodes>
   </xsl:template>

   <xsl:template match="rsd:TaxInspectorateInfo">
      <tns:TaxInspectorateInfo>
         <xsl:if test="./rsd:RegistrationSertificateNumber">
            <tns:RegistrationSertificateNumber>
               <xsl:value-of select="./rsd:RegistrationSertificateNumber"/>
            </tns:RegistrationSertificateNumber>
         </xsl:if>
         <xsl:if test="./rsd:TaxId">
            <tns:TaxId>
               <xsl:value-of select="./rsd:TaxId"/>
            </tns:TaxId>
         </xsl:if>
         <xsl:if test="./rsd:TaxRegReasonCode">
            <tns:TaxRegReasonCode>
               <xsl:value-of select="./rsd:TaxRegReasonCode"/>
            </tns:TaxRegReasonCode>
         </xsl:if>
         <xsl:if test="./rsd:KIO">
            <tns:KIO>
               <xsl:value-of select="./rsd:KIO"/>
            </tns:KIO>
         </xsl:if>
         <xsl:if test="./rsd:TaxInspectorate">
            <tns:TaxInspectorate>
               <xsl:value-of select="./rsd:TaxInspectorate"/>
            </tns:TaxInspectorate>
         </xsl:if>
         <xsl:if test="./rsd:RegistrationDate">
            <tns:RegistrationDate>
               <xsl:value-of select="./rsd:RegistrationDate"/>
            </tns:RegistrationDate>
         </xsl:if>
      </tns:TaxInspectorateInfo>
   </xsl:template>

   <xsl:template match="rsd:Registration">
      <tns:Registration>
         <xsl:if test="./rsd:OGRN">
            <tns:OGRN>
               <xsl:value-of select="./rsd:OGRN"/>
            </tns:OGRN>
         </xsl:if>
         <xsl:if test="./rsd:OGRNAssignDate">
            <tns:OGRNAssignDate>
               <xsl:value-of select="./rsd:OGRNAssignDate"/>
            </tns:OGRNAssignDate>
         </xsl:if>
         <xsl:if test="./rsd:OGRNIssueDate">
            <tns:OGRNIssueDate>
               <xsl:value-of select="./rsd:OGRNIssueDate"/>
            </tns:OGRNIssueDate>
         </xsl:if>
         <tns:ShortName>
            <xsl:value-of select="./rsd:ShortName"/>
         </tns:ShortName>
         <tns:FullName>
            <xsl:value-of select="./rsd:FullName"/>
         </tns:FullName>
         <xsl:if test="./rsd:ShortNameInEnglish">
            <tns:ShortNameInEnglish>
               <xsl:value-of select="./rsd:ShortNameInEnglish"/>
            </tns:ShortNameInEnglish>
         </xsl:if>
         <xsl:if test="./rsd:FullNameInEnglish">
            <tns:FullNameInEnglish>
               <xsl:value-of select="./rsd:FullNameInEnglish"/>
            </tns:FullNameInEnglish>
         </xsl:if>
         <xsl:if test="./rsd:RegistrationSertificateNumber">
            <tns:RegistrationSertificateNumber>
               <xsl:value-of select="./rsd:RegistrationSertificateNumber"/>
            </tns:RegistrationSertificateNumber>
         </xsl:if>
         <xsl:if test="./rsd:RegistrationSertificateIssueDate">
            <tns:RegistrationSertificateIssueDate>
               <xsl:value-of select="./rsd:RegistrationSertificateIssueDate"/>
            </tns:RegistrationSertificateIssueDate>
         </xsl:if>
         <xsl:if test="./rsd:RegistrationAuthority">
            <tns:RegistrationAuthority>
               <xsl:value-of select="./rsd:RegistrationAuthority"/>
            </tns:RegistrationAuthority>
         </xsl:if>
      </tns:Registration>
   </xsl:template>

   <xsl:template match="rsd:SpecialData">
      <tns:SpecialData>
         <tns:ClientType>
            <xsl:value-of select="./rsd:ClientType"/>
         </tns:ClientType>
         <xsl:if test="./rsd:Resident">
            <tns:Resident>
               <xsl:value-of select="./rsd:Resident"/>
            </tns:Resident>
         </xsl:if>
         <xsl:if test="./rsd:Risk">
            <tns:Risk>
               <xsl:value-of select="./rsd:Risk"/>
            </tns:Risk>
         </xsl:if>
         <xsl:if test="./rsd:RiskExplanation">
            <tns:RiskExplanation>
               <xsl:value-of select="./rsd:RiskExplanation"/>
            </tns:RiskExplanation>
         </xsl:if>
         <xsl:if test="./rsd:CountryOfRegistration">
            <tns:CountryOfRegistration>
               <xsl:value-of select="./rsd:CountryOfRegistration"/>
            </tns:CountryOfRegistration>
         </xsl:if>
         <xsl:if test="./rsd:CountryOfAccreditation">
            <tns:CountryOfAccreditation>
               <xsl:value-of select="./rsd:CountryOfAccreditation"/>
            </tns:CountryOfAccreditation>
         </xsl:if>
         <xsl:if test="./rsd:RepresentedInCountry">
            <tns:RepresentedInCountry>
               <xsl:value-of select="./rsd:RepresentedInCountry"/>
            </tns:RepresentedInCountry>
         </xsl:if>
         <xsl:if test="./rsd:AuthorizedCapital">
            <tns:AuthorizedCapital>
               <xsl:value-of select="./rsd:AuthorizedCapital"/>
            </tns:AuthorizedCapital>
         </xsl:if>
      </tns:SpecialData>
   </xsl:template>

   <xsl:template match="rsd:Identifiers">
      <tns:Identifiers>
         <tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <tns:State>
            <xsl:value-of select="./rsd:State"/>
         </tns:State>
         <tns:CreateDate>
            <xsl:value-of select="./rsd:CreateDate"/>
         </tns:CreateDate>
         <xsl:if test="./rsd:CloseDate">
            <tns:CloseDate>
               <xsl:value-of select="./rsd:CloseDate"/>
            </tns:CloseDate>
         </xsl:if>
      </tns:Identifiers>
   </xsl:template>

   <xsl:template match="rsd:ClientAddress">
      <tns:ClientAddress>
         <tns:AddressType>
            <xsl:value-of select="./rsd:AddressType"/>
         </tns:AddressType>
         <xsl:if test="./rsd:UnnormalizedAddress">
            <tns:UnnormalizedAddress>
               <xsl:value-of select="./rsd:UnnormalizedAddress"/>
            </tns:UnnormalizedAddress>
         </xsl:if>
         <xsl:if test="./rsd:Country">
            <tns:Country>
               <xsl:value-of select="./rsd:Country"/>
            </tns:Country>
         </xsl:if>
         <xsl:if test="./rsd:PostalCode">
            <tns:PostalCode>
               <xsl:value-of select="./rsd:PostalCode"/>
            </tns:PostalCode>
         </xsl:if>
         <xsl:if test="./rsd:IdentificationCode">
            <tns:IdentificationCode>
               <xsl:value-of select="./rsd:IdentificationCode"/>
            </tns:IdentificationCode>
         </xsl:if>
         <xsl:if test="./rsd:OKATO">
            <tns:OKATO>
               <xsl:value-of select="./rsd:OKATO"/>
            </tns:OKATO>
         </xsl:if>
         <xsl:if test="./rsd:RegionType">
            <tns:RegionType>
               <xsl:value-of select="./rsd:RegionType"/>
            </tns:RegionType>
         </xsl:if>
         <xsl:if test="./rsd:Region">
            <tns:Region>
               <xsl:value-of select="./rsd:Region"/>
            </tns:Region>
         </xsl:if>
         <xsl:if test="./rsd:AreaType">
            <tns:AreaType>
               <xsl:value-of select="./rsd:AreaType"/>
            </tns:AreaType>
         </xsl:if>
         <xsl:if test="./rsd:Area">
            <tns:Area>
               <xsl:value-of select="./rsd:Area"/>
            </tns:Area>
         </xsl:if>
         <xsl:if test="./rsd:CityType">
            <tns:CityType>
               <xsl:value-of select="./rsd:CityType"/>
            </tns:CityType>
         </xsl:if>
         <xsl:if test="./rsd:City">
            <tns:City>
               <xsl:value-of select="./rsd:City"/>
            </tns:City>
         </xsl:if>
         <xsl:if test="./rsd:PlaceType">
            <tns:PlaceType>
               <xsl:value-of select="./rsd:PlaceType"/>
            </tns:PlaceType>
         </xsl:if>
         <xsl:if test="./rsd:Place">
            <tns:Place>
               <xsl:value-of select="./rsd:Place"/>
            </tns:Place>
         </xsl:if>
         <xsl:if test="./rsd:StreetType">
            <tns:StreetType>
               <xsl:value-of select="./rsd:StreetType"/>
            </tns:StreetType>
         </xsl:if>
         <xsl:if test="./rsd:Street">
            <tns:Street>
               <xsl:value-of select="./rsd:Street"/>
            </tns:Street>
         </xsl:if>
         <xsl:if test="./rsd:House">
            <tns:House>
               <xsl:value-of select="./rsd:House"/>
            </tns:House>
         </xsl:if>
         <xsl:if test="./rsd:Corpus">
            <tns:Corpus>
               <xsl:value-of select="./rsd:Corpus"/>
            </tns:Corpus>
         </xsl:if>
         <xsl:if test="./rsd:Building">
            <tns:Building>
               <xsl:value-of select="./rsd:Building"/>
            </tns:Building>
         </xsl:if>
         <xsl:if test="./rsd:FlatorOffice">
            <tns:FlatorOffice>
               <xsl:value-of select="./rsd:FlatorOffice"/>
            </tns:FlatorOffice>
         </xsl:if>
      </tns:ClientAddress>
   </xsl:template>

   <xsl:template match="rsd:ClientContact">
      <tns:ClientContact>
         <tns:ContactCategory>
            <xsl:value-of select="./rsd:ContactCategory"/>
         </tns:ContactCategory>
         <xsl:if test="./rsd:ContactType">
            <tns:ContactType>
               <xsl:value-of select="./rsd:ContactType"/>
            </tns:ContactType>
         </xsl:if>
         <xsl:if test="./rsd:ContactName">
            <tns:ContactName>
               <xsl:value-of select="./rsd:ContactName"/>
            </tns:ContactName>
         </xsl:if>
         <tns:ContactData>
            <xsl:value-of select="./rsd:ContactData"/>
         </tns:ContactData>
      </tns:ClientContact>
   </xsl:template>

   <xsl:template match="rsd:NaturalPerson">
      <tns:NaturalPerson>
         <tns:RKOCode>
            <xsl:value-of select="./rsd:RKOCode"/>
         </tns:RKOCode>
         <tns:RelationToClient>
            <xsl:value-of select="./rsd:RelationToClient"/>
         </tns:RelationToClient>
         <tns:LastName>
            <xsl:value-of select="./rsd:LastName"/>
         </tns:LastName>
         <tns:FirstName>
            <xsl:value-of select="./rsd:FirstName"/>
         </tns:FirstName>
         <xsl:if test="./rsd:MiddleName">
            <tns:MiddleName>
               <xsl:value-of select="./rsd:MiddleName"/>
            </tns:MiddleName>
         </xsl:if>
         <tns:LastNameInGenitiveCase>
            <xsl:value-of select="./rsd:LastNameInGenitiveCase"/>
         </tns:LastNameInGenitiveCase>
         <tns:FirstNameInGenitiveCase>
            <xsl:value-of select="./rsd:FirstNameInGenitiveCase"/>
         </tns:FirstNameInGenitiveCase>
         <xsl:if test="./rsd:MiddleNameInGenitiveCase">
            <tns:MiddleNameInGenitiveCase>
               <xsl:value-of select="./rsd:MiddleNameInGenitiveCase"/>
            </tns:MiddleNameInGenitiveCase>
         </xsl:if>
         <tns:LastNameInEnglish>
            <xsl:value-of select="./rsd:LastNameInEnglish"/>
         </tns:LastNameInEnglish>
         <tns:FirstNameInEnglish>
            <xsl:value-of select="./rsd:FirstNameInEnglish"/>
         </tns:FirstNameInEnglish>
         <xsl:if test="./rsd:MiddleNameInEnglish">
            <tns:MiddleNameInEnglish>
               <xsl:value-of select="./rsd:MiddleNameInEnglish"/>
            </tns:MiddleNameInEnglish>
         </xsl:if>
         <xsl:if test="./rsd:Sex">
            <tns:Sex>
               <xsl:value-of select="./rsd:Sex"/>
            </tns:Sex>
         </xsl:if>
         <xsl:if test="./rsd:BirthDate">
            <tns:BirthDate>
               <xsl:value-of select="./rsd:BirthDate"/>
            </tns:BirthDate>
         </xsl:if>
         <xsl:if test="./rsd:BirthPlace">
            <tns:BirthPlace>
               <xsl:value-of select="./rsd:BirthPlace"/>
            </tns:BirthPlace>
         </xsl:if>
         <xsl:if test="./rsd:Nationality">
            <tns:Nationality>
               <xsl:value-of select="./rsd:Nationality"/>
            </tns:Nationality>
         </xsl:if>
         <tns:Resident>
            <xsl:value-of select="./rsd:Resident"/>
         </tns:Resident>
         <xsl:if test="./rsd:TaxResidentChangeDate">
            <tns:TaxResidentChangeDate>
               <xsl:value-of select="./rsd:TaxResidentChangeDate"/>
            </tns:TaxResidentChangeDate>
         </xsl:if>
         <xsl:if test="./rsd:TaxId">
            <tns:TaxId>
               <xsl:value-of select="./rsd:TaxId"/>
            </tns:TaxId>
         </xsl:if>
         <tns:IdentityDocument>
            <tns:DocumentType>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentType"/>
            </tns:DocumentType>
            <tns:DocumentStatus>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentStatus"/>
            </tns:DocumentStatus>
            <xsl:if test="./rsd:IdentityDocument/rsd:DocumentSeries">
               <tns:DocumentSeries>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentSeries"/>
               </tns:DocumentSeries>
            </xsl:if>
            <tns:DocumentNumber>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentNumber"/>
            </tns:DocumentNumber>
            <xsl:if test="./rsd:IdentityDocument/rsd:IssueDate">
               <tns:IssueDate>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueDate"/>
               </tns:IssueDate>
            </xsl:if>
            <xsl:if test="./rsd:IdentityDocument/rsd:IssueAuthority">
               <tns:IssueAuthority>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueAuthority"/>
               </tns:IssueAuthority>
            </xsl:if>
            <xsl:if test="./rsd:IdentityDocument/rsd:IssueAuthorityCode">
               <tns:IssueAuthorityCode>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueAuthorityCode"/>
               </tns:IssueAuthorityCode>
            </xsl:if>
            <xsl:if test="./rsd:IdentityDocument/rsd:ExpirationDate">
               <tns:ExpirationDate>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:ExpirationDate"/>
               </tns:ExpirationDate>
            </xsl:if>
         </tns:IdentityDocument>
         <xsl:apply-templates select="./rsd:ClientAddress"/>
         <xsl:apply-templates select="./rsd:ClientContact"/>
         <xsl:if test="./rsd:AuthorityBase">
            <tns:AuthorityBase>
               <xsl:if test="./rsd:AuthorityBase/rsd:DocumentActAuthority">
                  <tns:DocumentActAuthority>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentActAuthority"/>
                  </tns:DocumentActAuthority>
               </xsl:if>
               <xsl:if test="./rsd:AuthorityBase/rsd:DocumentSeriesNumber">
                  <tns:DocumentSeriesNumber>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentSeriesNumber"/>
                  </tns:DocumentSeriesNumber>
               </xsl:if>
               <xsl:if test="./rsd:AuthorityBase/rsd:DocumentDate">
                  <tns:DocumentDate>
                     <xsl:value-of select="./rsd:AuthorityBase/rsd:DocumentDate"/>
                  </tns:DocumentDate>
               </xsl:if>
            </tns:AuthorityBase>
         </xsl:if>
      </tns:NaturalPerson>
   </xsl:template>

   <xsl:template match="rsd:LegalPersonBankAccountAgreement">
      <tns:LegalPersonBankAccountAgreement>
         <xsl:apply-templates select="./rsd:BankSubdivision"/>
         <tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <xsl:if test="./rsd:AgreementIdentifier">
            <tns:AgreementIdentifier>
               <xsl:value-of select="./rsd:AgreementIdentifier"/>
            </tns:AgreementIdentifier>
         </xsl:if>
         <xsl:if test="./rsd:AgreementNumber">
            <tns:AgreementNumber>
               <xsl:value-of select="./rsd:AgreementNumber"/>
            </tns:AgreementNumber>
         </xsl:if>
         <tns:AgreementType>
            <xsl:value-of select="./rsd:AgreementType"/>
         </tns:AgreementType>
         <tns:AgreementStatus>
            <xsl:value-of select="./rsd:AgreementStatus"/>
         </tns:AgreementStatus>
         <tns:AgreementIssueDate>
            <xsl:value-of select="./rsd:AgreementIssueDate"/>
         </tns:AgreementIssueDate>
         <xsl:if test="./rsd:AgreementCancelDate">
            <tns:AgreementCancelDate>
               <xsl:value-of select="./rsd:AgreementCancelDate"/>
            </tns:AgreementCancelDate>
         </xsl:if>
         <tns:Operator>
            <xsl:value-of select="./rsd:Operator"/>
         </tns:Operator>
         <tns:MainAccountNumber>
            <xsl:value-of select="./rsd:MainAccountNumber"/>
         </tns:MainAccountNumber>
         <tns:AccountBalance>
            <xsl:value-of select="./rsd:AccountBalance"/>
         </tns:AccountBalance>
         <xsl:if test="./rsd:FilePresence1">
            <tns:FilePresence1>
               <xsl:value-of select="./rsd:FilePresence1"/>
            </tns:FilePresence1>
         </xsl:if>
         <xsl:if test="./rsd:FilePresence2">
            <tns:FilePresence2>
               <xsl:value-of select="./rsd:FilePresence2"/>
            </tns:FilePresence2>
         </xsl:if>
         <xsl:if test="./rsd:ArrestByAccountPresence">
            <tns:ArrestByAccountPresence>
               <xsl:value-of select="./rsd:ArrestByAccountPresence"/>
            </tns:ArrestByAccountPresence>
         </xsl:if>
         <xsl:if test="./rsd:ArrestedSum">
            <tns:ArrestedSum>
               <xsl:value-of select="./rsd:ArrestedSum"/>
            </tns:ArrestedSum>
         </xsl:if>
         <xsl:apply-templates select="./rsd:RKOAgreementDetails"/>
      </tns:LegalPersonBankAccountAgreement>
   </xsl:template>

   <xsl:template match="rsd:RKOAgreementDetails">
      <tns:RKOAgreementDetails>
         <xsl:if test="./rsd:PercentCalculationAlgorithm">
            <tns:PercentCalculationAlgorithm>
               <xsl:value-of select="./rsd:PercentCalculationAlgorithm"/>
            </tns:PercentCalculationAlgorithm>
         </xsl:if>
         <xsl:if test="./rsd:PartialRatedPeriodCalculation">
            <tns:PartialRatedPeriodCalculation>
               <xsl:value-of select="./rsd:PartialRatedPeriodCalculation"/>
            </tns:PartialRatedPeriodCalculation>
         </xsl:if>
         <tns:CurrentRatedPeriodCalculation>
            <xsl:value-of select="./rsd:CurrentRatedPeriodCalculation"/>
         </tns:CurrentRatedPeriodCalculation>
         <xsl:if test="./rsd:OpeningReason">
            <tns:OpeningReason>
               <xsl:value-of select="./rsd:OpeningReason"/>
            </tns:OpeningReason>
         </xsl:if>
         <xsl:if test="./rsd:AccountUserType">
            <tns:AccountUserType>
               <xsl:value-of select="./rsd:AccountUserType"/>
            </tns:AccountUserType>
         </xsl:if>
         <xsl:if test="./rsd:AgreementUserType">
            <tns:AgreementUserType>
               <xsl:value-of select="./rsd:AgreementUserType"/>
            </tns:AgreementUserType>
         </xsl:if>
      </tns:RKOAgreementDetails>
   </xsl:template>

   <xsl:template match="rsd:SignPatternCard">
      <tns:SignPatternCard>
         <tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <xsl:apply-templates select="./rsd:BankSubdivision"/>
         <tns:AgreementNumber>
            <xsl:value-of select="./rsd:AgreementNumber"/>
         </tns:AgreementNumber>
         <tns:AccountNumber>
            <xsl:value-of select="./rsd:AccountNumber"/>
         </tns:AccountNumber>
         <tns:CardType>
            <xsl:value-of select="./rsd:CardType"/>
         </tns:CardType>
         <tns:RegistrationDate>
            <xsl:value-of select="./rsd:RegistrationDate"/>
         </tns:RegistrationDate>
         <xsl:if test="./rsd:ActivationDate">
            <tns:ActivationDate>
               <xsl:value-of select="./rsd:ActivationDate"/>
            </tns:ActivationDate>
         </xsl:if>
         <xsl:if test="./rsd:CloseDate">
            <tns:CloseDate>
               <xsl:value-of select="./rsd:CloseDate"/>
            </tns:CloseDate>
         </xsl:if>
         <tns:CardRegistartionMethod>
            <xsl:value-of select="./rsd:CardRegistartionMethod"/>
         </tns:CardRegistartionMethod>
         <xsl:apply-templates select="./rsd:Sign"/>
      </tns:SignPatternCard>
   </xsl:template>

   <xsl:template match="rsd:Sign">
      <tns:Sign>
         <tns:RKONaturalPersonCode>
            <xsl:value-of select="./rsd:RKONaturalPersonCode"/>
         </tns:RKONaturalPersonCode>
         <tns:SignType>
            <xsl:value-of select="./rsd:SignType"/>
         </tns:SignType>
         <tns:Position>
            <xsl:value-of select="./rsd:Position"/>
         </tns:Position>
         <xsl:if test="./rsd:AuthorityEndDate">
            <tns:AuthorityEndDate>
               <xsl:value-of select="./rsd:AuthorityEndDate"/>
            </tns:AuthorityEndDate>
         </xsl:if>
      </tns:Sign>
   </xsl:template>

   <xsl:template match="rsd:BankSubdivision">
      <tns:BankSubdivision>
         <tns:TB>
            <xsl:value-of select="./rsd:TB"/>
         </tns:TB>
         <tns:OSB>
            <xsl:value-of select="./rsd:OSB"/>
         </tns:OSB>
         <tns:VSP>
            <xsl:value-of select="./rsd:VSP"/>
         </tns:VSP>
      </tns:BankSubdivision>
   </xsl:template>

   <xsl:template name="SrvUpdateClientReferenceDataRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvUpdateClientReferenceDataRq">
         <tns:UpdateType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:UpdateType"/>
         </tns:UpdateType>
         <tns:ChangeType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ChangeType"/>
         </tns:ChangeType>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:ChangeSubject">
            <tns:ChangeSubject>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ChangeSubject"/>
            </tns:ChangeSubject>
         </xsl:if>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:VSPCommissionPayment">
            <tns:VSPCommissionPayment>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:VSPCommissionPayment"/>
            </tns:VSPCommissionPayment>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:ClientReferenceData"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
