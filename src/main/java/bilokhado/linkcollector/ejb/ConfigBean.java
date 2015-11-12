package bilokhado.linkcollector.ejb;

import static javax.ejb.ConcurrencyManagementType.BEAN;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;

@ConcurrencyManagement(BEAN)
@Singleton
@Startup
public class ConfigBean {
	private static final String CONFIG_FILE = "/config";
	private static final Logger logger = Logger
			.getLogger("bilokhado.linkcollector.ejb.ConfigBean");
	private final Map<String, String> config = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		Properties props = new Properties();
		try {
			logger.log(Level.INFO, "Trying to load properties file");
			props.load(ConfigBean.class.getResourceAsStream(CONFIG_FILE));
			logger.log(Level.INFO, "Properties file loaded successfully");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unable to load properties file: "
					+ CONFIG_FILE);
			throw new EJBException("Could not load config file \""
					+ CONFIG_FILE + "\"", ex);
		}
		for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			config.put(name, props.getProperty(name));
		}
	}

	public String getConfigValue(String name) {
		String result = config.get(name);
		if (result == null) {
			logger.log(Level.SEVERE, "Unable to find property: " + name);
			throw new EJBException("Property: \"" + name + "\" not found");
		}
		return result;
	}
}
