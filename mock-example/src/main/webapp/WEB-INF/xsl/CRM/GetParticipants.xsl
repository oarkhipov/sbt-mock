<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/prtspRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='prtspRq' and namespace-uri()='http://sbrf.ru/NCP/CRM/']/*[local-name()='requestType' and namespace-uri()='http://sbrf.ru/NCP/CRM/GetParticipantsRq/1.04/']/text()"/>
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
            <xsl:with-param name="operation-name" select="string('GetParticipantsOfCreditDeal')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="GetParticipantsOfCreditDeal">
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

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/:OperationStatus--><xsl:template match="rsd:operationStatus">
      <tns:operationStatus><!--xsd:element - Inside--><tns:errorCode>
            <xsl:value-of select="./rsd:errorCode"/>
         </tns:errorCode>
         <!--xsd:element - Inside--><xsl:if test="./rsd:errorMessage">
            <tns:errorMessage>
               <xsl:value-of select="./rsd:errorMessage"/>
            </tns:errorMessage>
         </xsl:if>
      </tns:operationStatus>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/:Rating--><xsl:template match="rsd:ratings">
      <tns:ratings><!--xsd:element - Inside--><tns:ratingID>
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
      </tns:ratings>
   </xsl:template>

   <xsl:template match="rsd:rating">
      <tns:rating><!--xsd:element - Inside--><tns:ratingID>
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
      </tns:rating>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/:ParticipantsGroup--><xsl:template match="rsd:participantsGroup">
      <tns:participantsGroup><!-- xsd:element[$typesList] - Inside Participant--><xsl:apply-templates select="./rsd:participants"/>
         <!--xsd:element - Inside--><tns:groupType>
            <xsl:value-of select="./rsd:groupType"/>
         </tns:groupType>
         <!-- xsd:element[$typesList] - Inside GroupInfo--><xsl:apply-templates select="./rsd:groupInfo"/>
      </tns:participantsGroup>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/:GroupInfo--><xsl:template match="rsd:groupInfo">
      <tns:groupInfo><!--xsd:element - Inside--><tns:id>
            <xsl:value-of select="./rsd:id"/>
         </tns:id>
         <!--xsd:element - Inside--><tns:label>
            <xsl:value-of select="./rsd:label"/>
         </tns:label>
         <!--xsd:element - Inside--><tns:status>
            <xsl:value-of select="./rsd:status"/>
         </tns:status>
         <!--xsd:element - Inside--><tns:updateDate>
            <xsl:value-of select="./rsd:updateDate"/>
         </tns:updateDate>
         <!--xsd:element - Inside--><xsl:if test="./rsd:approvalDate">
            <tns:approvalDate>
               <xsl:value-of select="./rsd:approvalDate"/>
            </tns:approvalDate>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside Rating--><xsl:apply-templates select="./rsd:ratings"/>
         <!--xsd:element - Inside--><xsl:if test="./rsd:topLevelGroupName">
            <tns:topLevelGroupName>
               <xsl:value-of select="./rsd:topLevelGroupName"/>
            </tns:topLevelGroupName>
         </xsl:if>
      </tns:groupInfo>
   </xsl:template>

   <!--xsd:complexType - template http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/:Participant--><xsl:template match="rsd:participants">
      <tns:participants><!--xsd:element - Inside--><tns:participantID>
            <xsl:value-of select="./rsd:participantID"/>
         </tns:participantID>
         <!--xsd:element - Inside--><tns:type>
            <xsl:value-of select="./rsd:type"/>
         </tns:type>
         <!--xsd:element - Inside--><tns:isBorrower>
            <xsl:value-of select="./rsd:isBorrower"/>
         </tns:isBorrower>
         <!--xsd:element - Inside--><xsl:if test="./rsd:clientType">
            <tns:clientType>
               <xsl:value-of select="./rsd:clientType"/>
            </tns:clientType>
         </xsl:if>
         <!--xsd:element - Inside--><tns:name>
            <xsl:value-of select="./rsd:name"/>
         </tns:name>
         <!--xsd:element - Inside--><tns:resident>
            <xsl:value-of select="./rsd:resident"/>
         </tns:resident>
         <!--xsd:element - Inside--><xsl:if test="./rsd:consolidatedGroupID">
            <tns:consolidatedGroupID>
               <xsl:value-of select="./rsd:consolidatedGroupID"/>
            </tns:consolidatedGroupID>
         </xsl:if>
         <!--xsd:element - Inside--><xsl:if test="./rsd:linkedBorrowersGroupID">
            <tns:linkedBorrowersGroupID>
               <xsl:value-of select="./rsd:linkedBorrowersGroupID"/>
            </tns:linkedBorrowersGroupID>
         </xsl:if>
         <!--xsd:element - Inside--><tns:industry>
            <xsl:value-of select="./rsd:industry"/>
         </tns:industry>
         <!--xsd:element - Inside--><tns:inn>
            <xsl:value-of select="./rsd:inn"/>
         </tns:inn>
         <!--xsd:element - Inside--><xsl:if test="./rsd:reportingDate">
            <tns:reportingDate>
               <xsl:value-of select="./rsd:reportingDate"/>
            </tns:reportingDate>
         </xsl:if>
         <!-- xsd:element[$typesList] - Inside Rating--><xsl:apply-templates select="./rsd:rating"/>
      </tns:participants>
   </xsl:template>

   <!--xsd:complexType - template :PrtspRs--><!--local-name=$xsdTagsToImport base complexType - complexTypehttp://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/-http://sbrf.ru/NCP/CRM/--><xsl:template name="GetParticipantsOfCreditDeal">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:prtspRs"><!--xsd:element - Inside--><tns:dealID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:dealID"/>
         </tns:dealID>
         <!--xsd:element - Inside--><tns:requestType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:requestType"/>
         </tns:requestType>
         <!-- xsd:element[$typesList] - Inside ParticipantsGroup--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:participantsGroup"/>
         <!-- xsd:element[$typesList] - Inside OperationStatus--><xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:operationStatus"/>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>
