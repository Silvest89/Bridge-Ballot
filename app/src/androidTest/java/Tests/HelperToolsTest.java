package Tests;

import android.test.InstrumentationTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

import eu.silvenia.bridgeballot.HelperTools;

/**
 * Created by Jesse on 19-6-2015.
 */
public class HelperToolsTest extends InstrumentationTestCase {

    public void testEmailValidatorTrue(){
        boolean validEmail = HelperTools.emailValidator("Henk@gmail.com");
        assertTrue(validEmail);
    }

    public void testEmailValidatorFalse(){
        boolean invalidEmail = HelperTools.emailValidator("heiuwfiew");
        assertFalse(invalidEmail);
    }

    public void testGPSDistance(){
        double distance = HelperTools.calculateGpsDistance(3, 5, 7, 9);
        assertEquals(distance, 314.12);
    }

    public void testGetTimeStamp(){
        String testDate = HelperTools.getCurrentTimeStamp();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss ");
        String formatDate = sdf.format(date);
        assertEquals(testDate, formatDate);
    }

    public void testRandomString1(){
        String random = HelperTools.getRandomString(78);
        assertEquals(random.length(), 78);
    }

    public void testRandomString2(){
        String random = HelperTools.getRandomString(23);
        assertNotNull(random);
    }

}