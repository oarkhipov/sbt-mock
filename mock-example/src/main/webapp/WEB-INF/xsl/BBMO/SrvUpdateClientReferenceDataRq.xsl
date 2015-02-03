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
      <tns:ClientReferenceData><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:ClientInformation"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:NaturalPerson"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:LegalPersonBankAccountAgreement"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:SignPatternCard"/>
      </tns:ClientReferenceData>
   </xsl:template>

   <xsl:template match="rsd:ClientInformation">
      <tns:ClientInformation><!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:Identifiers"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:SpecialData"/>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:MainDetails"/>
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

   <xsl:template match="rsd:NaturalPerson">
      <tns:NaturalPerson><!--match="xsd:element[@name]"--><tns:RKOCode>
            <xsl:value-of select="./rsd:RKOCode"/>
         </tns:RKOCode>
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
         <!--match="xsd:element[@name]"--><tns:LastNameInGenitiveCase>
            <xsl:value-of select="./rsd:LastNameInGenitiveCase"/>
         </tns:LastNameInGenitiveCase>
         <!--match="xsd:element[@name]"--><tns:FirstNameInGenitiveCase>
            <xsl:value-of select="./rsd:FirstNameInGenitiveCase"/>
         </tns:FirstNameInGenitiveCase>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:MiddleNameInGenitiveCase">
            <tns:MiddleNameInGenitiveCase>
               <xsl:value-of select="./rsd:MiddleNameInGenitiveCase"/>
            </tns:MiddleNameInGenitiveCase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:LastNameInEnglish>
            <xsl:value-of select="./rsd:LastNameInEnglish"/>
         </tns:LastNameInEnglish>
         <!--match="xsd:element[@name]"--><tns:FirstNameInEnglish>
            <xsl:value-of select="./rsd:FirstNameInEnglish"/>
         </tns:FirstNameInEnglish>
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
         <!--match="xsd:element[@name]"--><tns:Resident>
            <xsl:value-of select="./rsd:Resident"/>
         </tns:Resident>
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
         <!--match="xsd:element[./xsd:complexType/xsd:sequence]"--><tns:IdentityDocument><!--match="xsd:element[@name]"--><tns:DocumentType>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentType"/>
            </tns:DocumentType>
            <!--match="xsd:element[@name]"--><tns:DocumentStatus>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentStatus"/>
            </tns:DocumentStatus>
            <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentityDocument/rsd:DocumentSeries">
               <tns:DocumentSeries>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentSeries"/>
               </tns:DocumentSeries>
            </xsl:if>
            <!--match="xsd:element[@name]"--><tns:DocumentNumber>
               <xsl:value-of select="./rsd:IdentityDocument/rsd:DocumentNumber"/>
            </tns:DocumentNumber>
            <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentityDocument/rsd:IssueDate">
               <tns:IssueDate>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueDate"/>
               </tns:IssueDate>
            </xsl:if>
            <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentityDocument/rsd:IssueAuthority">
               <tns:IssueAuthority>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueAuthority"/>
               </tns:IssueAuthority>
            </xsl:if>
            <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentityDocument/rsd:IssueAuthorityCode">
               <tns:IssueAuthorityCode>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:IssueAuthorityCode"/>
               </tns:IssueAuthorityCode>
            </xsl:if>
            <!--match="xsd:element[@name]"--><xsl:if test="./rsd:IdentityDocument/rsd:ExpirationDate">
               <tns:ExpirationDate>
                  <xsl:value-of select="./rsd:IdentityDocument/rsd:ExpirationDate"/>
               </tns:ExpirationDate>
            </xsl:if>
         </tns:IdentityDocument>
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
      </tns:NaturalPerson>
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
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FilePresence1">
            <tns:FilePresence1>
               <xsl:value-of select="./rsd:FilePresence1"/>
            </tns:FilePresence1>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:FilePresence2">
            <tns:FilePresence2>
               <xsl:value-of select="./rsd:FilePresence2"/>
            </tns:FilePresence2>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ArrestByAccountPresence">
            <tns:ArrestByAccountPresence>
               <xsl:value-of select="./rsd:ArrestByAccountPresence"/>
            </tns:ArrestByAccountPresence>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:ArrestedSum">
            <tns:ArrestedSum>
               <xsl:value-of select="./rsd:ArrestedSum"/>
            </tns:ArrestedSum>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:RKOAgreementDetails"/>
      </tns:LegalPersonBankAccountAgreement>
   </xsl:template>

   <xsl:template match="rsd:RKOAgreementDetails">
      <tns:RKOAgreementDetails><!--match="xsd:element[@name]"--><xsl:if test="./rsd:PercentCalculationAlgorithm">
            <tns:PercentCalculationAlgorithm>
               <xsl:value-of select="./rsd:PercentCalculationAlgorithm"/>
            </tns:PercentCalculationAlgorithm>
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
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AccountUserType">
            <tns:AccountUserType>
               <xsl:value-of select="./rsd:AccountUserType"/>
            </tns:AccountUserType>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AgreementUserType">
            <tns:AgreementUserType>
               <xsl:value-of select="./rsd:AgreementUserType"/>
            </tns:AgreementUserType>
         </xsl:if>
      </tns:RKOAgreementDetails>
   </xsl:template>

   <xsl:template match="rsd:SignPatternCard">
      <tns:SignPatternCard><!--match="xsd:element[@name]"--><tns:ClientId>
            <xsl:value-of select="./rsd:ClientId"/>
         </tns:ClientId>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:BankSubdivision"/>
         <!--match="xsd:element[@name]"--><tns:AgreementNumber>
            <xsl:value-of select="./rsd:AgreementNumber"/>
         </tns:AgreementNumber>
         <!--match="xsd:element[@name]"--><tns:AccountNumber>
            <xsl:value-of select="./rsd:AccountNumber"/>
         </tns:AccountNumber>
         <!--match="xsd:element[@name]"--><tns:CardType>
            <xsl:value-of select="./rsd:CardType"/>
         </tns:CardType>
         <!--match="xsd:element[@name]"--><tns:RegistrationDate>
            <xsl:value-of select="./rsd:RegistrationDate"/>
         </tns:RegistrationDate>
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
         <!--match="xsd:element[@name]"--><tns:CardRegistartionMethod>
            <xsl:value-of select="./rsd:CardRegistartionMethod"/>
         </tns:CardRegistartionMethod>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="./rsd:Sign"/>
      </tns:SignPatternCard>
   </xsl:template>

   <xsl:template match="rsd:Sign">
      <tns:Sign><!--match="xsd:element[@name]"--><tns:RKONaturalPersonCode>
            <xsl:value-of select="./rsd:RKONaturalPersonCode"/>
         </tns:RKONaturalPersonCode>
         <!--match="xsd:element[@name]"--><tns:SignType>
            <xsl:value-of select="./rsd:SignType"/>
         </tns:SignType>
         <!--match="xsd:element[@name]"--><tns:Position>
            <xsl:value-of select="./rsd:Position"/>
         </tns:Position>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:AuthorityEndDate">
            <tns:AuthorityEndDate>
               <xsl:value-of select="./rsd:AuthorityEndDate"/>
            </tns:AuthorityEndDate>
         </xsl:if>
      </tns:Sign>
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

   <xsl:template name="SrvUpdateClientReferenceDataRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvUpdateClientReferenceDataRq"><!--match="xsd:element[@name]"--><tns:UpdateType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:UpdateType"/>
         </tns:UpdateType>
         <!--match="xsd:element[@name]"--><tns:ChangeType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ChangeType"/>
         </tns:ChangeType>
         <!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:ChangeSubject">
            <tns:ChangeSubject>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ChangeSubject"/>
            </tns:ChangeSubject>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="$data/rsd:request[@name=$request]/rsd:VSPCommissionPayment">
            <tns:VSPCommissionPayment>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:VSPCommissionPayment"/>
            </tns:VSPCommissionPayment>
         </xsl:if>
         <!--match="xsd:element[@ref]"--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:ClientReferenceData"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
