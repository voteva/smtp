package com.bmstu.nets.client.utils;

import org.junit.Test;

import java.util.List;

import static com.bmstu.nets.client.utils.MailUtils.getDomainName;
import static com.bmstu.nets.client.utils.MailUtils.getMxRecords;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MailUtilsTest {

    @Test
    public void testGetDomainName() {
        String domainName = getDomainName("alex.smith@gmail.com");

        assertNotNull(domainName);
        assertEquals("gmail.com", domainName);
    }

    @Test
    public void testGetDomainNameLocalhost() {
        String domainName = getDomainName("john.brown@localhost");

        assertNotNull(domainName);
        assertEquals("localhost", domainName);
    }

    @Test
    public void testGetMxRecords() {
        List<String> mailHosts = getMxRecords("yandex.ru");

        assertNotNull(mailHosts);
        assertTrue(mailHosts.size() >= 1);
        assertEquals("mx.yandex.ru", mailHosts.get(0));
    }

    @Test
    public void testGetMxRecordsLocalhost() {
        List<String> mailHosts = getMxRecords("localhost");

        assertNotNull(mailHosts);
        assertEquals(1, mailHosts.size());
        assertEquals("localhost", mailHosts.get(0));
    }
}