<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:crmct="http://sbrf.ru/NCP/CRM/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.04/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.04/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//soap:Body/*//*[local-name()='performer'][1]/text()"/>
   <xsl:param name="dataFileName"
              select="'../../data/CRM/xml/GetParticipantsData.xml'"/>
   <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
   <xsl:param name="id" select="null"/>
   <!--Optional params for optional header values-->
<xsl:param name="correlation-id" select="null"/>
   <xsl:param name="eis-name" select="null"/>
   <xsl:param name="system-id" select="null"/>
   <xsl:param name="operation-version" select="null"/>
   <xsl:param name="user-id" select="null"/>
   <xsl:param name="user-name" select="null"/>

   <xsl:template match="soap:Envelope">
      <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
      <xsl:variable name="linkedTag" select="$name"/>
      <xsl:element name="soap:Envelope">
         <xsl:choose>
            <xsl:when test="soap:Header">
               <xsl:copy-of select="soap:Header"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:call-template name="NCPHeader">
                  <xsl:with-param name="response">
                     <xsl:choose>
                        <xsl:when test="count(./rsd:response[@name=$linkedTag])=1">
                           <xsl:value-of select="$linkedTag"/>
                        </xsl:when>
                        <xsl:otherwise>default</xsl:otherwise>
                     </xsl:choose>
                  </xsl:with-param>
                  <xsl:with-param name="timestamp" select="$timestamp"/>
                  <xsl:with-param name="id" select="$id"/>
                  <xsl:with-param name="operation-name" select="string('prtspRs')"/>
                  <xsl:with-param name="correlation-id" select="$correlation-id"/>
                  <xsl:with-param name="eis-name" select="$eis-name"/>
                  <xsl:with-param name="system-id" select="$system-id"/>
                  <xsl:with-param name="operation-version" select="$operation-version"/>
                  <xsl:with-param name="user-id" select="$user-id"/>
                  <xsl:with-param name="user-name" select="$user-name"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
         <soap:Body>
            <xsl:call-template name="PrtspRs">
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

   <xsl:template match="rsd:operationStatus">
      <tns:operationStatus>
         <tns:errorCode>
            <xsl:value-of select="./rsd:errorCode"/>
         </tns:errorCode>
         <xsl:if test="./rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="./rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
      </tns:operationStatus>
   </xsl:template>

   <xsl:template match="rsd:identities">
      <tns:identities>
         <tns:identityTypeCode>
            <xsl:value-of select="./rsd:identityTypeCode"/>
         </tns:identityTypeCode>
         <tns:identityType>
            <xsl:value-of select="./rsd:identityType"/>
         </tns:identityType>
         <tns:identitySeries>
            <xsl:value-of select="./rsd:identitySeries"/>
         </tns:identitySeries>
         <tns:identityNumber>
            <xsl:value-of select="./rsd:identityNumber"/>
         </tns:identityNumber>
         <tns:identityIssueDate>
            <xsl:value-of select="./rsd:identityIssueDate"/>
         </tns:identityIssueDate>
         <tns:identityIssuer>
            <xsl:value-of select="./rsd:identityIssuer"/>
         </tns:identityIssuer>
         <tns:identityIssuerCode>
            <xsl:value-of select="./rsd:identityIssuerCode"/>
         </tns:identityIssuerCode>
         <xsl:if test="./rsd:identityIsPrimary">
            <tns:identityIsPrimary>
               <xsl:value-of select="./rsd:identityIsPrimary"/>
            </tns:identityIsPrimary>
         </xsl:if>
      </tns:identities>
   </xsl:template>

   <xsl:template match="rsd:topManager">
      <tns:topManager>
         <xsl:if test="./rsd:managerID">
            <tns:managerID>
               <xsl:value-of select="./rsd:managerID"/>
            </tns:managerID>
         </xsl:if>
         <xsl:if test="./rsd:fullName">
            <tns:fullName>
               <xsl:value-of select="./rsd:fullName"/>
            </tns:fullName>
         </xsl:if>
         <xsl:if test="./rsd:position">
            <tns:position>
               <xsl:value-of select="./rsd:position"/>
            </tns:position>
         </xsl:if>
         <xsl:if test="./rsd:age">
            <tns:age>
               <xsl:value-of select="./rsd:age"/>
            </tns:age>
         </xsl:if>
         <xsl:if test="./rsd:experienceInPosition">
            <tns:experienceInPosition>
               <xsl:value-of select="./rsd:experienceInPosition"/>
            </tns:experienceInPosition>
         </xsl:if>
         <xsl:if test="./rsd:experienceInSimilarPosition">
            <tns:experienceInSimilarPosition>
               <xsl:value-of select="./rsd:experienceInSimilarPosition"/>
            </tns:experienceInSimilarPosition>
         </xsl:if>
         <xsl:if test="./rsd:education">
            <tns:education>
               <xsl:value-of select="./rsd:education"/>
            </tns:education>
         </xsl:if>
      </tns:topManager>
   </xsl:template>

   <xsl:template match="rsd:ratings">
      <tns:ratings>
         <tns:ratingID>
            <xsl:value-of select="./rsd:ratingID"/>
         </tns:ratingID>
         <tns:ratingValue>
            <xsl:value-of select="./rsd:ratingValue"/>
         </tns:ratingValue>
         <tns:ratingType>
            <xsl:value-of select="./rsd:ratingType"/>
         </tns:ratingType>
         <tns:ratingModel>
            <xsl:value-of select="./rsd:ratingModel"/>
         </tns:ratingModel>
         <tns:ratingCalculatedDate>
            <xsl:value-of select="./rsd:ratingCalculatedDate"/>
         </tns:ratingCalculatedDate>
         <tns:ratingCalculatedRole>
            <xsl:value-of select="./rsd:ratingCalculatedRole"/>
         </tns:ratingCalculatedRole>
         <tns:ratingCalculatedBy>
            <xsl:value-of select="./rsd:ratingCalculatedBy"/>
         </tns:ratingCalculatedBy>
         <tns:ratingApprovalDate>
            <xsl:value-of select="./rsd:ratingApprovalDate"/>
         </tns:ratingApprovalDate>
         <tns:ratingApprovedBy>
            <xsl:value-of select="./rsd:ratingApprovedBy"/>
         </tns:ratingApprovedBy>
      </tns:ratings>
   </xsl:template>

   <xsl:template match="rsd:rating">
      <tns:rating>
         <tns:ratingID>
            <xsl:value-of select="./rsd:ratingID"/>
         </tns:ratingID>
         <tns:ratingValue>
            <xsl:value-of select="./rsd:ratingValue"/>
         </tns:ratingValue>
         <tns:ratingType>
            <xsl:value-of select="./rsd:ratingType"/>
         </tns:ratingType>
         <tns:ratingModel>
            <xsl:value-of select="./rsd:ratingModel"/>
         </tns:ratingModel>
         <tns:ratingCalculatedDate>
            <xsl:value-of select="./rsd:ratingCalculatedDate"/>
         </tns:ratingCalculatedDate>
         <tns:ratingCalculatedRole>
            <xsl:value-of select="./rsd:ratingCalculatedRole"/>
         </tns:ratingCalculatedRole>
         <tns:ratingCalculatedBy>
            <xsl:value-of select="./rsd:ratingCalculatedBy"/>
         </tns:ratingCalculatedBy>
         <tns:ratingApprovalDate>
            <xsl:value-of select="./rsd:ratingApprovalDate"/>
         </tns:ratingApprovalDate>
         <tns:ratingApprovedBy>
            <xsl:value-of select="./rsd:ratingApprovedBy"/>
         </tns:ratingApprovedBy>
      </tns:rating>
   </xsl:template>

   <xsl:template match="rsd:participantsGroup">
      <tns:participantsGroup>
         <xsl:apply-templates select="./rsd:participants"/>
         <tns:groupType>
            <xsl:value-of select="./rsd:groupType"/>
         </tns:groupType>
         <xsl:apply-templates select="./rsd:groupInfo"/>
      </tns:participantsGroup>
   </xsl:template>

   <xsl:template match="rsd:groupInfo">
      <tns:groupInfo>
         <tns:id>
            <xsl:value-of select="./rsd:id"/>
         </tns:id>
         <tns:label>
            <xsl:value-of select="./rsd:label"/>
         </tns:label>
         <tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <tns:updateDate>
            <xsl:value-of select="./rsd:updateDate"/>
         </tns:updateDate>
         <xsl:if test="./rsd:approvalDate">
            <tns:approvalDate>
               <xsl:value-of select="./rsd:approvalDate"/>
            </tns:approvalDate>
         </xsl:if>
         <xsl:apply-templates select="./rsd:ratings"/>
         <xsl:if test="./rsd:topLevelGroupName">
            <tns:topLevelGroupName>
               <xsl:value-of select="./rsd:topLevelGroupName"/>
            </tns:topLevelGroupName>
         </xsl:if>
      </tns:groupInfo>
   </xsl:template>

   <xsl:template match="rsd:address">
      <tns:address>
         <tns:type>
            <xsl:value-of select="./rsd:type"/>
         </tns:type>
         <xsl:if test="./rsd:zipCode">
            <tns:zipCode>
               <xsl:value-of select="./rsd:zipCode"/>
            </tns:zipCode>
         </xsl:if>
         <tns:city>
            <xsl:value-of select="./rsd:city"/>
         </tns:city>
         <tns:street>
            <xsl:value-of select="./rsd:street"/>
         </tns:street>
         <tns:building>
            <xsl:value-of select="./rsd:building"/>
         </tns:building>
         <xsl:if test="./rsd:block">
            <tns:block>
               <xsl:value-of select="./rsd:block"/>
            </tns:block>
         </xsl:if>
         <xsl:if test="./rsd:phone">
            <tns:phone>
               <xsl:value-of select="./rsd:phone"/>
            </tns:phone>
         </xsl:if>
         <xsl:if test="./rsd:email">
            <tns:email>
               <xsl:value-of select="./rsd:email"/>
            </tns:email>
         </xsl:if>
      </tns:address>
   </xsl:template>

   <xsl:template match="rsd:share">
      <tns:share>
         <tns:ownershipID>
            <xsl:value-of select="./rsd:ownershipID"/>
         </tns:ownershipID>
         <xsl:if test="./rsd:share">
            <tns:share>
               <xsl:value-of select="./rsd:share"/>
            </tns:share>
         </xsl:if>
         <xsl:if test="./rsd:periodOfOwnerhip">
            <tns:periodOfOwnerhip>
               <xsl:value-of select="./rsd:periodOfOwnerhip"/>
            </tns:periodOfOwnerhip>
         </xsl:if>
      </tns:share>
   </xsl:template>

   <xsl:template match="rsd:participants">
      <tns:participants>
         <tns:participantID>
            <xsl:value-of select="./rsd:participantID"/>
         </tns:participantID>
         <tns:type>
            <xsl:value-of select="./rsd:type"/>
         </tns:type>
         <tns:isBorrower>
            <xsl:value-of select="./rsd:isBorrower"/>
         </tns:isBorrower>
         <xsl:if test="./rsd:clientType">
            <tns:clientType>
               <xsl:value-of select="./rsd:clientType"/>
            </tns:clientType>
         </xsl:if>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:resident>
            <xsl:value-of select="./rsd:resident"/>
         </tns:resident>
         <xsl:apply-templates select="./rsd:address"/>
         <xsl:apply-templates select="./rsd:share"/>
         <xsl:if test="./rsd:consolidatedGroupID">
            <tns:consolidatedGroupID>
               <xsl:value-of select="./rsd:consolidatedGroupID"/>
            </tns:consolidatedGroupID>
         </xsl:if>
         <xsl:if test="./rsd:linkedBorrowersGroupID">
            <tns:linkedBorrowersGroupID>
               <xsl:value-of select="./rsd:linkedBorrowersGroupID"/>
            </tns:linkedBorrowersGroupID>
         </xsl:if>
         <tns:industry>
            <xsl:value-of select="./rsd:industry"/>
         </tns:industry>
         <tns:inn>
            <xsl:value-of select="./rsd:inn"/>
         </tns:inn>
         <xsl:apply-templates select="./rsd:rating"/>
         <xsl:if test="./rsd:roleInConsolidatedGroup">
            <tns:roleInConsolidatedGroup>
               <xsl:value-of select="./rsd:roleInConsolidatedGroup"/>
            </tns:roleInConsolidatedGroup>
         </xsl:if>
      </tns:participants>
   </xsl:template>

   <xsl:template match="rsd:shareholder">
      <tns:shareholder>
         <tns:participantID>
            <xsl:value-of select="./rsd:participantID"/>
         </tns:participantID>
         <tns:type>
            <xsl:value-of select="./rsd:type"/>
         </tns:type>
         <tns:isBorrower>
            <xsl:value-of select="./rsd:isBorrower"/>
         </tns:isBorrower>
         <xsl:if test="./rsd:clientType">
            <tns:clientType>
               <xsl:value-of select="./rsd:clientType"/>
            </tns:clientType>
         </xsl:if>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:resident>
            <xsl:value-of select="./rsd:resident"/>
         </tns:resident>
         <xsl:apply-templates select="./rsd:address"/>
         <xsl:apply-templates select="./rsd:share"/>
         <xsl:if test="./rsd:consolidatedGroupID">
            <tns:consolidatedGroupID>
               <xsl:value-of select="./rsd:consolidatedGroupID"/>
            </tns:consolidatedGroupID>
         </xsl:if>
         <xsl:if test="./rsd:linkedBorrowersGroupID">
            <tns:linkedBorrowersGroupID>
               <xsl:value-of select="./rsd:linkedBorrowersGroupID"/>
            </tns:linkedBorrowersGroupID>
         </xsl:if>
         <tns:industry>
            <xsl:value-of select="./rsd:industry"/>
         </tns:industry>
         <tns:inn>
            <xsl:value-of select="./rsd:inn"/>
         </tns:inn>
         <xsl:apply-templates select="./rsd:rating"/>
         <xsl:if test="./rsd:roleInConsolidatedGroup">
            <tns:roleInConsolidatedGroup>
               <xsl:value-of select="./rsd:roleInConsolidatedGroup"/>
            </tns:roleInConsolidatedGroup>
         </xsl:if>
      </tns:shareholder>
   </xsl:template>

   <xsl:template match="rsd:beneficiar">
      <tns:beneficiar>
         <tns:participantID>
            <xsl:value-of select="./rsd:participantID"/>
         </tns:participantID>
         <tns:type>
            <xsl:value-of select="./rsd:type"/>
         </tns:type>
         <tns:isBorrower>
            <xsl:value-of select="./rsd:isBorrower"/>
         </tns:isBorrower>
         <xsl:if test="./rsd:clientType">
            <tns:clientType>
               <xsl:value-of select="./rsd:clientType"/>
            </tns:clientType>
         </xsl:if>
         <tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <tns:resident>
            <xsl:value-of select="./rsd:resident"/>
         </tns:resident>
         <xsl:apply-templates select="./rsd:address"/>
         <xsl:apply-templates select="./rsd:share"/>
         <xsl:if test="./rsd:consolidatedGroupID">
            <tns:consolidatedGroupID>
               <xsl:value-of select="./rsd:consolidatedGroupID"/>
            </tns:consolidatedGroupID>
         </xsl:if>
         <xsl:if test="./rsd:linkedBorrowersGroupID">
            <tns:linkedBorrowersGroupID>
               <xsl:value-of select="./rsd:linkedBorrowersGroupID"/>
            </tns:linkedBorrowersGroupID>
         </xsl:if>
         <tns:industry>
            <xsl:value-of select="./rsd:industry"/>
         </tns:industry>
         <tns:inn>
            <xsl:value-of select="./rsd:inn"/>
         </tns:inn>
         <xsl:apply-templates select="./rsd:rating"/>
         <xsl:if test="./rsd:roleInConsolidatedGroup">
            <tns:roleInConsolidatedGroup>
               <xsl:value-of select="./rsd:roleInConsolidatedGroup"/>
            </tns:roleInConsolidatedGroup>
         </xsl:if>
      </tns:beneficiar>
   </xsl:template>

   <xsl:template name="PrtspRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:prtspRs">
         <tns:id>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:id"/>
         </tns:id>
         <tns:requestType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:requestType"/>
         </tns:requestType>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:participantsGroup"/>
         <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:operationStatus"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
