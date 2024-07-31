package factory;

import contants.DriverType;

public class DriverManagerFactory {
    public static DriverManager selectDriverType(DriverType driverType) {
        switch (driverType) {
            case CHROME: {
                return new ChromeDriverManager();
            }
            case EDGE: {
                return new EdgeDriverManager();
            }
            default:
                throw new IllegalStateException("Unexpected value: " + driverType);
        }
    }
}
