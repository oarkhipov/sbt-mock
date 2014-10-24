<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:esbhd="http://sbrf.ru/prpc/esb/headers/" xmlns:ns1="http://sbrf.ru/NCP/RefData/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="soap-env:Envelope">
		<xsl:element name="soap-env:Envelope">
			<xsl:copy-of select="soap-env:Header" />
			<soap-env:Body>
				<xsl:choose>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_CURRENCY'">
						<xsl:call-template name="REF_CURRENCY" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_CUSTOMERS_SHARE'">
						<xsl:call-template name="REF_CUSTOMERS_SHARE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_INDUSTRY'">
						<xsl:call-template name="REF_INDUSTRY" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_INSTRUMENT'">
						<xsl:call-template name="REF_INSTRUMENT" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_INTEREST_RATE_TYPE'">
						<xsl:call-template name="REF_INTEREST_RATE_TYPE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_LEGAL_ENTITY_TYPE'">
						<xsl:call-template name="REF_LEGAL_ENTITY_TYPE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_LIMIT_TYPE'">
						<xsl:call-template name="REF_LIMIT_TYPE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_LOAN_PURPOSE'">
						<xsl:call-template name="REF_LOAN_PURPOSE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_MARKET_TREND'">
						<xsl:call-template name="REF_MARKET_TREND" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_PROBLEM_CATEGORY'">
						<xsl:call-template name="REF_PROBLEM_CATEGORY" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_PROBLEM_CRITERIA'">
						<xsl:call-template name="REF_PROBLEM_CRITERIA" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_PRODUCT'">
						<xsl:call-template name="REF_PRODUCT" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_PRODUCT_CATEGORY'">
						<xsl:call-template name="REF_PRODUCT_CATEGORY" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_PRODUCT_LIMIT_TYPE'">
						<xsl:call-template name="REF_PRODUCT_LIMIT_TYPE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_RATING'">
						<xsl:call-template name="REF_RATING" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_REPAYMENT_MODEL'">
						<xsl:call-template name="REF_REPAYMENT_MODEL" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_SEGMENT'">
						<xsl:call-template name="REF_SEGMENT" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_STRESS_ANALYSIS'">
						<xsl:call-template name="REF_STRESS_ANALYSIS" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_SUPPLIERS_SHARE'">
						<xsl:call-template name="REF_SUPPLIERS_SHARE" />
					</xsl:when>
					<xsl:when test="/soap-env:Envelope/soap-env:Body/ns1:requestDictionary/referenceId='REF_DISBURSEMENT_MODEL'">
						<xsl:call-template name="REF_DISBURSEMENT_MODEL" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="Error"/>
					</xsl:otherwise>
				</xsl:choose>
			</soap-env:Body>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="Error">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>0</id>
				<description>Ошибка в запросе. Не удалось найти справочник с указанным referenceId</description>
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_CURRENCY">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_CURRENCY_0</id>
				<description>KZT</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CURRENCY_1</id>
				<description>RUR</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CURRENCY_2</id>
				<description>USD</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CURRENCY_3</id>
				<description>EUR</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CURRENCY_4</id>
				<description>BYR</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_CUSTOMERS_SHARE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_0</id>
				<description>Высокий (доля одного потребителя более 50%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_1</id>
				<description>Значительный (доля одного потребителя от 30% до 50%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_2</id>
				<description>Средний (доля одного потребителя от 10% до 30%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_3</id>
				<description>Низкий (доля одного потребителя менее 10%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_4</id>
				<description>Зависимость потребителя не влияет на стабильность в
					данной отрасли</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_CUSTOMERS_SHARE_5</id>
				<description>Нет информации</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_INDUSTRY">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_INDUSTRY_0</id>
				<description>Пищевая промышленность и производство табачных изделий
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_1</id>
				<description>Химическая промышленность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_2</id>
				<description>Энергетика и водоснабжение</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_3</id>
				<description>Металлургическая промышленность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_4</id>
				<description>Машиностроение</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_5</id>
				<description>Лесная, деревообрабатывающая и целлюлозно-бумажная
					промышленность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_6</id>
				<description>Угольная промышленность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_7</id>
				<description>Нефтегазовая промышленность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_8</id>
				<description>Сельское хозяйство</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_9</id>
				<description>Строительство и операции с недвижимым имуществом
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_10</id>
				<description>Транспорт</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_11</id>
				<description>Связь и телекоммуникации</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_12</id>
				<description>Торговля (Кроме специализированной оптовой)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_13</id>
				<description>Услуги (кроме специализированных услуг, указанных в
					других разделах классификатора)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_14</id>
				<description>Здравоохранение, образование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_15</id>
				<description>Деятельность в области лизинга</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_16</id>
				<description>Финансовая деятельность</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_17</id>
				<description>Страховая компания</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_18</id>
				<description>Государственное управление и обеспечение гражданской
					безопасности; обязательное социальное обеспечение</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INDUSTRY_19</id>
				<description>Прочие виды экономической деятельности.</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_INSTRUMENT">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_INSTRUMENT_0</id>
				<description>Не возобновляемая кредитная линия</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INSTRUMENT_1</id>
				<description>Единовременная выдача займа</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INSTRUMENT_2</id>
				<description>Возобновляемая кредитная линия</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_INTEREST_RATE_TYPE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_INTEREST_RATE_TYPE_0</id>
				<description>Фиксированная</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INTEREST_RATE_TYPE_1</id>
				<description>Переменная</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INTEREST_RATE_TYPE_2</id>
				<description>Плавающая + фиксирован. Маржа</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INTEREST_RATE_TYPE_3</id>
				<description>Плавающая + перемен. Маржа</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_INTEREST_RATE_TYPE_4</id>
				<description>Комбинированная</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_LEGAL_ENTITY_TYPE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_0</id>
				<description>ОАО</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_1</id>
				<description>ЗАО</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_2</id>
				<description>ООО</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_3</id>
				<description>ИП</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_4</id>
				<description>ФЛ</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_5</id>
				<description>КФХ</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LEGAL_ENTITY_TYPE_6</id>
				<description>СПК</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_LIMIT_TYPE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_LIMIT_TYPE_0</id>
				<description>Унифицированный</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LIMIT_TYPE_1</id>
				<description>Унифицированный, переданный на использование по старой
					системе</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LIMIT_TYPE_2</id>
				<description>Структурированный</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_LOAN_PURPOSE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_0</id>
				<description>Овердрафт</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_1</id>
				<description>Корпоративное кредитование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_2</id>
				<description>Инвестиционное кредитование (кроме строительства)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_3</id>
				<description>Инвестиционное кредитование строительства объектов
					жилой недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_4</id>
				<description>Инвестиционное кредитование строительства объектов
					коммерческой недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_5</id>
				<description>Непокрытые аккредитивы (с постфинансированием или с
					обеспечением денежными средствами менее 100%)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_6</id>
				<description>Факторинг</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_7</id>
				<description>Проектное финансирование (кроме строительства)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_8</id>
				<description>Непокрытые аккредитивы (с постфинансированием или с
					обеспечением денежными средствами менее 100%)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_9</id>
				<description>Проектное финансирование строительства объектов жилой
					недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_10</id>
				<description>Проектное финансирование строительства объектов
					коммерческой недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_11</id>
				<description>Контрактное кредитование предприятий</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_12</id>
				<description>Гарантия исполнения обязательств по договору исполнения
					контракта</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_13</id>
				<description>Тендерная гарантия</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_LOAN_PURPOSE_14</id>
				<description> Финансирование лизинговых сделок</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_MARKET_TREND">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_MARKET_TREND_0</id>
				<description>Рост</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_MARKET_TREND_1</id>
				<description>Стагнация</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_MARKET_TREND_2</id>
				<description>Падение</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_MARKET_TREND_3</id>
				<description>Волатильность</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_PROBLEM_CATEGORY">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_0</id>
				<description>ухудшение рейтинга заемщика на 3 уровня, при этом
					рейтинг заемщика после снижения определен на уровне 17 и хуже, но
					не превышает уровень 24</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_1</id>
				<description>ухудшение рейтинга заемщика до уровня 22 – 24
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_2</id>
				<description>наличие просроченной задолженности по обязательствам
					перед Банком непрерывной длительностью от 5 до 30 календарных дней
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_3</id>
				<description>снижение выручки и/или EBITDA заемщика, в течение 2
					последних квартальных отчетных дат от запланированного и
					утвержденного  Банком значения, в соответствии с прогнозом движения
					денежных средств, утвержденным Банком при принятии решения
					Коллегиальным органом о заключении наиболее поздней из действующих
					сделок,  более чем на 20%, но не более чем на 50%</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_4</id>
				<description>возникновение у участника кредитной сделки просроченной
					дебиторской задолженности сроком более 3 месяцев в размере не менее
					25  процентов от его совокупной дебиторской задолженности
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_5</id>
				<description>снижение собственного капитала заемщика до
					отрицательной величины и сохранение данного отрицательного значения
					собственного капитала  на протяжении 3 последних квартальных
					отчетных дат</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_6</id>
				<description>возрастание (по сравнению с уровнем на дату образования
					задолженности) рисков, связанных с регулированием деятельности
					заемщика, включая отмену существующих льгот, изменение режима
					налогообложения и другие подобные факторы, негативно влияющие на
					бизнес участника кредитной сделки</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_7</id>
				<description>изменения в составе собственников и/или руководстве
					участника кредитной сделки, которые могут негативно повлиять на
					бизнес, информация о наличии серьезных разногласий и конфронтации
					между акционерами/участниками и их возможной смене</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_8</id>
				<description>усиление влияния рисков по сравнению с их уровнем на
					момент образования задолженности, связанных с наличием негативной
					информации о деловой репутации участника кредитной сделки, его
					владельцах, руководителях и взаимосвязанных с ними лиц, в том числе
					недобросовестные действия участника кредитной сделки, в частности
					предоставление фиктивных документов, нарушение достигнутых с Банком
					договоренностей, выраженных в устной (отраженной в протоколе
					переговоров) и письменной форме</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_9</id>
				<description>обращение заемщика с заявлением о реструктуризации
					задолженности, связанное с отсутствием возможности или намерения
					заемщика исполнить обязательства на первоначальных условиях
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_10</id>
				<description>по результатам проверки консолидированной группы
					выявлено отрицательное влияние члена консолидированной группы,
					относящегося к желтой зоне, обосновывающее отнесение заемщика к
					желтой зоне</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_11</id>
				<description>наличие предъявленных к участнику кредитной сделки
					исков третьих лиц, которые могут оказать негативное влияние на
					возврат задолженности</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_12</id>
				<description>наличие просроченной задолженности Участника кредитной
					сделки перед другими кредиторами непрерывной длительностью от 15 до
					90 календарных дней в сумме, превышающей 50 процентов его
					собственного капитала</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_13</id>
				<description>снижение совокупной стоимости залогового обеспечения,
					отнесенного к категориям "основное" и "комфортное обеспечение" (в
					том числе в результате его повреждения, утраты и т.п.) в размере
					более чем на 20%, но не более чем на 30% от рыночной стоимости
					данного  обеспечения, определенной последним заключением залоговой
					службы</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_14</id>
				<description>нарушение условий кредитной документации/ковенант
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_15</id>
				<description>ухудшение рейтинга заемщика до уровня 25 или 26
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_16</id>
				<description>наличие просроченной задолженности по обязательствам
					перед Банком непрерывной длительностью от 31 до 90 календарных дней
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_17</id>
				<description>наличие просроченной задолженности участника кредитной
					сделки перед другими кредиторами непрерывной длительностью свыше 90
					календарных дней в сумме, превышающей 50% его собственного капитала
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_18</id>
				<description>снижение выручки и/или EBITDA заемщика, в течение 2
					(двух) последних квартальных отчетных дат от запланированного и
					утвержденного  Банком значения, в соответствии с прогнозом движения
					денежных средств, утвержденным Банком при принятии решения
					Коллегиальным органом о заключении наиболее поздней из действующих
					сделок,  более чем на 50%</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_19</id>
				<description>отсутствие  у заемщика действующей лицензии на право
					осуществления деятельности, поступления от которой генерировали
					свыше 30 % денежных потоков заемщика на протяжении последних 6
					месяцев, если данный вид деятельности подлежит лицензированию
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_20</id>
				<description>принятие судом к рассмотрению иска о признании
					недействительности сделки, в рамках исполнения которой на заемщика
					возложена обязанность по погашению задолженности, или отдельных
					условий данной сделки, снижающих риски Банка</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_21</id>
				<description>снижение совокупной стоимости залогового обеспечения,
					отнесенного к категориям "основное" и "комфортное обеспечение" (в
					том числе в результате его повреждения, утраты и т.п.) в размере
					более чем на 30% от рыночной стоимости данного  обеспечения,
					определенной последним заключением залоговой службы</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_22</id>
				<description>по результатам проверки консолидированной группы
					выявлено отрицательное влияние члена консолидированной группы,
					относящегося к красной зоне, обосновывающее отнесение заемщика к
					красной зоне</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_23</id>
				<description>фактическое прекращение участником кредитной сделки,
					кроме заемщика, финансово-хозяйственной деятельности</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_24</id>
				<description>повторное обращение заемщика с заявлением о
					реструктуризации задолженности, связанное с отсутствием возможности
					или намерения заемщика исполнить обязательства на первоначальных
					условиях, если в составе задолженности есть обязательства, которые
					ранее уже были реструктурированы</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_25</id>
				<description>иные факторы, которые лидер процесса и/или андеррайтер
					считает достаточным основанием для отнесения задолженности к
					красной зоне</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_26</id>
				<description>наличие просроченной задолженности по обязательствам
					перед Банком непрерывной длительностью свыше 90 календарных дней
					(дефолт)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_27</id>
				<description>отказ заемщика от погашения задолженности, выраженный в
					письменной или устной форме</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_28</id>
				<description>принятие участником кредитной сделки решения о
					ликвидации</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_29</id>
				<description>наличие производства по делу о несостоятельности
					(банкротстве) участника кредитной сделки</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_30</id>
				<description>фактическое прекращение заемщиком
					финансово-хозяйственной деятельности</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_31</id>
				<description>принятие кредитующим подразделением и ПРПА совместного
					решения о выборе дефолтной стратегии работы с ПА</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_32</id>
				<description>инициирование Банком процедуры принудительного
					взыскания всей суммы/части задолженности заемщика в судебном
					порядке/обращения взыскания на заложенное имущество</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_33</id>
				<description>принятие коллегиальным органом Банка решения обратить
					взыскание на заложенное имущество во внесудебном порядке
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_34</id>
				<description>инициирование Банком возбуждения уголовных дел в
					отношении участника кредитной сделки/его руководителей и/или
					владельцев</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_35</id>
				<description>введение временной финансовой администрации
					(конкурсный, реабилитационный управляющий и т.п.) в отношении
					заемщика</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_36</id>
				<description>по результатам проверки ГСЗ выявлено отрицательное
					влияние члена ГСЗ, относящегося к черной зоне, обосновывающее
					отнесение заемщика к черной зоне</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_37</id>
				<description>иные факторы, которые лидер процесса и/или андеррайтер
					считает достаточным основанием для отнесения задолженности к черной
					зоне</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CATEGORY_38</id>
				<description>отсутствие любых источников погашения</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_PROBLEM_CRITERIA">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_PROBLEM_CRITERIA_0</id>
				<description>Признаки проблемности не идентифицированы</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CRITERIA_1</id>
				<description>Потенциально проблемный</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CRITERIA_2</id>
				<description>Проблемный</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PROBLEM_CRITERIA_3</id>
				<description>Безнадежный</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_PRODUCT">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_PRODUCT_0</id>
				<description>Кредит на пополнение оборотных активов (финансирование
					производственной, торговой, снабженческой, иной деятельности)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_1</id>
				<description>Кредит на выплату заработной платы, включая суммы
					обязательных страховых взносов и подоходного налога с физических
					лиц, взносов в Фонд социальной защиты населения Министерства труда
					и социальной защиты населения Министерства труда и социальной
					защиты, и  иные взносы, исчисляемые с суммы заработной платы
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_2</id>
				<description>Кредит на уплату налогов, сборов, пошлин и  иных
					обязательных платежей, взносов и т.п.</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_3</id>
				<description>Кредит на финансирование затрат по капитальному
					(текущему) ремонту</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_4</id>
				<description>Кредит на иные расходы, необходимые для осуществления
					текущей деятельности</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_5</id>
				<description>Кредит на приобретение  отдельных единиц движимого и
					недвижимого имущества и нематериальных активов (включая косвенные
					затраты) вне реализации клиентом проектов технического
					перевооружения, модернизации действующего/создания нового
					производства с общим принципом классификации кредита по типу
					финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_6</id>
				<description>Кредиты на реконструкцию/модернизацию зданий,
					сооружений, инженерных и транспортных коммуникаций в соответствии с
					общим принципом классификации кредита по типу финансирования.
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_7</id>
				<description>Кредиты на приобретение движимого и недвижимого
					имущества и нематериальных активов (включая косвенные затраты) в
					рамках реализации клиентом проектов технического перевооружения,
					модернизации действующего/создания нового производства в
					соответствии с общим принципом классификации кредита по типу
					финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_8</id>
				<description>Операции прямого финансового лизинга в соответствии с
					общим принципом классификации кредита по типу финансирования
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_9</id>
				<description>Финансирование под уступку денежных требований
					(факторинг)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_10</id>
				<description>Финансирование в рамках специализированных программ
					ЕАБР и ЕБРР с общим принципом классификации продукта по типу
					финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_11</id>
				<description>Финансирование с привлечением ресурсов иностранных
					банков с общим принципом классификации продукта по типу
					финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_12</id>
				<description>Контрактное кредитование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_13</id>
				<description>Кредитование под заклад денег</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_14</id>
				<description>Кредитование внешнеторговых контрактов</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_15</id>
				<description>Финансирование строительства объектов жилой
					недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_16</id>
				<description>Финансирование строительства объектов коммерческой
					недвижимости</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_17</id>
				<description>Проектное финансирование (кроме строительства)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_18</id>
				<description>Кредитование на приобретение лизингодателем
					(лизинговыми компаниями) предметов лизинга</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_19</id>
				<description>Контрактное кредитование предприятий</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_20</id>
				<description>Корпоративное кредитование на инвестиционные цели
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_21</id>
				<description>Корпоративное кредитование на пополнение оборотных
					средств</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_22</id>
				<description>Овердрафт</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_23</id>
				<description>Факторинг</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_24</id>
				<description>Гарантия исполнения обязательств по договору исполнения
					контракта</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_25</id>
				<description>Аккредитивы без перевода покрытия</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_26</id>
				<description>Гарантия/ контргарантия исполнения обязательств по
					договору/ контракту с общим принципом распределения продукта по
					типу финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_27</id>
				<description>Гарантия/ контргарантия возврата авансового платежа с
					общим принципом распределения продукта по типу финансирования
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_28</id>
				<description>Гарантия обеспечения кредита с общим принципом
					распределения продукта по типу финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_29</id>
				<description>Тендерные гарантии</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_30</id>
				<description>Гарантия/контргарантия платежа с общим принципом
					распределения продукта по типу финансирования</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_31</id>
				<description>Гарантии в пользу таможенных органов</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_32</id>
				<description>Гарантии в пользу БАМАП</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_33</id>
				<description>Непокрытые аккредитивы (с постфинансированием или с
					обеспечением денежными средствами менее 100%)</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_PRODUCT_CATEGORY">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_PRODUCT_CATEGORY_0</id>
				<description>Унифицированный</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_CATEGORY_1</id>
				<description>Унифицированный, в рамках лимита, переданного на
					использование по старой системе</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_CATEGORY_2</id>
				<description>Структурированный</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_PRODUCT_LIMIT_TYPE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_PRODUCT_LIMIT_TYPE_0</id>
				<description>Овердрафт</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_LIMIT_TYPE_1</id>
				<description>Корпоративное кредитование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_LIMIT_TYPE_2</id>
				<description>Проектное финансирование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_LIMIT_TYPE_3</id>
				<description>Контрактное кредитование</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_PRODUCT_LIMIT_TYPE_4</id>
				<description>Гарантии</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_RATING">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_RATING_0</id>
				<description>Не рассчитан</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_1</id>
				<description>1</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_2</id>
				<description>2</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_3</id>
				<description>3</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_4</id>
				<description>4</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_5</id>
				<description>5</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_6</id>
				<description>6</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_7</id>
				<description>7</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_8</id>
				<description>8</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_9</id>
				<description>9</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_10</id>
				<description>10</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_11</id>
				<description>11</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_12</id>
				<description>12</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_13</id>
				<description>13</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_14</id>
				<description>14</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_15</id>
				<description>15</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_16</id>
				<description>16</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_17</id>
				<description>17</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_18</id>
				<description>18</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_19</id>
				<description>19</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_20</id>
				<description>20</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_21</id>
				<description>21</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_22</id>
				<description>22</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_23</id>
				<description>23</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_24</id>
				<description>24</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_25</id>
				<description>25</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_RATING_26</id>
				<description>26</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_REPAYMENT_MODEL">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_REPAYMENT_MODEL_0</id>
				<description>Одним платежом</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_REPAYMENT_MODEL_1</id>
				<description>Ежемесячно</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_REPAYMENT_MODEL_2</id>
				<description>Ежеквартально</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_REPAYMENT_MODEL_3</id>
				<description>Иное</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_SEGMENT">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_SEGMENT_0</id>
				<description>Крупный и крупнейший бизнес</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SEGMENT_1</id>
				<description>Средний бизнес</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_STRESS_ANALYSIS">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_STRESS_ANALYSIS_0</id>
				<description>Финансовый долг / EBITDA</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_STRESS_ANALYSIS_1</id>
				<description>Чистый Долг / EBITDA</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_STRESS_ANALYSIS_2</id>
				<description>Скорректированный финансовый долг/EBITDA</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_STRESS_ANALYSIS_3</id>
				<description>Скорректированный чистый долг / EBITDA</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

	<xsl:template name="REF_SUPPLIERS_SHARE">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_0</id>
				<description>Высокий (доля одного поставщика более 50%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_1</id>
				<description>Значительный (доля одного поставщика от 30% до 50%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_2</id>
				<description>Средний (доля одного поставщика от 10% до 30%)
				</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_3</id>
				<description>Низкий (доля одного поставщика менее 10%)</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_4</id>
				<description>Зависимость поставщика не влияет на стабильность в
					данной отрасли</description>
				<parentId />
			</listOfReferenceData>
			<listOfReferenceData>
				<id>REF_SUPPLIERS_SHARE_5</id>
				<description>Нет информации</description>
				<parentId />
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>
	
	<xsl:template name="REF_DISBURSEMENT_MODEL">
		<ref:responseDictionary xmlns:ref="http://sbrf.ru/NCP/RefData/">
			<listOfReferenceData>
				<id>PERIOD</id>
				<description>Период</description>
				<parentId/>
			</listOfReferenceData>
			<listOfReferenceData>
				<id>SCHEDULE</id>
				<description>Дата/Сумма</description>
				<parentId/>
			</listOfReferenceData>
			<listOfReferenceData>
				<id>NOT_AVAILABLE</id>
				<description>Не устанавливается</description>
				<parentId/>
			</listOfReferenceData>
		</ref:responseDictionary>
	</xsl:template>

</xsl:stylesheet>