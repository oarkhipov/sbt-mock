<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:tns="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/GetParticipantsRs/1.11/prtspRs/Data/"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:CRM="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
   <xsl:import href="../util/headerTemplate.xsl"/>
   <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
   <xsl:param name="name"
              select="//*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCP/esb/envelope/']/*[local-name()='prtspRq' and namespace-uri()='http://sbrf.ru/NCP/CRM/']/*[local-name()='performer' and namespace-uri()='http://sbrf.ru/NCP/CRM/GetParticipantsRq/1.04/']/text()"/>
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
            <xsl:with-param name="operation-name" select="string('prtspRs')"/>
            <xsl:with-param name="correlation-id" select="$correlation-id"/>
            <xsl:with-param name="eis-name" select="$eis-name"/>
            <xsl:with-param name="system-id" select="$system-id"/>
            <xsl:with-param name="operation-version" select="$operation-version"/>
            <xsl:with-param name="user-id" select="$user-id"/>
            <xsl:with-param name="user-name" select="$user-name"/>
         </xsl:call-template>
         <soap:Body>
            <xsl:call-template name="prtspRs">
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
         <xsl:if test="./rsd:reportingDate">
            <tns:reportingDate>
               <xsl:value-of select="./rsd:reportingDate"/>
            </tns:reportingDate>
         </xsl:if>
         <xsl:apply-templates select="./rsd:rating"/>
      </tns:participants>
   </xsl:template>

   <xsl:template name="prtspRs">
      <xsl:param name="response"/>
      <xsl:param name="data"/>
      <xsl:element name="CRM:prtspRs">
			      <tns:dealID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:dealID"/>
         </tns:dealID>
			      <tns:requestType>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:requestType"/>
         </tns:requestType>
			      <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:participantsGroup"/>
			      <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:operationStatus"/>
		    </xsl:element>
   </xsl:template>
</xsl:stylesheet>
