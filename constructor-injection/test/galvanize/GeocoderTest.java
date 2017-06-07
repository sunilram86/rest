package galvanize;

import com.galvanize.Address;
import com.galvanize.GoogleGeocoder;
import static galvanize.Helpers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.galvanize.LatLng;
import com.galvanize.MapQuestGeocoder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GeocoderTest {

    @Test
    public void theGeocoderInterfaceExists() {
        try {
            Class clazz = Class.forName("com.galvanize.Geocoder");
            Assert.assertTrue(
                    "Expected there to be an Interface named Geocoder",
                    clazz.isInterface()
            );
        } catch (ClassNotFoundException e) {
            Assert.fail("You haven't implemented the Geocoder Interface yet");
        }
    }

    @Test
    public void googleGeocoderShouldImplementTheGeocoderInterface() {
        try {
            Class geocoderInterface = Class.forName("com.galvanize.Geocoder");
            Assert.assertTrue(
                    "Expected the GoogleGeocoder Class to implement the Geocoder Interface",
                    classImplementsInterface(GoogleGeocoder.class, geocoderInterface)
            );
        } catch (ClassNotFoundException e) {
            Assert.fail("You haven't implemented the Geocoder Interface yet");
        }
    }

    @Test
    public void mapQuestGeocoderShouldImplementTheGeocoderInterface() {
        try {
            Class geocoderInterface = Class.forName("com.galvanize.Geocoder");
            Assert.assertTrue(
                    "Expected the MapQuest Class to implement the Geocoder Interface",
                    classImplementsInterface(MapQuestGeocoder.class, geocoderInterface)
            );
        } catch (ClassNotFoundException e) {
            Assert.fail("You haven't implemented the Geocoder Interface yet");
        }
    }

    @Test
    public void addressInjectsGeocoder() {
        try {
            Class geocoderInterface = Class.forName("com.galvanize.Geocoder");
            Assert.assertTrue(
                    "Expected Address to inject a Geocoder",
                    classInjectsDependency(Address.class, geocoderInterface)
            );
        } catch (ClassNotFoundException e) {
            Assert.fail("You haven't implemented the Geocoder Interface yet");
        }
    }

    @Test
    public void addressUsesThePassedInGeocoder() {
        try {
            Class geocoderInterface = Class.forName("com.galvanize.Geocoder");
            Constructor addressConstructor = getConstructorForInterface(Address.class, geocoderInterface);
            Object mockedGeocoder = mock(geocoderInterface);
            Method m = geocoderInterface.getDeclaredMethod("geocode");
            LatLng latLng = new LatLng(1.0, 2.0);
            when(m.invoke(mockedGeocoder)).thenReturn(latLng);
            Address address = null;
            if (addressConstructor != null) {
                address = (Address) addressConstructor.newInstance(mockedGeocoder);
            } else {
                Assert.fail("You haven't injected the Geocoder correctly yet");
            }
            Assert.assertEquals(latLng, address.getLatLng());
        } catch (ClassNotFoundException e) {
            Assert.fail("You haven't implemented the Geocoder Interface yet");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
