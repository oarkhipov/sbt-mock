<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:rsd="http://sbrf.ru/mockService/CKPITProductsDepositsNSOReq/Data/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="all"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="/">
      <xsl:variable name="data" select="//rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element xmlns:soap="http://sbrf.ru/NCP/esb/envelope/" name="soap:Envelope">
         <xsl:call-template name="NCPHeader">
            <xsl:with-param name="response">
               <xsl:choose>
                  <xsl:when test="count(./rsd:request[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="timestamp" select="$timestamp"/>
            <xsl:with-param name="id" select="$id"/>
            <xsl:with-param name="operation-name" select="string('CKPITProductsDepositsNSOReq')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="CKPITProductsDepositsNSOReq">
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

   <xsl:template match="rsd:Option">
      <Option>
         <OptionID>
            <xsl:value-of select="./rsd:OptionID"/>
         </OptionID>
         <OptionName>
            <xsl:value-of select="./rsd:OptionName"/>
         </OptionName>
         <OptionValue>
            <xsl:value-of select="./rsd:OptionValue"/>
         </OptionValue>
      </Option>
   </xsl:template>

   <xsl:template match="rsd:Restriction">
      <Restriction>
         <Currency>
            <xsl:value-of select="./rsd:Currency"/>
         </Currency>
         <xsl:if test="./rsd:AmountMin">
            <AmountMin>
               <xsl:value-of select="./rsd:AmountMin"/>
            </AmountMin>
         </xsl:if>
         <xsl:if test="./rsd:AmountMax">
            <AmountMax>
               <xsl:value-of select="./rsd:AmountMax"/>
            </AmountMax>
         </xsl:if>
         <PeriodMin>
            <xsl:value-of select="./rsd:PeriodMin"/>
         </PeriodMin>
         <PeriodMax>
            <xsl:value-of select="./rsd:PeriodMax"/>
         </PeriodMax>
      </Restriction>
   </xsl:template>

   <xsl:template name="CKPITProductsDepositsNSOReq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="CKPITProductsDepositsNSOReq">
            <RqUID>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RqUID"/>
         </RqUID>  
            <RqTm>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RqTm"/>
         </RqTm>   
            <RqAction>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:RqAction"/>
         </RqAction>   
        <IDCKPIT>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:IDCKPIT"/>
         </IDCKPIT>
         <Label>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:Label"/>
         </Label>
         <Name>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:Name"/>
         </Name>
         <ShortName>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ShortName"/>
         </ShortName>
         <InstrumentType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:InstrumentType"/>
         </InstrumentType>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:ValidityStartDate">
            <ValidityStartDate>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ValidityStartDate"/>
            </ValidityStartDate>
         </xsl:if>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:ValidityEndDate">
            <ValidityEndDate>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ValidityEndDate"/>
            </ValidityEndDate>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:Option"/>
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:Restriction"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
