<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/updateDealRq/Data/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
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

   <!--baseContainer if UpdateDealRq - 1updateDealRq--><!--all types DealTeamMember FinancingInfo Rating Project Schedule Losses Collateral Fee ParticipantProductInfo Product ProductConditions StopFactor ProblemCriteria Deal UpdateDealRq--><!--types to import DealTeamMember FinancingInfo Rating Project Schedule Losses Collateral Fee ParticipantProductInfo Product ProductConditions StopFactor ProblemCriteria Deal UpdateDealRq--><!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:DealTeamMember--><xsl:template match="rsd:dealTeamMembers">
      <tns:dealTeamMembers><!--xsd:element - Inside--><tns:memberId>
            <xsl:value-of select="./rsd:memberId"/>
         </tns:memberId>
         <!--xsd:element - Inside--><tns:memberName>
            <xsl:value-of select="./rsd:memberName"/>
         </tns:memberName>
         <!--xsd:element - Inside--><tns:memberEmail>
            <xsl:value-of select="./rsd:memberEmail"/>
         </tns:memberEmail>
         <!--xsd:element - Inside--><tns:memberRole>
            <xsl:value-of select="./rsd:memberRole"/>
         </tns:memberRole>
      </tns:dealTeamMembers>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:FinancingInfo--><xsl:template match="rsd:financingInfo">
      <tns:financingInfo><!--xsd:element - Inside--><tns:interestRate>
            <xsl:value-of select="./rsd:interestRate"/>
         </tns:interestRate>
         <!--xsd:element - Inside--><tns:interestRateType>
            <xsl:value-of select="./rsd:interestRateType"/>
         </tns:interestRateType>
         <!--xsd:element - Inside--><tns:interestRateOrder>
            <xsl:value-of select="./rsd:interestRateOrder"/>
         </tns:interestRateOrder>
         <!--xsd:element - Inside--><xsl:if test="./rsd:anotherInterestRateOrder">
            <tns:anotherInterestRateOrder>
               <xsl:value-of select="./rsd:anotherInterestRateOrder"/>
            </tns:anotherInterestRateOrder>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:minRate">
            <tns:minRate>
               <xsl:value-of select="./rsd:minRate"/>
            </tns:minRate>
         </xsl:if>
         <!--xsd:element - Inside--><tns:currencyOfFunding>
            <xsl:value-of select="./rsd:currencyOfFunding"/>
         </tns:currencyOfFunding>
         <!--xsd:element - Inside--><tns:principal>
            <xsl:value-of select="./rsd:principal"/>
         </tns:principal>
      </tns:financingInfo>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Rating--><xsl:template match="rsd:projectRating">
      <tns:projectRating><!--xsd:element - Inside--><tns:ratingID>
            <xsl:value-of select="./rsd:ratingID"/>
         </tns:ratingID>
         <!--xsd:element - Inside--><tns:ratingValue>
            <xsl:value-of select="./rsd:ratingValue"/>
         </tns:ratingValue>
         <!--xsd:element - Inside--><tns:ratingType>
            <xsl:value-of select="./rsd:ratingType"/>
         </tns:ratingType>
         <!--xsd:element - Inside--><tns:ratingModel>
            <xsl:value-of select="./rsd:ratingModel"/>
         </tns:ratingModel>
         <!--xsd:element - Inside--><tns:ratingCalculatedDate>
            <xsl:value-of select="./rsd:ratingCalculatedDate"/>
         </tns:ratingCalculatedDate>
         <!--xsd:element - Inside--><tns:ratingCalculatedRole>
            <xsl:value-of select="./rsd:ratingCalculatedRole"/>
         </tns:ratingCalculatedRole>
         <!--xsd:element - Inside--><tns:ratingCalculatedBy>
            <xsl:value-of select="./rsd:ratingCalculatedBy"/>
         </tns:ratingCalculatedBy>
         <!--xsd:element - Inside--><tns:ratingApprovalDate>
            <xsl:value-of select="./rsd:ratingApprovalDate"/>
         </tns:ratingApprovalDate>
         <!--xsd:element - Inside--><tns:ratingApprovedBy>
            <xsl:value-of select="./rsd:ratingApprovedBy"/>
         </tns:ratingApprovedBy>
      </tns:projectRating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Project--><xsl:template match="rsd:project">
      <tns:project><!--xsd:element - Inside--><tns:projectID>
            <xsl:value-of select="./rsd:projectID"/>
         </tns:projectID>
         <!--xsd:element - Inside--><xsl:if test="./rsd:projectName">
            <tns:projectName>
               <xsl:value-of select="./rsd:projectName"/>
            </tns:projectName>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:indicatorsOfConstruction">
            <tns:indicatorsOfConstruction>
               <xsl:value-of select="./rsd:indicatorsOfConstruction"/>
            </tns:indicatorsOfConstruction>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:parcel">
            <tns:parcel>
               <xsl:value-of select="./rsd:parcel"/>
            </tns:parcel>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:documentsPresence">
            <tns:documentsPresence>
               <xsl:value-of select="./rsd:documentsPresence"/>
            </tns:documentsPresence>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:equityVolume">
            <tns:equityVolume>
               <xsl:value-of select="./rsd:equityVolume"/>
            </tns:equityVolume>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:repaymentSpring">
            <tns:repaymentSpring>
               <xsl:value-of select="./rsd:repaymentSpring"/>
            </tns:repaymentSpring>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:fundingDirection">
            <tns:fundingDirection>
               <xsl:value-of select="./rsd:fundingDirection"/>
            </tns:fundingDirection>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:sameIndustryAsBorrower">
            <tns:sameIndustryAsBorrower>
               <xsl:value-of select="./rsd:sameIndustryAsBorrower"/>
            </tns:sameIndustryAsBorrower>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside Rating--><xsl:apply-templates select="./rsd:projectRating"/>
      </tns:project>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Schedule--><xsl:template match="rsd:repaymentScheduleDetails">
      <tns:repaymentScheduleDetails><!--xsd:element - Inside--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--xsd:element - Inside--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:repaymentScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:selectionScheduleDetails">
      <tns:selectionScheduleDetails><!--xsd:element - Inside--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--xsd:element - Inside--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:selectionScheduleDetails>
   </xsl:template>

   <xsl:template match="rsd:changesScheduleDetails">
      <tns:changesScheduleDetails><!--xsd:element - Inside--><tns:sheduleDate>
            <xsl:value-of select="./rsd:sheduleDate"/>
         </tns:sheduleDate>
         <!--xsd:element - Inside--><tns:sheduleAmount>
            <xsl:value-of select="./rsd:sheduleAmount"/>
         </tns:sheduleAmount>
      </tns:changesScheduleDetails>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Losses--><xsl:template match="rsd:losses">
      <tns:losses><!--xsd:element - Inside--><xsl:if test="./rsd:percentLGD">
            <tns:percentLGD>
               <xsl:value-of select="./rsd:percentLGD"/>
            </tns:percentLGD>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:countEAD">
            <tns:countEAD>
               <xsl:value-of select="./rsd:countEAD"/>
            </tns:countEAD>
         </xsl:if>
         <!--xsd:element - Inside--><tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <!--xsd:element - Inside--><xsl:if test="./rsd:reportDateAmount">
            <tns:reportDateAmount>
               <xsl:value-of select="./rsd:reportDateAmount"/>
            </tns:reportDateAmount>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:lossELpercent">
            <tns:lossELpercent>
               <xsl:value-of select="./rsd:lossELpercent"/>
            </tns:lossELpercent>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:lossELcount">
            <tns:lossELcount>
               <xsl:value-of select="./rsd:lossELcount"/>
            </tns:lossELcount>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:typeLGD">
            <tns:typeLGD>
               <xsl:value-of select="./rsd:typeLGD"/>
            </tns:typeLGD>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:statusLGD">
            <tns:statusLGD>
               <xsl:value-of select="./rsd:statusLGD"/>
            </tns:statusLGD>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:dateLGDcalculated">
            <tns:dateLGDcalculated>
               <xsl:value-of select="./rsd:dateLGDcalculated"/>
            </tns:dateLGDcalculated>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:finalizationDate">
            <tns:finalizationDate>
               <xsl:value-of select="./rsd:finalizationDate"/>
            </tns:finalizationDate>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
      </tns:losses>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Collateral--><xsl:template match="rsd:collateral">
      <tns:collateral><!--xsd:element - Inside--><tns:collateralID>
            <xsl:value-of select="./rsd:collateralID"/>
         </tns:collateralID>
         <!--xsd:element - Inside--><tns:collateralType>
            <xsl:value-of select="./rsd:collateralType"/>
         </tns:collateralType>
         <!--xsd:element - Inside--><tns:ownerID>
            <xsl:value-of select="./rsd:ownerID"/>
         </tns:ownerID>
         <!--xsd:element - Inside--><tns:isLegalPerson>
            <xsl:value-of select="./rsd:isLegalPerson"/>
         </tns:isLegalPerson>
         <!--xsd:element - Inside--><tns:estimatedValue>
            <xsl:value-of select="./rsd:estimatedValue"/>
         </tns:estimatedValue>
         <!--xsd:element - Inside--><tns:discountRate>
            <xsl:value-of select="./rsd:discountRate"/>
         </tns:discountRate>
         <!--xsd:element - Inside--><tns:estimatedValueCorrected>
            <xsl:value-of select="./rsd:estimatedValueCorrected"/>
         </tns:estimatedValueCorrected>
         <!--xsd:element - Inside--><tns:qualityCategory>
            <xsl:value-of select="./rsd:qualityCategory"/>
         </tns:qualityCategory>
         <!--xsd:element - Inside--><xsl:if test="./rsd:daysForCalculation">
            <tns:daysForCalculation>
               <xsl:value-of select="./rsd:daysForCalculation"/>
            </tns:daysForCalculation>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:estimatedValueCorrectedToEAD">
            <tns:estimatedValueCorrectedToEAD>
               <xsl:value-of select="./rsd:estimatedValueCorrectedToEAD"/>
            </tns:estimatedValueCorrectedToEAD>
         </xsl:if>
         <!--xsd:element - Inside--><tns:estimatedValueSource>
            <xsl:value-of select="./rsd:estimatedValueSource"/>
         </tns:estimatedValueSource>
         <!--xsd:element - Inside--><tns:estimationDate>
            <xsl:value-of select="./rsd:estimationDate"/>
         </tns:estimationDate>
         <!--xsd:element - Inside--><tns:businessMeaning>
            <xsl:value-of select="./rsd:businessMeaning"/>
         </tns:businessMeaning>
         <!--xsd:element - Inside--><tns:qualityAndLiquidityEstimation>
            <xsl:value-of select="./rsd:qualityAndLiquidityEstimation"/>
         </tns:qualityAndLiquidityEstimation>
         <!--xsd:element - Inside--><tns:currency>
            <xsl:value-of select="./rsd:currency"/>
         </tns:currency>
         <!--xsd:element - Inside--><tns:estimatedValueType>
            <xsl:value-of select="./rsd:estimatedValueType"/>
         </tns:estimatedValueType>
         <!--xsd:element - Inside--><tns:guarantorRating>
            <xsl:value-of select="./rsd:guarantorRating"/>
         </tns:guarantorRating>
      </tns:collateral>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Fee--><xsl:template match="rsd:fee">
      <tns:fee><!--xsd:element - Inside--><tns:feeName>
            <xsl:value-of select="./rsd:feeName"/>
         </tns:feeName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:anotherFeeName">
            <tns:anotherFeeName>
               <xsl:value-of select="./rsd:anotherFeeName"/>
            </tns:anotherFeeName>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:calculationBase">
            <tns:calculationBase>
               <xsl:value-of select="./rsd:calculationBase"/>
            </tns:calculationBase>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:paymentOrder">
            <tns:paymentOrder>
               <xsl:value-of select="./rsd:paymentOrder"/>
            </tns:paymentOrder>
         </xsl:if>
      </tns:fee>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:ParticipantProductInfo--><xsl:template match="rsd:participantProductInfo">
      <tns:participantProductInfo><!--xsd:element - Inside--><tns:participantID>
            <xsl:value-of select="./rsd:participantID"/>
         </tns:participantID>
         <!--xsd:element - Inside--><tns:personType>
            <xsl:value-of select="./rsd:personType"/>
         </tns:personType>
         <!--xsd:element - Inside--><tns:participantType>
            <xsl:value-of select="./rsd:participantType"/>
         </tns:participantType>
      </tns:participantProductInfo>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Product--><xsl:template match="rsd:productInfo">
      <tns:productInfo><!--xsd:element - Inside--><tns:productVersion>
            <xsl:value-of select="./rsd:productVersion"/>
         </tns:productVersion>
         <!--xsd:element - Inside--><tns:purpose>
            <xsl:value-of select="./rsd:purpose"/>
         </tns:purpose>
         <!--xsd:element - Inside--><tns:creditMode>
            <xsl:value-of select="./rsd:creditMode"/>
         </tns:creditMode>
         <!--xsd:element - Inside--><tns:selectionSchedule>
            <xsl:value-of select="./rsd:selectionSchedule"/>
         </tns:selectionSchedule>
         <!--xsd:element - Inside--><xsl:if test="./rsd:changesSchedule">
            <tns:changesSchedule>
               <xsl:value-of select="./rsd:changesSchedule"/>
            </tns:changesSchedule>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:selectionSchedulePeriod">
            <tns:selectionSchedulePeriod>
               <xsl:value-of select="./rsd:selectionSchedulePeriod"/>
            </tns:selectionSchedulePeriod>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:changesSchedulePeriod">
            <tns:changesSchedulePeriod>
               <xsl:value-of select="./rsd:changesSchedulePeriod"/>
            </tns:changesSchedulePeriod>
         </xsl:if>
         <!--xsd:element - Inside--><tns:interestPaymentOrder>
            <xsl:value-of select="./rsd:interestPaymentOrder"/>
         </tns:interestPaymentOrder>
         <!--xsd:element - Inside--><tns:refinancing>
            <xsl:value-of select="./rsd:refinancing"/>
         </tns:refinancing>
         <!--xsd:element - Inside--><xsl:if test="./rsd:suspensiveCondition">
            <tns:suspensiveCondition>
               <xsl:value-of select="./rsd:suspensiveCondition"/>
            </tns:suspensiveCondition>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:principalBaseCurrency">
            <tns:principalBaseCurrency>
               <xsl:value-of select="./rsd:principalBaseCurrency"/>
            </tns:principalBaseCurrency>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside FinancingInfo--><xsl:apply-templates select="./rsd:financingInfo"/>
         <!--xsd:element - Inside--><xsl:if test="./rsd:startDate">
            <tns:startDate>
               <xsl:value-of select="./rsd:startDate"/>
            </tns:startDate>
         </xsl:if>
         <!--xsd:element - Inside--><tns:creditTerm>
            <xsl:value-of select="./rsd:creditTerm"/>
         </tns:creditTerm>
         <!--xsd:element - Inside--><tns:period>
            <xsl:value-of select="./rsd:period"/>
         </tns:period>
         <!--xsd:element - Inside--><xsl:if test="./rsd:gracePeriod">
            <tns:gracePeriod>
               <xsl:value-of select="./rsd:gracePeriod"/>
            </tns:gracePeriod>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:repaymentSchedule">
            <tns:repaymentSchedule>
               <xsl:value-of select="./rsd:repaymentSchedule"/>
            </tns:repaymentSchedule>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:turnover">
            <tns:turnover>
               <xsl:value-of select="./rsd:turnover"/>
            </tns:turnover>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:covenants">
            <tns:covenants>
               <xsl:value-of select="./rsd:covenants"/>
            </tns:covenants>
         </xsl:if>
         <!--xsd:element - Inside--><tns:proposedCollateral>
            <xsl:value-of select="./rsd:proposedCollateral"/>
         </tns:proposedCollateral>
         <!--xsd:element - Inside--><xsl:if test="./rsd:debtLimit">
            <tns:debtLimit>
               <xsl:value-of select="./rsd:debtLimit"/>
            </tns:debtLimit>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:pledgeInsurance">
            <tns:pledgeInsurance>
               <xsl:value-of select="./rsd:pledgeInsurance"/>
            </tns:pledgeInsurance>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:customerProspects">
            <tns:customerProspects>
               <xsl:value-of select="./rsd:customerProspects"/>
            </tns:customerProspects>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:financingTerm">
            <tns:financingTerm>
               <xsl:value-of select="./rsd:financingTerm"/>
            </tns:financingTerm>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:firstCountry">
            <tns:firstCountry>
               <xsl:value-of select="./rsd:firstCountry"/>
            </tns:firstCountry>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:lastCountry">
            <tns:lastCountry>
               <xsl:value-of select="./rsd:lastCountry"/>
            </tns:lastCountry>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside Fee--><xsl:apply-templates select="./rsd:fee"/>
         <!-- xsd:element[$typesList] - Inside Schedule--><xsl:apply-templates select="./rsd:repaymentScheduleDetails"/>
         <!-- xsd:element[$typesList] - Inside Schedule--><xsl:apply-templates select="./rsd:selectionScheduleDetails"/>
         <!-- xsd:element[$typesList] - Inside Schedule--><xsl:apply-templates select="./rsd:changesScheduleDetails"/>
         <!--xsd:element - Inside--><xsl:if test="./rsd:unutilizedLimit">
            <tns:unutilizedLimit>
               <xsl:value-of select="./rsd:unutilizedLimit"/>
            </tns:unutilizedLimit>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:correctedUnutilizedLimit">
            <tns:correctedUnutilizedLimit>
               <xsl:value-of select="./rsd:correctedUnutilizedLimit"/>
            </tns:correctedUnutilizedLimit>
         </xsl:if>
      </tns:productInfo>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:ProductConditions--><xsl:template match="rsd:products">
      <tns:products><!--xsd:element - Inside--><tns:productID>
            <xsl:value-of select="./rsd:productID"/>
         </tns:productID>
         <!--xsd:element - Inside--><xsl:if test="./rsd:projectID">
            <tns:projectID>
               <xsl:value-of select="./rsd:projectID"/>
            </tns:projectID>
         </xsl:if>
         <!--xsd:element - Inside--><tns:productName>
            <xsl:value-of select="./rsd:productName"/>
         </tns:productName>
         <!--xsd:element - Inside--><tns:category>
            <xsl:value-of select="./rsd:category"/>
         </tns:category>
         <!-- xsd:element[$typesList] - Inside ParticipantProductInfo--><xsl:apply-templates select="./rsd:participantProductInfo"/>
         <!-- xsd:element[$typesList] - Inside Product--><xsl:apply-templates select="./rsd:productInfo"/>
         <!-- xsd:element[$typesList] - Inside Collateral--><xsl:apply-templates select="./rsd:collateral"/>
         <!-- xsd:element[$typesList] - Inside Losses--><xsl:apply-templates select="./rsd:losses"/>
      </tns:products>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:StopFactor--><xsl:template match="rsd:stopFactors">
      <tns:stopFactors><!--xsd:element - Inside--><tns:stopFactorID>
            <xsl:value-of select="./rsd:stopFactorID"/>
         </tns:stopFactorID>
         <!--xsd:element - Inside--><tns:stopFactorName>
            <xsl:value-of select="./rsd:stopFactorName"/>
         </tns:stopFactorName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:stopFactorComment">
            <tns:stopFactorComment>
               <xsl:value-of select="./rsd:stopFactorComment"/>
            </tns:stopFactorComment>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:stopFactorArgument">
            <tns:stopFactorArgument>
               <xsl:value-of select="./rsd:stopFactorArgument"/>
            </tns:stopFactorArgument>
         </xsl:if>
      </tns:stopFactors>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:ProblemCriteria--><xsl:template match="rsd:problemCriterias">
      <tns:problemCriterias><!--xsd:element - Inside--><tns:problemCriteriaID>
            <xsl:value-of select="./rsd:problemCriteriaID"/>
         </tns:problemCriteriaID>
         <!--xsd:element - Inside--><tns:problemCriteriaName>
            <xsl:value-of select="./rsd:problemCriteriaName"/>
         </tns:problemCriteriaName>
         <!--xsd:element - Inside--><xsl:if test="./rsd:creditJustification">
            <tns:creditJustification>
               <xsl:value-of select="./rsd:creditJustification"/>
            </tns:creditJustification>
         </xsl:if>
         <!--xsd:element - Inside--><tns:problemProductName>
            <xsl:value-of select="./rsd:problemProductName"/>
         </tns:problemProductName>
         <!--xsd:element - Inside--><tns:problemProductAmount>
            <xsl:value-of select="./rsd:problemProductAmount"/>
         </tns:problemProductAmount>
         <!--xsd:element - Inside--><tns:problemProductCurrency>
            <xsl:value-of select="./rsd:problemProductCurrency"/>
         </tns:problemProductCurrency>
         <!--xsd:element - Inside--><tns:problemProductTerm>
            <xsl:value-of select="./rsd:problemProductTerm"/>
         </tns:problemProductTerm>
      </tns:problemCriterias>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/:Deal--><xsl:template match="rsd:deal">
      <tns:deal><!--xsd:element - Inside--><tns:dealID>
            <xsl:value-of select="./rsd:dealID"/>
         </tns:dealID>
         <!--xsd:element - Inside--><xsl:if test="./rsd:contractID">
            <tns:contractID>
               <xsl:value-of select="./rsd:contractID"/>
            </tns:contractID>
         </xsl:if>
         <!--xsd:element - Inside--><tns:dealCreationDate>
            <xsl:value-of select="./rsd:dealCreationDate"/>
         </tns:dealCreationDate>
         <!--xsd:element - Inside--><tns:dealEssense>
            <xsl:value-of select="./rsd:dealEssense"/>
         </tns:dealEssense>
         <!--xsd:element - Inside--><tns:requestPurpose>
            <xsl:value-of select="./rsd:requestPurpose"/>
         </tns:requestPurpose>
         <!--xsd:element - Inside--><tns:dealType>
            <xsl:value-of select="./rsd:dealType"/>
         </tns:dealType>
         <!--xsd:element - Inside--><tns:dealStatus>
            <xsl:value-of select="./rsd:dealStatus"/>
         </tns:dealStatus>
         <!--xsd:element - Inside--><tns:requestDate>
            <xsl:value-of select="./rsd:requestDate"/>
         </tns:requestDate>
         <!--xsd:element - Inside--><tns:origOrgUnit>
            <xsl:value-of select="./rsd:origOrgUnit"/>
         </tns:origOrgUnit>
         <!--xsd:element - Inside--><tns:origOrgDivision>
            <xsl:value-of select="./rsd:origOrgDivision"/>
         </tns:origOrgDivision>
         <!--xsd:element - Inside--><tns:codeCBS>
            <xsl:value-of select="./rsd:codeCBS"/>
         </tns:codeCBS>
         <!--xsd:element - Inside--><tns:clientFileID>
            <xsl:value-of select="./rsd:clientFileID"/>
         </tns:clientFileID>
         <!--xsd:element - Inside--><tns:dealFileID>
            <xsl:value-of select="./rsd:dealFileID"/>
         </tns:dealFileID>
         <!--xsd:element - Inside--><xsl:if test="./rsd:comment">
            <tns:comment>
               <xsl:value-of select="./rsd:comment"/>
            </tns:comment>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:collateralProvided">
            <tns:collateralProvided>
               <xsl:value-of select="./rsd:collateralProvided"/>
            </tns:collateralProvided>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:limitKM">
            <tns:limitKM>
               <xsl:value-of select="./rsd:limitKM"/>
            </tns:limitKM>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside DealTeamMember--><xsl:apply-templates select="./rsd:dealTeamMembers"/>
         <!-- xsd:element[$typesList] - Inside ProductConditions--><xsl:apply-templates select="./rsd:products"/>
         <!-- xsd:element[$typesList] - Inside Project--><xsl:apply-templates select="./rsd:project"/>
         <!-- xsd:element[$typesList] - Inside StopFactor--><xsl:apply-templates select="./rsd:stopFactors"/>
         <!-- xsd:element[$typesList] - Inside ProblemCriteria--><xsl:apply-templates select="./rsd:problemCriterias"/>
      </tns:deal>
   </xsl:template>

   <!--xsd:complexType - template :UpdateDealRq--><!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/CRM/UpdateDealRq/1.15/-http://sbrf.ru/NCP/CRM/--><xsl:template name="updateDealRq">
      <xsl:param name="request"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:updateDealRq"><!-- xsd:element[$typesList] - Inside Deal--><xsl:apply-templates select="$data/rsd:request[@name=$request]/rsd:deal"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
