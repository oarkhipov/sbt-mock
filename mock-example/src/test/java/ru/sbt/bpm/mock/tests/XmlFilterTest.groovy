package ru.sbt.bpm.mock.tests

import groovy.xml.QName
import ru.sbt.bpm.mock.generator.spring.integration.Pair

/**
 * @author sbt-bochev-as on 28.12.2015.
 *
 * Company: SBT - Moscow
 */

def requestDom = new XmlParser().parseText("""
    <ns:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:ns="http://sbrf.ru/legal/enquiry/integration"
             xmlns:ns1="http://sbrf.ru/legal/enquiry/integration/types">
   <ns:sendReferenceData>
      <ns:legalServices>
         <ns1:legalService>
            <ns1:serviceID>serviceID1</ns1:serviceID>
            <ns1:category>category1</ns1:category>
            <ns1:subject>subject1</ns1:subject>
            <ns1:type>type1</ns1:type>
            <ns1:subType>subType1</ns1:subType>
            <ns1:complexity>complexity1</ns1:complexity>
            <ns1:complexityType>SINGLE</ns1:complexityType>
            <ns1:FNTemplateId>FNTemplateId1</ns1:FNTemplateId>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:legalService>
      </ns:legalServices>
      <ns:priorities>
         <ns1:requestPriority>
            <ns1:referenceItemId>referenceItemId1</ns1:referenceItemId>
            <ns1:value>value1</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestPriority>
      </ns:priorities>
      <ns:categories>
         <ns1:requestCategory>
            <ns1:referenceItemId>referenceItemId2</ns1:referenceItemId>
            <ns1:value>value2</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestCategory>
      </ns:categories>
      <ns:subjects>
         <ns1:requestSubject>
            <ns1:referenceItemId>referenceItemId3</ns1:referenceItemId>
            <ns1:value>value3</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestSubject>
      </ns:subjects>
      <ns:types>
         <ns1:requestType>
            <ns1:referenceItemId>referenceItemId4</ns1:referenceItemId>
            <ns1:value>value4</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestType>
      </ns:types>
      <ns:subtypes>
         <ns1:requestSubType>
            <ns1:referenceItemId>referenceItemId5</ns1:referenceItemId>
            <ns1:value>value5</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestSubType>
      </ns:subtypes>
      <ns:complexities>
         <ns1:requestComplexity>
            <ns1:referenceItemId>referenceItemId6</ns1:referenceItemId>
            <ns1:value>value6</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestComplexity>
      </ns:complexities>
      <ns:rejectionReasons>
         <ns1:rejectionReason>
            <ns1:referenceItemId>referenceItemId7</ns1:referenceItemId>
            <ns1:value>value7</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:rejectionReason>
      </ns:rejectionReasons>
      <ns:qualityAssessments>
         <ns1:qualityAssessment>
            <ns1:referenceItemId>referenceItemId8</ns1:referenceItemId>
            <ns1:value>value8</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:qualityAssessment>
      </ns:qualityAssessments>
      <ns:clientStatuses>
         <ns1:clientStatusDictElement>
            <ns1:referenceItemId>referenceItemId9</ns1:referenceItemId>
            <ns1:value>value9</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:clientStatusDictElement>
      </ns:clientStatuses>
      <ns:clientJurisdictions>
         <ns1:clientJurisdiction>
            <ns1:referenceItemId>referenceItemId10</ns1:referenceItemId>
            <ns1:value>value10</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:clientJurisdiction>
      </ns:clientJurisdictions>
      <ns:requestStatuses>
         <ns1:requestStatus>
            <ns1:referenceItemId>referenceItemId11</ns1:referenceItemId>
            <ns1:value>value11</ns1:value>
            <ns1:effectiveTo>2015-12-28T18:28:19</ns1:effectiveTo>
         </ns1:requestStatus>
      </ns:requestStatuses>
   </ns:sendReferenceData>
   <ns:getAvailableExecutorsResponse>
      <ns:clientSystemTaskID>clientSystemTa1</ns:clientSystemTaskID>
      <ns:executorList>
         <ns1:operatorExecutor>
            <ns1:operatorInfo>
               <ns1:operatorID>operatorID1</ns1:operatorID>
               <ns1:operatorFullName>operatorFullName1</ns1:operatorFullName>
               <ns1:position>position1</ns1:position>
               <ns1:operatorOrgUnitName>operatorOrgUnitName1</ns1:operatorOrgUnitName>
            </ns1:operatorInfo>
            <ns1:expectedStartDate>2015-12-28T18:28:19</ns1:expectedStartDate>
            <ns1:expectedEndDate>2015-12-28T18:28:19</ns1:expectedEndDate>
            <ns1:isExecutorInRequest>false</ns1:isExecutorInRequest>
         </ns1:operatorExecutor>
      </ns:executorList>
   </ns:getAvailableExecutorsResponse>
   <ns:createLegalEnquiry>
      <ns:clientSystemTaskID>clientSystemTa2</ns:clientSystemTaskID>
      <ns:serviceID>serviceID2</ns:serviceID>
      <ns:complexityList>
         <ns:requestComplexityId>requestComplexityId1</ns:requestComplexityId>
      </ns:complexityList>
      <ns:legalEnquiryParameters>
         <ns1:requestTopic>requestTopic1</ns1:requestTopic>
         <ns1:requestDescription>requestDescription1</ns1:requestDescription>
         <ns1:creationDateTime>2015-12-28T18:28:19</ns1:creationDateTime>
         <ns1:priority>priority1</ns1:priority>
         <ns1:priorityReason>priorityReason1</ns1:priorityReason>
         <ns1:commentsHistory>
            <ns1:comment>
               <ns1:operatorId>operatorId1</ns1:operatorId>
               <ns1:operatorName>operatorName1</ns1:operatorName>
               <ns1:creationTime>2015-12-28T18:28:19</ns1:creationTime>
               <ns1:text>text1</ns1:text>
            </ns1:comment>
         </ns1:commentsHistory>
         <ns1:creditSubject>creditSubject1</ns1:creditSubject>
         <ns1:creditAmount>-922337203685477580</ns1:creditAmount>
         <ns1:creditCurrency>creditCurrency1</ns1:creditCurrency>
         <ns1:creditAmountRub>-922337203685477580</ns1:creditAmountRub>
         <ns1:residentCount>-922337203685477580</ns1:residentCount>
         <ns1:nonResidentCount>-922337203685477580</ns1:nonResidentCount>
         <ns1:clientInformationList>
            <ns1:clientInformation>
               <ns1:clientJurisdiction>clientJurisdiction1</ns1:clientJurisdiction>
               <ns1:clientStatus>clientStatus1</ns1:clientStatus>
               <ns1:clientName>clientName1</ns1:clientName>
               <ns1:productName>productName1</ns1:productName>
               <ns1:productType>productType1</ns1:productType>
               <ns1:creditType>creditType1</ns1:creditType>
               <ns1:creditPeriod>-922337203685477580</ns1:creditPeriod>
               <ns1:partyType>partyType1</ns1:partyType>
               <ns1:OPF>OPF1</ns1:OPF>
               <ns1:INN>INN1</ns1:INN>
               <ns1:branch>branch1</ns1:branch>
               <ns1:guaranteeAmount>-922337203685477580</ns1:guaranteeAmount>
               <ns1:guaranteeCurrency>guaranteeCurrency1</ns1:guaranteeCurrency>
               <ns1:guaranteeAmountRub>-922337203685477580</ns1:guaranteeAmountRub>
               <ns1:dateBoth>2015-12-28T18:28:19</ns1:dateBoth>
               <ns1:KIO>KIO1</ns1:KIO>
               <ns1:IDlink>IDlink1</ns1:IDlink>
            </ns1:clientInformation>
         </ns1:clientInformationList>
         <ns1:creditSubjectCount>-922337203685477580</ns1:creditSubjectCount>
         <ns1:typicalAgreementCount>-922337203685477580</ns1:typicalAgreementCount>
         <ns1:nonTypicalAgreementCount>-922337203685477580</ns1:nonTypicalAgreementCount>
         <ns1:agreementAmount>-922337203685477580</ns1:agreementAmount>
         <ns1:agreementAmountRub>-922337203685477580</ns1:agreementAmountRub>
         <ns1:agreementCurrency>agreementCurrency1</ns1:agreementCurrency>
      </ns:legalEnquiryParameters>
      <ns:initiatorId>initiatorId1</ns:initiatorId>
      <ns:initiatorFullName>initiatorFullName1</ns:initiatorFullName>
      <ns:documentList>
         <ns1:DocumentListGUID>DocumentListGUID1</ns1:DocumentListGUID>
      </ns:documentList>
      <ns:customerDossier>
         <ns1:DocumentListGUID>DocumentListGUID2</ns1:DocumentListGUID>
      </ns:customerDossier>
      <ns:relatedRequestID>relatedRequestID1</ns:relatedRequestID>
      <ns:chosenExecutor>chosenExecutor1</ns:chosenExecutor>
      <ns:initiatorManager>initiatorManager1</ns:initiatorManager>
      <ns:initiatorManagerFullName>initiatorManagerFullName1</ns:initiatorManagerFullName>
      <ns:clientSystemId>clientSystemId1</ns:clientSystemId>
   </ns:createLegalEnquiry>
   <ns:sendLegalEnquiryStatus>
      <ns:clientSystemTaskID>clientSystemTa3</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID1</ns:workObjectID>
      <ns:workObjectStatus>workObjectStatus1</ns:workObjectStatus>
      <ns:assignedToOperatorList>
         <ns1:operator>
            <ns1:operatorID>operatorID2</ns1:operatorID>
            <ns1:operatorFullName>operatorFullName2</ns1:operatorFullName>
            <ns1:position>position2</ns1:position>
            <ns1:operatorOrgUnitName>operatorOrgUnitName2</ns1:operatorOrgUnitName>
         </ns1:operator>
      </ns:assignedToOperatorList>
      <ns:executorList>
         <ns1:operator>
            <ns1:operatorID>operatorID3</ns1:operatorID>
            <ns1:operatorFullName>operatorFullName3</ns1:operatorFullName>
            <ns1:position>position3</ns1:position>
            <ns1:operatorOrgUnitName>operatorOrgUnitName3</ns1:operatorOrgUnitName>
         </ns1:operator>
      </ns:executorList>
      <ns:responsible>
         <ns1:operatorID>operatorID4</ns1:operatorID>
         <ns1:operatorFullName>operatorFullName4</ns1:operatorFullName>
         <ns1:position>position4</ns1:position>
         <ns1:operatorOrgUnitName>operatorOrgUnitName4</ns1:operatorOrgUnitName>
      </ns:responsible>
      <ns:expectedStartDate>2015-12-28T18:28:19</ns:expectedStartDate>
      <ns:expectedEndDate>2015-12-28T18:28:19</ns:expectedEndDate>
   </ns:sendLegalEnquiryStatus>
   <ns:cancelLegalEnquiry>
      <ns:clientSystemTaskID>clientSystemTa4</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID2</ns:workObjectID>
      <ns:cancelReason>cancelReason1</ns:cancelReason>
      <ns:clientSystemId>clientSystemId2</ns:clientSystemId>
   </ns:cancelLegalEnquiry>
   <ns:sendLegalEnquiryExecutionResult>
      <ns:clientSystemTaskID>clientSystemTa5</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID3</ns:workObjectID>
      <ns:commentsHistory>
         <ns1:comment>
            <ns1:operatorId>operatorId2</ns1:operatorId>
            <ns1:operatorName>operatorName2</ns1:operatorName>
            <ns1:creationTime>2015-12-28T18:28:19</ns1:creationTime>
            <ns1:text>text2</ns1:text>
         </ns1:comment>
      </ns:commentsHistory>
      <ns:referenceToDocumentsInBPM>referenceToDocumentsInBPM1</ns:referenceToDocumentsInBPM>
   </ns:sendLegalEnquiryExecutionResult>
   <ns:getAvailableExecutors>
      <ns:clientSystemTaskID>clientSystemTa6</ns:clientSystemTaskID>
      <ns:serviceID>serviceID3</ns:serviceID>
      <ns:initiatorId>initiatorId2</ns:initiatorId>
      <ns:relatedRequestID>relatedRequestID2</ns:relatedRequestID>
      <ns:clientSystemId>clientSystemId3</ns:clientSystemId>
   </ns:getAvailableExecutors>
   <ns:confirmationMessage>
      <ns:errorMessage>
         <ns1:errorCode>errorCo1</ns1:errorCode>
         <ns1:errorShortDescription>errorShortDescription1</ns1:errorShortDescription>
         <ns1:errorText>errorText1</ns1:errorText>
         <ns1:validationErrorsOnFields>
            <ns1:xsdFieldName>xsdFieldName1</ns1:xsdFieldName>
         </ns1:validationErrorsOnFields>
      </ns:errorMessage>
      <ns:clientSystemTaskID>clientSystemTa7</ns:clientSystemTaskID>
   </ns:confirmationMessage>
   <ns:sendLegalEnquiryCreationResult>
      <ns:clientSystemTaskID>clientSystemTa8</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID4</ns:workObjectID>
      <ns:referenceToDocumentsInBPM>referenceToDocumentsInBPM2</ns:referenceToDocumentsInBPM>
   </ns:sendLegalEnquiryCreationResult>
   <ns:assessQuality>
      <ns:clientSystemTaskID>clientSystemTa9</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID5</ns:workObjectID>
      <ns:qualityAssessment>qualityAssessment1</ns:qualityAssessment>
      <ns:qualityAssessmentDetalization>qualityAssessmentDetalization1</ns:qualityAssessmentDetalization>
      <ns:clientSystemId>clientSystemId4</ns:clientSystemId>
   </ns:assessQuality>
   <ns:sendAdditionalInfo>
      <ns:clientSystemTaskID>clientSystemT10</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID6</ns:workObjectID>
      <ns:commentsHistory>
         <ns1:comment>
            <ns1:operatorId>operatorId3</ns1:operatorId>
            <ns1:operatorName>operatorName3</ns1:operatorName>
            <ns1:creationTime>2015-12-28T18:28:19</ns1:creationTime>
            <ns1:text>text3</ns1:text>
         </ns1:comment>
      </ns:commentsHistory>
      <ns:clientSystemId>clientSystemId5</ns:clientSystemId>
   </ns:sendAdditionalInfo>
   <ns:getReferenceData>
      <ns:requestMessage>requestMessage1</ns:requestMessage>
      <ns:clientSystemId>clientSystemId6</ns:clientSystemId>
   </ns:getReferenceData>
   <ns:getAdditionalInfo>
      <ns:clientSystemTaskID>clientSystemT11</ns:clientSystemTaskID>
      <ns:workObjectID>workObjectID7</ns:workObjectID>
      <ns:rejectionReason>rejectionReason1</ns:rejectionReason>
      <ns:commentsHistory>
         <ns1:comment>
            <ns1:operatorId>operatorId4</ns1:operatorId>
            <ns1:operatorName>operatorName4</ns1:operatorName>
            <ns1:creationTime>2015-12-28T18:28:19</ns1:creationTime>
            <ns1:text>text4</ns1:text>
         </ns1:comment>
      </ns:commentsHistory>
      <ns:referenceToDocumentsInBPM>referenceToDocumentsInBPM3</ns:referenceToDocumentsInBPM>
   </ns:getAdditionalInfo>
</ns:Envelope>
""")


