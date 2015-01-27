<xsl:stylesheet xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/req/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10/Data"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0" >
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

    <xsl:template match="rsd:ContractInfo">
        <ns1:ContractInfo xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10"
                          xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10">
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
        </ns1:ContractInfo>
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