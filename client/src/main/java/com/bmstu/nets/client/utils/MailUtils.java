package com.bmstu.nets.client.utils;

import com.bmstu.nets.common.logger.Logger;

import javax.annotation.Nonnull;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Comparator;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.util.Arrays.asList;
import static java.util.Arrays.sort;
import static java.util.Collections.singletonList;

public class MailUtils {
    private static final Logger logger = getLogger(MailUtils.class);

    private static final String DNS_PREFIX = "dns:/";
    private static final String MX_ATTR = "MX";
    private static final String DEFAULT_HOST = "localhost";

    @Nonnull
    public static String getDomainName(@Nonnull String mailAddress) {
        return mailAddress.split("@")[1];
    }

    @Nonnull
    public static List<String> getMxRecords(@Nonnull String domainName) {
        try {
            logger.debug("Trying to get MX records for domain '{}'", domainName);

            final InitialDirContext iDirC = new InitialDirContext();

            final Attributes attributes = iDirC.getAttributes(DNS_PREFIX + domainName, new String[]{MX_ATTR});
            final Attribute attributeMX = attributes.get(MX_ATTR);

            if (attributeMX == null) {
                return (singletonList(domainName));
            }

            final String[][] preferenceValuesHostNames = new String[attributeMX.size()][2];
            for (int i = 0; i < attributeMX.size(); i++) {
                preferenceValuesHostNames[i] = ("" + attributeMX.get(i)).split("\\s+");
            }

            sort(preferenceValuesHostNames, Comparator.comparingInt(o -> Integer.parseInt(o[0])));

            final String[] sortedHostNames = new String[preferenceValuesHostNames.length];
            for (int i = 0; i < preferenceValuesHostNames.length; i++) {
                sortedHostNames[i] = preferenceValuesHostNames[i][1].endsWith(".")
                        ? preferenceValuesHostNames[i][1].substring(0, preferenceValuesHostNames[i][1].length() - 1)
                        : preferenceValuesHostNames[i][1];
            }

            return asList(sortedHostNames);

        } catch (Exception e) {
            logger.error("Failed to get MX records. {}", e.getMessage());
            return singletonList(DEFAULT_HOST);
        }
    }
}
