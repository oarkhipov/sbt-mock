<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.07/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.07/Data/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
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
            <xsl:with-param name="operation-name" select="string('updateDealRq')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="updateDealRq">
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

   <xsl:template match="rsd:exchangeRates">
      <tns:exchangeRates><!--match="xsd:element[@name]"--><tns:currencyName>
            <xsl:value-of select="./rsd:currencyName"/>
         </tns:currencyName>
         <!--match="xsd:element[@name]"--><tns:currencyValue>
            <xsl:value-of select="./rsd:currencyValue"/>
         </tns:currencyValue>
         <!--match="xsd:element[@name]"--><tns:currencyDate>
            <xsl:value-of select="./rsd:currencyDate"/>
         </tns:currencyDate>
      </tns:exchangeRates>
   </xsl:template>

   <xsl:template match="rsd:dealTeamMembers">
      <tns:dealTeamMembers><!--match="xsd:element[@name]"--><tns:memberId>
            <xsl:value-of select="./rsd:memberId"/>
         </tns:memberId>
         <!--match="xsd:element[@name]"--><tns:memberName>
            <xsl:value-of select="./rsd:memberName"/>
         </tns:memberName>
         <!--match="xsd:element[@name]"--><tns:mamberEmail>
            <xsl:value-of select="./rsd:mamberEmail"/>
         </tns:mamberEmail>
         <!--match="xsd:element[@name]"--><tns:mamberRole>
            <xsl:value-of select="./rsd:mamberRole"/>
         </tns:mamberRole>
      </tns:dealTeamMembers>
   </xsl:template>

   <xsl:template match="rsd:financingInfo">
      <tns:financingInfo><!--match="xsd:element[@name]"--><tns:currencyOfFunding>
            <xsl:value-of select="./rsd:currencyOfFunding"/>
         </tns:currencyOfFunding>
         <!--match="xsd:element[@name]"--><tns:principal>
            <xsl:value-of select="./rsd:principal"/>
         </tns:principal>
      </tns:financingInfo>
   </xsl:template>

   <xsl:template match="rsd:interestRateInfo">
      <tns:interestRateInfo><!--match="xsd:element[@name]"--><tns:interestRate>
            <xsl:value-of select="./rsd:interestRate"/>
         </tns:interestRate>
         <!--match="xsd:element[@name]"--><tns:interestRateType>
            <xsl:value-of select="./rsd:interestRateType"/>
         </tns:interestRateType>
         <!--match="xsd:element[@name]"--><tns:interestRateOrder>
            <xsl:value-of select="./rsd:interestRateOrder"/>
         </tns:interestRateOrder>
         <!--match="xsd:element[@name]"--><tns:minRate>
            <xsl:value-of select="./rsd:minRate"/>
         </tns:minRate>
      </tns:interestRateInfo>
   </xsl:template>

   <xsl:template match="rsd:project">
      <tns:project><!--match="xsd:element[@name]"--><xsl:if test="./rsd:projectName">
            <tns:projectName>
               <xsl:value-of select="./rsd:projectName"/>
            </tns:projectName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:indicatorsOfConstruction">
            <tns:indicatorsOfConstruction>
               <xsl:value-of select="./rsd:indicatorsOfConstruction"/>
            </tns:indicatorsOfConstruction>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:parcel">
            <tns:parcel>
               <xsl:value-of select="./rsd:parcel"/>
            </tns:parcel>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:documentsPresence">
            <tns:documentsPresence>
               <xsl:value-of select="./rsd:documentsPresence"/>
            </tns:documentsPresence>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:equityVolume">
            <tns:equityVolume>
               <xsl:value-of select="./rsd:equityVolume"/>
            </tns:equityVolume>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:repaymentSpring">
            <tns:repaymentSpring>
               <xsl:value-of select="./rsd:repaymentSpring"/>
            </tns:repaymentSpring>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:fundingDirection">
            <tns:fundingDirection>
               <xsl:value-of select="./rsd:fundingDirection"/>
            </tns:fundingDirection>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:sameIndustryAsBorrower">
            <tns:sameIndustryAsBorrower>
               <xsl:value-of select="./rsd:sameIndustryAsBorrower"/>
            </tns:sameIndustryAsBorrower>
         </xsl:if>
      </tns:project>
   </xsl:template>

   <xsl:template match="rsd:repaymentScheduleDetails">
      <tns:repaymentScheduleDetails><!--match="xsd:element[@name]"--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--match="xsd:element[@name]"--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:repaymentScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:selectionScheduleDetails">
      <tns:selectionScheduleDetails><!--match="xsd:element[@name]"--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--match="xsd:element[@name]"--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:selectionScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:changesScheduleDetails">
      <tns:changesScheduleDetails><!--match="xsd:element[@name]"--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--match="xsd:element[@name]"--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:changesScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:losses">
      <tns:losses><!--match="xsd:element[@name]"--><xsl:if test="./rsd:percentLGD">
            <tns:percentLGD>
               <xsl:value-of select="./rsd:percentLGD"/>
            </tns:percentLGD>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:countEAD">
            <tns:countEAD>
               <xsl:value-of select="./rsd:countEAD"/>
            </tns:countEAD>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:reportDateAmount">
            <tns:reportDateAmount>
               <xsl:value-of select="./rsd:reportDateAmount"/>
            </tns:reportDateAmount>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:lossELpercent">
            <tns:lossELpercent>
               <xsl:value-of select="./rsd:lossELpercent"/>
            </tns:lossELpercent>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:lossELcount">
            <tns:lossELcount>
               <xsl:value-of select="./rsd:lossELcount"/>
            </tns:lossELcount>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:typeLGD">
            <tns:typeLGD>
               <xsl:value-of select="./rsd:typeLGD"/>
            </tns:typeLGD>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:statusLGD">
            <tns:statusLGD>
               <xsl:value-of select="./rsd:statusLGD"/>
            </tns:statusLGD>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:dateLGDcalculated">
            <tns:dateLGDcalculated>
               <xsl:value-of select="./rsd:dateLGDcalculated"/>
            </tns:dateLGDcalculated>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:finalizationDate">
            <tns:finalizationDate>
               <xsl:value-of select="./rsd:finalizationDate"/>
            </tns:finalizationDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
      </tns:losses>
   </xsl:template>

   <xsl:template match="rsd:collateral">
      <tns:collateral><!--match="xsd:element[@name]"--><tns:collateralType>
            <xsl:value-of select="./rsd:collateralType"/>
         </tns:collateralType>
         <!--match="xsd:element[@name]"--><tns:ownerID>
            <xsl:value-of select="./rsd:ownerID"/>
         </tns:ownerID>
         <!--match="xsd:element[@name]"--><tns:estimatedValue>
            <xsl:value-of select="./rsd:estimatedValue"/>
         </tns:estimatedValue>
         <!--match="xsd:element[@name]"--><tns:discountRate>
            <xsl:value-of select="./rsd:discountRate"/>
         </tns:discountRate>
         <!--match="xsd:element[@name]"--><tns:estimatedValueCorrected>
            <xsl:value-of select="./rsd:estimatedValueCorrected"/>
         </tns:estimatedValueCorrected>
         <!--match="xsd:element[@name]"--><tns:qualityCategory>
            <xsl:value-of select="./rsd:qualityCategory"/>
         </tns:qualityCategory>
         <!--match="xsd:element[@name]"--><tns:daysForCalculation>
            <xsl:value-of select="./rsd:daysForCalculation"/>
         </tns:daysForCalculation>
         <!--match="xsd:element[@name]"--><tns:estimatedValueCorrectedToEAD>
            <xsl:value-of select="./rsd:estimatedValueCorrectedToEAD"/>
         </tns:estimatedValueCorrectedToEAD>
         <!--match="xsd:element[@name]"--><tns:securityShare>
            <xsl:value-of select="./rsd:securityShare"/>
         </tns:securityShare>
         <!--match="xsd:element[@name]"--><tns:EstimatedValueSource>
            <xsl:value-of select="./rsd:EstimatedValueSource"/>
         </tns:EstimatedValueSource>
         <!--match="xsd:element[@name]"--><tns:estimationDate>
            <xsl:value-of select="./rsd:estimationDate"/>
         </tns:estimationDate>
         <!--match="xsd:element[@name]"--><tns:businessMeaning>
            <xsl:value-of select="./rsd:businessMeaning"/>
         </tns:businessMeaning>
         <!--match="xsd:element[@name]"--><tns:qualityAndLiquidityEstimation>
            <xsl:value-of select="./rsd:qualityAndLiquidityEstimation"/>
         </tns:qualityAndLiquidityEstimation>
         <!--match="xsd:element[@name]"--><tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <!--match="xsd:element[@name]"--><tns:estimatedValueType>
            <xsl:value-of select="./rsd:estimatedValueType"/>
         </tns:estimatedValueType>
         <!--match="xsd:element[@name]"--><tns:guarantorRating>
            <xsl:value-of select="./rsd:guarantorRating"/>
         </tns:guarantorRating>
      </tns:collateral>
   </xsl:template>

   <xsl:template match="rsd:fee">
      <tns:fee><!--match="xsd:element[@name]"--><tns:feeName>
            <xsl:value-of select="./rsd:feeName"/>
         </tns:feeName>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:anotherFeeName">
            <tns:anotherFeeName>
               <xsl:value-of select="./rsd:anotherFeeName"/>
            </tns:anotherFeeName>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:calculationBase">
            <tns:calculationBase>
               <xsl:value-of select="./rsd:calculationBase"/>
            </tns:calculationBase>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:paymentOrder">
            <tns:paymentOrder>
               <xsl:value-of select="./rsd:paymentOrder"/>
            </tns:paymentOrder>
         </xsl:if>
      </tns:fee>
   </xsl:template>

   <xsl:template match="rsd:productInfo">
      <tns:productInfo><!--match="xsd:element[@name]"--><tns:productVersion>
            <xsl:value-of select="./rsd:productVersion"/>
         </tns:productVersion>
         <!--match="xsd:element[@name]"--><tns:purpose>
            <xsl:value-of select="./rsd:purpose"/>
         </tns:purpose>
         <!--match="xsd:element[@name]"--><tns:creditMode>
            <xsl:value-of select="./rsd:creditMode"/>
         </tns:creditMode>
         <!--match="xsd:element[@name]"--><tns:selectionSchedule>
            <xsl:value-of select="./rsd:selectionSchedule"/>
         </tns:selectionSchedule>
         <!--match="xsd:element[@name]"--><tns:changesSchedule>
            <xsl:value-of select="./rsd:changesSchedule"/>
         </tns:changesSchedule>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:selectionSchedulePeriod">
            <tns:selectionSchedulePeriod>
               <xsl:value-of select="./rsd:selectionSchedulePeriod"/>
            </tns:selectionSchedulePeriod>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:changesSchedulePeriod">
            <tns:changesSchedulePeriod>
               <xsl:value-of select="./rsd:changesSchedulePeriod"/>
            </tns:changesSchedulePeriod>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:interestPaymentOrder>
            <xsl:value-of select="./rsd:interestPaymentOrder"/>
         </tns:interestPaymentOrder>
         <!--match="xsd:element[@name]"--><tns:refinancing>
            <xsl:value-of select="./rsd:refinancing"/>
         </tns:refinancing>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:suspensiveCondition">
            <tns:suspensiveCondition>
               <xsl:value-of select="./rsd:suspensiveCondition"/>
            </tns:suspensiveCondition>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:principalBaseCurrency">
            <tns:principalBaseCurrency>
               <xsl:value-of select="./rsd:principalBaseCurrency"/>
            </tns:principalBaseCurrency>
         </xsl:if>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:financingInfo"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:interestRateInfo"/>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:startDate">
            <tns:startDate>
               <xsl:value-of select="./rsd:startDate"/>
            </tns:startDate>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:creditTerm>
            <xsl:value-of select="./rsd:creditTerm"/>
         </tns:creditTerm>
         <!--match="xsd:element[@name]"--><tns:period>
            <xsl:value-of select="./rsd:period"/>
         </tns:period>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:gracePeriod">
            <tns:gracePeriod>
               <xsl:value-of select="./rsd:gracePeriod"/>
            </tns:gracePeriod>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:repaymentSchedule">
            <tns:repaymentSchedule>
               <xsl:value-of select="./rsd:repaymentSchedule"/>
            </tns:repaymentSchedule>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:turnover>
            <xsl:value-of select="./rsd:turnover"/>
         </tns:turnover>
         <!--match="xsd:element[@name]"--><tns:covenants>
            <xsl:value-of select="./rsd:covenants"/>
         </tns:covenants>
         <!--match="xsd:element[@name]"--><tns:proposedCollateral>
            <xsl:value-of select="./rsd:proposedCollateral"/>
         </tns:proposedCollateral>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:debtLimit">
            <tns:debtLimit>
               <xsl:value-of select="./rsd:debtLimit"/>
            </tns:debtLimit>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:pledgeInsurance">
            <tns:pledgeInsurance>
               <xsl:value-of select="./rsd:pledgeInsurance"/>
            </tns:pledgeInsurance>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:customerProspects">
            <tns:customerProspects>
               <xsl:value-of select="./rsd:customerProspects"/>
            </tns:customerProspects>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:financingTerm>
            <xsl:value-of select="./rsd:financingTerm"/>
         </tns:financingTerm>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:firstCountry">
            <tns:firstCountry>
               <xsl:value-of select="./rsd:firstCountry"/>
            </tns:firstCountry>
         </xsl:if>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:lastCountry">
            <tns:lastCountry>
               <xsl:value-of select="./rsd:lastCountry"/>
            </tns:lastCountry>
         </xsl:if>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:fee"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:repaymentScheduleDetails"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:selectionScheduleDetails"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:changesScheduleDetails"/>
      </tns:productInfo>
   </xsl:template>

   <xsl:template match="rsd:products">
      <tns:products><!--match="xsd:element[@name]"--><tns:productID>
            <xsl:value-of select="./rsd:productID"/>
         </tns:productID>
         <!--match="xsd:element[@name]"--><tns:productName>
            <xsl:value-of select="./rsd:productName"/>
         </tns:productName>
         <!--match="xsd:element[@name]"--><tns:category>
            <xsl:value-of select="./rsd:category"/>
         </tns:category>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:productInfo"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:collateral"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:losses"/>
      </tns:products>
   </xsl:template>

   <xsl:template match="rsd:deal">
      <tns:deal><!--match="xsd:element[@name]"--><tns:dealID>
            <xsl:value-of select="./rsd:dealID"/>
         </tns:dealID>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:contractID">
            <tns:contractID>
               <xsl:value-of select="./rsd:contractID"/>
            </tns:contractID>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:dealCreationDate>
            <xsl:value-of select="./rsd:dealCreationDate"/>
         </tns:dealCreationDate>
         <!--match="xsd:element[@name]"--><tns:dealEssense>
            <xsl:value-of select="./rsd:dealEssense"/>
         </tns:dealEssense>
         <!--match="xsd:element[@name]"--><tns:requestPurpose>
            <xsl:value-of select="./rsd:requestPurpose"/>
         </tns:requestPurpose>
         <!--match="xsd:element[@name]"--><tns:dealType>
            <xsl:value-of select="./rsd:dealType"/>
         </tns:dealType>
         <!--match="xsd:element[@name]"--><tns:dealStatus>
            <xsl:value-of select="./rsd:dealStatus"/>
         </tns:dealStatus>
         <!--match="xsd:element[@name]"--><tns:requestDate>
            <xsl:value-of select="./rsd:requestDate"/>
         </tns:requestDate>
         <!--match="xsd:element[@name]"--><tns:origOrgUnit>
            <xsl:value-of select="./rsd:origOrgUnit"/>
         </tns:origOrgUnit>
         <!--match="xsd:element[@name]"--><tns:origOrgDivision>
            <xsl:value-of select="./rsd:origOrgDivision"/>
         </tns:origOrgDivision>
         <!--match="xsd:element[@name]"--><tns:clientFileID>
            <xsl:value-of select="./rsd:clientFileID"/>
         </tns:clientFileID>
         <!--match="xsd:element[@name]"--><tns:dealFileID>
            <xsl:value-of select="./rsd:dealFileID"/>
         </tns:dealFileID>
         <!--match="xsd:element[@name]"--><xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
         <!--match="xsd:element[@name]"--><tns:supLMID>
            <xsl:value-of select="./rsd:supLMID"/>
         </tns:supLMID>
         <!--match="xsd:element[@name]"--><tns:collateralProvided>
            <xsl:value-of select="./rsd:collateralProvided"/>
         </tns:collateralProvided>
         <!--match="xsd:element[@name]"--><tns:limitKM>
            <xsl:value-of select="./rsd:limitKM"/>
         </tns:limitKM>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:dealTeamMembers"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:products"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:project"/>
         <!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="./rsd:exchangeRates"/>
      </tns:deal>
   </xsl:template>

   <xsl:template name="updateDealRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:updateDealRq">
			<!--xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]"--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:deal"/>
		    </xsl:element>
   </xsl:template>
</xsl:stylesheet>
