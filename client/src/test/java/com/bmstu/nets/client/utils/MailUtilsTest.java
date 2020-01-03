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
        String domainName = getDomainName("john.smith@gmail.com");

        assertNotNull(domainName);
        assertEquals("gmail.com", domainName);
    }

    @Test
    public void testGetMxRecords() {
        List<String> mailHosts = getMxRecords("google.com");

        assertNotNull(mailHosts);
        assertTrue(mailHosts.size() > 1);
    }
}