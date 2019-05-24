package cn.aysst.www.doctor;

import cn.aysst.www.doctor.utils.Http;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        String str = "0iMC/TC";
        System.out.println(Http.cutStringSmall(str));
    }
}