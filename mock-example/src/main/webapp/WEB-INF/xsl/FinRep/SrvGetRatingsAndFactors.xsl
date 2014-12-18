<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
				xmlns:rq="http://sbrf.ru/NCP/ASFO/GetRatingsAndFactorsRq/"
				xmlns:rs="http://sbrf.ru/NCP/ASFO/GetRatingsAndFactorsRs/"
				xmlns:rsd="http://sbrf.ru/NCP/ASFO/RatingsAndFactors/Data"
				xmlns:crm="http://sbrf.ru/NCP/ASFO/">

	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

	<!--Prepare data and section of data XML-->
	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header"/>
			<soap-env:Body>
				<xsl:variable name="data" select="document('../../data/FinRep/xml/SrvGetRatingsAndFactorsData.xml')/rsd:data"/>
				<xsl:variable name="linkedTag" select="./soap-env:Body/crm:getRatingsAndFactorsRq/rq:comment"/>
				<xsl:call-template name="FinAnalysis">
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
	<xsl:template name="FinAnalysis">
		<!--Get params-->
		<xsl:param name="response"/>
		<xsl:param name="data"/>
		<!-- - - - - - - - -->
		<crm:getRatingsAndFactorsRs>
			<rs:listOfRating>
				<!--1 or more repetitions:-->
				<xsl:for-each select="$data/rsd:response[@name=$response]//rsd:listOfRating/rsd:rating">
					<rs:rating>
						<rs:ratingId><xsl:value-of select="./rsd:ratingId"/></rs:ratingId>
						<!--Optional:-->
						<xsl:if test="./rsd:finalRatingValue">
							<rs:finalRatingValue><xsl:value-of select="./rsd:finalRatingValue"/></rs:finalRatingValue>
						</xsl:if>
						<!--Optional:-->
						<xsl:if test="./rsd:intermediateRatingValue">
							<rs:intermediateRatingValue><xsl:value-of select="./rsd:intermediateRatingValue"/></rs:intermediateRatingValue>
						</xsl:if>
						<!--Optional:-->
						<xsl:if test="./rsd:ajustedRatingValue">
							<rs:ajustedRatingValue><xsl:value-of select="./rsd:ajustedRatingValue"/></rs:ajustedRatingValue>
						</xsl:if>
						<!--Optional:-->
						<xsl:if test="./rsd:pd">
							<rs:pd><xsl:value-of select="./rsd:pd"/>0</rs:pd>
						</xsl:if>
						<rs:status><xsl:value-of select="./rsd:status"/></rs:status>
						<!--Optional:-->
						<xsl:if test="./rsd:approvalDate">
							<rs:approvalDate><xsl:value-of select="./rsd:approvalDate"/></rs:approvalDate>
						</xsl:if>
						<!--Optional:-->
						<xsl:if test="./rsd:calculationDate">
							<rs:calculationDate><xsl:value-of select="./rsd:calculationDate"/></rs:calculationDate>
						</xsl:if>
						<!--Optional:-->
						<xsl:if test="./rsd:ajustmentDate">
							<rs:ajustmentDate><xsl:value-of select="./rsd:ajustmentDate"/></rs:ajustmentDate>
						</xsl:if>
						<rs:modelName><xsl:value-of select="./rsd:modelName"/></rs:modelName>
						<rs:modelIntegrationId><xsl:value-of select="./rsd:modelIntegrationId"/></rs:modelIntegrationId>
						<rs:ratingName><xsl:value-of select="./rsd:ratingName"/></rs:ratingName>
						<rs:ratingType><xsl:value-of select="./rsd:ratingType"/></rs:ratingType>
						<rs:employeeForApprove>
							<rs:login><xsl:value-of select="./rsd:employeeForApprove/rsd:login"/></rs:login>
							<rs:lastName><xsl:value-of select="./rsd:employeeForApprove/rsd:lastName"/></rs:lastName>
							<rs:firstName><xsl:value-of select="./rsd:employeeForApprove/rsd:firstName"/></rs:firstName>
							<!--Optional:-->
							<xsl:if test="./rsd:employeeForApprove/rsd:middleName">
								<rs:middleName><xsl:value-of select="./rsd:employeeForApprove/rsd:middleName"/></rs:middleName>
							</xsl:if>
							<rs:division><xsl:value-of select="./rsd:employeeForApprove/rsd:division"/></rs:division>
							<rs:divisionCode><xsl:value-of select="./rsd:employeeForApprove/rsd:divisionCode"/></rs:divisionCode>
							<!--Optional:-->
							<xsl:if test="./rsd:employeeForApprove/rsd:jobTitle">
								<rs:jobTitle><xsl:value-of select="./rsd:employeeForApprove/rsd:jobTitle"/></rs:jobTitle>
							</xsl:if>
							<rs:role><xsl:value-of select="./rsd:employeeForApprove/rsd:role"/></rs:role>
						</rs:employeeForApprove>
						<rs:listOfRatingCalc>
							<!--1 or more repetitions:-->
							<xsl:for-each select="./rsd:listOfRatingCalc/rsd:ratingCalc">
								<rs:ratingCalc>
									<rs:ratingCalcId><xsl:value-of select="./rsd:ratingCalcId"/></rs:ratingCalcId>
									<rs:modelName><xsl:value-of select="./rsd:modelName"/></rs:modelName>
									<rs:modelIntegrationId><xsl:value-of select="./rsd:modelIntegrationId"/></rs:modelIntegrationId>
									<!--Optional:-->
									<xsl:if test="./rsd:dateCalc">
										<rs:dateCalc><xsl:value-of select="./rsd:dateCalc"/></rs:dateCalc>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:calcValue">
										<rs:calcValue><xsl:value-of select="./rsd:calcValue"/></rs:calcValue>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfRatingAjustment">
										<rs:listOfRatingAjustment>
											<!--Zero or more repetitions:-->
											<xsl:for-each select="./rsd:listOfRatingAjustment/rsd:ratingAjustment">
												<rs:ratingAjustment>
													<rs:ajustmentDate><xsl:value-of select="./rsd:ajustmentDate"/></rs:ajustmentDate>
													<rs:ajustedValue><xsl:value-of select="./rsd:ajustedValue"/></rs:ajustedValue>
													<rs:ajustmetReason><xsl:value-of select="./rsd:ajustmetReason"/></rs:ajustmetReason>
													<!--Optional:-->
													<xsl:if test="./rsd:comments">
														<rs:comments><xsl:value-of select="./rsd:comments"/></rs:comments>
													</xsl:if>
													<rs:status><xsl:value-of select="./rsd:status"/></rs:status>
													<rs:employeeForAjustment>
														<rs:login><xsl:value-of select="./rsd:employeeForAjustment/rsd:login"/></rs:login>
														<rs:lastName><xsl:value-of select="./rsd:employeeForAjustment/rsd:lastName"/></rs:lastName>
														<rs:firstName><xsl:value-of select="./rsd:employeeForAjustment/rsd:firstName"/></rs:firstName>
														<!--Optional:-->
														<xsl:if test="./rsd:employeeForAjustment/rsd:middleName">
															<rs:middleName><xsl:value-of select="./rsd:employeeForAjustment/rsd:middleName"/></rs:middleName>
														</xsl:if>
														<rs:division><xsl:value-of select="./rsd:employeeForAjustment/rsd:division"/></rs:division>
														<rs:divisionCode><xsl:value-of select="./rsd:employeeForAjustment/rsd:divisionCode"/></rs:divisionCode>
														<!--Optional:-->
														<xsl:if test="./rsd:employeeForAjustment/rsd:jobTitl">
															<rs:jobTitle><xsl:value-of select="./rsd:employeeForAjustment/rsd:jobTitle"/></rs:jobTitle>
														</xsl:if>
														<rs:role><xsl:value-of select="./rsd:employeeForAjustment/rsd:role"/></rs:role>
													</rs:employeeForAjustment>
												</rs:ratingAjustment>
											</xsl:for-each>
										</rs:listOfRatingAjustment>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:employeeForCalc/rsd:login">
									<rs:employeeForCalc>
										<rs:login><xsl:value-of select="./rsd:employeeForCalc/rsd:login"/></rs:login>
										<rs:lastName><xsl:value-of select="./rsd:employeeForCalc/rsd:lastName"/></rs:lastName>
										<rs:firstName><xsl:value-of select="./rsd:employeeForCalc/rsd:firstName"/></rs:firstName>
										<!--Optional:-->
										<xsl:if test="./rsd:employeeForCalc/rsd:middleName">
											<rs:middleName><xsl:value-of select="./rsd:employeeForCalc/rsd:middleName"/></rs:middleName>
										</xsl:if>
										<rs:division><xsl:value-of select="./rsd:employeeForCalc/rsd:division"/></rs:division>
										<rs:divisionCode><xsl:value-of select="./rsd:employeeForCalc/rsd:divisionCode"/></rs:divisionCode>
										<!--Optional:-->
										<xsl:if test="./rsd:employeeForCalc/rsd:jobTitle">
											<rs:jobTitle><xsl:value-of select="./rsd:employeeForCalc/rsd:jobTitle"/></rs:jobTitle>
										</xsl:if>
										<rs:role><xsl:value-of select="./rsd:employeeForCalc/rsd:role"/></rs:role>
									</rs:employeeForCalc>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfResultRating">
										<rs:listOfResultRating>
											<!--Zero or more repetitions:-->
											<xsl:for-each select="./rsd:listOfResultRating/rsd:resultRating">
												<rs:resultRating>
													<rs:isPrimary><xsl:value-of select="./rsd:isPrimary"/></rs:isPrimary>
													<rs:name><xsl:value-of select="./rsd:name"/></rs:name>
													<rs:value><xsl:value-of select="./rsd:value"/></rs:value>
													<!--Optional:-->
													<xsl:if test="./rsd:type">
														<rs:type><xsl:value-of select="./rsd:type"/></rs:type>
													</xsl:if>
												</rs:resultRating>
											</xsl:for-each>
										</rs:listOfResultRating>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfCalculatedFactor">
										<rs:listOfCalculatedFactor>
											<!--Zero or more repetitions:-->
											<xsl:for-each select="./rsd:listOfCalculatedFactor/rsd:calculatedFactor">
												<rs:calculatedFactor>
													<rs:code><xsl:value-of select="./rsd:code"/></rs:code>
													<rs:name><xsl:value-of select="./rsd:name"/></rs:name>
													<rs:value><xsl:value-of select="./rsd:value"/></rs:value>
												</rs:calculatedFactor>
											</xsl:for-each>
										</rs:listOfCalculatedFactor>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfFixedFactor">
										<rs:listOfFixedFactor>
											<!--1 or more repetitions:-->
											<xsl:for-each select="./rsd:listOfFixedFactor/rsd:fixedFactor">
												<rs:fixedFactor>
													<rs:factor><xsl:value-of select="./rsd:factor"/></rs:factor>
													<rs:factorIntegrationId><xsl:value-of select="./rsd:factorIntegrationId"/></rs:factorIntegrationId>
													<rs:group><xsl:value-of select="./rsd:group"/></rs:group>
													<!--Optional:-->
													<xsl:if test="./rsd:value">
														<rs:value><xsl:value-of select="./rsd:value"/></rs:value>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:valueIntegrationId">
														<rs:valueIntegrationId><xsl:value-of select="./rsd:valueIntegrationId"/></rs:valueIntegrationId>
													</xsl:if>
												</rs:fixedFactor>
											</xsl:for-each>
										</rs:listOfFixedFactor>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfRatingPeriod">
										<rs:listOfRatingPeriod>
											<!--1 or more repetitions:-->
											<xsl:for-each select="./rsd:listOfRatingPeriod/rsd:ratingPeriod">
												<rs:ratingPeriod>
													<!--Optional:-->
													<xsl:if test="./rsd:reportDate">
														<rs:reportDate><xsl:value-of select="./rsd:reportDate"/></rs:reportDate>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:period">
														<rs:period><xsl:value-of select="./rsd:period"/></rs:period>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:startDate">
														<rs:startDate><xsl:value-of select="./rsd:startDate"/></rs:startDate>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:endDate">
														<rs:endDate><xsl:value-of select="./rsd:endDate"/></rs:endDate>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:type">
														<rs:type><xsl:value-of select="./rsd:type"/></rs:type>
													</xsl:if>
													<!--Optional:-->
													<xsl:if test="./rsd:comments">
													<rs:comments><xsl:value-of select="./rsd:comments"/></rs:comments>
													</xsl:if>
													<rs:listOfCardinalFactor>
														<!--1 or more repetitions:-->
														<xsl:for-each select="./rsd:listOfCardinalFactor/rsd:cardinalFactor">
															<rs:cardinalFactor>
																<rs:factor><xsl:value-of select="./rsd:factor"/></rs:factor>
																<rs:factorIntegrationId><xsl:value-of select="./rsd:factorIntegrationId"/></rs:factorIntegrationId>
																<rs:group><xsl:value-of select="./rsd:group"/></rs:group>
																<!--Optional:-->
																<xsl:if test="./rsd:numValue">
																	<rs:numValue><xsl:value-of select="./rsd:numValue"/></rs:numValue>
																</xsl:if>
															</rs:cardinalFactor>
														</xsl:for-each>
													</rs:listOfCardinalFactor>
												</rs:ratingPeriod>
											</xsl:for-each>
										</rs:listOfRatingPeriod>
									</xsl:if>
									<!--Optional:-->
									<xsl:if test="./rsd:listOfRatingSupport">
										<rs:listOfRatingSupport>
											<!--Zero or more repetitions:-->
											<xsl:for-each select="./rsd:listOfRatingSupport/rsd:ratingSupport">
												<rs:ratingSupport>
													<rs:name><xsl:value-of select="./rsd:name"/></rs:name>
													<rs:integrationId><xsl:value-of select="./rsd:integrationId"/></rs:integrationId>
													<rs:listOfSupportFactor>
														<!--1 or more repetitions:-->
														<xsl:for-each select="./rsd:listOfSupportFactor/rsd:supportFactor">
															<rs:supportFactor>
																<rs:factor><xsl:value-of select="./rsd:factor"/></rs:factor>
																<rs:factorIntegrationId><xsl:value-of select="./rsd:factorIntegrationId"/></rs:factorIntegrationId>
																<rs:group><xsl:value-of select="./rsd:group"/></rs:group>
																<!--Optional:-->
																<xsl:if test="./rsd:value">
																	<rs:value><xsl:value-of select="./rsd:value"/></rs:value>
																</xsl:if>
																<!--Optional:-->
																<xsl:if test="./rsd:valueIntegrationId">
																	<rs:valueIntegrationId><xsl:value-of select="./rsd:valueIntegrationId"/></rs:valueIntegrationId>
																</xsl:if>
																<!--Optional:-->
																<xsl:if test="./rsd:freeValue">
																	<rs:freeValue><xsl:value-of select="./rsd:freeValue"/></rs:freeValue>
																</xsl:if>
															</rs:supportFactor>
														</xsl:for-each>
													</rs:listOfSupportFactor>
												</rs:ratingSupport>
											</xsl:for-each>
										</rs:listOfRatingSupport>
									</xsl:if>
								</rs:ratingCalc>
							</xsl:for-each>
						</rs:listOfRatingCalc>
					</rs:rating>
				</xsl:for-each>
			</rs:listOfRating>
		</crm:getRatingsAndFactorsRs>
	</xsl:template>

</xsl:stylesheet>