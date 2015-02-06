<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://service.scoring.sberbank.luxoft.com/"
                xmlns:rsd="http://service.scoring.sberbank.luxoft.com/Data/"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:AMRLiRT="http://service.scoring.sberbank.luxoft.com/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name" select="//soap:Body/*//*[local-name()='Model'][1]/text()"/>
   <xsl:param name="dataFileName" select="'../../data/AMRLiRT/xml/calculateData.xml'"/>
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

   <xsl:template match="soap:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap:Envelope">
         <xsl:call-template name="KD4Header">
            <xsl:with-param name="response">
               <xsl:choose>
                  <xsl:when test="count(./rsd:response[@name=$linkedTag])=1">
                     <xsl:value-of select="$linkedTag"/>
                  </xsl:when>
                  <xsl:otherwise>default</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="request-time" select="$request-time"/>
            <xsl:with-param name="message-id" select="$message-id"/>
            <xsl:with-param name="operation-name" select="string('calculateResponse')"/>
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
            <xsl:call-template name="calculateResponse">
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

   <xsl:template match="rsd:calculateResponse">
      <tns:calculateResponse/>
   </xsl:template>

   <xsl:template name="calculateResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:calculateResponse">
         <xsl:if test="$data/rsd:response[@name=$response]/rsd:return">
            <tns:return>
               <tns:ErrorCode>
                  <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ErrorCode"/>
               </tns:ErrorCode>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ErrorMessage">
                  <tns:ErrorMessage>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ErrorMessage"/>
                  </tns:ErrorMessage>
               </xsl:if>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating">
                  <tns:ListOfResultRating>
                     <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating/rsd:ResultRating">
                        <tns:ResultRating>
                           <tns:IsPrimary>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating/rsd:ResultRating/rsd:IsPrimary"/>
                           </tns:IsPrimary>
                           <tns:Name>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating/rsd:ResultRating/rsd:Name"/>
                           </tns:Name>
                           <tns:Value>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating/rsd:ResultRating/rsd:Value"/>
                           </tns:Value>
                           <tns:Type>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfResultRating/rsd:ResultRating/rsd:Type"/>
                           </tns:Type>
                        </tns:ResultRating>
                     </xsl:if>
                  </tns:ListOfResultRating>
               </xsl:if>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfCalculatedFactor">
                  <tns:ListOfCalculatedFactor>
                     <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfCalculatedFactor/rsd:CalculatedFactor">
                        <tns:CalculatedFactor>
                           <tns:Code>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfCalculatedFactor/rsd:CalculatedFactor/rsd:Code"/>
                           </tns:Code>
                           <tns:Name>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfCalculatedFactor/rsd:CalculatedFactor/rsd:Name"/>
                           </tns:Name>
                           <tns:Value>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfCalculatedFactor/rsd:CalculatedFactor/rsd:Value"/>
                           </tns:Value>
                        </tns:CalculatedFactor>
                     </xsl:if>
                  </tns:ListOfCalculatedFactor>
               </xsl:if>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfAddParameter">
                  <tns:ListOfAddParameter>
                     <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfAddParameter/rsd:AddParameter">
                        <tns:AddParameter>
                           <tns:Order>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Order"/>
                           </tns:Order>
                           <tns:Name>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Name"/>
                           </tns:Name>
                           <tns:Value>
                              <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Value"/>
                           </tns:Value>
                        </tns:AddParameter>
                     </xsl:if>
                  </tns:ListOfAddParameter>
               </xsl:if>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:return/rsd:AMMessage">
                  <tns:AMMessage>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:return/rsd:AMMessage"/>
                  </tns:AMMessage>
               </xsl:if>
            </tns:return>
         </xsl:if>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
