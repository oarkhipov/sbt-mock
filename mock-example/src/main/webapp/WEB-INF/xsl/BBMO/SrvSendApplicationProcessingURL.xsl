<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/prpc/bbmo/10"
                xmlns:rsd="http://sbrf.ru/prpc/bbmo/10/SrvSendApplicationProcessingURLRq/Data/"
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
                  <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="request-time" select="$request-time"/>
            <xsl:with-param name="message-id" select="$message-id"/>
            <xsl:with-param name="operation-name"
                            select="string('SrvSendApplicationProcessingURL')"/>
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
            <xsl:call-template name="SrvSendApplicationProcessingURL">
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

   <xsl:template match="rsd:SrvSendApplicationProcessingURLRq">
      <tns:SrvSendApplicationProcessingURLRq/>
   </xsl:template>

   <xsl:template match="rsd:ExtensionData">
      <ct:ExtensionData xmlns:ct="http://sbrf.ru/prpc/bbmo/commonTypes/10"/>
   </xsl:template>

   <xsl:template name="SrvSendApplicationProcessingURL">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:SrvSendApplicationProcessingURLRq">
         <xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:ExtensionData"/>
         <tns:ApplicationId>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:ApplicationId"/>
         </tns:ApplicationId>
         <tns:URLType>
            <xsl:value-of select="$data/rsd:request[@name=$request]/rsd:URLType"/>
         </tns:URLType>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
