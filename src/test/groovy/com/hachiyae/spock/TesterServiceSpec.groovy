package com.hachiyae.spock

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(locations = "classpath:spring-test.xml")
class TesterServiceSpec extends Specification {
    TesterService testerService;

    def setup() {
        testerService = new TesterService();
    }

    @Unroll
    def "SayHello#nameのときは#expect"() {
        expect:
        expect == testerService.sayHello(name)
        where:
        name  || expect
        "foo" || "Hello foo"
        ""    || "Hello "
    }

    @Unroll
    def "sayCurrentDate"() {
        given: "Mockの宣言がかけます.日付を2010/01/02に指定"
        def currentDate = Mock(CurrentDateFactory)
        currentDate.getCurrentDate() >> new DateTime(2010, 1, 2, 0, 0).toDate()
        TesterService.metaClass.setAttribute(testerService, "currentDateFactory", currentDate)
        when:
        def expect = testerService.sayCurrentDate()
        then:
        expect == "current date is 2010/01/02"
    }

    @Unroll
    def "testSayCurrentTime"() {
        given:
        _setCurrentTime(new DateTime(2010, 1, 2, 1, 2, 3))
        when:
        def expect = testerService.sayCurrentTime()
        then:
        expect == "current time is 01:02:03"
        cleanup:
        _resetCurrentTime()
    }

    @Unroll
    def "executeのテストdaoをmock化したい"() {
        given:
        def factory = Mock(DaoFactory)
        factory.execute("foo") >> "BAR"
        Dao.metaClass.setAttribute(testerService, "factory", factory)
        when:
        def expect = testerService.convert("foo")
        then:
        expect == "test_BAR"
    }

    @Unroll
    def "executeのテスト#srcを#dstに変更した結果は#expect"() {
        given:
        def factory = Mock(DaoFactory)
        factory.execute(src) >> dst
        Dao.metaClass.setAttribute(testerService, "factory", factory)
        expect:
        expect == testerService.convert(src)
        where:
        src   | dst    || expect
        "foo" | "BAR"  || "test_BAR"
        "foo" | "bar"  || "test_bar"
        "foo" | "Abar" || "test_Abar"
    }

    def _setCurrentTime(DateTime dateTime) {
        DateTimeUtils.setCurrentMillisFixed(dateTime.getMillis())
    }

    def _resetCurrentTime() {
        DateTimeUtils.setCurrentMillisSystem()
    }
}
