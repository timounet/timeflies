package org.timeflies.projects;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;


public class DateTest {
    @Test
    public void parseDate() {
        System.out.println(ZonedDateTime.now());
        String d = "2019-09-05T20:15:55Z";
        ZonedDateTime l = ZonedDateTime.parse(d);
        Assertions.assertEquals(l.getYear(), 2019);

    }
}
