<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/"
                xmlns:rsd="http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/calculateLGDRs/Data/"
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/:CollateralLGDResponse--><xsl:template match="rsd:collateral">
      <tns:collateral><!--xsd:element - Inside--><tns:crmId>
            <xsl:value-of select="./rsd:crmId"/>
         </tns:crmId>
         <!--xsd:element - Inside--><tns:collType>
            <xsl:value-of select="./rsd:collType"/>
         </tns:collType>
         <!--xsd:element - Inside--><tns:returnRate>
            <xsl:value-of select="./rsd:returnRate"/>
         </tns:returnRate>
         <!--xsd:element - Inside--><tns:discountRate>
            <xsl:value-of select="./rsd:discountRate"/>
         </tns:discountRate>
         <!--xsd:element - Inside--><tns:collValueEad>
            <xsl:value-of select="./rsd:collValueEad"/>
         </tns:collValueEad>
         <!--xsd:element - Inside--><tns:collValueLgd>
            <xsl:value-of select="./rsd:collValueLgd"/>
         </tns:collValueLgd>
      </tns:collateral>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/:ListOfCollateral--><xsl:template match="rsd:listOfCollateral">
      <tns:listOfCollateral><!-- xsd:element[$typesList] - Inside CollateralLGDResponse--><xsl:apply-templates select="./rsd:collateral"/>
      </tns:listOfCollateral>
   </xsl:template>

   <!--xsd:complexType - template :LgdCalculationResponse--><!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/AMRLIRT/CalculateLGDRs/-http://sbrf.ru/NCP/AMRLIRT/--><xsl:template name="calculateLGDRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="AMRLiRT:calculateLGDRs"><!--xsd:element - Inside--><tns:errorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
         </tns:errorCode>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
         <!--xsd:element - Inside--><tns:crmId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:crmId"/>
         </tns:crmId>
         <!--xsd:element - Inside--><tns:lgdType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgdType"/>
         </tns:lgdType>
         <!--xsd:element - Inside--><tns:lgdDate>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgdDate"/>
         </tns:lgdDate>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:pd">
            <tns:pd>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:pd"/>
            </tns:pd>
         </xsl:if>
         <!--xsd:element - Inside--><tns:lgd>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:lgd"/>
         </tns:lgd>
         <!--xsd:element - Inside--><tns:ead>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ead"/>
         </tns:ead>
         <!--xsd:element - Inside--><tns:sum>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:sum"/>
         </tns:sum>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:elPercent">
            <tns:elPercent>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:elPercent"/>
            </tns:elPercent>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="$data/rsd:response[@name=$response]/rsd:el">
            <tns:el>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:el"/>
            </tns:el>
         </xsl:if>
         <!--xsd:element - Inside--><tns:totalValue>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalValue"/>
         </tns:totalValue>
         <!--xsd:element - Inside--><tns:totalColValueLgd>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueLgd"/>
         </tns:totalColValueLgd>
         <!--xsd:element - Inside--><tns:totalColValueEad>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:totalColValueEad"/>
         </tns:totalColValueEad>
         <!-- xsd:element[$typesList] - Inside ListOfCollateral--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:listOfCollateral"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
