<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/UpdateDealRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/UpdateDealRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!-- OH MY GOD! -->
    <!-- IT'S A -->
    <!-- DRIVER!!! -->

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <!--xsl:variable name="data" select="document('../../data/CRM/xml/UpdateDealData.xml')/rsd:data"/-->
                <xsl:variable name="data" select="."/>
                <xsl:variable name="linkedTag" select="./soap-env:Body/crm:UpdateDealRq/rq:comment"/>
                <xsl:call-template name="UpdateDealRs">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
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

    <!--Transform main XML-->
    <xsl:template name="UpdateDealRs">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:updateDealRq xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:upd="http://sbrf.ru/NCP/CRM/UpdateDealRq/">
            <xsl:for-each select="$data/rsd:response[@name=$response]/rsd:deal">
                <rq:deal>
                    <rq:dealID><xsl:value-of select="./rsd:dealID"/></rq:dealID>
                    <!--Optional:-->
                    <rq:contractID><xsl:value-of select="./rsd:contractID"/></rq:contractID>
                    <rq:dealCreationDate><xsl:value-of select="./rsd:dealCreationDate"/></rq:dealCreationDate>
                    <rq:dealEssense><xsl:value-of select="./rsd:dealEssense"/></rq:dealEssense>
                    <rq:requestPurpose><xsl:value-of select="./rsd:requestPurpose"/></rq:requestPurpose>
                    <rq:salesMethod><xsl:value-of select="./rsd:salesMethod"/></rq:salesMethod>
                    <rq:dealStatus><xsl:value-of select="./rsd:dealStatus"/></rq:dealStatus>
                    <rq:requestDate><xsl:value-of select="./rsd:requestDate"/></rq:requestDate>
                    <rq:origOrgUnit><xsl:value-of select="./rsd:origOrgUnit"/></rq:origOrgUnit>
                    <rq:origOrgDivision><xsl:value-of select="./rsd:origOrgDivision"/></rq:origOrgDivision>
                    <rq:clientFileID><xsl:value-of select="./rsd:clientFileID"/></rq:clientFileID>
                    <rq:dealFileID><xsl:value-of select="./rsd:dealFileID"/></rq:dealFileID>
                    <rq:comment><xsl:value-of select="./rsd:comment"/></rq:comment>
                    <rq:supLMID><xsl:value-of select="./rsd:supLMID"/></rq:supLMID>
                    <rq:clientManagerID><xsl:value-of select="./rsd:clientManagerID"/></rq:clientManagerID>
                    <!--Zero or more repetitions:-->
                    <xsl:for-each select="./rsd:products">
                        <rq:products>
                            <rq:productID><xsl:value-of select="./rsd:productID"/></rq:productID>
                            <rq:productName><xsl:value-of select="./rsd:productName"/></rq:productName>
                            <rq:category><xsl:value-of select="./rsd:category"/></rq:category>
                            <!--1 or more repetitions:-->
                            <xsl:for-each select="./rsd:productInfo">
                                <rq:productInfo>
                                    <rq:purpose><xsl:value-of select="./rsd:category"/></rq:purpose>
                                    <rq:creditMode><xsl:value-of select="./rsd:category"/></rq:creditMode>
                                    <rq:currencyOfFunding><xsl:value-of select="./rsd:category"/></rq:currencyOfFunding>
                                    <rq:currency><xsl:value-of select="./rsd:category"/></rq:currency>
                                    <rq:interestRateType><xsl:value-of select="./rsd:category"/></rq:interestRateType>
                                    <rq:selectionSchedule><xsl:value-of select="./rsd:category"/></rq:selectionSchedule>
                                    <rq:changesSchedule><xsl:value-of select="./rsd:category"/></rq:changesSchedule>
                                    <rq:selectionSchedulePeriod><xsl:value-of select="./rsd:category"/></rq:selectionSchedulePeriod>
                                    <rq:changesSchedulePeriod><xsl:value-of select="./rsd:category"/></rq:changesSchedulePeriod>
                                    <rq:interestRateOrder><xsl:value-of select="./rsd:category"/></rq:interestRateOrder>
                                    <rq:interestPaymentOrder><xsl:value-of select="./rsd:category"/></rq:interestPaymentOrder>
                                    <rq:refinancing><xsl:value-of select="./rsd:category"/></rq:refinancing>
                                    <rq:providing><xsl:value-of select="./rsd:category"/></rq:providing>
                                    <rq:suspensiveСondition><xsl:value-of select="./rsd:category"/></rq:suspensiveСondition>
                                    <rq:principal><xsl:value-of select="./rsd:category"/></rq:principal>
                                    <rq:principalBaseCurrency><xsl:value-of select="./rsd:category"/></rq:principalBaseCurrency>
                                    <rq:startDate><xsl:value-of select="./rsd:category"/></rq:startDate>
                                    <rq:interestRate><xsl:value-of select="./rsd:category"/></rq:interestRate>
                                    <rq:creditTerm><xsl:value-of select="./rsd:category"/></rq:creditTerm>
                                    <rq:period><xsl:value-of select="./rsd:category"/></rq:period>
                                    <rq:gracePeriod><xsl:value-of select="./rsd:category"/></rq:gracePeriod>
                                    <rq:repaymentSchedule><xsl:value-of select="./rsd:category"/></rq:repaymentSchedule>
                                    <rq:turnover><xsl:value-of select="./rsd:category"/></rq:turnover>
                                    <rq:covenants><xsl:value-of select="./rsd:category"/></rq:covenants>
                                    <rq:minRate><xsl:value-of select="./rsd:category"/></rq:minRate>
                                    <rq:proposedCollateral><xsl:value-of select="./rsd:category"/></rq:proposedCollateral>
                                    <rq:debtLimit><xsl:value-of select="./rsd:category"/></rq:debtLimit>
                                    <rq:pledgeInsurance><xsl:value-of select="./rsd:category"/></rq:pledgeInsurance>
                                    <rq:customerProspects><xsl:value-of select="./rsd:category"/></rq:customerProspects>
                                    <rq:financingTerm><xsl:value-of select="./rsd:category"/></rq:financingTerm>
                                    <rq:firstCountry><xsl:value-of select="./rsd:category"/></rq:firstCountry>
                                    <rq:lastCountry><xsl:value-of select="./rsd:category"/></rq:lastCountry>
                                    <!--Zero or more repetitions:-->
                                    <xsl:for-each select="./rsd:fee">
                                        <rq:fee>
                                            <rq:feeName><xsl:value-of select="./rsd:feeName"/></rq:feeName>
                                            <rq:anotherFeeName><xsl:value-of select="./rsd:anotherFeeName"/></rq:anotherFeeName>
                                            <rq:calculationBase><xsl:value-of select="./rsd:calculationBase"/></rq:calculationBase>
                                            <rq:paymentOrder><xsl:value-of select="./rsd:paymentOrder"/></rq:paymentOrder>
                                        </rq:fee>
                                    </xsl:for-each>
                                    <!--Zero or more repetitions:-->
                                    <xsl:for-each select="./rsd:repaymentScheduleDetails">
                                        <rq:repaymentScheduleDetails>
                                            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
                                            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
                                        </rq:repaymentScheduleDetails>
                                    </xsl:for-each>
                                    <!--Zero or more repetitions:-->
                                    <xsl:for-each select="./rsd:selectionScheduleDetails">
                                        <rq:selectionScheduleDetails>
                                            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
                                            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
                                        </rq:selectionScheduleDetails>
                                    </xsl:for-each>
                                    <!--Zero or more repetitions:-->
                                    <xsl:for-each select="./rsd:changesScheduleDetails">
                                        <rq:changesScheduleDetails>
                                            <rq:sheduleDate><xsl:value-of select="./rsd:sheduleDate"/></rq:sheduleDate>
                                            <rq:sheduleAmount><xsl:value-of select="./rsd:sheduleAmount"/></rq:sheduleAmount>
                                        </rq:changesScheduleDetails>
                                    </xsl:for-each>
                                </rq:productInfo>
                            </xsl:for-each>
                            <!--Zero or more repetitions:-->
                            <xsl:for-each select="./rsd:collateral">
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
                            </xsl:for-each>
                            <!--Optional:-->
                            <xsl:for-each select="./rsd:losses">
                                <rq:losses>
                                    <rq:percentLGD><xsl:value-of select="./rsd:percentLGD"/></rq:percentLGD>
                                    <rq:countEAD><xsl:value-of select="./rsd:countEAD"/></rq:countEAD>
                                    <rq:currency><xsl:value-of select="./rsd:currency"/></rq:currency>
                                    <rq:reportDateAmount><xsl:value-of select="./rsd:reportDateAmount"/></rq:reportDateAmount>
                                    <rq:lossELpercent><xsl:value-of select="./rsd:lossELpercent"/></rq:lossELpercent>
                                    <rq:lossELcount><xsl:value-of select="./rsd:lossELcount"/></rq:lossELcount>
                                    <rq:typeLGD><xsl:value-of select="./rsd:typeLGD"/></rq:typeLGD>
                                    <rq:statusLGD><xsl:value-of select="./rsd:statusLGD"/></rq:statusLGD>
                                    <rq:dateLGDcalculated><xsl:value-of select="./rsd:dateLGDcalculated"/></rq:dateLGDcalculated>
                                    <rq:finalizationDate><xsl:value-of select="./rsd:finalizationDate"/></rq:finalizationDate>
                                    <rq:comment><xsl:value-of select="./rsd:comment"/></rq:comment>
                                </rq:losses>
                            </xsl:for-each>
                        </rq:products>
                    </xsl:for-each>
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
                        <xsl:for-each select="./rsd:project/rsd:projectRating">
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
                        </xsl:for-each>
                    </rq:project>
                    <rq:baseCurrency><xsl:value-of select="./rsd:baseCurrency"/></rq:baseCurrency>
                    <!--Zero or more repetitions:-->
                    <xsl:for-each select="./rsd:exchangeRates">
                        <rq:exchangeRates>
                            <rq:currencyName><xsl:value-of select="./rsd:currencyName"/></rq:currencyName>
                            <rq:currencyValue><xsl:value-of select="./rsd:currencyValue"/></rq:currencyValue>
                            <rq:currencyDate><xsl:value-of select="./rsd:currencyDate"/></rq:currencyDate>
                        </rq:exchangeRates>
                    </xsl:for-each>
                </rq:deal>
            </xsl:for-each>
        </crm:updateDealRq>
    </xsl:template>

</xsl:stylesheet>