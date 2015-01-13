<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:crmct="http://sbrf.ru/NCP/CRM/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.08/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.08/Data/"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
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
      <xsl:element name="soap-env:Envelope">
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
            <xsl:with-param name="operation-name" select="string('UpdateDealRq')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap-env:Body>
            <xsl:call-template name="UpdateDealRq">
               <xsl:with-param name="data" select="$data"/>
               <xsl:with-param name="response">
                  <xsl:choose>
                     <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1">
                        <xsl:value-of select="$linkedTag"/>
                     </xsl:when>
                     <xsl:otherwise>default</xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </soap-env:Body>
      </xsl:element>
   </xsl:template>

   <xsl:template match="rsd:dealTeamMembers">
      <tns:dealTeamMembers>
         <tns:memberId>
            <xsl:value-of select="./rsd:memberId"/>
         </tns:memberId>
         <tns:memberName>
            <xsl:value-of select="./rsd:memberName"/>
         </tns:memberName>
         <tns:mamberEmail>
            <xsl:value-of select="./rsd:mamberEmail"/>
         </tns:mamberEmail>
         <tns:mamberRole>
            <xsl:value-of select="./rsd:mamberRole"/>
         </tns:mamberRole>
      </tns:dealTeamMembers>
   </xsl:template>

   <xsl:template match="rsd:financingInfo">
      <tns:financingInfo>
         <tns:interestRate>
            <xsl:value-of select="./rsd:interestRate"/>
         </tns:interestRate>
         <tns:interestRateType>
            <xsl:value-of select="./rsd:interestRateType"/>
         </tns:interestRateType>
         <tns:interestRateOrder>
            <xsl:value-of select="./rsd:interestRateOrder"/>
         </tns:interestRateOrder>
         <tns:minRate>
            <xsl:value-of select="./rsd:minRate"/>
         </tns:minRate>
         <tns:currencyOfFunding>
            <xsl:value-of select="./rsd:currencyOfFunding"/>
         </tns:currencyOfFunding>
         <tns:principal>
            <xsl:value-of select="./rsd:principal"/>
         </tns:principal>
      </tns:financingInfo>
   </xsl:template>

   <xsl:template match="rsd:project">
      <tns:project>
         <xsl:if test="./rsd:projectName">
            <tns:projectName>
               <xsl:value-of select="./rsd:projectName"/>
            </tns:projectName>
         </xsl:if>
         <xsl:if test="./rsd:indicatorsOfConstruction">
            <tns:indicatorsOfConstruction>
               <xsl:value-of select="./rsd:indicatorsOfConstruction"/>
            </tns:indicatorsOfConstruction>
         </xsl:if>
         <xsl:if test="./rsd:parcel">
            <tns:parcel>
               <xsl:value-of select="./rsd:parcel"/>
            </tns:parcel>
         </xsl:if>
         <xsl:if test="./rsd:documentsPresence">
            <tns:documentsPresence>
               <xsl:value-of select="./rsd:documentsPresence"/>
            </tns:documentsPresence>
         </xsl:if>
         <xsl:if test="./rsd:equityVolume">
            <tns:equityVolume>
               <xsl:value-of select="./rsd:equityVolume"/>
            </tns:equityVolume>
         </xsl:if>
         <xsl:if test="./rsd:repaymentSpring">
            <tns:repaymentSpring>
               <xsl:value-of select="./rsd:repaymentSpring"/>
            </tns:repaymentSpring>
         </xsl:if>
         <xsl:if test="./rsd:fundingDirection">
            <tns:fundingDirection>
               <xsl:value-of select="./rsd:fundingDirection"/>
            </tns:fundingDirection>
         </xsl:if>
         <xsl:if test="./rsd:sameIndustryAsBorrower">
            <tns:sameIndustryAsBorrower>
               <xsl:value-of select="./rsd:sameIndustryAsBorrower"/>
            </tns:sameIndustryAsBorrower>
         </xsl:if>
      </tns:project>
   </xsl:template>

   <xsl:template match="rsd:repaymentScheduleDetails">
      <tns:repaymentScheduleDetails>
         <tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:repaymentScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:selectionScheduleDetails">
      <tns:selectionScheduleDetails>
         <tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:selectionScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:changesScheduleDetails">
      <tns:changesScheduleDetails>
         <tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:changesScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:losses">
      <tns:losses>
         <xsl:if test="./rsd:percentLGD">
            <tns:percentLGD>
               <xsl:value-of select="./rsd:percentLGD"/>
            </tns:percentLGD>
         </xsl:if>
         <xsl:if test="./rsd:countEAD">
            <tns:countEAD>
               <xsl:value-of select="./rsd:countEAD"/>
            </tns:countEAD>
         </xsl:if>
         <tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <xsl:if test="./rsd:reportDateAmount">
            <tns:reportDateAmount>
               <xsl:value-of select="./rsd:reportDateAmount"/>
            </tns:reportDateAmount>
         </xsl:if>
         <xsl:if test="./rsd:lossELpercent">
            <tns:lossELpercent>
               <xsl:value-of select="./rsd:lossELpercent"/>
            </tns:lossELpercent>
         </xsl:if>
         <xsl:if test="./rsd:lossELcount">
            <tns:lossELcount>
               <xsl:value-of select="./rsd:lossELcount"/>
            </tns:lossELcount>
         </xsl:if>
         <xsl:if test="./rsd:typeLGD">
            <tns:typeLGD>
               <xsl:value-of select="./rsd:typeLGD"/>
            </tns:typeLGD>
         </xsl:if>
         <xsl:if test="./rsd:statusLGD">
            <tns:statusLGD>
               <xsl:value-of select="./rsd:statusLGD"/>
            </tns:statusLGD>
         </xsl:if>
         <xsl:if test="./rsd:dateLGDcalculated">
            <tns:dateLGDcalculated>
               <xsl:value-of select="./rsd:dateLGDcalculated"/>
            </tns:dateLGDcalculated>
         </xsl:if>
         <xsl:if test="./rsd:finalizationDate">
            <tns:finalizationDate>
               <xsl:value-of select="./rsd:finalizationDate"/>
            </tns:finalizationDate>
         </xsl:if>
         <xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
      </tns:losses>
   </xsl:template>

   <xsl:template match="rsd:collateral">
      <tns:collateral>
         <tns:collateralType>
            <xsl:value-of select="./rsd:collateralType"/>
         </tns:collateralType>
         <tns:ownerID>
            <xsl:value-of select="./rsd:ownerID"/>
         </tns:ownerID>
         <tns:estimatedValue>
            <xsl:value-of select="./rsd:estimatedValue"/>
         </tns:estimatedValue>
         <tns:discountRate>
            <xsl:value-of select="./rsd:discountRate"/>
         </tns:discountRate>
         <tns:estimatedValueCorrected>
            <xsl:value-of select="./rsd:estimatedValueCorrected"/>
         </tns:estimatedValueCorrected>
         <tns:qualityCategory>
            <xsl:value-of select="./rsd:qualityCategory"/>
         </tns:qualityCategory>
         <tns:daysForCalculation>
            <xsl:value-of select="./rsd:daysForCalculation"/>
         </tns:daysForCalculation>
         <tns:estimatedValueCorrectedToEAD>
            <xsl:value-of select="./rsd:estimatedValueCorrectedToEAD"/>
         </tns:estimatedValueCorrectedToEAD>
         <tns:securityShare>
            <xsl:value-of select="./rsd:securityShare"/>
         </tns:securityShare>
         <tns:EstimatedValueSource>
            <xsl:value-of select="./rsd:EstimatedValueSource"/>
         </tns:EstimatedValueSource>
         <tns:estimationDate>
            <xsl:value-of select="./rsd:estimationDate"/>
         </tns:estimationDate>
         <tns:businessMeaning>
            <xsl:value-of select="./rsd:businessMeaning"/>
         </tns:businessMeaning>
         <tns:qualityAndLiquidityEstimation>
            <xsl:value-of select="./rsd:qualityAndLiquidityEstimation"/>
         </tns:qualityAndLiquidityEstimation>
         <tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <tns:estimatedValueType>
            <xsl:value-of select="./rsd:estimatedValueType"/>
         </tns:estimatedValueType>
         <tns:guarantorRating>
            <xsl:value-of select="./rsd:guarantorRating"/>
         </tns:guarantorRating>
      </tns:collateral>
   </xsl:template>

   <xsl:template match="rsd:fee">
      <tns:fee>
         <tns:feeName>
            <xsl:value-of select="./rsd:feeName"/>
         </tns:feeName>
         <xsl:if test="./rsd:anotherFeeName">
            <tns:anotherFeeName>
               <xsl:value-of select="./rsd:anotherFeeName"/>
            </tns:anotherFeeName>
         </xsl:if>
         <xsl:if test="./rsd:calculationBase">
            <tns:calculationBase>
               <xsl:value-of select="./rsd:calculationBase"/>
            </tns:calculationBase>
         </xsl:if>
         <xsl:if test="./rsd:paymentOrder">
            <tns:paymentOrder>
               <xsl:value-of select="./rsd:paymentOrder"/>
            </tns:paymentOrder>
         </xsl:if>
      </tns:fee>
   </xsl:template>

   <xsl:template match="rsd:productInfo">
      <tns:productInfo>
         <tns:productVersion>
            <xsl:value-of select="./rsd:productVersion"/>
         </tns:productVersion>
         <tns:purpose>
            <xsl:value-of select="./rsd:purpose"/>
         </tns:purpose>
         <tns:creditMode>
            <xsl:value-of select="./rsd:creditMode"/>
         </tns:creditMode>
         <tns:selectionSchedule>
            <xsl:value-of select="./rsd:selectionSchedule"/>
         </tns:selectionSchedule>
         <tns:changesSchedule>
            <xsl:value-of select="./rsd:changesSchedule"/>
         </tns:changesSchedule>
         <xsl:if test="./rsd:selectionSchedulePeriod">
            <tns:selectionSchedulePeriod>
               <xsl:value-of select="./rsd:selectionSchedulePeriod"/>
            </tns:selectionSchedulePeriod>
         </xsl:if>
         <xsl:if test="./rsd:changesSchedulePeriod">
            <tns:changesSchedulePeriod>
               <xsl:value-of select="./rsd:changesSchedulePeriod"/>
            </tns:changesSchedulePeriod>
         </xsl:if>
         <tns:interestPaymentOrder>
            <xsl:value-of select="./rsd:interestPaymentOrder"/>
         </tns:interestPaymentOrder>
         <tns:refinancing>
            <xsl:value-of select="./rsd:refinancing"/>
         </tns:refinancing>
         <xsl:if test="./rsd:suspensiveCondition">
            <tns:suspensiveCondition>
               <xsl:value-of select="./rsd:suspensiveCondition"/>
            </tns:suspensiveCondition>
         </xsl:if>
         <xsl:if test="./rsd:principalBaseCurrency">
            <tns:principalBaseCurrency>
               <xsl:value-of select="./rsd:principalBaseCurrency"/>
            </tns:principalBaseCurrency>
         </xsl:if>
         <xsl:apply-templates select="./rsd:financingInfo"/>
         <xsl:if test="./rsd:startDate">
            <tns:startDate>
               <xsl:value-of select="./rsd:startDate"/>
            </tns:startDate>
         </xsl:if>
         <tns:creditTerm>
            <xsl:value-of select="./rsd:creditTerm"/>
         </tns:creditTerm>
         <tns:period>
            <xsl:value-of select="./rsd:period"/>
         </tns:period>
         <xsl:if test="./rsd:gracePeriod">
            <tns:gracePeriod>
               <xsl:value-of select="./rsd:gracePeriod"/>
            </tns:gracePeriod>
         </xsl:if>
         <xsl:if test="./rsd:repaymentSchedule">
            <tns:repaymentSchedule>
               <xsl:value-of select="./rsd:repaymentSchedule"/>
            </tns:repaymentSchedule>
         </xsl:if>
         <tns:turnover>
            <xsl:value-of select="./rsd:turnover"/>
         </tns:turnover>
         <tns:covenants>
            <xsl:value-of select="./rsd:covenants"/>
         </tns:covenants>
         <tns:proposedCollateral>
            <xsl:value-of select="./rsd:proposedCollateral"/>
         </tns:proposedCollateral>
         <xsl:if test="./rsd:debtLimit">
            <tns:debtLimit>
               <xsl:value-of select="./rsd:debtLimit"/>
            </tns:debtLimit>
         </xsl:if>
         <xsl:if test="./rsd:pledgeInsurance">
            <tns:pledgeInsurance>
               <xsl:value-of select="./rsd:pledgeInsurance"/>
            </tns:pledgeInsurance>
         </xsl:if>
         <xsl:if test="./rsd:customerProspects">
            <tns:customerProspects>
               <xsl:value-of select="./rsd:customerProspects"/>
            </tns:customerProspects>
         </xsl:if>
         <tns:financingTerm>
            <xsl:value-of select="./rsd:financingTerm"/>
         </tns:financingTerm>
         <xsl:if test="./rsd:firstCountry">
            <tns:firstCountry>
               <xsl:value-of select="./rsd:firstCountry"/>
            </tns:firstCountry>
         </xsl:if>
         <xsl:if test="./rsd:lastCountry">
            <tns:lastCountry>
               <xsl:value-of select="./rsd:lastCountry"/>
            </tns:lastCountry>
         </xsl:if>
         <xsl:apply-templates select="./rsd:fee"/>
         <xsl:apply-templates select="./rsd:repaymentScheduleDetails"/>
         <xsl:apply-templates select="./rsd:selectionScheduleDetails"/>
         <xsl:apply-templates select="./rsd:changesScheduleDetails"/>
      </tns:productInfo>
   </xsl:template>

   <xsl:template match="rsd:products">
      <tns:products>
         <tns:productID>
            <xsl:value-of select="./rsd:productID"/>
         </tns:productID>
         <tns:productName>
            <xsl:value-of select="./rsd:productName"/>
         </tns:productName>
         <tns:category>
            <xsl:value-of select="./rsd:category"/>
         </tns:category>
         <xsl:apply-templates select="./rsd:productInfo"/>
         <xsl:apply-templates select="./rsd:collateral"/>
         <xsl:apply-templates select="./rsd:losses"/>
      </tns:products>
   </xsl:template>

   <xsl:template match="rsd:deal">
      <tns:deal>
         <tns:dealID>
            <xsl:value-of select="./rsd:dealID"/>
         </tns:dealID>
         <xsl:if test="./rsd:contractID">
            <tns:contractID>
               <xsl:value-of select="./rsd:contractID"/>
            </tns:contractID>
         </xsl:if>
         <tns:dealCreationDate>
            <xsl:value-of select="./rsd:dealCreationDate"/>
         </tns:dealCreationDate>
         <tns:dealEssense>
            <xsl:value-of select="./rsd:dealEssense"/>
         </tns:dealEssense>
         <tns:requestPurpose>
            <xsl:value-of select="./rsd:requestPurpose"/>
         </tns:requestPurpose>
         <tns:dealType>
            <xsl:value-of select="./rsd:dealType"/>
         </tns:dealType>
         <tns:dealStatus>
            <xsl:value-of select="./rsd:dealStatus"/>
         </tns:dealStatus>
         <tns:requestDate>
            <xsl:value-of select="./rsd:requestDate"/>
         </tns:requestDate>
         <tns:origOrgUnit>
            <xsl:value-of select="./rsd:origOrgUnit"/>
         </tns:origOrgUnit>
         <tns:origOrgDivision>
            <xsl:value-of select="./rsd:origOrgDivision"/>
         </tns:origOrgDivision>
         <tns:clientFileID>
            <xsl:value-of select="./rsd:clientFileID"/>
         </tns:clientFileID>
         <tns:dealFileID>
            <xsl:value-of select="./rsd:dealFileID"/>
         </tns:dealFileID>
         <xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
         <tns:supLMID>
            <xsl:value-of select="./rsd:supLMID"/>
         </tns:supLMID>
         <tns:collateralProvided>
            <xsl:value-of select="./rsd:collateralProvided"/>
         </tns:collateralProvided>
         <tns:limitKM>
            <xsl:value-of select="./rsd:limitKM"/>
         </tns:limitKM>
         <xsl:apply-templates select="./rsd:dealTeamMembers"/>
         <xsl:apply-templates select="./rsd:products"/>
         <xsl:apply-templates select="./rsd:project"/>
      </tns:deal>
   </xsl:template>

   <xsl:template name="UpdateDealRq">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="crm:UpdateDealRq">
         <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
