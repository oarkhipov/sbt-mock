<xsl:stylesheet xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/req/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10/Data"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0" xmlns:ns3="http://www.w3.org/1999/XSL/Transform">
    <!--xmlns:ns3="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10"-->

    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="name" select="all"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="//rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="ExportContractRq">
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
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:Comission">
        <xsl:element name="ns2:Comission">
            <xsl:element name="ns2:NameComission">
                <xsl:value-of select="./ns2:NameComission"/>
            </xsl:element>
            <xsl:if test="./ns2:NameComissionOther">
                <xsl:element name="ns2:NameComissionOther">
                    <xsl:value-of select="./ns2:NameComissionOther"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ComissionBase">
                <xsl:value-of select="./ns2:ComissionBase"/>
            </xsl:element>
            <xsl:element name="ns2:PaymentRegulation">
                <xsl:value-of select="./ns2:PaymentRegulation"/>
            </xsl:element>
            <xsl:element name="ns2:PaymentDate">
                <xsl:value-of select="./ns2:PaymentDate"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:AdvancedRepaymentFee">
        <xsl:element name="ns2:AdvancedRepaymentFee">
            <xsl:if test="./ns2:AdvancedRepaymentFeeCondtion">
                <xsl:element name="ns2:AdvancedRepaymentFeeCondtion">
                    <xsl:value-of select="./ns2:AdvancedRepaymentFeeCondtion"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Amount">
                <xsl:element name="ns2:Amount">
                    <xsl:value-of select="./ns2:Amount"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:NotificationDate">
                <xsl:element name="ns2:NotificationDate">
                    <xsl:value-of select="./ns2:NotificationDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:CalcProcedure">
                <xsl:element name="ns2:CalcProcedure">
                    <xsl:value-of select="./ns2:CalcProcedure"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:FreePeriod">
                <xsl:element name="ns2:FreePeriod">
                    <xsl:value-of select="./ns2:FreePeriod"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Comment">
                <xsl:element name="ns2:Comment">
                    <xsl:value-of select="./ns2:Comment"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:TermOfProduct">
        <xsl:element name="ns2:TermOfProduct">
            <xsl:element name="ns2:ProductIntID">
                <xsl:value-of select="./ns2:ProductIntID"/>
            </xsl:element>
            <xsl:element name="ns2:CreditMode">
                <xsl:value-of select="./ns2:CreditMode"/>
            </xsl:element>
            <xsl:element name="ns2:CreditInstrument">
                <xsl:value-of select="./ns2:CreditInstrument"/>
            </xsl:element>
            <xsl:element name="ns2:GeneralAgreement">
                <xsl:value-of select="./ns2:GeneralAgreement"/>
            </xsl:element>
            <xsl:element name="ns2:ProductLimitID">
                <xsl:value-of select="./ns2:ProductLimitID"/>
            </xsl:element>
            <xsl:element name="ns2:TypeOfTermOfProduct">
                <xsl:value-of select="./ns2:TypeOfTermOfProduct"/>
            </xsl:element>
            <xsl:element name="ns2:ProductCategory">
                <xsl:value-of select="./ns2:ProductCategory"/>
            </xsl:element>
            <xsl:element name="ns2:Product">
                <xsl:value-of select="./ns2:Product"/>
            </xsl:element>
            <xsl:if test="./ns2:CreditAvailabilityPeriod">
                <xsl:element name="ns2:CreditAvailabilityPeriod">
                    <xsl:value-of select="./ns2:CreditAvailabilityPeriod"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:StartDate">
                <xsl:element name="ns2:StartDate">
                    <xsl:value-of select="./ns2:StartDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:EndDate">
                <xsl:element name="ns2:EndDate">
                    <xsl:value-of select="./ns2:EndDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:WarrantyPaymentTerm">
                <xsl:element name="ns2:WarrantyPaymentTerm">
                    <xsl:value-of select="./ns2:WarrantyPaymentTerm"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:PrincipalRepaymentDeferment">
                <xsl:element name="ns2:PrincipalRepaymentDeferment">
                    <xsl:value-of select="./ns2:PrincipalRepaymentDeferment"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:RepaymentSchedule">
                <xsl:value-of select="./ns2:RepaymentSchedule"/>
            </xsl:element>
            <xsl:element name="ns2:SumInCurrency">
                <xsl:value-of select="./ns2:SumInCurrency"/>
            </xsl:element>
            <xsl:element name="ns2:Currency">
                <xsl:value-of select="./ns2:Currency"/>
            </xsl:element>
            <xsl:if test="./ns2:SumInRub">
                <xsl:element name="ns2:SumInRub">
                    <xsl:value-of select="./ns2:SumInRub"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:CollateralFullySatisfiesDebt">
                <xsl:element name="ns2:CollateralFullySatisfiesDebt">
                    <xsl:value-of select="./ns2:CollateralFullySatisfiesDebt"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Purpose">
                <xsl:element name="ns2:Purpose">
                    <xsl:value-of select="./ns2:Purpose"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:PurposeElaboration">
                <xsl:element name="ns2:PurposeElaboration">
                    <xsl:value-of select="./ns2:PurposeElaboration"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:CreditDuration">
                <xsl:value-of select="./ns2:CreditDuration"/>
            </xsl:element>
            <xsl:if test="./ns2:Rate">
                <xsl:element name="ns2:Rate">
                    <xsl:value-of select="./ns2:Rate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:RateType">
                <xsl:element name="ns2:RateType">
                    <xsl:value-of select="./ns2:RateType"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:InterestPaymentProcess">
                <xsl:element name="ns2:InterestPaymentProcess">
                    <xsl:value-of select="./ns2:InterestPaymentProcess"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:VariableInterestRatePeriod">
                <xsl:element name="ns2:VariableInterestRatePeriod">
                    <xsl:value-of select="./ns2:VariableInterestRatePeriod"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:VariableInterestRateCalcPeriod">
                <xsl:element name="ns2:VariableInterestRateCalcPeriod">
                    <xsl:value-of select="./ns2:VariableInterestRateCalcPeriod"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:VariableInterestRatePercentPeriod">
                <xsl:element name="ns2:VariableInterestRatePercentPeriod">
                    <xsl:value-of select="./ns2:VariableInterestRatePercentPeriod"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfComission">
                <xsl:if test="./ns2:ListOfComission/ns2:Comission">
                    <xsl:apply-templates select="./ns2:ListOfComission/ns2:Comission"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfAdvancedRepaymentFee">
                <xsl:if test="./ns2:ListOfAdvancedRepaymentFee/ns2:AdvancedRepaymentFee">
                    <xsl:apply-templates select="./ns2:ListOfAdvancedRepaymentFee/ns2:AdvancedRepaymentFee"/>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:Term">
        <xsl:element name="ns2:Term">
            <xsl:element name="ns2:Type">
                <xsl:value-of select="./ns2:Type"/>
            </xsl:element>
            <xsl:element name="ns2:DescriptionOfTerm">
                <xsl:value-of select="./ns2:DescriptionOfTerm"/>
            </xsl:element>
            <xsl:if test="./ns2:ResponsibleEmployee">
                <xsl:element name="ns2:ResponsibleEmployee">
                    <xsl:value-of select="./ns2:ResponsibleEmployee"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ParticipantFL">
        <xsl:element name="ns2:ParticipantFL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusFL">
                <xsl:if test="./ns2:ListOfStatusFL/ns2:StatusFL">
                    <xsl:element name="ns2:StatusFL">
                        <xsl:value-of select="./ns2:ListOfStatusFL/ns2:StatusFL"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleFL">
                <xsl:value-of select="./ns2:RoleFL"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:ParticipantUL">
        <xsl:element name="ns2:ParticipantUL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusUL">
                <xsl:if test="./ns2:ListOfStatusUL/ns2:StatusUL">
                    <xsl:element name="ns2:StatusUL">
                        <xsl:value-of select="./ns2:ListOfStatusUL/ns2:StatusUL"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleUL">
                <xsl:value-of select="./ns2:RoleUL"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:PartGSLFL">
        <xsl:element name="ns2:PartGSLFL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusFL">
                <xsl:if test="./ns2:ListOfStatusFL/ns2:StatusFL">
                    <xsl:element name="ns2:StatusFL">
                        <xsl:value-of select="./ns2:ListOfStatusFL/ns2:StatusFL"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleFL">
                <xsl:value-of select="./ns2:RoleFL"/>
            </xsl:element>
            <xsl:element name="ns2:CommentOnInclusion">
                <xsl:value-of select="./ns2:CommentOnInclusion"/>
            </xsl:element>
            <xsl:element name="ns2:Stake">
                <xsl:value-of select="./ns2:Stake"/>
            </xsl:element>
            <xsl:if test="./ns2:CommentGSLFL">
                <xsl:element name="ns2:CommentGSLFL">
                    <xsl:value-of select="./ns2:CommentGSLFL"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:PartGSLUL">
        <xsl:element name="ns2:PartGSLUL">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfStatusUL">
                <xsl:if test="./ns2:ListOfStatusUL/ns2:StatusUL">
                    <xsl:element name="ns2:StatusUL">
                        <xsl:value-of select="./ns2:ListOfStatusUL/ns2:StatusUL"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:RoleUL">
                <xsl:value-of select="./ns2:RoleUL"/>
            </xsl:element>
            <xsl:element name="ns2:CommentOnInclusion">
                <xsl:value-of select="./ns2:CommentOnInclusion"/>
            </xsl:element>
            <xsl:element name="ns2:Stake">
                <xsl:value-of select="./ns2:Stake"/>
            </xsl:element>
            <xsl:if test="./ns2:CommentGSLFL">
                <xsl:element name="ns2:CommentGSLUL">
                    <xsl:value-of select="./ns2:CommentGSLUL"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="rsd:Limit">
        <xsl:element name="ns1:Limit">
            <xsl:element name="ns1:ParentLimitID">
                <xsl:value-of select="./rsd:ParentLimitID"/>
            </xsl:element>
            <xsl:if test="./rsd:ClientID">
                <xsl:element name="ns1:ClientID">
                    <xsl:value-of select="./rsd:ClientID"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:GSZ_ID">
                <xsl:element name="ns1:GSZ_ID">
                    <xsl:value-of select="./rsd:GSZ_ID"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:KG_ID">
                <xsl:element name="ns1:KG_ID">
                    <xsl:value-of select="./rsd:KG_ID"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns1:LimitType">
                <xsl:value-of select="./rsd:LimitType"/>
            </xsl:element>
            <xsl:if test="./rsd:LimitStatus">
                <xsl:element name="ns1:LimitStatus">
                    <xsl:value-of select="./rsd:LimitStatus"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns1:LimitID">
                <xsl:value-of select="./rsd:LimitID"/>
            </xsl:element>
            <xsl:if test="./rsd:ApplicationID">
                <xsl:element name="ns1:ApplicationID">
                    <xsl:value-of select="./rsd:ApplicationID"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:ApprovalDate">
                <xsl:element name="ns1:ApprovalDate">
                    <xsl:value-of select="./rsd:ApprovalDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:LimitValue">
                <xsl:element name="ns1:LimitValue">
                    <xsl:value-of select="./rsd:LimitValue"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:TypicalLoanTerm">
                <xsl:element name="ns1:TypicalLoanTerm">
                    <xsl:value-of select="./rsd:TypicalLoanTerm"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:CreditPurpose">
                <xsl:element name="ns1:CreditPurpose">
                    <xsl:value-of select="./rsd:CreditPurpose"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:OwnerLimit">
                <xsl:element name="ns1:OwnerLimit">
                    <xsl:value-of select="./rsd:OwnerLimit"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:DocApprovalNum">
                <xsl:element name="ns1:DocApprovalNum">
                    <xsl:value-of select="./rsd:DocApprovalNum"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns1:TypeOfBusinessPartner">
                <xsl:value-of select="./rsd:TypeOfBusinessPartner"/>
            </xsl:element>
            <xsl:if test="./rsd:FreeLimitBalance">
                <xsl:element name="ns1:FreeLimitBalance">
                    <xsl:value-of select="./rsd:FreeLimitBalance"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:MaximumRatingOfBorrower">
                <xsl:element name="ns1:MaximumRatingOfBorrower">
                    <xsl:value-of select="./rsd:MaximumRatingOfBorrower"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:MaximumRatingOfGroup">
                <xsl:element name="ns1:MaximumRatingOfGroup">
                    <xsl:value-of select="./rsd:MaximumRatingOfGroup"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:MaximumLGD">
                <xsl:element name="ns1:MaximumLGD">
                    <xsl:value-of select="./rsd:MaximumLGD"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:AssuranceFramework">
                <xsl:element name="ns1:AssuranceFramework">
                    <xsl:value-of select="./rsd:AssuranceFramework"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:OtherConditions">
                <xsl:element name="ns1:OtherConditions">
                    <xsl:value-of select="./rsd:OtherConditions"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:Utilization">
                <xsl:element name="ns1:Utilization">
                    <xsl:value-of select="./rsd:Utilization"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:ProductName">
                <xsl:element name="ns1:ProductName">
                    <xsl:value-of select="./rsd:ProductName"/>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="rsd:Rating">
        <xsl:element name="ns1:Rating">
            <xsl:element name="ns1:RatingID">
                <xsl:value-of select="./rsd:RatingID"/>
            </xsl:element>
            <xsl:element name="ns1:ClientID">
                <xsl:value-of select="./rsd:ClientID"/>
            </xsl:element>
            <xsl:if test="./rsd:KG_ID">
                <xsl:element name="ns1:KG_ID">
                    <xsl:value-of select="./rsd:KG_ID"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:Type">
                <xsl:element name="ns1:Type">
                    <xsl:value-of select="./rsd:Type"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:Value">
                <xsl:element name="ns1:Value">
                    <xsl:value-of select="./rsd:Value"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns1:Name">
                <xsl:value-of select="./rsd:Name"/>
            </xsl:element>
            <xsl:if test="./rsd:CalculatedDate">
                <xsl:element name="ns1:CalculatedDate">
                    <xsl:value-of select="./rsd:CalculatedDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:ApprovalDate">
                <xsl:element name="ns1:ApprovalDate">
                    <xsl:value-of select="./rsd:ApprovalDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:EndDate">
                <xsl:element name="ns1:EndDate">
                    <xsl:value-of select="./rsd:EndDate"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns1:Status">
                <xsl:value-of select="./rsd:Status"/>
            </xsl:element>
            <xsl:element name="ns1:IsProjectRating">
                <xsl:value-of select="./rsd:IsProjectRating"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:LoanSecurityInformation">
        <xsl:element name="ns2:LoanSecurityInformation">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:IsLegalPerson">
                <xsl:value-of select="./ns2:IsLegalPerson"/>
            </xsl:element>
            <xsl:element name="ns2:SecurityType">
                <xsl:value-of select="./ns2:SecurityType"/>
            </xsl:element>
            <xsl:if test="./ns2:PropertyName">
                <xsl:element name="ns2:PropertyName">
                    <xsl:value-of select="./ns2:PropertyName"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:IdentifyingFeatures">
                <xsl:element name="ns2:IdentifyingFeatures">
                    <xsl:value-of select="./ns2:IdentifyingFeatures"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:PropertyLocationAddress">
                <xsl:element name="ns2:PropertyLocationAddress">
                    <xsl:value-of select="./ns2:PropertyLocationAddress"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ExpectedValueEstimationProcess">
                <xsl:value-of select="./ns2:ExpectedValueEstimationProcess"/>
            </xsl:element>
            <xsl:element name="ns2:MarketValue">
                <xsl:value-of select="./ns2:MarketValue"/>
            </xsl:element>
            <xsl:element name="ns2:CorrectionFactor">
                <xsl:value-of select="./ns2:CorrectionFactor"/>
            </xsl:element>
            <xsl:element name="ns2:PledgeValue">
                <xsl:value-of select="./ns2:PledgeValue"/>
            </xsl:element>
            <xsl:element name="ns2:Insurance">
                <xsl:value-of select="./ns2:Insurance"/>
            </xsl:element>
            <xsl:if test="./ns2:InsuranceCompany">
                <xsl:element name="ns2:InsuranceCompany">
                    <xsl:value-of select="./ns2:InsuranceCompany"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:InsuranceAmount">
                <xsl:element name="ns2:InsuranceAmount">
                    <xsl:value-of select="./ns2:InsuranceAmount"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Beneficiary">
                <xsl:element name="ns2:Beneficiary">
                    <xsl:value-of select="./ns2:Beneficiary"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:DecisionOnContract">
                <xsl:value-of select="./ns2:DecisionOnContract"/>
            </xsl:element>
            <xsl:element name="ns2:ExpectedValueType">
                <xsl:value-of select="./ns2:ExpectedValueType"/>
            </xsl:element>
            <xsl:element name="ns2:ExpectedValueSource">
                <xsl:value-of select="./ns2:ExpectedValueSource"/>
            </xsl:element>
            <xsl:element name="ns2:BusinessValue">
                <xsl:value-of select="./ns2:BusinessValue"/>
            </xsl:element>
            <xsl:element name="ns2:QualityAndLiquidityExpertEstimation">
                <xsl:value-of select="./ns2:QualityAndLiquidityExpertEstimation"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfDefermentTermsOfPledger">
                <xsl:if test="./ns2:ListOfDefermentTermsOfPledger/ns2:DefermentTermsOfPledger">
                    <xsl:element name="ns2:DefermentTermsOfPledger">
                        <xsl:value-of select="./ns2:ListOfDefermentTermsOfPledger/ns2:DefermentTermsOfPledger"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:EstimationDate">
                <xsl:value-of select="./ns2:EstimationDate"/>
            </xsl:element>
            <xsl:if test="./ns2:TermsOfExtrajudicialPropertyClaims">
                <xsl:element name="ns2:TermsOfExtrajudicialPropertyClaims">
                    <xsl:value-of select="./ns2:TermsOfExtrajudicialPropertyClaims"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:IncludeTerms">
                <xsl:element name="ns2:IncludeTerms">
                    <xsl:value-of select="./ns2:IncludeTerms"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:SecurityCategory">
                <xsl:value-of select="./ns2:SecurityCategory"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:Guarantor">
        <xsl:element name="ns2:Guarantor">
            <xsl:element name="ns2:ClientID">
                <xsl:value-of select="./ns2:ClientID"/>
            </xsl:element>
            <xsl:element name="ns2:IsLegalPerson">
                <xsl:value-of select="./ns2:IsLegalPerson"/>
            </xsl:element>
            <xsl:element name="ns2:Guarantor">
                <xsl:value-of select="./ns2:Guarantor"/>
            </xsl:element>
            <xsl:element name="ns2:DecisionOnContract">
                <xsl:value-of select="./ns2:DecisionOnContract"/>
            </xsl:element>
            <xsl:element name="ns2:GuaranteeAmount">
                <xsl:value-of select="./ns2:GuaranteeAmount"/>
            </xsl:element>
            <xsl:if test="./ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity">
                <xsl:element name="ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity">
                    <xsl:value-of select="./ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity">
                <xsl:element name="ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity">
                    <xsl:value-of select="./ns2:GeneralConclusionOnLiquidityAndSufficiencyOfSecurity"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Comment">
                <xsl:element name="ns2:Comment">
                    <xsl:value-of select="./ns2:Comment"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfDefermentTermsOfGuarantor">
                <xsl:if test="./ns2:ListOfDefermentTermsOfGuarantor/ns2:DefermentTermsOfGuarantor">
                    <xsl:element name="ns2:DefermentTermsOfGuarantor">
                        <xsl:value-of select="./ns2:DefermentTermsOfGuarantor"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:GSZ">
        <xsl:element name="ns2:GSZ">
            <xsl:element name="ns2:GSZ_ID">
                <xsl:value-of select="./ns2:GSZ_ID"/>
            </xsl:element>
            <xsl:element name="ns2:NameGSZ">
                <xsl:value-of select="./ns2:NameGSZ"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberGSZFL">
                <xsl:if test="./ns2:ListOfMemberGSZFL/ns2:ClientID">
                    <xsl:element name="ns2:ClientID">
                        <xsl:value-of select="./ns2:ListOfMemberGSZFL/ns2:ClientID"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberGSZUL">
                <xsl:if test="./ns2:ListOfMemberGSZUL/ns2:ClientID">
                    <xsl:element name="ns2:ClientID">
                        <xsl:value-of select="./ns2:ListOfMemberGSZUL/ns2:ClientID"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:KG">
        <xsl:element name="ns2:KG">
            <xsl:element name="ns2:KG_ID">
                <xsl:value-of select="./ns2:KG_ID"/>
            </xsl:element>
            <xsl:element name="ns2:NameKG">
                <xsl:value-of select="./ns2:NameKG"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberKGFL">
                <xsl:if test="./ns2:ListOfMemberKGFL/ns2:ClientID">
                    <xsl:element name="ns2:ClientID">
                        <xsl:value-of select="./ns2:ListOfMemberKGFL/ns2:ClientID"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfMemberKGUL">
                <xsl:if test="./ns2:ListOfMemberKGUL/ns2:ClientID">
                    <xsl:element name="ns2:ClientID">
                        <xsl:value-of select="./ns2:ListOfMemberKGUL/ns2:ClientID"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:LegalPerson">
        <xsl:element name="ns2:LegalPerson">
            <xsl:element name="ns2:LegalPersonID">
                <xsl:value-of select="./ns2:LegalPersonID"/>
            </xsl:element>
            <xsl:element name="ns2:NaturalPersonID">
                <xsl:value-of select="./ns2:NaturalPersonID"/>
            </xsl:element>
            <xsl:if test="./ns2:SubDivision">
                <xsl:element name="ns2:SubDivision">
                    <xsl:value-of select="./ns2:SubDivision"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:StoppingList">
                <xsl:value-of select="./ns2:StoppingList"/>
            </xsl:element>
            <xsl:element name="ns2:OPF">
                <xsl:value-of select="./ns2:OPF"/>
            </xsl:element>
            <xsl:element name="ns2:OrganizationName">
                <xsl:value-of select="./ns2:OrganizationName"/>
            </xsl:element>
            <xsl:element name="ns2:INN">
                <xsl:value-of select="./ns2:INN"/>
            </xsl:element>
            <xsl:element name="ns2:KPP">
                <xsl:value-of select="./ns2:KPP"/>
            </xsl:element>
            <xsl:if test="./ns2:OGRN">
                <xsl:element name="ns2:OGRN">
                    <xsl:value-of select="./ns2:OGRN"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:Industry">
                <xsl:value-of select="./ns2:Industry"/>
            </xsl:element>
            <xsl:if test="./ns2:ProblemGroup">
                <xsl:element name="ns2:ProblemGroup">
                    <xsl:value-of select="./ns2:ProblemGroup"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:OKPO">
                <xsl:element name="ns2:OKPO">
                    <xsl:value-of select="./ns2:OKPO"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfAddressInfoUL">
                <xsl:element name="ns2:AddressInfoUL">
                    <xsl:element name="ns2:AddressTypeUL">
                        <xsl:value-of select="./ns2:ListOfAddressInfoUL/ns2:AddressInfoUL/ns2:AddressTypeUL"/>
                    </xsl:element>
                    <xsl:element name="ns2:City">
                        <xsl:value-of select="./ns2:ListOfAddressInfoUL/ns2:AddressInfoUL/ns2:City"/>
                    </xsl:element>
                    <xsl:element name="ns2:Street">
                        <xsl:value-of select="./ns2:ListOfAddressInfoUL/ns2:AddressInfoUL/ns2:Street"/>
                    </xsl:element>
                    <xsl:element name="ns2:House">
                        <xsl:value-of select="./ns2:ListOfAddressInfoUL/ns2:AddressInfoUL/ns2:House"/>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
            <xsl:element name="ns2:ListOfRegistrationInfo">
                <xsl:if test="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo">
                    <xsl:element name="ns2:RegistrationInfo">
                        <xsl:element name="ns2:RegistrationDate">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationDate"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationAuthority">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationAuthority"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationSertificateSeries">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationSertificateSeries"/>
                        </xsl:element>
                        <xsl:element name="ns2:RegistrationSertificateDate">
                            <xsl:value-of select="./ns2:ListOfRegistrationInfo/ns2:RegistrationInfo/ns2:RegistrationSertificateDate"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="ns2:NaturalPerson">
        <xsl:element name="ns2:NaturalPerson">
            <xsl:element name="ns2:NaturalPersonID">
                <xsl:value-of select="./ns2:NaturalPersonID"/>
            </xsl:element>
            <xsl:if test="./ns2:SubDivision">
                <xsl:element name="ns2:SubDivision">
                    <xsl:value-of select="./ns2:SubDivision"/>
                </xsl:element>
            <xsl:element name="ns2:LastName">
                <xsl:value-of select="./ns2:LastName"/>
            </xsl:element>
            <xsl:element name="ns2:FirstName">
                <xsl:value-of select="./ns2:FirstName"/>
            </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:MiddleName">
                <xsl:element name="ns2:MiddleName">
                    <xsl:value-of select="./ns2:MiddleName"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:DateOfBirth">
                <xsl:element name="ns2:DateOfBirth">
                    <xsl:value-of select="./ns2:DateOfBirth"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:PlaceOfBirth">
                <xsl:element name="ns2:PlaceOfBirth">
                    <xsl:value-of select="./ns2:PlaceOfBirth"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="./ns2:Gender">
                <xsl:element name="ns2:Gender">
                    <xsl:value-of select="./ns2:Gender"/>
                </xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfDocument">
                <xsl:if test="./ns2:ListOfDocument/ns2:Document">
                    <xsl:element name="ns2:Document">
                        <xsl:element name="ns2:DocumentTypeCode">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentTypeCode"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentType">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentType"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentSeries">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentSeries"/>
                        </xsl:element>
                        <xsl:element name="ns2:DocumentName">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:DocumentName"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueDate">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueDate"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueAuthorityCode">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueAuthorityCode"/>
                        </xsl:element>
                        <xsl:element name="ns2:IssueAuthority">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:IssueAuthority"/>
                        </xsl:element>
                        <xsl:element name="ns2:Primary">
                            <xsl:value-of select="./ns2:ListOfDocument/ns2:Document/ns2:Primary"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfAddressInfoFL">
                <xsl:element name="ns2:AddressInfoFL">
                    <xsl:element name="ns2:AddressTypeFL">
                        <xsl:value-of select="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:AddressTypeFL"/>
                    </xsl:element>
                    <xsl:if test="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:City">
                        <xsl:element name="ns2:City">
                            <xsl:value-of select="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:City"/>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:Street">
                        <xsl:element name="ns2:Street">
                            <xsl:value-of select="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:Street"/>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:House">
                        <xsl:element name="ns2:House">
                            <xsl:value-of select="./ns2:ListOfAddressInfoFL/ns2:AddressInfoFL/ns2:House"/>
                        </xsl:element>
                    </xsl:if>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="rsd:ContractInfo">
        <ns1:ContractInfo xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10">
            <ns1:ContractID><xsl:value-of select="./rsd:ContractID"/></ns1:ContractID>
            <ns1:ContractDate><xsl:value-of select="./rsd:ContractDate"/></ns1:ContractDate>
            <ns1:OrgUnit><xsl:value-of select="./rsd:OrgUnit"/></ns1:OrgUnit>
            <ns1:MotiveOfContract><xsl:value-of select="./rsd:MotiveOfContract"/></ns1:MotiveOfContract>
            <ns1:MethodOfSale><xsl:value-of select="./rsd:MethodOfSale"/></ns1:MethodOfSale>
            <ns1:Utilization><xsl:value-of select="./rsd:Utilization"/></ns1:Utilization>
            <ns1:ContractStatus><xsl:value-of select="./rsd:ContractStatus"/></ns1:ContractStatus>
            <xsl:if test="./rsd:RequestDate">
                <ns1:RequestDate><xsl:value-of select="./rsd:RequestDate"/></ns1:RequestDate>
            </xsl:if>
            <ns1:DivisionCode><xsl:value-of select="./rsd:DivisionCode"/></ns1:DivisionCode>
            <ns1:ClientFileId><xsl:value-of select="./rsd:ClientFileId"/></ns1:ClientFileId>
            <ns1:ContractFileId><xsl:value-of select="./rsd:ContractFileId"/></ns1:ContractFileId>
            <ns1:ListOfTermOfProduct>
                <xsl:if test="./rsd:ListOfTermOfProduct/ns2:TermOfProduct">
                    <xsl:apply-templates select="./rsd:ListOfTermOfProduct/ns2:TermOfProduct"/>
                </xsl:if>
            </ns1:ListOfTermOfProduct>
            <ns1:ListOfTerm>
                <xsl:if test="./rsd:ListOfTerm/ns2:Term">
                    <xsl:apply-templates select="./rsd:ListOfTerm/ns2:Term"/>
                </xsl:if>
            </ns1:ListOfTerm>
            <ns2:Participant>
                <ns2:ListOfParticipantFL>
                    <xsl:if test="./ns2:Participant/ns2:ListOfParticipantFL/ns2:ParticipantFL">
                        <xsl:apply-templates select="./ns2:Participant/ns2:ListOfParticipantFL/ns2:ParticipantFL"/>
                    </xsl:if>
                </ns2:ListOfParticipantFL>
                <ns2:ListOfParticipantUL>
                    <xsl:apply-templates select="./ns2:Participant/ns2:ListOfParticipantUL/ns2:ParticipantUL"/>
                </ns2:ListOfParticipantUL>
            </ns2:Participant>
            <ns2:GSL>
                <ns2:ListOfPartGSLFL>
                    <xsl:if test="./ns2:GSL/ns2:ListOfPartGSLFL/ns2:PartGSLFL">
                        <xsl:apply-templates select="./ns2:GSL/ns2:ListOfPartGSLFL/ns2:PartGSLFL"/>
                    </xsl:if>
                </ns2:ListOfPartGSLFL>
                <ns2:ListOfPartGSLUL>
                    <xsl:if test="./ns2:GSL/ns2:ListOfPartGSLUL/ns2:PartGSLUL">
                        <xsl:apply-templates select="./ns2:GSL/ns2:ListOfPartGSLUL/ns2:PartGSLUL"/>
                    </xsl:if>
                </ns2:ListOfPartGSLUL>
            </ns2:GSL>
            <ns1:ListOfLimit>
                <xsl:if test="./rsd:ListOfLimit/rsd:Limit">
                    <xsl:apply-templates select="./rsd:ListOfLimit/rsd:Limit"/>
                </xsl:if>
            </ns1:ListOfLimit>
            <ns1:ListOfRating>
                <xsl:if test="./rsd:ListOfRating/rsd:Rating">
                    <xsl:apply-templates select="./rsd:ListOfRating/rsd:Rating"/>
                </xsl:if>
            </ns1:ListOfRating>
            <ns1:ListOfLoanSecurityInformation>
                <xsl:if test="./rsd:ListOfLoanSecurityInformation/ns2:LoanSecurityInformation">
                    <xsl:apply-templates select="./rsd:ListOfLoanSecurityInformation/ns2:LoanSecurityInformation"/>
                </xsl:if>
            </ns1:ListOfLoanSecurityInformation>
            <ns1:ListOfGuarantor>
                <xsl:if test="./rsd:ListOfGuarantor/ns2:Guarantor">
                    <xsl:apply-templates select="./rsd:ListOfGuarantor/ns2:Guarantor"/>
                </xsl:if>
            </ns1:ListOfGuarantor>
            <ns1:ListOfGSZ>
                <xsl:if test="./rsd:ListOfGSZ/ns2:GSZ">
                    <xsl:apply-templates select="./rsd:ListOfGSZ/ns2:GSZ"/>
                </xsl:if>
            </ns1:ListOfGSZ>
            <ns1:ListOfKG>
                <xsl:if test="./rsd:ListOfKG/ns2:KG">
                    <xsl:apply-templates select="./rsd:ListOfKG/ns2:KG"/>
                </xsl:if>
            </ns1:ListOfKG>
            <ns1:ListOfLegalPerson>
                <xsl:if test="./rsd:ListOfLegalPerson/ns2:LegalPerson">
                    <xsl:apply-templates select="./rsd:ListOfLegalPerson/ns2:LegalPerson"/>
                </xsl:if>
            </ns1:ListOfLegalPerson>
            <ns1:ListOfNaturalPerson>
                <xsl:if test="./rsd:ListOfNaturalPerson/ns2:NaturalPerson">
                    <xsl:apply-templates select="./rsd:ListOfNaturalPerson/ns2:NaturalPerson"/>
                </xsl:if>
            </ns1:ListOfNaturalPerson>
            <ns1:ListOfExpectedCurrencyRate>
                <xsl:if test="./rsd:ListOfExpectedCurrencyRate/rsd:ExpectedCurrencyRate">
                    <xsl:apply-templates select="./rsd:ListOfExpectedCurrencyRate/rsd:ExpectedCurrencyRate"/>
                </xsl:if>
            </ns1:ListOfExpectedCurrencyRate>
        </ns1:ContractInfo>
    </xsl:template>

    <xsl:template match="rsd:ExpectedCurrencyRate">
        <xsl:element name="ns1:ExpectedCurrencyRate">
            <xsl:element name="ns1:Currency1">
                <xsl:value-of select="./rsd:Currency1"/>
            </xsl:element>
            <xsl:element name="ns1:Currency2">
                <xsl:value-of select="./rsd:Currency2"/>
            </xsl:element>
            <xsl:element name="ns1:RateDate">
                <xsl:value-of select="./rsd:RateDate"/>
            </xsl:element>
            <xsl:element name="ns1:Rate">
                <xsl:value-of select="./rsd:Rate"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="ExportContractRq">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:RqUID">
            <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqUID"/>
        </xsl:element>
        <xsl:element name="ns1:RqTm">
            <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqTm"/>
        </xsl:element>
        <xsl:element name="ns1:SPName">
            <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SPName"/>
        </xsl:element>
        <xsl:element name="ns1:SystemId">
            <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SystemId"/>
        </xsl:element>
        <xsl:element name="ns1:OperationName">
            <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:OperationName"/>
        </xsl:element>
        <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:ContractInfo"/>
    </xsl:template>
</xsl:stylesheet>