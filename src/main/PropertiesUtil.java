package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    /** Singleton instance */
    private static PropertiesUtil _instance;

    private PropertiesUtil() {
    }

    /** Singleton factory method - lazy load */
    public static synchronized PropertiesUtil instance() {
        if (_instance == null)
            _instance = new PropertiesUtil();
        return _instance;
    }

    /**
     * Reads a properties (key=value) file into a String.
     * 
     * @param filename
     * @return string 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String readProperty(String propertyToRead) throws FileNotFoundException, IOException {
		String property = "";
		InputStream inputStream = null;
	
    	try {
   		
			Properties prop = new Properties();
			String propFileName = "properties.properties";
 
			inputStream = new FileInputStream(propFileName);
 
			prop.load(inputStream);
 
			property = prop.getProperty(propertyToRead);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return property;
    }
}