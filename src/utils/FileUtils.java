/*
 * Created on May 6, 2013 10:49:56 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package utils;

import java.io.File;

/**
 * @author Phoenix Xu
 */
public final class FileUtils {

    public static final String ROOT_PATH                    = FileUtils.class.getResource("/").getPath().substring(1);

    public static final String TEMP_EXCEL_PATH              = ROOT_PATH + "temp" + File.separator;

    public static final String EXCEL_REPORT_PATH_NO_EXT     = ROOT_PATH + "report" + File.separator + "PowerReport";

    public static final String EXCEL_2003_FILE_AFFIX        = ".xls";

    public static final String EXCEL_2007_FILE_AFFIX        = ".xlsx";

    public static final String FILE_NAME_INTERNAL_SEPARATOR = "_";
}
