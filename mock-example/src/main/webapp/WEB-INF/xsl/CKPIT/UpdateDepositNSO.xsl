<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/SrvCKPITUpdateDepositNSORq/Data/"
                xmlns:CKPIT="http://sbrf.ru/prpc/bbmo/10"
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
            <xsl:with-param name="operation-name" select="string('UpdateDepositNSO')"/>
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
            <xsl:call-template name="UpdateDepositNSO">
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

   <xsl:template match="rsd:SrvCKPITUpdateDepositNSORq">
      <tns:SrvCKPITUpdateDepositNSORq/>
   </xsl:template>

   <xsl:template match="rsd:Option">
      <tns:Option>
         <tns:OptionID>
            <xsl:value-of select="./rsd:OptionID"/>
         </tns:OptionID>
         <tns:OptionName>
            <xsl:value-of select="./rsd:OptionName"/>
         </tns:OptionName>
         <tns:OptionValue>
            <xsl:value-of select="./rsd:OptionValue"/>
         </tns:OptionValue>
      </tns:Option>
   </xsl:template>

   <xsl:template match="rsd:Restriction">
      <tns:Restriction>
         <tns:Currency>
            <xsl:value-of select="./rsd:Currency"/>
         </tns:Currency>
         <xsl:if test="./rsd:AmountMin">
            <tns:AmountMin>
               <xsl:value-of select="./rsd:AmountMin"/>
            </tns:AmountMin>
         </xsl:if>
         <xsl:if test="./rsd:AmountMax">
            <tns:AmountMax>
               <xsl:value-of select="./rsd:AmountMax"/>
            </tns:AmountMax>
         </xsl:if>
         <tns:PeriodMin>
            <xsl:value-of select="./rsd:PeriodMin"/>
         </tns:PeriodMin>
         <tns:PeriodMax>
            <xsl:value-of select="./rsd:PeriodMax"/>
         </tns:PeriodMax>
      </tns:Restriction>
   </xsl:template>

   <xsl:template name="UpdateDepositNSO">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvCKPITUpdateDepositNSORq">
         <tns:IDCKPIT>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:IDCKPIT"/>
         </tns:IDCKPIT>
         <tns:Label>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:Label"/>
         </tns:Label>
         <tns:Name>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:Name"/>
         </tns:Name>
         <tns:ShortName>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ShortName"/>
         </tns:ShortName>
         <tns:InstrumentType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:InstrumentType"/>
         </tns:InstrumentType>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:ValidityStartDate">
            <tns:ValidityStartDate>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ValidityStartDate"/>
            </tns:ValidityStartDate>
         </xsl:if>
         <xsl:if test="$data/rsd:request[@name=$request]/rsd:ValidityEndDate">
            <tns:ValidityEndDate>
               <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ValidityEndDate"/>
            </tns:ValidityEndDate>
         </xsl:if>
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:Option"/>
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:Restriction"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
