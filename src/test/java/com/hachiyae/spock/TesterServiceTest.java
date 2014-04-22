package com.hachiyae.spock;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class TesterServiceTest {
    @Tested
    private TesterService tester;

//    // これでもOK
//    @Mocked
//    private CurrentDateFactory currentDateFactory;

    @Test
    public void testSayHello() throws Exception {
        String message = tester.sayHello("user1");
        assertThat(message, is("Hello user1"));
    }

    // 引数でもいいよ
    @Test
    public void testSayCurrentDate(@Injectable final CurrentDateFactory currentDateFactory) throws Exception {
        new Expectations() {
            // ここにつけてもいいよ
//            CurrentDateFactory currentDateFactory;
            {
//                setField(tester, currentDateFactory);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(2014, 0, 1);
                // 直後にresultに値を入れることでモックの戻り値を定義
                // timesは回数(1の時は省略
                currentDateFactory.getCurrentDate();
                result = calendar.getTime();
                times = 1;
            }
        };
        String message = tester.sayCurrentDate();
        assertThat(message, is("current date is 2014/01/01"));
    }

    // 無駄にモックオブジェクトインスタンスを生成しない
    @Mocked
    final SampleUtil sampleUtil = null;
    @Test
    public void testSayCurrentTime() throws Exception {
        // static method
        new Expectations() {
            {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(2014, 0, 1, 1, 2, 3);
                sampleUtil.getCurrentDate();
                result = calendar.getTime();
            }
        };
        String message = tester.sayCurrentTime();
        assertThat(message, is("current time is 01:02:03"));
    }

    @Test
    @SuppressWarnings({"unused", "deprecation"})
    public void testSayCurrentYear() throws Exception {
        new MockUp<System>() {
            @Mock
            public long currentTimeMillis() {
                return new Date(110, 0, 1).getTime();
            }
        };
        String message = tester.sayCurrentYear();
        assertThat(message, is("current year is 2010"));
    }

    @Test
    public void test_executeVoidMethod(@Injectable final CurrentDateFactory currentDateFactory) throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2014, 0, 1);
        new Expectations() {{
            currentDateFactory.add(calendar.getTime(), 1);
        }};

        String s = tester.executeVoidMethod(calendar.getTime(), 1);
        assertThat(s, is("foo -> " + 114));
    }
}
