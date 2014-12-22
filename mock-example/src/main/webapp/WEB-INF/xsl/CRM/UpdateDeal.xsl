<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.04/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/UpdateDealRq/1.04/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRq/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">
    <xsl:import href="../NCPSoapRqHeaderXSLTTemplate.xsl"/>

    <!-- опускаем строку <?xml version="1.0" encoding="UTF-8"?>. С ней не работает MQ очередь -->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <!--<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>-->


    <xsl:param name="name" select="all"/>
    <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
    <xsl:param name="id" select="null"/>

    <!-- Optional params for optional header values -->
    <xsl:param name="correlation-id" select="null"/>
    <xsl:param name="eis-name" select="null"/>
    <xsl:param name="system-id" select="null"/>
    <xsl:param name="operation-version" select="null"/>
    <xsl:param name="user-id" select="null"/>
    <xsl:param name="user-name" select="null"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:variable name="data" select="."/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="soap-env:Envelope">
            <xsl:call-template name="NCPHeader">
                <xsl:with-param name="response">
                    <xsl:choose>
                        <xsl:when test="count(./rsd:request[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
                        <xsl:otherwise>default</xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
                <xsl:with-param name="timestamp" select="$timestamp"/>
                <xsl:with-param name="id" select="$id"/>
                <!-- Здесь нужно встаивть имя операции -->
                <xsl:with-param name="operation-name" select="string('UpdateDeal')"/>
                <xsl:with-param name="correlation-id" select="$correlation-id"/>
                <xsl:with-param name="eis-name" select="$eis-name"/>
                <xsl:with-param name="system-id" select="$system-id"/>
                <xsl:with-param name="operation-version" select="$operation-version"/>
                <xsl:with-param name="user-id" select="$user-id"/>
                <xsl:with-param name="user-name" select="$user-name"/>
            </xsl:call-template>
            <soap-env:Body>
                <xsl:call-template name="forceSignal">
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

    <xsl:template match="rsd:dealTeamMembers">
        <rq:dealTeamMembers>
            <rq:memberId><xsl:value-of select="./rsd:memberId"/></rq:memberId>
            <rq:memberName><xsl:value-of select="./rsd:memberName"/></rq:memberName>
            <rq:mamberEmail><xsl:value-of select="./rsd:mamberEmail"/></rq:mamberEmail>
            <rq:mamberRole><xsl:value-of select="./rsd:mamberRole"/></rq:mamberRole>
        </rq:dealTeamMembers>
    </xsl:template>

    <xsl:template match="rsd:productInfo">
        <rq:productInfo>
            <rq:productVersion><xsl:value-of select="./rsd:productVersion"/></rq:productVersion>
            <rq:purpose><xsl:value-of select="./rsd:purpose"/></rq:purpose>
            <rq:creditMode><xsl:value-of select="./rsd:creditMode"/></rq:creditMode>
            <rq:selectionSchedule><xsl:value-of select="./rsd:selectionSchedule"/></rq:selectionSchedule>
            <rq:changesSchedule><xsl:value-of select="./rsd:changesSchedule"/></rq:changesSchedule>
            <!--Optional:-->
            <xsl:if test="./rsd:selectionSchedulePeriod">
                <rq:selectionSchedulePeriod><xsl:value-of select="./rsd:selectionSchedulePeriod"/></rq:selectionSchedulePeriod>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:changesSchedulePeriod">
                <rq:changesSchedulePeriod><xsl:value-of select="./rsd:changesSchedulePeriod"/></rq:changesSchedulePeriod>
            </xsl:if>
            <rq:interestPaymentOrder><xsl:value-of select="./rsd:interestPaymentOrder"/></rq:interestPaymentOrder>
            <rq:refinancing><xsl:value-of select="./rsd:refinancing"/></rq:refinancing>
            <rq:providing><xsl:value-of select="./rsd:providing"/></rq:providing>
            <!--Optional:-->
            <xsl:if test="./rsd:suspensiveСondition">
                <rq:suspensiveСondition><xsl:value-of select="./rsd:suspensiveСondition"/></rq:suspensiveСondition>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:principalBaseCurrency">
                <rq:principalBaseCurrency><xsl:value-of select="./rsd:principalBaseCurrency"/></rq:principalBaseCurrency>
            </xsl:if>
            <!--1 or more repetitiorq:-->
            <xsl:apply-templates select="./financingInfo"/>
            <!--1 or more repetitiorq:-->
            <xsl:apply-templates select="./interestRateInfo"/>
            <!--Optional:-->
            <xsl:if test="./rsd:startDate">
                <rq:startDate><xsl:value-of select="./rsd:startDate"/></rq:startDate>
            </xsl:if>
            <rq:creditTerm><xsl:value-of select="./rsd:creditTerm"/></rq:creditTerm>
            <rq:period><xsl:value-of select="./rsd:period"/></rq:period>
            <!--Optional:-->
            <xsl:if test="./rsd:gracePeriod">
                <rq:gracePeriod><xsl:value-of select="./rsd:gracePeriod"/></rq:gracePeriod>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:repaymentSchedule">
                <rq:repaymentSchedule><xsl:value-of select="./rsd:repaymentSchedule"/></rq:repaymentSchedule>
            </xsl:if>
            <rq:turnover><xsl:value-of select="./rsd:turnover"/></rq:turnover>
            <rq:covenants><xsl:value-of select="./rsd:covenants"/></rq:covenants>
            <!--Optional:-->
            <xsl:if test="./rsd:proposedCollateral">
                <rq:proposedCollateral><xsl:value-of select="./rsd:proposedCollateral"/></rq:proposedCollateral>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:debtLimit">
                <rq:debtLimit><xsl:value-of select="./rsd:debtLimit"/></rq:debtLimit>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:pledgeInsurance">
                <rq:pledgeInsurance><xsl:value-of select="./rsd:pledgeInsurance"/></rq:pledgeInsurance>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:customerProspects">
                <rq:customerProspects><xsl:value-of select="./rsd:customerProspects"/></rq:customerProspects>
            </xsl:if>
            <rq:financingTerm><xsl:value-of select="./rsd:financingTerm"/></rq:financingTerm>
            <!--Optional:-->
            <xsl:if test="./rsd:firstCountry">
                <rq:firstCountry><xsl:value-of select="./rsd:firstCountry"/></rq:firstCountry>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:lastCountry">
                <rq:lastCountry><xsl:value-of select="./rsd:lastCountry"/></rq:lastCountry>
            </xsl:if>
            <!--Zero or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:fee"/>
            <!--Zero or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:repaymentScheduleDetails"/>
            <!--Zero or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:selectionScheduleDetails"/>
            <!--Zero or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:changesScheduleDetails"/>
        </rq:productInfo>
    </xsl:template>

    <xsl:template match="rsd:financingInfo">
        <rq:financingInfo>
            <rq:currencyOfFunding><xsl:value-of select="./rsd:currencyOfFunding"/></rq:currencyOfFunding>
            <rq:principal><xsl:value-of select="./rsd:principal"/></rq:principal>
        </rq:financingInfo>
    </xsl:template>

    <xsl:template match="rsd:interestRateInfo">
        <rq:interestRateInfo>
            <rq:interestRate><xsl:value-of select="./rsd:interestRate"/></rq:interestRate>
            <rq:interestRateType><xsl:value-of select="./rsd:interestRateType"/></rq:interestRateType>
            <rq:interestRateOrder><xsl:value-of select="./rsd:interestRateOrder"/></rq:interestRateOrder>
            <rq:minRate><xsl:value-of select="./rsd:minRate"/></rq:minRate>
        </rq:interestRateInfo>
    </xsl:template>

    <xsl:template match="rsd:fee">
        <rq:fee>
            <rq:feeName><xsl:value-of select="./rsd:feeName"/></rq:feeName>
            <!--Optional:-->
            <xsl:if test="./rsd:anotherFeeName">
                <rq:anotherFeeName><xsl:value-of select="./rsd:anotherFeeName"/></rq:anotherFeeName>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:calculationBase">
                <rq:calculationBase><xsl:value-of select="./rsd:calculationBase"/></rq:calculationBase>
            </xsl:if>
            <!--Optional:-->
            <xsl:if test="./rsd:paymentOrder">
                <rq:paymentOrder><xsl:value-of select="./rsd:paymentOrder"/></rq:paymentOrder>
            </xsl:if>
        </rq:fee>
    </xsl:template>

    <xsl:template match="rsd:products">
        <rq:products>
            <rq:productID><xsl:value-of select="./rsd:productID"/></rq:productID>
            <rq:productName><xsl:value-of select="./rsd:productName"/></rq:productName>
            <rq:category><xsl:value-of select="./rsd:category"/></rq:category>
            <!--1 or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:productInfo"/>
            <!--Zero or more repetitiorq:-->
            <xsl:apply-templates select="./rsd:collateral"/>
            <!--Optional:-->
            <xsl:if test="./rsd:losses">
                <rq:losses>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:percentLGD">
                        <rq:percentLGD><xsl:value-of select="./rsd:losses/rsd:percentLGD"/></rq:percentLGD>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:countEAD">
                        <rq:countEAD><xsl:value-of select="./rsd:losses/rsd:countEAD"/></rq:countEAD>
                    </xsl:if>
                    <rq:currency><xsl:value-of select="./rsd:losses/rsd:currency"/></rq:currency>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:reportDateAmount">
                        <rq:reportDateAmount><xsl:value-of select="./rsd:losses/rsd:reportDateAmount"/></rq:reportDateAmount>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:lossELpercent">
                        <rq:lossELpercent><xsl:value-of select="./rsd:losses/rsd:lossELpercent"/></rq:lossELpercent>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:lossELcount">
                        <rq:lossELcount><xsl:value-of select="./rsd:losses/rsd:lossELcount"/></rq:lossELcount>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:typeLGD">
                        <rq:typeLGD><xsl:value-of select="./rsd:losses/rsd:typeLGD"/></rq:typeLGD>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:statusLGD">
                        <rq:statusLGD><xsl:value-of select="./rsd:losses/rsd:statusLGD"/></rq:statusLGD>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:dateLGDcalculated">
                        <rq:dateLGDcalculated><xsl:value-of select="./rsd:losses/rsd:dateLGDcalculated"/></rq:dateLGDcalculated>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:finalizationDate">
                        <rq:finalizationDate><xsl:value-of select="./rsd:losses/rsd:finalizationDate"/></rq:finalizationDate>
                    </xsl:if>
                    <!--Optional:-->
                    <xsl:if test="./rsd:losses/rsd:comment">
                        <rq:comment><xsl:value-of select="./rsd:losses/rsd:comment"/></rq:comment>
                    </xsl:if>
                </rq:losses>
            </xsl:if>
        </rq:products>
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

    <!--Transform main XML-->
    <xsl:template name="forceSignal">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:updateDealRq>
            <rq:deal>
                <rq:dealID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealID"/></rq:dealID>
                <!--Optional:-->
                <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:contractID">
                    <rq:contractID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:contractID"/></rq:contractID>
                </xsl:if>
                <rq:dealCreationDate><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealCreationDate"/></rq:dealCreationDate>
                <rq:dealEssense><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealEssense"/></rq:dealEssense>
                <rq:requestPurpose><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:requestPurpose"/></rq:requestPurpose>
                <rq:dealType><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealType"/></rq:dealType>
                <rq:dealStatus><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealStatus"/></rq:dealStatus>
                <rq:requestDate><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:requestDate"/></rq:requestDate>
                <rq:origOrgUnit><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:origOrgUnit"/></rq:origOrgUnit>
                <rq:origOrgDivision><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:origOrgDivision"/></rq:origOrgDivision>
                <rq:clientFileID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:clientFileID"/></rq:clientFileID>
                <rq:dealFileID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealFileID"/></rq:dealFileID>
                <!--Optional:-->
                <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:comment">
                    <rq:comment><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:comment"/></rq:comment>
                </xsl:if>
                <rq:supLMID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:supLMID"/></rq:supLMID>
                <rq:collateralProvided><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:collateralProvided"/></rq:collateralProvided>
                <rq:limitKM><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:limitKM"/></rq:limitKM>
                <!--1 or more repetitiorq:-->
                <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal/rsd:dealTeamMembers"/>
                <!--Zero or more repetitiorq:-->
                <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal/rsd:products"/>
                <!--Optional:-->
                <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project">
                    <rq:project>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:projectName">
                            <rq:projectName><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:projectName"/></rq:projectName>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:indicatorsOfConstruction">
                            <rq:indicatorsOfConstruction><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:indicatorsOfConstruction"/></rq:indicatorsOfConstruction>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:parcel">
                            <rq:parcel><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:parcel"/></rq:parcel>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:documentsPresence">
                            <rq:documentsPresence><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:documentsPresence"/></rq:documentsPresence>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:equityVolume">
                            <rq:equityVolume><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:equityVolume"/></rq:equityVolume>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:repaymentSpring">
                            <rq:repaymentSpring><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:repaymentSpring"/></rq:repaymentSpring>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:fundingDirection">
                            <rq:fundingDirection><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:fundingDirection"/></rq:fundingDirection>
                        </xsl:if>
                        <!--Optional:-->
                        <xsl:if test="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:sameIndustryAsBorrower">
                            <rq:sameIndustryAsBorrower><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:sameIndustryAsBorrower"/></rq:sameIndustryAsBorrower>
                        </xsl:if>
                        <!--Zero or more repetitiorq:-->
                        <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal/rsd:project/rsd:projectRating"/>
                    </rq:project>
                </xsl:if>
                <rq:baseCurrency><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:deal/rsd:baseCurrency"/></rq:baseCurrency>
                <!--Zero or more repetitiorq:-->
                <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:deal/rsd:exchangeRates"/>
            </rq:deal>
        </crm:updateDealRq>
    </xsl:template>

</xsl:stylesheet>