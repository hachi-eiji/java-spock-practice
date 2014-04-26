package com.hachiyae.spock

import org.joda.time.DateTimeUtils
import spock.lang.Specification
import spock.lang.Unroll

class SampleUtilSpec extends Specification {
    def "GetDate_1個の場合"() {
        setup: "日付のモックを設定する"
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2010, 0, 1);
        DateTimeUtils.setCurrentMillisFixed(calendar.getTimeInMillis())
        when: "現在の日付を返却する"
        Date currentDate = SampleUtil.getCurrentDate();
        then:
        currentDate.month == 0
        cleanup:
        DateTimeUtils.setCurrentMillisSystem()
    }

    @Unroll
    def "GetDate_オブジェクト型のチェック#yearと#monthで#dateは#currentDate"() {
        // givenはsetupのエイリアス
        given:
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, date);
        DateTimeUtils.setCurrentMillisFixed(calendar.getTimeInMillis())
        expect:
        currentDate == SampleUtil.getCurrentDate();
        cleanup:
        DateTimeUtils.setCurrentMillisSystem()
        where:
        year | month | date || currentDate
        2010 | 0     | 1    || new Date(2010 - 1900, 0, 1, 0, 0, 0)
    }

    @Unroll
    def "increaseの計算#i+#j=#expectになる"() {
        expect:
        expect == SampleUtil.increase(i, j);

        where:
        i | j  || expect
        1 | 2  || 3
        1 | -1 || 0
    }
}
