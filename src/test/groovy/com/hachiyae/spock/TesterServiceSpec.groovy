package com.hachiyae.spock

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

/**
 * テンプレート的に作ってます
 * クラス名のデフォルトは一般規則に則りxxxSpecで.
 */
// springのautowiredをつかうときはいつもどおりContextConfigurationを使う
@ContextConfiguration(locations = "classpath:spring-test.xml")
class TesterServiceSpec extends Specification {
    // springのautowiredもつかえますよ
    @Autowired
    TesterService testerService;

    // setupをここに書くこともできます
    def setup() {
//        testerService = new TesterService();
    }

    // もちろんメソッド名はわかりやすくしてください
    // ダブルコーテーションで囲むことでメソッドの名の最初が数字から開始できます
    // throws 句は必要ないです
    @Unroll
    def "SayHello#nameのときは#expect"() {
        expect:
        expect == testerService.sayHello(name)
        where:
        name  || expect
        "foo" || "Hello foo"
        ""    || "Hello "
    }

    def "sayCurrentDate"() {
        setup: "Mockの宣言がかけます.日付を2010/01/02に指定"
        def currentDate = Mock(CurrentDateFactory)
        currentDate.getCurrentDate() >> new DateTime(2010, 1, 2, 0, 0).toDate()
        TesterService.metaClass.setAttribute(testerService, "currentDateFactory", currentDate)
        when:
        def expect = testerService.sayCurrentDate()
        then:
        expect == "current date is 2010/01/02"
    }

    def "testSayCurrentTime"() {
        // givenはテスト実行前に実施(JUnitの@Before)
        setup:
        // 日付を変更する
        _setCurrentTime(new DateTime(2010, 1, 2, 1, 2, 3))
        // whenでテスト内容を記載
        when:
        def expect = testerService.sayCurrentTime()
        // thenでチェックします
        then:
        expect == "current time is 01:02:03"
        // テスト完了後に実施.(JUnitの@After)
        cleanup:
        // ここで戻さないと他のテストに影響あるので注意
        _resetCurrentTime()
    }

    def "executeのテストdaoをmock化したい"() {
        setup: "DaoFactory factory = Mock();の記法だとIDEサポートが得られやすい"
        DaoFactory factory = Mock();
        factory.execute("foo") >> "BAR"
        Dao.metaClass.setAttribute(testerService, "factory", factory)
        when:
        def expect = testerService.convert("foo")
        then:
        expect == "test_BAR"
    }

    // unrollをつけるとメソッド名にパラメータを置換できる
    @Unroll
    def "executeのテスト#srcを#dstに変更した結果は#expect"() {
        setup:
        DaoFactory factory = Mock {
            execute(src) >> dst
        }
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
