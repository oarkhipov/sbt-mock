<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/SrvSendApplicationForCBDFProcessingRq/Data/"
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
            <xsl:with-param name="operation-name"
                            select="string('SrvSendApplicationForCBDFProcessingRq')"/>
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
            <xsl:call-template name="SrvSendApplicationForCBDFProcessingRq">
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

   <xsl:template match="rsd:SrvSendApplicationForCBDFProcessingRq">
      <tns:SrvSendApplicationForCBDFProcessingRq/>
   </xsl:template>

   <xsl:template match="rsd:NaturalPersonCompesationApplication">
      <tns:NaturalPersonCompesationApplication>
         <tns:ApplicationId>
            <xsl:value-of select="./rsd:ApplicationId"/>
         </tns:ApplicationId>
         <tns:DocumentPackageType>
            <xsl:value-of select="./rsd:DocumentPackageType"/>
         </tns:DocumentPackageType>
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
         <xsl:if test="./rsd:IdentificationDocumentType">
            <tns:IdentificationDocumentType>
               <xsl:value-of select="./rsd:IdentificationDocumentType"/>
            </tns:IdentificationDocumentType>
         </xsl:if>
         <xsl:if test="./rsd:IdentificationDocumentSeries">
            <tns:IdentificationDocumentSeries>
               <xsl:value-of select="./rsd:IdentificationDocumentSeries"/>
            </tns:IdentificationDocumentSeries>
         </xsl:if>
         <xsl:if test="./rsd:IdentificationDocumentNumber">
            <tns:IdentificationDocumentNumber>
               <xsl:value-of select="./rsd:IdentificationDocumentNumber"/>
            </tns:IdentificationDocumentNumber>
         </xsl:if>
         <tns:BirthDate>
            <xsl:value-of select="./rsd:BirthDate"/>
         </tns:BirthDate>
         <xsl:apply-templates select="./rsd:BankSubdivision"/>
         <tns:IssueDate>
            <xsl:value-of select="./rsd:IssueDate"/>
         </tns:IssueDate>
         <xsl:if test="./rsd:PackageId">
            <tns:PackageId>
               <xsl:value-of select="./rsd:PackageId"/>
            </tns:PackageId>
         </xsl:if>
         <xsl:if test="./rsd:BarCode">
            <tns:BarCode>
               <xsl:value-of select="./rsd:BarCode"/>
            </tns:BarCode>
         </xsl:if>
      </tns:NaturalPersonCompesationApplication>
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

   <xsl:template name="SrvSendApplicationForCBDFProcessingRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvSendApplicationForCBDFProcessingRq">
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:NaturalPersonCompesationApplication"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
