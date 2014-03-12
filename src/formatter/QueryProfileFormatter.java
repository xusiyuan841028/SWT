/*
 * querquer * quer * Created on May 14, 2013 11:45:19 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.QueryProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Phoenix Xu
 */
public class QueryProfileFormatter {

    private static final Logger logger              = LoggerFactory.getLogger(QueryProfileFormatter.class);

    public static final String  COMMENT_CHAR        = "#";

    public static final String  PARAMETER_DELIMITER = ",";

    public String getQueryProfileRecord(QueryProfile query) {
        return query.account.accountNum + PARAMETER_DELIMITER + query.account.password + PARAMETER_DELIMITER + query.account.city + PARAMETER_DELIMITER
                + query.parameter.year + PARAMETER_DELIMITER + query.parameter.interval;
    }

    public QueryProfile getQueryProfileFromRecord(String queryRecord) {
        if (queryRecord == null || !StringUtils.hasText(queryRecord)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Null or empty string.");
            }
            return null;
        }

        Scanner scanner = new Scanner(queryRecord);
        scanner.useDelimiter(",");

        QueryProfile query = null;
        String firstField = scanner.next();
        logger.debug("Profile Record First field:  " + firstField);
        if (!(firstField == null || firstField.isEmpty() || firstField.startsWith(COMMENT_CHAR))) {
            query = new QueryProfile(firstField, scanner.next(), scanner.next(), scanner.nextInt(), scanner.nextInt());
        }
        scanner.close();
        return query;
    }

    public List<QueryProfile> getQueryProfilesFromRecord(String content) {
        List<QueryProfile> profiles = new ArrayList<QueryProfile>();

        Scanner scanner = new Scanner(content);
        String line = null;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            logger.debug("Profile Record Line:  " + line);
            if (StringUtils.hasText(line)) {
                QueryProfile query = this.getQueryProfileFromRecord(line);
                if (query != null) {
                    profiles.add(query);
                }
            }
        }
        scanner.close();
        return profiles;
    }
}
