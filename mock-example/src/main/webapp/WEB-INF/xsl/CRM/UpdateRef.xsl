<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:esbhd="http://sbrf.ru/prpc/esb/headers/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:call-template name="UpdateRef" />
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

	<xsl:template name="UpdateRef">
        <crm:updateRefRs xmlns:crm="http://sbrf.ru/NCP/CRM/" xmlns:ur="http://sbrf.ru/NCP/CRM/UpdateRefRs/">
            <ur:referenceItem>
                <ur:referenceid>REF_CURRENCY</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>KZT</ur:value>
                    <ur:valueId>REF_CURRENCY_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>RUR</ur:value>
                    <ur:valueId>REF_CURRENCY_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>USD</ur:value>
                    <ur:valueId>REF_CURRENCY_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>EUR</ur:value>
                    <ur:valueId>REF_CURRENCY_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>BYR</ur:value>
                    <ur:valueId>REF_CURRENCY_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_CUSTOMERS_SHARE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Высокий (доля одного потребителя более 50%)
                    </ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Значительный (доля одного потребителя от 30% до 50%)
                    </ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Средний (доля одного потребителя от 10% до 30%)
                    </ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Низкий (доля одного потребителя менее 10%)
                    </ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Зависимость потребителя не влияет на стабильность в
                        данной отрасли
                    </ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Нет информации</ur:value>
                    <ur:valueId>REF_CUSTOMERS_SHARE_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_INDUSTRY</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Пищевая промышленность и производство табачных изделий
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Химическая промышленность</ur:value>
                    <ur:valueId>REF_INDUSTRY_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Энергетика и водоснабжение</ur:value>
                    <ur:valueId>REF_INDUSTRY_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Металлургическая промышленность</ur:value>
                    <ur:valueId>REF_INDUSTRY_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Машиностроение</ur:value>
                    <ur:valueId>REF_INDUSTRY_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Лесная, деревообрабатывающая и целлюлозно-бумажная
                        промышленность
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Угольная промышленность</ur:value>
                    <ur:valueId>REF_INDUSTRY_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Нефтегазовая промышленность</ur:value>
                    <ur:valueId>REF_INDUSTRY_7</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Сельское хозяйство</ur:value>
                    <ur:valueId>REF_INDUSTRY_8</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Строительство и операции с недвижимым имуществом
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_9</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Транспорт</ur:value>
                    <ur:valueId>REF_INDUSTRY_10</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Связь и телекоммуникации</ur:value>
                    <ur:valueId>REF_INDUSTRY_11</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Торговля (Кроме специализированной оптовой)
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_12</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Услуги (кроме специализированных услуг, указанных в
                        других разделах классификатора)
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_13</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Здравоохранение, образование</ur:value>
                    <ur:valueId>REF_INDUSTRY_14</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Деятельность в области лизинга</ur:value>
                    <ur:valueId>REF_INDUSTRY_15</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансовая деятельность</ur:value>
                    <ur:valueId>REF_INDUSTRY_16</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Страховая компания</ur:value>
                    <ur:valueId>REF_INDUSTRY_17</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Государственное управление и обеспечение гражданской
                        безопасности; обязательное социальное обеспечение
                    </ur:value>
                    <ur:valueId>REF_INDUSTRY_18</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Прочие виды экономической деятельности.</ur:value>
                    <ur:valueId>REF_INDUSTRY_19</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_INSTRUMENT</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Не возобновляемая кредитная линия</ur:value>
                    <ur:valueId>REF_INSTRUMENT_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Единовременная выдача займа</ur:value>
                    <ur:valueId>REF_INSTRUMENT_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Возобновляемая кредитная линия</ur:value>
                    <ur:valueId>REF_INSTRUMENT_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_INTEREST_RATE_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Фиксированная</ur:value>
                    <ur:valueId>REF_INTEREST_RATE_TYPE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Переменная</ur:value>
                    <ur:valueId>REF_INTEREST_RATE_TYPE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Плавающая + фиксирован. Маржа</ur:value>
                    <ur:valueId>REF_INTEREST_RATE_TYPE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Плавающая + перемен. Маржа</ur:value>
                    <ur:valueId>REF_INTEREST_RATE_TYPE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Комбинированная</ur:value>
                    <ur:valueId>REF_INTEREST_RATE_TYPE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_LEGAL_ENTITY_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>ОАО</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ЗАО</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ООО</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ИП</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ФЛ</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>КФХ</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>СПК</ur:value>
                    <ur:valueId>REF_LEGAL_ENTITY_TYPE_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_LIMIT_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Унифицированный</ur:value>
                    <ur:valueId>REF_LIMIT_TYPE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Унифицированный, переданный на использование по старой
                        системе
                    </ur:value>
                    <ur:valueId>REF_LIMIT_TYPE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Структурированный</ur:value>
                    <ur:valueId>REF_LIMIT_TYPE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_LOAN_PURPOSE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Овердрафт</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Корпоративное кредитование</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Инвестиционное кредитование (кроме строительства)
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Инвестиционное кредитование строительства объектов
                        жилой недвижимости
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Инвестиционное кредитование строительства объектов
                        коммерческой недвижимости
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Непокрытые аккредитивы (с постфинансированием или с
                        обеспечением денежными средствами менее 100%)
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Факторинг</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проектное финансирование (кроме строительства)
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_7</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Непокрытые аккредитивы (с постфинансированием или с
                        обеспечением денежными средствами менее 100%)
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_8</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проектное финансирование строительства объектов жилой
                        недвижимости
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_9</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проектное финансирование строительства объектов
                        коммерческой недвижимости
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_10</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Контрактное кредитование предприятий</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_11</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия исполнения обязательств по договору исполнения
                        контракта
                    </ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_12</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Тендерная гарантия</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_13</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование лизинговых сделок</ur:value>
                    <ur:valueId>REF_LOAN_PURPOSE_14</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_MARKET_TREND</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Рост</ur:value>
                    <ur:valueId>REF_MARKET_TREND_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Стагнация</ur:value>
                    <ur:valueId>REF_MARKET_TREND_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Падение</ur:value>
                    <ur:valueId>REF_MARKET_TREND_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Волатильность</ur:value>
                    <ur:valueId>REF_MARKET_TREND_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PROBLEM_CATEGORY</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>ухудшение рейтинга заемщика на 3 уровня, при этом
                        рейтинг заемщика после снижения определен на уровне 17 и хуже, но
                        не превышает уровень 24
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ухудшение рейтинга заемщика до уровня 22 – 24
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие просроченной задолженности по обязательствам
                        перед Банком непрерывной длительностью от 5 до 30 календарных дней
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>снижение выручки и/или EBITDA заемщика, в течение 2
                        последних квартальных отчетных дат от запланированного и
                        утвержденного Банком значения, в соответствии с прогнозом движения
                        денежных средств, утвержденным Банком при принятии решения
                        Коллегиальным органом о заключении наиболее поздней из действующих
                        сделок, более чем на 20%, но не более чем на 50%
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>возникновение у участника кредитной сделки просроченной
                        дебиторской задолженности сроком более 3 месяцев в размере не менее
                        25 процентов от его совокупной дебиторской задолженности
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>снижение собственного капитала заемщика до
                        отрицательной величины и сохранение данного отрицательного значения
                        собственного капитала на протяжении 3 последних квартальных
                        отчетных дат
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>возрастание (по сравнению с уровнем на дату образования
                        задолженности) рисков, связанных с регулированием деятельности
                        заемщика, включая отмену существующих льгот, изменение режима
                        налогообложения и другие подобные факторы, негативно влияющие на
                        бизнес участника кредитной сделки
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>изменения в составе собственников и/или руководстве
                        участника кредитной сделки, которые могут негативно повлиять на
                        бизнес, информация о наличии серьезных разногласий и конфронтации
                        между акционерами/участниками и их возможной смене
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_7</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>усиление влияния рисков по сравнению с их уровнем на
                        момент образования задолженности, связанных с наличием негативной
                        информации о деловой репутации участника кредитной сделки, его
                        владельцах, руководителях и взаимосвязанных с ними лиц, в том числе
                        недобросовестные действия участника кредитной сделки, в частности
                        предоставление фиктивных документов, нарушение достигнутых с Банком
                        договоренностей, выраженных в устной (отраженной в протоколе
                        переговоров) и письменной форме
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_8</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>обращение заемщика с заявлением о реструктуризации
                        задолженности, связанное с отсутствием возможности или намерения
                        заемщика исполнить обязательства на первоначальных условиях
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_9</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>по результатам проверки консолидированной группы
                        выявлено отрицательное влияние члена консолидированной группы,
                        относящегося к желтой зоне, обосновывающее отнесение заемщика к
                        желтой зоне
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_10</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие предъявленных к участнику кредитной сделки
                        исков третьих лиц, которые могут оказать негативное влияние на
                        возврат задолженности
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_11</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие просроченной задолженности Участника кредитной
                        сделки перед другими кредиторами непрерывной длительностью от 15 до
                        90 календарных дней в сумме, превышающей 50 процентов его
                        собственного капитала
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_12</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>снижение совокупной стоимости залогового обеспечения,
                        отнесенного к категориям "основное" и "комфортное обеспечение" (в
                        том числе в результате его повреждения, утраты и т.п.) в размере
                        более чем на 20%, но не более чем на 30% от рыночной стоимости
                        данного обеспечения, определенной последним заключением залоговой
                        службы
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_13</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>нарушение условий кредитной документации/ковенант
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_14</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>ухудшение рейтинга заемщика до уровня 25 или 26
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_15</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие просроченной задолженности по обязательствам
                        перед Банком непрерывной длительностью от 31 до 90 календарных дней
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_16</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие просроченной задолженности участника кредитной
                        сделки перед другими кредиторами непрерывной длительностью свыше 90
                        календарных дней в сумме, превышающей 50% его собственного капитала
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_17</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>снижение выручки и/или EBITDA заемщика, в течение 2
                        (двух) последних квартальных отчетных дат от запланированного и
                        утвержденного Банком значения, в соответствии с прогнозом движения
                        денежных средств, утвержденным Банком при принятии решения
                        Коллегиальным органом о заключении наиболее поздней из действующих
                        сделок, более чем на 50%
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_18</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>отсутствие у заемщика действующей лицензии на право
                        осуществления деятельности, поступления от которой генерировали
                        свыше 30 % денежных потоков заемщика на протяжении последних 6
                        месяцев, если данный вид деятельности подлежит лицензированию
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_19</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>принятие судом к рассмотрению иска о признании
                        недействительности сделки, в рамках исполнения которой на заемщика
                        возложена обязанность по погашению задолженности, или отдельных
                        условий данной сделки, снижающих риски Банка
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_20</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>снижение совокупной стоимости залогового обеспечения,
                        отнесенного к категориям "основное" и "комфортное обеспечение" (в
                        том числе в результате его повреждения, утраты и т.п.) в размере
                        более чем на 30% от рыночной стоимости данного обеспечения,
                        определенной последним заключением залоговой службы
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_21</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>по результатам проверки консолидированной группы
                        выявлено отрицательное влияние члена консолидированной группы,
                        относящегося к красной зоне, обосновывающее отнесение заемщика к
                        красной зоне
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_22</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>фактическое прекращение участником кредитной сделки,
                        кроме заемщика, финансово-хозяйственной деятельности
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_23</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>повторное обращение заемщика с заявлением о
                        реструктуризации задолженности, связанное с отсутствием возможности
                        или намерения заемщика исполнить обязательства на первоначальных
                        условиях, если в составе задолженности есть обязательства, которые
                        ранее уже были реструктурированы
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_24</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>иные факторы, которые лидер процесса и/или андеррайтер
                        считает достаточным основанием для отнесения задолженности к
                        красной зоне
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_25</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие просроченной задолженности по обязательствам
                        перед Банком непрерывной длительностью свыше 90 календарных дней
                        (дефолт)
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_26</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>отказ заемщика от погашения задолженности, выраженный в
                        письменной или устной форме
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_27</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>принятие участником кредитной сделки решения о
                        ликвидации
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_28</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>наличие производства по делу о несостоятельности
                        (банкротстве) участника кредитной сделки
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_29</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>фактическое прекращение заемщиком
                        финансово-хозяйственной деятельности
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_30</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>принятие кредитующим подразделением и ПРПА совместного
                        решения о выборе дефолтной стратегии работы с ПА
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_31</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>инициирование Банком процедуры принудительного
                        взыскания всей суммы/части задолженности заемщика в судебном
                        порядке/обращения взыскания на заложенное имущество
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_32</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>принятие коллегиальным органом Банка решения обратить
                        взыскание на заложенное имущество во внесудебном порядке
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_33</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>инициирование Банком возбуждения уголовных дел в
                        отношении участника кредитной сделки/его руководителей и/или
                        владельцев
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_34</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>введение временной финансовой администрации
                        (конкурсный, реабилитационный управляющий и т.п.) в отношении
                        заемщика
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_35</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>по результатам проверки ГСЗ выявлено отрицательное
                        влияние члена ГСЗ, относящегося к черной зоне, обосновывающее
                        отнесение заемщика к черной зоне
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_36</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>иные факторы, которые лидер процесса и/или андеррайтер
                        считает достаточным основанием для отнесения задолженности к черной
                        зоне
                    </ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_37</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>отсутствие любых источников погашения</ur:value>
                    <ur:valueId>REF_PROBLEM_CATEGORY_38</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PROBLEM_CRITERIA</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Признаки проблемности не идентифицированы</ur:value>
                    <ur:valueId>REF_PROBLEM_CRITERIA_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Потенциально проблемный</ur:value>
                    <ur:valueId>REF_PROBLEM_CRITERIA_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проблемный</ur:value>
                    <ur:valueId>REF_PROBLEM_CRITERIA_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Безнадежный</ur:value>
                    <ur:valueId>REF_PROBLEM_CRITERIA_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PRODUCT</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Кредит на пополнение оборотных активов (финансирование
                        производственной, торговой, снабженческой, иной деятельности)
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредит на выплату заработной платы, включая суммы
                        обязательных страховых взносов и подоходного налога с физических
                        лиц, взносов в Фонд социальной защиты населения Министерства труда
                        и социальной защиты населения Министерства труда и социальной
                        защиты, и иные взносы, исчисляемые с суммы заработной платы
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредит на уплату налогов, сборов, пошлин и иных
                        обязательных платежей, взносов и т.п.
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредит на финансирование затрат по капитальному
                        (текущему) ремонту
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредит на иные расходы, необходимые для осуществления
                        текущей деятельности
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредит на приобретение отдельных единиц движимого и
                        недвижимого имущества и нематериальных активов (включая косвенные
                        затраты) вне реализации клиентом проектов технического
                        перевооружения, модернизации действующего/создания нового
                        производства с общим принципом классификации кредита по типу
                        финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредиты на реконструкцию/модернизацию зданий,
                        сооружений, инженерных и транспортных коммуникаций в соответствии с
                        общим принципом классификации кредита по типу финансирования.
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредиты на приобретение движимого и недвижимого
                        имущества и нематериальных активов (включая косвенные затраты) в
                        рамках реализации клиентом проектов технического перевооружения,
                        модернизации действующего/создания нового производства в
                        соответствии с общим принципом классификации кредита по типу
                        финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_7</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Операции прямого финансового лизинга в соответствии с
                        общим принципом классификации кредита по типу финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_8</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование под уступку денежных требований
                        (факторинг)
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_9</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование в рамках специализированных программ
                        ЕАБР и ЕБРР с общим принципом классификации продукта по типу
                        финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_10</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование с привлечением ресурсов иностранных
                        банков с общим принципом классификации продукта по типу
                        финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_11</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Контрактное кредитование</ur:value>
                    <ur:valueId>REF_PRODUCT_12</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредитование под заклад денег</ur:value>
                    <ur:valueId>REF_PRODUCT_13</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредитование внешнеторговых контрактов</ur:value>
                    <ur:valueId>REF_PRODUCT_14</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование строительства объектов жилой
                        недвижимости
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_15</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Финансирование строительства объектов коммерческой
                        недвижимости
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_16</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проектное финансирование (кроме строительства)
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_17</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Кредитование на приобретение лизингодателем
                        (лизинговыми компаниями) предметов лизинга
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_18</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Контрактное кредитование предприятий</ur:value>
                    <ur:valueId>REF_PRODUCT_19</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Корпоративное кредитование на инвестиционные цели
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_20</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Корпоративное кредитование на пополнение оборотных
                        средств
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_21</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Овердрафт</ur:value>
                    <ur:valueId>REF_PRODUCT_22</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Факторинг</ur:value>
                    <ur:valueId>REF_PRODUCT_23</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия исполнения обязательств по договору исполнения
                        контракта
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_24</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Аккредитивы без перевода покрытия</ur:value>
                    <ur:valueId>REF_PRODUCT_25</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия/ контргарантия исполнения обязательств по
                        договору/ контракту с общим принципом распределения продукта по
                        типу финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_26</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия/ контргарантия возврата авансового платежа с
                        общим принципом распределения продукта по типу финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_27</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия обеспечения кредита с общим принципом
                        распределения продукта по типу финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_28</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Тендерные гарантии</ur:value>
                    <ur:valueId>REF_PRODUCT_29</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантия/контргарантия платежа с общим принципом
                        распределения продукта по типу финансирования
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_30</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантии в пользу таможенных органов</ur:value>
                    <ur:valueId>REF_PRODUCT_31</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантии в пользу БАМАП</ur:value>
                    <ur:valueId>REF_PRODUCT_32</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Непокрытые аккредитивы (с постфинансированием или с
                        обеспечением денежными средствами менее 100%)
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_33</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PRODUCT_CATEGORY</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Унифицированный</ur:value>
                    <ur:valueId>REF_PRODUCT_CATEGORY_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Унифицированный, в рамках лимита, переданного на
                        использование по старой системе
                    </ur:value>
                    <ur:valueId>REF_PRODUCT_CATEGORY_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Структурированный</ur:value>
                    <ur:valueId>REF_PRODUCT_CATEGORY_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PRODUCT_LIMIT_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Овердрафт</ur:value>
                    <ur:valueId>REF_PRODUCT_LIMIT_TYPE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Корпоративное кредитование</ur:value>
                    <ur:valueId>REF_PRODUCT_LIMIT_TYPE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Проектное финансирование</ur:value>
                    <ur:valueId>REF_PRODUCT_LIMIT_TYPE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Контрактное кредитование</ur:value>
                    <ur:valueId>REF_PRODUCT_LIMIT_TYPE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Гарантии</ur:value>
                    <ur:valueId>REF_PRODUCT_LIMIT_TYPE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_RATING</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Не рассчитан</ur:value>
                    <ur:valueId>REF_RATING_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>1</ur:value>
                    <ur:valueId>REF_RATING_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>2</ur:value>
                    <ur:valueId>REF_RATING_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>3</ur:value>
                    <ur:valueId>REF_RATING_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>4</ur:value>
                    <ur:valueId>REF_RATING_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>5</ur:value>
                    <ur:valueId>REF_RATING_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>6</ur:value>
                    <ur:valueId>REF_RATING_6</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>7</ur:value>
                    <ur:valueId>REF_RATING_7</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>8</ur:value>
                    <ur:valueId>REF_RATING_8</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>9</ur:value>
                    <ur:valueId>REF_RATING_9</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>10</ur:value>
                    <ur:valueId>REF_RATING_10</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>11</ur:value>
                    <ur:valueId>REF_RATING_11</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>12</ur:value>
                    <ur:valueId>REF_RATING_12</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>13</ur:value>
                    <ur:valueId>REF_RATING_13</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>14</ur:value>
                    <ur:valueId>REF_RATING_14</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>15</ur:value>
                    <ur:valueId>REF_RATING_15</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>16</ur:value>
                    <ur:valueId>REF_RATING_16</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>17</ur:value>
                    <ur:valueId>REF_RATING_17</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>18</ur:value>
                    <ur:valueId>REF_RATING_18</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>19</ur:value>
                    <ur:valueId>REF_RATING_19</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>20</ur:value>
                    <ur:valueId>REF_RATING_20</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>21</ur:value>
                    <ur:valueId>REF_RATING_21</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>22</ur:value>
                    <ur:valueId>REF_RATING_22</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>23</ur:value>
                    <ur:valueId>REF_RATING_23</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>24</ur:value>
                    <ur:valueId>REF_RATING_24</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>25</ur:value>
                    <ur:valueId>REF_RATING_25</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>26</ur:value>
                    <ur:valueId>REF_RATING_26</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_REPAYMENT_MODEL</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Одним платежом</ur:value>
                    <ur:valueId>REF_REPAYMENT_MODEL_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Ежемесячно</ur:value>
                    <ur:valueId>REF_REPAYMENT_MODEL_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Ежеквартально</ur:value>
                    <ur:valueId>REF_REPAYMENT_MODEL_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Иное</ur:value>
                    <ur:valueId>REF_REPAYMENT_MODEL_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_SEGMENT</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Крупный и крупнейший бизнес</ur:value>
                    <ur:valueId>REF_SEGMENT_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Средний бизнес</ur:value>
                    <ur:valueId>REF_SEGMENT_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_STRESS_ANALYSIS</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Финансовый долг / EBITDA</ur:value>
                    <ur:valueId>REF_STRESS_ANALYSIS_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Чистый Долг / EBITDA</ur:value>
                    <ur:valueId>REF_STRESS_ANALYSIS_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Скорректированный финансовый долг/EBITDA</ur:value>
                    <ur:valueId>REF_STRESS_ANALYSIS_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Скорректированный чистый долг / EBITDA</ur:value>
                    <ur:valueId>REF_STRESS_ANALYSIS_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_SUPPLIERS_SHARE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Высокий (доля одного поставщика более 50%)
                    </ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Значительный (доля одного поставщика от 30% до 50%)
                    </ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Средний (доля одного поставщика от 10% до 30%)
                    </ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Низкий (доля одного поставщика менее 10%)</ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Зависимость поставщика не влияет на стабильность в
                        данной отрасли
                    </ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Нет информации</ur:value>
                    <ur:valueId>REF_SUPPLIERS_SHARE_5</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_DISBURSEMENT_MODEL</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Период</ur:value>
                    <ur:valueId>PERIOD</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Дата/Сумма</ur:value>
                    <ur:valueId>SCHEDULE</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Не устанавливается</ur:value>
                    <ur:valueId>NOT_AVAILABLE</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_PARTICIPANT_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Гарант</ur:value>
                    <ur:valueId>REF_PARTICIPANT_GUARANTOR</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Залогодатель</ur:value>
                    <ur:valueId>REF_PARTICIPANT_PLEDGER</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Поручитель</ur:value>
                    <ur:valueId>REF_PARTICIPANT_SURETY</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Должник</ur:value>
                    <ur:valueId>REF_PARTICIPANT_DEBTOR</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Лизингополучатель</ur:value>
                    <ur:valueId>REF_PARTICIPANT_LEASEHOLDER</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Консолидированная группа</ur:value>
                    <ur:valueId>REF_PARTICIPANT_CONSOLIDATED_GROUP</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Доминирующая компания</ur:value>
                    <ur:valueId>REF_PARTICIPANT_DOMINANT_COMPANY</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Иной участник</ur:value>
                    <ur:valueId>REF_PARTICIPANT_OTHER</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_RISK_GROUP_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Риск заявителя</ur:value>
                    <ur:valueId>REF_RISK_GROUP_TYPE_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск сделки</ur:value>
                    <ur:valueId>REF_RISK_GROUP_TYPE_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск проекта</ur:value>
                    <ur:valueId>REF_RISK_GROUP_TYPE_2</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск контракта</ur:value>
                    <ur:valueId>REF_RISK_GROUP_TYPE_3</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Прочие риски</ur:value>
                    <ur:valueId>REF_RISK_GROUP_TYPE_4</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_RISK_TYPE</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Финансовые риски</ur:value>
                    <ur:valueId>REF_RISK_TYPE_0</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риски деловой репутации/репутационные риски</ur:value>
                    <ur:valueId>REF_RISK_TYPE_1</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Маркетинговый риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_2</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Акционерный/управленческий риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_3</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Регуляторный риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_4</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риски концентрации</ur:value>
                    <ur:valueId>REF_RISK_TYPE_5</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Страновой риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_6</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск оттока капитала</ur:value>
                    <ur:valueId>REF_RISK_TYPE_7</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Производственный риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_8</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск форсмажорных обстоятельств</ur:value>
                    <ur:valueId>REF_RISK_TYPE_9</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Валютный риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_10</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_0</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Юридический риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_11</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск недостаточности/отсутствия обеспечения</ur:value>
                    <ur:valueId>REF_RISK_TYPE_12</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск утраты обеспечения</ur:value>
                    <ur:valueId>REF_RISK_TYPE_13</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск отсутствия/утраты источника погашения кредита</ur:value>
                    <ur:valueId>REF_RISK_TYPE_14</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск некорректности Cash Flow</ur:value>
                    <ur:valueId>REF_RISK_TYPE_15</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск неправильного/некорректного структурирования сделки</ur:value>
                    <ur:valueId>REF_RISK_TYPE_16</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_1</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск невыполнения обязательств поставщиками/подрядчиками/субподрядчиками</ur:value>
                    <ur:valueId>REF_RISK_TYPE_17</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск недофинансирования проекта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_18</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Технологический риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_19</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск увеличения стоимости проекта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_20</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск не достижения заданных параметров проекта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_21</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Логистические и инфраструктурные риски</ur:value>
                    <ur:valueId>REF_RISK_TYPE_22</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Экологические риски</ur:value>
                    <ur:valueId>REF_RISK_TYPE_23</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_2</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск невыполнения обязательств поставщиками / подрядчиками/ соисполнителями Заявителя</ur:value>
                    <ur:valueId>REF_RISK_TYPE_24</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск невыполнения обязательств заказчиками Заявителя</ur:value>
                    <ur:valueId>REF_RISK_TYPE_25</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск неисполнения обязательств контрагентами-соисполнителями в рамках одного контракта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_26</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск недофинансирования контракта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_27</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск увеличения стоимости реализации контракта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_28</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск неисполнения контракта</ur:value>
                    <ur:valueId>REF_RISK_TYPE_29</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Юридический риск</ur:value>
                    <ur:valueId>REF_RISK_TYPE_30</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Риск изменения основных условий контракта (стоимости/графика/условий поставки/оплаты)</ur:value>
                    <ur:valueId>REF_RISK_TYPE_31</ur:valueId>
                    <ur:parentValueId>REF_RISK_GROUP_TYPE_3</ur:parentValueId>
                </ur:listOfValue>
            </ur:referenceItem>
            <ur:referenceItem>
                <ur:referenceid>REF_REJECTION_REASON</ur:referenceid>
                <ur:listOfValue>
                    <ur:value>Недостаточно поступивших документов</ur:value>
                    <ur:valueId>REF_REJECTION_REASON_0</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
                <ur:listOfValue>
                    <ur:value>Необходимо актуализировать в BPM структуру Сделки</ur:value>
                    <ur:valueId>REF_REJECTION_REASON_1</ur:valueId>
                    <ur:parentValueId/>
                </ur:listOfValue>
            </ur:referenceItem>
        </crm:updateRefRs>
    </xsl:template>

	
</xsl:stylesheet>