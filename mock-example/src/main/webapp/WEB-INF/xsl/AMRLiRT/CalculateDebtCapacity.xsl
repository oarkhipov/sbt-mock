<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:amrct="http://sbrf.ru/NCP/AMRLIRT/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/Data/"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:AMRLiRT="http://sbrf.ru/NCP/AMRLIRT/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//soap-env:Body/*//*[local-name()='model'][1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/AMRLiRT/xml/CalculateDebtCapacityData.xml'"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="soap-env:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap-env:Envelope">
         <xsl:choose>
            <xsl:when test="soap-env:Header">
               <xsl:copy-of select="soap-env:Header"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:call-template name="NCPHeader">
                  <xsl:with-param name="response">
                     <xsl:choose>
                        <xsl:when test="count(./rsd:response[@name=$linkedTag])=1">
                           <xsl:value-of select="$linkedTag"/>
                        </xsl:when>
                        <xsl:otherwise>default</xsl:otherwise>
                     </xsl:choose>
                  </xsl:with-param>
                  <xsl:with-param name="timestamp" select="$timestamp"/>
                  <xsl:with-param name="id" select="$id"/>
                  <xsl:with-param name="operation-name" select="string('calculateDCRs')"/>
                  <xsl:with-param name="correlation-id" select="$correlation-id"/>
                  <xsl:with-param name="eis-name" select="$eis-name"/>
                  <xsl:with-param name="system-id" select="$system-id"/>
                  <xsl:with-param name="operation-version" select="$operation-version"/>
                  <xsl:with-param name="user-id" select="$user-id"/>
                  <xsl:with-param name="user-name" select="$user-name"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
         <soap-env:Body>
            <xsl:call-template name="DebtCapacityCalculationResponse">
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
         </soap-env:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template match="rsd:addParameter">
      <tns:addParameter>
         <xsl:if test="./rsd:order">
            <tns:order>
               <xsl:value-of select="./rsd:order"/>
            </tns:order>
         </xsl:if>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:addParameter>
   </xsl:template>

   <xsl:template match="rsd:listOfAddParameter">
      <tns:listOfAddParameter>
         <xsl:apply-templates select="./rsd:addParameter"/>
      </tns:listOfAddParameter>
   </xsl:template>

   <xsl:template name="DebtCapacityCalculationResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="AMRLiRT:calculateDCRs">
         <tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <tns:crmId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:crmId"/>
         </tns:crmId>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:rmk">
            <tns:rmk>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmk"/>
            </tns:rmk>
         </xsl:if>
         <tns:debtCapacity>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacity"/>
         </tns:debtCapacity>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:rmkInDealCurrency">
            <tns:rmkInDealCurrency>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkInDealCurrency"/>
            </tns:rmkInDealCurrency>
         </xsl:if>
         <tns:debtCapacityInDealCurrency>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityInDealCurrency"/>
         </tns:debtCapacityInDealCurrency>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:rmkForNextYear">
            <tns:rmkForNextYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:rmkForNextYear"/>
            </tns:rmkForNextYear>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:debtCapacityForNextYear">
            <tns:debtCapacityForNextYear>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:debtCapacityForNextYear"/>
            </tns:debtCapacityForNextYear>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:listOfAddParameter"/>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:amMessage">
            <tns:amMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:amMessage"/>
            </tns:amMessage>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
