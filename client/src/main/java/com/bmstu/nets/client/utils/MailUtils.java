package com.bmstu.nets.client.utils;

import com.bmstu.nets.common.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;

public class MailUtils {
    private static final Logger logger = getLogger(MailUtils.class);

    @Nullable
    public static String getMxRecord(@Nonnull String mailAddress) {
        final String domainName = getDomainName(mailAddress);
        return getMxRecords(domainName)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Nonnull
    public static String getDomainName(@Nonnull String mailAddress) {
        return mailAddress.split("@")[1];
    }

    @Nonnull
    public static List<String> getMxRecords(@Nonnull String domainName) {
        try {
            final InitialDirContext iDirC = new InitialDirContext();

            final Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[]{"MX"});
            final Attribute attributeMX = attributes.get("MX");

            if (attributeMX == null) {
                return (singletonList(domainName));
            }

            final String[][] preferenceValuesHostNames = new String[attributeMX.size()][2];
            for (int i = 0; i < attributeMX.size(); i++) {
                preferenceValuesHostNames[i] = ("" + attributeMX.get(i)).split("\\s+");
            }

            Arrays.sort(preferenceValuesHostNames, Comparator.comparingInt(o -> Integer.parseInt(o[0])));

            final String[] sortedHostNames = new String[preferenceValuesHostNames.length];
            for (int i = 0; i < preferenceValuesHostNames.length; i++) {
                sortedHostNames[i] = preferenceValuesHostNames[i][1].endsWith(".")
                        ? preferenceValuesHostNames[i][1].substring(0, preferenceValuesHostNames[i][1].length() - 1)
                        : preferenceValuesHostNames[i][1];
            }

            return Arrays.asList(sortedHostNames);

        } catch (NamingException e) {
            logger.error(e.getMessage());
            return newArrayList();
        }
    }
}
