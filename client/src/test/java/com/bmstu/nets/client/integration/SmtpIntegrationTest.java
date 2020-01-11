package com.bmstu.nets.client.integration;

import com.bmstu.nets.client.StartupService;
import com.bmstu.nets.client.helper.MailAuthenticator;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SmtpIntegrationTest {

    private static final String IMAP_PORT = "993";

    private static final String IMAP_AUTH_EMAIL = "iu7.test@yandex.ru";
    private static final String IMAP_AUTH_PWD = "aXU3LnRlc3RwYXNzd29yZA==";

    @Test
    public void sendMessage() {
        checkMessageSending();
    }

    private void checkMessageSending() {
        final List<Message> messagesListBeforeSending = getMessagesViaImap(IMAP_AUTH_EMAIL, IMAP_AUTH_PWD);
        assertNotNull(messagesListBeforeSending);

        try {
            StartupService client = new StartupService();
            client.start();
            Thread.sleep(10000);
            client.stop();

            List<Message> messagesListAfterSending = getMessagesViaImap(IMAP_AUTH_EMAIL, IMAP_AUTH_PWD);
            assertNotNull(messagesListAfterSending);
            assertTrue(messagesListAfterSending.size() > messagesListBeforeSending.size());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Message> getMessagesViaImap(@Nonnull String login, @Nonnull String secret) {
        final String password = new String(Base64.getDecoder().decode(secret));
        final String host = "imap." + login.split("@")[1];

        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", IMAP_PORT);

        Authenticator auth = new MailAuthenticator(login, password);
        Session session = Session.getDefaultInstance(properties, auth);
        session.setDebug(false);

        try {
            Store store = session.getStore();
            store.connect(host, login, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            return Arrays.asList(emailFolder.getMessages());
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
