<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService">

    <!--Файл для объединения примеров xml в Data файл. Не проверяет корректность, а просто копирует.
    Возмождный функционал:
     - создать пустой DataXml
     - создать новый DataXml из одного примера сообщения
     - добавить к существующему DataXml еще один пример сообщения
     на вход подается пример сообщения, в качестве параметра обязательно должен быть задан путь к файлу с DataXml. Обычно для этого достаточно задать имя Data файла и имя системы-->

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:include href="xsltFunctions.xsl"/>

    <!--То, что нужно задать при вызове-->
    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--Следующая строчка выкидывает ошибку, если нам не дали rootElementName-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>

    <!--Имя записи в Data-файле. Обозначает атрибут, которым будет обозначаться новый контейнер с данными -->
    <xsl:param name="name"
               select="'default'"/>
    <!--Имя дата файла. Чаще всего не совпадает с rootElementName, поэтому важно задать вручную-->
    <xsl:param name="dataFileName"
               select="$rootElementName"/>
    <!--Имя системы - подпапка, в которой хранится все файлы по сервисам-->
    <xsl:param name="system"
               select="'CRM'"/>

    <!--Параметры работы-->
    <!--При true возвращает пустой Data файл, без содержимого-->
    <xsl:param name="createEmptyData"
               select="'false'"/>
    <!--При true заменяет Data файл - новый файл будет содержать только новый пример сообщения. При false заменит только тот пример, что совпдает по имени с новым примером сообещния-->
    <xsl:param name="replace"
               select="'false'"/>
    <!--Тип контейнера - response или request-->
    <xsl:param name="type"
               select="'response'"/>


    <!--То, что чаще всего задавть не нужно, но теоритически возможен вариант что придется задать, если -->

    <!-- путь к папке с дата-файлами -->
    <xsl:param name="dataFolderPath"/>
    <!--полный путь к дата-файлу-->
    <xsl:param name="dataFilePath"
               select="concat($dataFolderPath,'/',$system,'/xml/',$dataFileName)"/>
    <!--Неймспейс примера сообщения. Пробует взять самый гулбокий элемпент тела сообщения. Если что-то нашел - добавляет к нему сзади "/Data/"-->
    <xsl:param name="operationsXSD" select="''"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>
    <xsl:variable name="dataNSFromFile" select="$operationXsdSchema/@targetNamespace"/>
    <!--<xsl:variable name="dataNSFromFile"-->
                  <!--select="mock:addDataToNamespaceUrl(/*[local-name()='Envelope']/*[local-name()='Body']/*/*[last()]/namespace-uri(), $rootElementName)"/>-->
    <!--Неймспейс дата-файла. Если в dataNSFromFile(неймспейс того файла, которого мы ему сокрмили) нет ничего - берет некий стандарьтный дефолтный неймспейс-->
    <xsl:param name="dataNsUrl"
               select="if ($dataNSFromFile!='') then mock:addDataToNamespaceUrl($dataNSFromFile, $rootElementName) else concat('http://sbrf.ru/mockService/',$rootElementName,'/Data/')"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="/">
        <!--Подгружаем уже имеющийся Дата-файл-->
        <xsl:variable name="data" select="document($dataFilePath)/*[local-name()='data']"/>
        <!--Выбираем неймспейс. Или если он есть - из дата файла. Или генерируем новый.-->
        <xsl:variable name="dataNS" select="if ($replace='false') then $data/namespace-uri() else $dataNsUrl"/>
        <xsl:element name="data" namespace="{$dataNS}">
            <xsl:if test="$createEmptyData='false'"> <!--ничего не создаем если попросили сделать пустой дата-файл-->
                <xsl:if test="$replace='false'"> <!--если надо - копируем то что было в дата-файле до нас. Кроме того, на что заменяем.-->
                    <xsl:copy-of select="$data/*[(local-name()='request' or local-name()='response')][@name!=$name]"/>
                </xsl:if>
                <xsl:apply-templates select="//*[local-name()='Body']" mode="add"> <!--копируем тело сообщения, заменяя неймспейсы-->
                    <xsl:with-param name="ns" select="$dataNS"/>
                </xsl:apply-templates>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <!--копируем тело сообщения, заменяя неймспейсы-->
    <xsl:template match="*[local-name()='Body']" mode="add">
        <xsl:param name="ns"/> <!--неймспейс, в котором будет наш дата-файл-->
        <xsl:element name="{$type}" namespace="{$ns}"> <!--контейнер response или request -->
            <xsl:attribute name="name"><xsl:value-of select="$name"/></xsl:attribute>
            <xsl:element name="SoapHeader" namespace="{$ns}"> <!--сначала копируем заголовок. его тоже надо вставить в дата-файл -->
                <xsl:apply-templates select="//*[local-name()='Header']/*" mode="copyNopNS">
                    <xsl:with-param name="ns" select="$ns"/>
                </xsl:apply-templates>
            </xsl:element>
            <xsl:apply-templates select="./*/*" mode="copyNopNS">
                <xsl:with-param name="ns" select="$ns"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--копирование нады с заменой неймспейса на параметр ns. Атрибуты не копирует - у нас пока нет атрибутов в сообщениях-->
    <xsl:template match="node()" mode="copyNopNS">
        <xsl:param name="ns"/>
        <xsl:element name="{local-name(.)}" namespace="{$ns}">
            <xsl:apply-templates select="./*" mode="copyNopNS">
                <xsl:with-param name="ns" select="$ns"/>
            </xsl:apply-templates>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="text()"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <!--Копирование коментариев. Нужно потому что генерация примеров сообщений вставляет полезные коментарии о обязательности и о возможном количестве нод-->
    <xsl:template match="comment()" mode="copyNopNS">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:stylesheet>