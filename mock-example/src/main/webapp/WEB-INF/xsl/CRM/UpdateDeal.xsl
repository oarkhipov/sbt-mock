<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/UpdateDealRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/UpdateDealRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:param name="name" select="all"/>
    <!-- OH MY GOD! -->
    <!-- IT'S A -->
    <!-- DRIVER!!! -->

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:element name="soap-env:Envelope">
            <xsl:choose>
                <xsl:when test="soap-env:Header"><xsl:copy-of select="soap-env:Header"/></xsl:when>
                <xsl:otherwise><soap-env:Header/></xsl:otherwise>
            </xsl:choose>
            <soap-env:Body>
                <!--xsl:variable name="data" select="document('../../data/CRM/xml/UpdateDealData.xml')/rsd:data"/-->
                <xsl:variable name="data" select="."/>
                <xsl:variable name="linkedTag" select="$name"/>
                <xsl:call-template name="UpdateDealRs">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
                            <xsl:otherwise>default</xsl:otherwise>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

    <!--Fill tags with data from data.xml (0..1)-->
    <xsl:template match="rsd:errorMessage">
        <rs:errorMessage>
            <xsl:value-of select="."/>
        </rs:errorMessage>
    </xsl:template>

    <xsl:template match="rsd:collateral">
        <rq:collateral>
            <rq:collateralType><xsl:value-of select="./rsd:collateralType"/></rq:collateralType>
            <rq:ownerID><xsl:value-of select="./rsd:ownerID"/></rq:ownerID>
            <rq:estimatedValue><xsl:value-of select="./rsd:estimatedValue"/></rq:estimatedValue>
            <rq:discountRate><xsl:value-of select="./rsd:discountRate"/></rq:discountRate>
            <rq:estimatedValueCorrected><xsl:value-of select="./rsd:estimatedValueCorrected"/></rq:estimatedValueCorrected>
            <rq:qualityCategory><xsl:value-of select="./rsd:qualityCategory"/></rq:qualityCategory>
            <rq:daysForCalculation><xsl:value-of select="./rsd:daysForCalculation"/></rq:daysForCalculation>
            <rq:estimatedValueCorrectedToEAD><xsl:value-of select="./rsd:estimatedValueCorrectedToEAD"/></rq:estimatedValueCorrectedToEAD>
            <rq:securityShare><xsl:value-of select="./rsd:securityShare"/></rq:securityShare>
            <rq:EstimatedValueSource><xsl:value-of select="./rsd:EstimatedValueSource"/></rq:EstimatedValueSource>
            <rq:estimationDate><xsl:value-of select="./rsd:estimationDate"/></rq:estimationDate>
            <rq:businessMeaning><xsl:value-of select="./rsd:businessMeaning"/></rq:businessMeaning>
            <rq:qualityAndLiquidityEstimation><xsl:value-of select="./rsd:qualityAndLiquidityEstimation"/></rq:qualityAndLiquidityEstimation>
            <rq:currency><xsl:value-of select="./rsd:currency"/></rq:currency>
            <rq:estimatedValueType><xsl:value-of select="./rsd:estimatedValueType"/></rq:estimatedValueType>
            <rq:guarantorRating><xsl:value-of select="./rsd:guarantorRating"/></rq:guarantorRating>
        </rq:collateral>
    </xsl:template>

    <xsl:template match="rsd:fee">
        <rq:fee>
            <rq:feeName><xsl:value-of select="./rsd:feeName"/></rq:feeName>
            <rq:anotherFeeName><xsl:value-of select="./rsd:anotherFeeName"/></rq:anotherFeeName>
            <rq:calculationBase><xsl:value-of select="./rsd:calculationBase"/></rq:calculationBase>
            <rq:paymentOrder><xsl:value-of select="./rsd:paymentOrder"/></rq:paymentOrder>
        </rq:fee>
    </xsl:template>

    <xsl:template match="rsd:repaymentScheduleDetails">
        <rq:repaymentScheduleDetails>
            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
        </rq:repaymentScheduleDetails>
    </xsl:template>

    <xsl:template match="rsd:selectionScheduleDetails">
        <rq:selectionScheduleDetails>
            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
        </rq:selectionScheduleDetails>
    </xsl:template>

    <xsl:template match="rsd:changesScheduleDetails">
        <rq:changesScheduleDetails>
            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
        </rq:changesScheduleDetails>
    </xsl:template>

    <xsl:template match="rsd:productInfo">
        <rq:productInfo>
            <rq:purpose><xsl:value-of select="./rsd:purpose"/></rq:purpose>
            <rq:creditMode><xsl:value-of select="./rsd:creditMode"/></rq:creditMode>
            <rq:currencyOfFunding><xsl:value-of select="./rsd:currencyOfFunding"/></rq:currencyOfFunding>
            <rq:currency><xsl:value-of select="./rsd:currency"/></rq:currency>
            <rq:interestRateType><xsl:value-of select="./rsd:interestRateType"/></rq:interestRateType>
            <rq:selectionSchedule><xsl:value-of select="./rsd:selectionSchedule"/></rq:selectionSchedule>
            <rq:changesSchedule><xsl:value-of select="./rsd:changesSchedule"/></rq:changesSchedule>
            <rq:selectionSchedulePeriod><xsl:value-of select="./rsd:selectionSchedulePeriod"/></rq:selectionSchedulePeriod>
            <rq:changesSchedulePeriod><xsl:value-of select="./rsd:changesSchedulePeriod"/></rq:changesSchedulePeriod>
            <rq:interestRateOrder><xsl:value-of select="./rsd:interestRateOrder"/></rq:interestRateOrder>
            <rq:interestPaymentOrder><xsl:value-of select="./rsd:interestPaymentOrder"/></rq:interestPaymentOrder>
            <rq:refinancing><xsl:value-of select="./rsd:refinancing"/></rq:refinancing>
            <rq:providing><xsl:value-of select="./rsd:providing"/></rq:providing>
            <rq:suspensiveСondition><xsl:value-of select="./rsd:suspensiveСondition"/></rq:suspensiveСondition>
            <rq:principal><xsl:value-of select="./rsd:principal"/></rq:principal>
            <rq:principalBaseCurrency><xsl:value-of select="./rsd:principalBaseCurrency"/></rq:principalBaseCurrency>
            <rq:startDate><xsl:value-of select="./rsd:startDate"/></rq:startDate>
            <rq:interestRate><xsl:value-of select="./rsd:interestRate"/></rq:interestRate>
            <rq:creditTerm><xsl:value-of select="./rsd:creditTerm"/></rq:creditTerm>
            <rq:period><xsl:value-of select="./rsd:period"/></rq:period>
            <rq:gracePeriod><xsl:value-of select="./rsd:gracePeriod"/></rq:gracePeriod>
            <rq:repaymentSchedule><xsl:value-of select="./rsd:repaymentSchedule"/></rq:repaymentSchedule>
            <rq:turnover><xsl:value-of select="./rsd:turnover"/></rq:turnover>
            <rq:covenants><xsl:value-of select="./rsd:covenants"/></rq:covenants>
            <rq:minRate><xsl:value-of select="./rsd:minRate"/></rq:minRate>
            <rq:proposedCollateral><xsl:value-of select="./rsd:proposedCollateral"/></rq:proposedCollateral>
            <rq:debtLimit><xsl:value-of select="./rsd:debtLimit"/></rq:debtLimit>
            <rq:pledgeInsurance><xsl:value-of select="./rsd:pledgeInsurance"/></rq:pledgeInsurance>
            <rq:customerProspects><xsl:value-of select="./rsd:customerProspects"/></rq:customerProspects>
            <rq:financingTerm><xsl:value-of select="./rsd:financingTerm"/></rq:financingTerm>
            <rq:firstCountry><xsl:value-of select="./rsd:firstCountry"/></rq:firstCountry>
            <rq:lastCountry><xsl:value-of select="./rsd:lastCountry"/></rq:lastCountry>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:fee"/>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:repaymentScheduleDetails"/>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:selectionScheduleDetails"/>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:changesScheduleDetails"/>
        </rq:productInfo>
    </xsl:template>

    <xsl:template match="rsd:products">
        <rq:products>
            <rq:productID><xsl:value-of select="./rsd:productID"/></rq:productID>
            <rq:productName><xsl:value-of select="./rsd:productName"/></rq:productName>
            <rq:category><xsl:value-of select="./rsd:category"/></rq:category>
            <!--1 or more repetitions:-->
            <xsl:apply-templates select="./rsd:productInfo"/>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:collateral"/>
            <!--Optional:-->
            <xsl:if test="./rsd:losses">
                <rq:losses>
                    <rq:percentLGD><xsl:value-of select="./rsd:losses/rsd:percentLGD"/></rq:percentLGD>
                    <rq:countEAD><xsl:value-of select="./rsd:losses/rsd:countEAD"/></rq:countEAD>
                    <rq:currency><xsl:value-of select="./rsd:losses/rsd:currency"/></rq:currency>
                    <rq:reportDateAmount><xsl:value-of select="./rsd:losses/rsd:reportDateAmount"/></rq:reportDateAmount>
                    <rq:lossELpercent><xsl:value-of select="./rsd:losses/rsd:lossELpercent"/></rq:lossELpercent>
                    <rq:lossELcount><xsl:value-of select="./rsd:losses/rsd:lossELcount"/></rq:lossELcount>
                    <rq:typeLGD><xsl:value-of select="./rsd:losses/rsd:typeLGD"/></rq:typeLGD>
                    <rq:statusLGD><xsl:value-of select="./rsd:losses/rsd:statusLGD"/></rq:statusLGD>
                    <rq:dateLGDcalculated><xsl:value-of select="./rsd:losses/rsd:dateLGDcalculated"/></rq:dateLGDcalculated>
                    <rq:finalizationDate><xsl:value-of select="./rsd:losses/rsd:finalizationDate"/></rq:finalizationDate>
                    <rq:comment><xsl:value-of select="./rsd:losses/rsd:comment"/></rq:comment>
                </rq:losses>
            </xsl:if>
        </rq:products>
    </xsl:template>


    <xsl:template match="rsd:projectRating">
        <rq:projectRating>
            <rq:ratingID><xsl:value-of select="./rsd:ratingID"/></rq:ratingID>
            <rq:ratingValue><xsl:value-of select="./rsd:ratingValue"/></rq:ratingValue>
            <rq:ratingType><xsl:value-of select="./rsd:ratingType"/></rq:ratingType>
            <rq:ratingModel><xsl:value-of select="./rsd:ratingModel"/></rq:ratingModel>
            <rq:ratingCalculatedDate><xsl:value-of select="./rsd:ratingCalculatedDate"/></rq:ratingCalculatedDate>
            <rq:ratingCalculatedRole><xsl:value-of select="./rsd:ratingCalculatedRole"/></rq:ratingCalculatedRole>
            <rq:ratingCalculatedBy><xsl:value-of select="./rsd:ratingCalculatedBy"/></rq:ratingCalculatedBy>
            <rq:ratingApprovalDate><xsl:value-of select="./rsd:ratingApprovalDate"/></rq:ratingApprovalDate>
            <rq:ratingApprovedBy><xsl:value-of select="./rsd:ratingApprovedBy"/></rq:ratingApprovedBy>
        </rq:projectRating>
    </xsl:template>

    <xsl:template match="rsd:exchangeRates">
        <rq:exchangeRates>
            <rq:currencyName><xsl:value-of select="./rsd:currencyName"/></rq:currencyName>
            <rq:currencyValue><xsl:value-of select="./rsd:currencyValue"/></rq:currencyValue>
            <rq:currencyDate><xsl:value-of select="./rsd:currencyDate"/></rq:currencyDate>
        </rq:exchangeRates>
    </xsl:template>

    <xsl:template match="rsd:deal">
        <rq:deal>
            <rq:dealID><xsl:value-of select="./rsd:dealID"/></rq:dealID>
            <!--Optional:-->
            <xsl:if test="./rsd:contractID">
                <rq:contractID><xsl:value-of select="./rsd:contractID"/></rq:contractID>
            </xsl:if>
            <xsl:if test="./rsd:dealCreationDate">
                <rq:dealCreationDate><xsl:value-of select="./rsd:dealCreationDate"/></rq:dealCreationDate>
            </xsl:if>
            <xsl:if test="./rsd:dealEssense">
                <rq:dealEssense><xsl:value-of select="./rsd:dealEssense"/></rq:dealEssense>
            </xsl:if>
            <xsl:if test="./rsd:requestPurpose">
                <rq:requestPurpose><xsl:value-of select="./rsd:requestPurpose"/></rq:requestPurpose>
            </xsl:if>
            <xsl:if test="./rsd:salesMethod">
                <rq:salesMethod><xsl:value-of select="./rsd:salesMethod"/></rq:salesMethod>
            </xsl:if>
            <xsl:if test="./rsd:dealStatus">
                <rq:dealStatus><xsl:value-of select="./rsd:dealStatus"/></rq:dealStatus>
            </xsl:if>
            <xsl:if test="./rsd:requestDate">
                <rq:requestDate><xsl:value-of select="./rsd:requestDate"/></rq:requestDate>
            </xsl:if>
            <xsl:if test="./rsd:origOrgUnit">
                <rq:origOrgUnit><xsl:value-of select="./rsd:origOrgUnit"/></rq:origOrgUnit>
            </xsl:if>
            <xsl:if test="./rsd:origOrgDivision">
                <rq:origOrgDivision><xsl:value-of select="./rsd:origOrgDivision"/></rq:origOrgDivision>
            </xsl:if>
            <xsl:if test="./rsd:clientFileID">
                <rq:clientFileID><xsl:value-of select="./rsd:clientFileID"/></rq:clientFileID>
            </xsl:if>
            <xsl:if test="./rsd:dealFileID">
                <rq:dealFileID><xsl:value-of select="./rsd:dealFileID"/></rq:dealFileID>
            </xsl:if>
            <xsl:if test="./rsd:comment">
                <rq:comment><xsl:value-of select="./rsd:comment"/></rq:comment>
            </xsl:if>
            <xsl:if test="./rsd:supLMID">
                <rq:supLMID><xsl:value-of select="./rsd:supLMID"/></rq:supLMID>
            </xsl:if>
            <xsl:if test="./rsd:clientManagerID">
                <rq:clientManagerID><xsl:value-of select="./rsd:clientManagerID"/></rq:clientManagerID>
            </xsl:if>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:products"/>
            <rq:project>
                <rq:projectName><xsl:value-of select="./rsd:project/rsd:projectName"/></rq:projectName>
                <rq:indicatorsOfConstruction><xsl:value-of select="./rsd:project/rsd:indicatorsOfConstruction"/></rq:indicatorsOfConstruction>
                <rq:parcel><xsl:value-of select="./rsd:project/rsd:parcel"/></rq:parcel>
                <rq:documentsPresence><xsl:value-of select="./rsd:project/rsd:documentsPresence"/></rq:documentsPresence>
                <rq:equityVolume><xsl:value-of select="./rsd:project/rsd:equityVolume"/></rq:equityVolume>
                <rq:repaymentSpring><xsl:value-of select="./rsd:project/rsd:repaymentSpring"/></rq:repaymentSpring>
                <rq:projectVariant><xsl:value-of select="./rsd:project/rsd:projectVariant"/></rq:projectVariant>
                <rq:sameIndustryAsBorrower><xsl:value-of select="./rsd:project/rsd:sameIndustryAsBorrower"/></rq:sameIndustryAsBorrower>
                <rq:collateralProvided><xsl:value-of select="./rsd:project/rsd:collateralProvided"/></rq:collateralProvided>
                <!--Zero or more repetitions:-->
                <xsl:apply-templates select="./rsd:project/rsd:projectRating"/>
            </rq:project>
            <rq:baseCurrency><xsl:value-of select="./rsd:baseCurrency"/></rq:baseCurrency>
            <!--Zero or more repetitions:-->
            <xsl:apply-templates select="./rsd:exchangeRates"/>
        </rq:deal>
    </xsl:template>

    <!--Transform main XML-->
    <xsl:template name="UpdateDealRs">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:updateDealRq xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:upd="http://sbrf.ru/NCP/CRM/UpdateDealRq/">
            <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal"/>
        </crm:updateDealRq>
    </xsl:template>

</xsl:stylesheet>