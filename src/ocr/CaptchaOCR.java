/*
 * Created on May 5, 2013 8:48:29 PM By Phoenix Xu Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package ocr;

import static utils.FileUtils.ROOT_PATH;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

/**
 * @author Phoenix Xu
 */
public class CaptchaOCR {

    public static final String  CAPTCHA_IMAGE_PATH      = ROOT_PATH + "captcha.jpg";

    private static final String CAPTCHA_OCR_RESULT_PATH = ROOT_PATH + "result";

    private static String getOCRCommand(Long timestamp) {
        return ROOT_PATH + "tesseract/tesseract.exe " + CAPTCHA_IMAGE_PATH + " " + CAPTCHA_OCR_RESULT_PATH + timestamp + " digit";
    }

    private static String getResultFile(Long timestamp) {
        return CAPTCHA_OCR_RESULT_PATH + timestamp + ".txt";
    }

    public static final String doOcr() throws IOException {
        Long timestamp = System.currentTimeMillis();
        Runtime.getRuntime().exec(getOCRCommand(timestamp));
        File captchaResult = new File(getResultFile(timestamp));
        String captchaCode = null;
        while (true) {
            if (captchaResult.canRead()) {
                LineNumberReader lineReader = new LineNumberReader(new FileReader(captchaResult));
                captchaCode = lineReader.readLine();
                lineReader.close();
                captchaResult.delete();
                break;
            } else {
                Long now = System.currentTimeMillis();
                if (now - timestamp > TimeUnit.SECONDS.toMillis(60)) {
                    break;
                }
            }
        }
        return captchaCode;
    }
}
