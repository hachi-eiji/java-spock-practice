package com.hachiyae.spock

import spock.lang.Specification
import spock.lang.Unroll


class SampleUtilSpec extends Specification {
    def "GetDate"() {

    }

    @Unroll
    def "increaseの計算 #i + #j = #expectになる"() {
        expect:
        expect == SampleUtil.increase(i, j);

        where:
        i | j  || expect
        1 | 2  || 3
        1 | -1 || 0
    }
}
