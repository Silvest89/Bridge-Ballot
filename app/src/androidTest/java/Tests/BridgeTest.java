package Tests;

import android.test.InstrumentationTestCase;

import eu.silvenia.bridgeballot.Bridge;

/**
 * Created by Jesse on 19-6-2015.
 */
public class BridgeTest extends InstrumentationTestCase {

    Bridge b = new Bridge(1, "Spijkenisserbrug", "Spijkenisse", 51.000, 150.000, false);

    public void testBridgeConstructor(){
        assertNotNull(b);
    }

    public void testGetId(){
        assertEquals(b.getId(), 1);
    }

    public void testGetName(){
        assertEquals("Spijkenisserbrug", b.getName());
    }

    public void testGetLocation(){
        assertEquals("Spijkenisse", b.getLocation());
    }

    public void testGetLatitude(){
        assertEquals(51.000, b.getLatitude());
    }

    public void testGetLongitude(){
        assertEquals(150.000, b.getLongitude());
    }

    public void testGetDistance(){
        assertEquals(0.0, b.getDistance());
    }

    public void testIsOpen(){
        assertFalse(b.isOpen());
    }

}