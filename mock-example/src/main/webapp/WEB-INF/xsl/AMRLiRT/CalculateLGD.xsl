<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:amrct="http://sbrf.ru/NCP/AMRLIRT/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateLGD/Data"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:AMRLiRT="http://sbrf.ru/NCP/ASFO/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="all"/>
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

   <xsl:template match="soap-env:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/*[local-name()='data']"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:comment><xsl:value-of select="namespace-uri(document($dataFileName)/*[1])"/></xsl:comment>
      <xsl:comment><xsl:value-of select="name(document($dataFileName)/*[local-name()='data'])"/></xsl:comment>
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
                  <xsl:with-param name="operation-name" select="string('LgdCalculationResponse')"/>
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
            <xsl:call-template name="LgdCalculationResponse">
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

   <xsl:template match="rsd:collateral">
      <tns:collateral>
         <tns:crmId>
            <xsl:value-of select="./rsd:crmId"/>
         </tns:crmId>
         <tns:collType>
            <xsl:value-of select="./rsd:collType"/>
         </tns:collType>
         <tns:returnRate>
            <xsl:value-of select="./rsd:returnRate"/>
         </tns:returnRate>
         <tns:discountRate>
            <xsl:value-of select="./rsd:discountRate"/>
         </tns:discountRate>
         <tns:collValueEad>
            <xsl:value-of select="./rsd:collValueEad"/>
         </tns:collValueEad>
         <tns:collValueLgd>
            <xsl:value-of select="./rsd:collValueLgd"/>
         </tns:collValueLgd>
      </tns:collateral>
   </xsl:template>

   <xsl:template match="rsd:listOfCollateral">
      <tns:listOfCollateral>
         <xsl:apply-templates select="./rsd:collateral"/>
      </tns:listOfCollateral>
   </xsl:template>

   <xsl:template name="LgdCalculationResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:comment><xsl:value-of select="local-name($data/rsd:response)"/></xsl:comment>
      <xsl:element name="AMRLiRT:LgdCalculationResponse">
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
         <tns:lgdType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgdType"/>
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
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:listOfCollateral"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