def xmlMap = new ArrayList<Pair<String, String>>() {
    {
        add(new Pair<String, String>("http://sbrf.ru/legal/enquiry/integration", "Envelope"))
        add(new Pair<String, String>("http://sbrf.ru/legal/enquiry/integration", "sendAdditionalInfo"))
        add(new Pair<String, String>("http://sbrf.ru/legal/enquiry/integration", "commentsHistory"))
//       add(new Pair<String, String>("http://sbrf.ru/legal/enquiry/integration/types", "comment"))
    }
}

//-------------- SCRIPT --------------------------
//assert first element
if (((QName) requestDom.name()).namespaceURI == xmlMap.get(0).first &&
        ((QName) requestDom.name()).localPart == xmlMap.get(0).second) {

    //dom to search in
    def filterDom = requestDom
    //new root element
    def newFilterDom
    //remove elements array
    def remove = []
    //parent element to delete from
//    def parent

    //drawn to last element
    for (int i = 1; i < xmlMap.size(); i++) {
        for (Node child : (List<Node>) filterDom.children()) {
            def el = (QName) child.name()
            if (el.namespaceURI == xmlMap.get(i).first && el.localPart == xmlMap.get(i).second) {
                //if element found - make new root
                newFilterDom = child
            } else {
                //add elements to remove array
                remove.add new Tuple(child.parent(), child)
                //save parent to remove from
                //parent = child.parent()
            }
        }
        if (newFilterDom == null) break;
        if (filterDom != newFilterDom) {
            //new root found
            filterDom = newFilterDom
        } else {
            //no new root
            break;
        }
    }
    //remove marked
    remove.each { it.get(0).remove(it.get(1)) }
//                "//response.test=requestDom[s.sendAdditionalInfo][s.clientSystemTaskID].text()
}
def stringWriter = new StringWriter()
xmlNodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))
xmlNodePrinter.with {
    preserveWhitespace = true
}
xmlNodePrinter.print(requestDom)
print(stringWriter.toString())
