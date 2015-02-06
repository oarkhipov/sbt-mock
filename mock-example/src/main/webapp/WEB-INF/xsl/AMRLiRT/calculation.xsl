<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://service.lgd.sberbank.luxoft.com/"
                xmlns:rsd="http://service.lgd.sberbank.luxoft.com/Data/"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:AMRLiRT="http://service.lgd.sberbank.luxoft.com/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//soap:Body/*//*[local-name()='ProductType'][1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/AMRLiRT/xml/calculationData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('calculationResponse')"/>
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
            <xsl:call-template name="calculationResponse">
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

   <xsl:template name="calculationResponse">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="tns:calculationResponse">
			      <tns:ErrorCode>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ErrorCode"/>
         </tns:ErrorCode>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorMessage">
            <tns:ErrorMessage>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ErrorMessage"/>
            </tns:ErrorMessage>
         </xsl:if>
			      <tns:CRMId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:CRMId"/>
         </tns:CRMId>
			      <tns:LGDType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:LGDType"/>
         </tns:LGDType>
			      <tns:LGDDate>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:LGDDate"/>
         </tns:LGDDate>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:PD">
            <tns:PD>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:PD"/>
            </tns:PD>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:LGD">
            <tns:LGD>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:LGD"/>
            </tns:LGD>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:EAD">
            <tns:EAD>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:EAD"/>
            </tns:EAD>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:Sum">
            <tns:Sum>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:Sum"/>
            </tns:Sum>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:ELPercent">
            <tns:ELPercent>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ELPercent"/>
            </tns:ELPercent>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:EL">
            <tns:EL>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:EL"/>
            </tns:EL>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:TotalValue">
            <tns:TotalValue>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:TotalValue"/>
            </tns:TotalValue>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:TotalCollValueLGD">
            <tns:TotalCollValueLGD>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:TotalCollValueLGD"/>
            </tns:TotalCollValueLGD>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:TotalCollValueEAD">
            <tns:TotalCollValueEAD>
               <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:TotalCollValueEAD"/>
            </tns:TotalCollValueEAD>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:ListOfCollateral">
            <tns:ListOfCollateral>
               <tns:Collateral>
                  <tns:CRMId>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:CRMId"/>
                  </tns:CRMId>
                  <tns:CollType>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:CollType"/>
                  </tns:CollType>
                  <tns:ReturnRate>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:ReturnRate"/>
                  </tns:ReturnRate>
                  <tns:DiscountRate>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:DiscountRate"/>
                  </tns:DiscountRate>
                  <tns:CollValueEAD>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:CollValueEAD"/>
                  </tns:CollValueEAD>
                  <tns:CollValueLGD>
                     <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfCollateral/rsd:Collateral/rsd:CollValueLGD"/>
                  </tns:CollValueLGD>
               </tns:Collateral>
            </tns:ListOfCollateral>
         </xsl:if>
			      <xsl:if test="$data/rsd:response[@name=$response]/rsd:ListOfAddParameter">
            <tns:ListOfAddParameter>
               <xsl:if test="$data/rsd:response[@name=$response]/rsd:ListOfAddParameter/rsd:AddParameter">
                  <tns:AddParameter>
                     <tns:Order>
                        <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Order"/>
                     </tns:Order>
                     <tns:Name>
                        <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Name"/>
                     </tns:Name>
                     <tns:Value>
                        <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:ListOfAddParameter/rsd:AddParameter/rsd:Value"/>
                     </tns:Value>
                  </tns:AddParameter>
               </xsl:if>
            </tns:ListOfAddParameter>
         </xsl:if>
		    </xsl:element>
   </xsl:template>
</xsl:stylesheet>
