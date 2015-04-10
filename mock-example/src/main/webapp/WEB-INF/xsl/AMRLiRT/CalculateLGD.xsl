<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/calculateLGDRs/Data/"
                xmlns:rq="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRq/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:AMRLiRT="http://sbrf.ru/NCP/AMRLIRT/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
   <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='calculateLGDRq' and namespace-uri()='http://sbrf.ru/NCP/AMRLIRT/']/*[local-name()='comment' and namespace-uri()='http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRq/']/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/AMRLiRT/xml/CalculateLGDData.xml'"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
   <xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="soap:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap:Envelope">
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
            <xsl:with-param name="operation-name" select="string('calculateLGDRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="calculateLGDRs">
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
         </soap:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template name="collateral">
      <xsl:param name="crmId"/>
      <xsl:param name="collType"/>
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <tns:collateral>
         <tns:crmId>
            <xsl:value-of select="$crmId"/>
         </tns:crmId>
         <tns:collType>
            <xsl:value-of select="$collType"/>
         </tns:collType>
         <tns:returnRate>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:listOfCollateral/rsd:collateral/rsd:returnRate"/>
         </tns:returnRate>
         <tns:discountRate>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:listOfCollateral/rsd:collateral/rsd:discountRate"/>
         </tns:discountRate>
         <tns:collValueEad>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:listOfCollateral/rsd:collateral/rsd:collValueEad"/>
         </tns:collValueEad>
         <tns:collValueLgd>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:listOfCollateral/rsd:collateral/rsd:collValueLgd"/>
         </tns:collValueLgd>
      </tns:collateral>
   </xsl:template>

   <xsl:template name="listOfCollateral">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <tns:listOfCollateral>
         <xsl:for-each select="soap:Body/AMRLiRT:calculateLGDRq/rq:listOfCollateral/rq:collateral">
            <xsl:call-template name="collateral">
               <xsl:with-param name="crmId" select="rq:crmId"/>
               <xsl:with-param name="collType" select="rq:collType"/>
               <xsl:with-param name="response" select="$response"/>
               <xsl:with-param name="data" select="$data"/>
            </xsl:call-template>
         </xsl:for-each>
      </tns:listOfCollateral>
   </xsl:template>

   <xsl:template name="calculateLGDRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="AMRLiRT:calculateLGDRs">
         <tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <tns:crmId>
            <xsl:value-of select="soap:Body/AMRLiRT:calculateLGDRq/rq:crmId"/>
         </tns:crmId>
         <tns:lgdType>
            <xsl:value-of select="soap:Body/AMRLiRT:calculateLGDRq/rq:lgdType"/>
         </tns:lgdType>
         <tns:lgdDate>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgdDate"/>
         </tns:lgdDate>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:pd">
            <tns:pd>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:pd"/>
            </tns:pd>
         </xsl:if>
         <tns:lgd>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgd"/>
         </tns:lgd>
         <tns:ead>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ead"/>
         </tns:ead>
         <tns:sum>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:sum"/>
         </tns:sum>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:elPercent">
            <tns:elPercent>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:elPercent"/>
            </tns:elPercent>
         </xsl:if>
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:el">
            <tns:el>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:el"/>
            </tns:el>
         </xsl:if>
         <tns:totalValue>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalValue"/>
         </tns:totalValue>
         <tns:totalColValueLgd>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueLgd"/>
         </tns:totalColValueLgd>
         <tns:totalColValueEad>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueEad"/>
         </tns:totalColValueEad>
         <xsl:call-template name="listOfCollateral">
            <xsl:with-param name="response" select="$response"/>
            <xsl:with-param name="data" select="$data"/>
         </xsl:call-template>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
