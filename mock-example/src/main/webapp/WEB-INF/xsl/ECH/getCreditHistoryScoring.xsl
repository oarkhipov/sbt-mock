<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://www.sbrf.ru/CreditHistoryScoring"
                xmlns:rsd="http://www.sbrf.ru/CreditHistoryScoringData/"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ECH="http://sbrf.ru/prpc/ech/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//soap:Body/*//*[local-name()='checkInternal'][1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/ECH/xml/getCreditHistoryScoringData.xml'"/>
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
            <xsl:with-param name="operation-name" select="string('SrvGetCreditHistoryScoringRs')"/>
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
            <xsl:call-template name="SrvGetCreditHistoryScoringRs">
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

   <xsl:template match="rsd:dealScoringResult">
      <tns:dealScoringResult>
         <tns:sid>
            <xsl:value-of select="./rsd:sid"/>
         </tns:sid>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:value>
            <xsl:value-of select="./rsd:value"/>
         </tns:value>
      </tns:dealScoringResult>
   </xsl:template>

   <xsl:template match="rsd:blockDataASSD">
      <tns:blockDataASSD>
         <xsl:if test="./rsd:_dcas_ResidualDebtAndArrear">
            <tns:_dcas_ResidualDebtAndArrear>
               <xsl:value-of select="./rsd:_dcas_ResidualDebtAndArrear"/>
            </tns:_dcas_ResidualDebtAndArrear>
         </xsl:if>
         <tns:productType>
            <xsl:value-of select="./rsd:productType"/>
         </tns:productType>
         <tns:productCode>
            <xsl:value-of select="./rsd:productCode"/>
         </tns:productCode>
         <tns:subProductCode>
            <xsl:value-of select="./rsd:subProductCode"/>
         </tns:subProductCode>
      </tns:blockDataASSD>
   </xsl:template>

   <xsl:template match="rsd:liabilityArrearsBlock">
      <tns:liabilityArrearsBlock>
         <tns:yearNumber>
            <xsl:value-of select="./rsd:yearNumber"/>
         </tns:yearNumber>
         <tns:DtYearBegin>
            <xsl:value-of select="./rsd:DtYearBegin"/>
         </tns:DtYearBegin>
         <tns:DtYearEnd>
            <xsl:value-of select="./rsd:DtYearEnd"/>
         </tns:DtYearEnd>
         <xsl:if test="./rsd:arrearsCount_1_5">
            <tns:arrearsCount_1_5>
               <xsl:value-of select="./rsd:arrearsCount_1_5"/>
            </tns:arrearsCount_1_5>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_6_30">
            <tns:arrearsCount_6_30>
               <xsl:value-of select="./rsd:arrearsCount_6_30"/>
            </tns:arrearsCount_6_30>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_1_30">
            <tns:arrearsCount_1_30>
               <xsl:value-of select="./rsd:arrearsCount_1_30"/>
            </tns:arrearsCount_1_30>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_31_60">
            <tns:arrearsCount_31_60>
               <xsl:value-of select="./rsd:arrearsCount_31_60"/>
            </tns:arrearsCount_31_60>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_61_90">
            <tns:arrearsCount_61_90>
               <xsl:value-of select="./rsd:arrearsCount_61_90"/>
            </tns:arrearsCount_61_90>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_91_120">
            <tns:arrearsCount_91_120>
               <xsl:value-of select="./rsd:arrearsCount_91_120"/>
            </tns:arrearsCount_91_120>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_121_150">
            <tns:arrearsCount_121_150>
               <xsl:value-of select="./rsd:arrearsCount_121_150"/>
            </tns:arrearsCount_121_150>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_151_180">
            <tns:arrearsCount_151_180>
               <xsl:value-of select="./rsd:arrearsCount_151_180"/>
            </tns:arrearsCount_151_180>
         </xsl:if>
         <xsl:if test="./rsd:arrearsCount_180">
            <tns:arrearsCount_180>
               <xsl:value-of select="./rsd:arrearsCount_180"/>
            </tns:arrearsCount_180>
         </xsl:if>
         <tns:nMaxDelayPayments>
            <xsl:value-of select="./rsd:nMaxDelayPayments"/>
         </tns:nMaxDelayPayments>
         <tns:arrearsCount_PerYear>
            <xsl:value-of select="./rsd:arrearsCount_PerYear"/>
         </tns:arrearsCount_PerYear>
      </tns:liabilityArrearsBlock>
   </xsl:template>

   <xsl:template match="rsd:histories">
      <tns:histories>
         <xsl:apply-templates select="./rsd:creditHistory"/>
      </tns:histories>
   </xsl:template>

   <xsl:template match="rsd:creditHistory">
      <tns:creditHistory>
         <tns:sourceCB>
            <xsl:value-of select="./rsd:sourceCB"/>
         </tns:sourceCB>
         <tns:BankSign>
            <xsl:value-of select="./rsd:BankSign"/>
         </tns:BankSign>
         <tns:nRelationshipDuration>
            <xsl:value-of select="./rsd:nRelationshipDuration"/>
         </tns:nRelationshipDuration>
         <tns:lastRecordUpdate>
            <xsl:value-of select="./rsd:lastRecordUpdate"/>
         </tns:lastRecordUpdate>
         <tns:liabilityStatus>
            <xsl:value-of select="./rsd:liabilityStatus"/>
         </tns:liabilityStatus>
         <tns:liabilityType>
            <xsl:value-of select="./rsd:liabilityType"/>
         </tns:liabilityType>
         <xsl:if test="./rsd:baseLiabilityType">
            <tns:baseLiabilityType>
               <xsl:value-of select="./rsd:baseLiabilityType"/>
            </tns:baseLiabilityType>
         </xsl:if>
         <xsl:if test="./rsd:relationshipCode">
            <tns:relationshipCode>
               <xsl:value-of select="./rsd:relationshipCode"/>
            </tns:relationshipCode>
         </xsl:if>
         <xsl:if test="./rsd:amountInitial">
            <tns:amountInitial>
               <xsl:value-of select="./rsd:amountInitial"/>
            </tns:amountInitial>
         </xsl:if>
         <xsl:if test="./rsd:interestRate">
            <tns:interestRate>
               <xsl:value-of select="./rsd:interestRate"/>
            </tns:interestRate>
         </xsl:if>
         <xsl:if test="./rsd:residualDebt">
            <tns:residualDebt>
               <xsl:value-of select="./rsd:residualDebt"/>
            </tns:residualDebt>
         </xsl:if>
         <xsl:if test="./rsd:liabilityCurrency">
            <tns:liabilityCurrency>
               <xsl:value-of select="./rsd:liabilityCurrency"/>
            </tns:liabilityCurrency>
         </xsl:if>
         <xsl:if test="./rsd:liabilityNo">
            <tns:liabilityNo>
               <xsl:value-of select="./rsd:liabilityNo"/>
            </tns:liabilityNo>
         </xsl:if>
         <tns:dateStart>
            <xsl:value-of select="./rsd:dateStart"/>
         </tns:dateStart>
         <tns:dateRedemptionScheduled>
            <xsl:value-of select="./rsd:dateRedemptionScheduled"/>
         </tns:dateRedemptionScheduled>
         <xsl:if test="./rsd:dateRedemptionActual">
            <tns:dateRedemptionActual>
               <xsl:value-of select="./rsd:dateRedemptionActual"/>
            </tns:dateRedemptionActual>
         </xsl:if>
         <xsl:if test="./rsd:repaymentMethod">
            <tns:repaymentMethod>
               <xsl:value-of select="./rsd:repaymentMethod"/>
            </tns:repaymentMethod>
         </xsl:if>
         <xsl:if test="./rsd:currentArrearAmount">
            <tns:currentArrearAmount>
               <xsl:value-of select="./rsd:currentArrearAmount"/>
            </tns:currentArrearAmount>
         </xsl:if>
         <tns:isRestructured>
            <xsl:value-of select="./rsd:isRestructured"/>
         </tns:isRestructured>
         <xsl:if test="./rsd:cred_sum_limit">
            <tns:cred_sum_limit>
               <xsl:value-of select="./rsd:cred_sum_limit"/>
            </tns:cred_sum_limit>
         </xsl:if>
         <xsl:if test="./rsd:cred_prolong">
            <tns:cred_prolong>
               <xsl:value-of select="./rsd:cred_prolong"/>
            </tns:cred_prolong>
         </xsl:if>
         <xsl:if test="./rsd:credMaxOverdue">
            <tns:credMaxOverdue>
               <xsl:value-of select="./rsd:credMaxOverdue"/>
            </tns:credMaxOverdue>
         </xsl:if>
         <xsl:if test="./rsd:kodTb">
            <tns:kodTb>
               <xsl:value-of select="./rsd:kodTb"/>
            </tns:kodTb>
         </xsl:if>
         <xsl:if test="./rsd:nameTb">
            <tns:nameTb>
               <xsl:value-of select="./rsd:nameTb"/>
            </tns:nameTb>
         </xsl:if>
         <tns:liabilityArrearsBlockCount>
            <xsl:value-of select="./rsd:liabilityArrearsBlockCount"/>
         </tns:liabilityArrearsBlockCount>
         <xsl:apply-templates select="./rsd:blockDataASSD"/>
         <xsl:apply-templates select="./rsd:liabilityArrearsBlock"/>
      </tns:creditHistory>
   </xsl:template>

   <xsl:template match="rsd:response">
      <tns:response>
         <xsl:apply-templates select="./rsd:dealResult"/>
         <xsl:apply-templates select="./rsd:participantResults"/>
      </tns:response>
   </xsl:template>

   <xsl:template match="rsd:dealResult">
      <tns:dealResult>
         <xsl:apply-templates select="./rsd:systemErrors"/>
         <xsl:apply-templates select="./rsd:dealScoringResult"/>
         <tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <xsl:if test="./rsd:strategyCodes">
            <tns:strategyCodes>
               <xsl:value-of select="./rsd:strategyCodes"/>
            </tns:strategyCodes>
         </xsl:if>
         <xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
      </tns:dealResult>
   </xsl:template>

   <xsl:template match="rsd:systemErrors">
      <tns:systemErrors>
         <xsl:apply-templates select="./rsd:error"/>
      </tns:systemErrors>
   </xsl:template>

   <xsl:template match="rsd:error">
      <tns:error>
         <xsl:if test="./rsd:code">
            <tns:code>
               <xsl:value-of select="./rsd:code"/>
            </tns:code>
         </xsl:if>
         <xsl:if test="./rsd:message">
            <tns:message>
               <xsl:value-of select="./rsd:message"/>
            </tns:message>
         </xsl:if>
         <xsl:if test="./rsd:cause">
            <tns:cause>
               <xsl:value-of select="./rsd:cause"/>
            </tns:cause>
         </xsl:if>
         <xsl:if test="./rsd:participantIndex">
            <tns:participantIndex>
               <xsl:value-of select="./rsd:participantIndex"/>
            </tns:participantIndex>
         </xsl:if>
      </tns:error>
   </xsl:template>

   <xsl:template match="rsd:participantResults">
      <tns:participantResults>
         <xsl:apply-templates select="./rsd:participantResult"/>
      </tns:participantResults>
   </xsl:template>

   <xsl:template match="rsd:participantScoringResult">
      <tns:participantScoringResult>
         <xsl:if test="./rsd:internalScoringGrade">
            <tns:internalScoringGrade>
               <xsl:value-of select="./rsd:internalScoringGrade"/>
            </tns:internalScoringGrade>
         </xsl:if>
         <xsl:if test="./rsd:externalScoringGrade">
            <tns:externalScoringGrade>
               <xsl:value-of select="./rsd:externalScoringGrade"/>
            </tns:externalScoringGrade>
         </xsl:if>
         <xsl:if test="./rsd:unifiedScoringGrade">
            <tns:unifiedScoringGrade>
               <xsl:value-of select="./rsd:unifiedScoringGrade"/>
            </tns:unifiedScoringGrade>
         </xsl:if>
         <xsl:if test="./rsd:strategyCodes">
            <tns:strategyCodes>
               <xsl:value-of select="./rsd:strategyCodes"/>
            </tns:strategyCodes>
         </xsl:if>
         <xsl:if test="./rsd:comments">
            <tns:comments>
               <xsl:value-of select="./rsd:comments"/>
            </tns:comments>
         </xsl:if>
      </tns:participantScoringResult>
   </xsl:template>

   <xsl:template match="rsd:participantResult">
      <tns:participantResult>
         <tns:participantIndex>
            <xsl:value-of select="./rsd:participantIndex"/>
         </tns:participantIndex>
         <tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <xsl:apply-templates select="./rsd:participantScoringResult"/>
         <xsl:apply-templates select="./rsd:histories"/>
      </tns:participantResult>
   </xsl:template>

   <xsl:template name="SrvGetCreditHistoryScoringRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="ECH:SrvGetCreditHistoryScoringRs">
            <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:dealResult"/>
            <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:participantResults"/>
        </xsl:element>
   </xsl:template>
</xsl:stylesheet>
