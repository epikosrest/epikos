/* This file is part of Epikos project>
		This program is free software: you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation, either version 3 of the License, or
		(at your option) any later version.

		This program is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package service.configuration;

import lib.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AppConfiguration implements IConfiguration {

	final static Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

	public static final AppConfiguration getAppConfiguration = new AppConfiguration();
	
	Properties properties = null;
	StringBuilder errorMessage = new StringBuilder();

	//static Configuration appConfiguration = new Configuration();
	
	private AppConfiguration() {
	try {
			loadConfiguration();
			loadConfigurationKeyValue(properties);
		} catch (IOException ioExp) {
			errorMessage
					.append("Following exception occured while loading configuraiton file, please fix it !\n"
							+ ioExp.getMessage());
		}
	}

	/*public static AppConfiguration getConfiguration(Configuration config) {
		//configConsumerList.add(config);
		return configuration;
	}*/

	public String getErrorMessage() {
		return errorMessage.toString();
	}

	public Properties getProperties() {
		return properties;
	}


	private void loadConfiguration() throws IOException {
		// We will first get full path of configuration file e.g.
		String configFileFullPath = Utility.getConfigFileFullPath();
		File configFile = new File(configFileFullPath);
		InputStream configFileInputStream = null;
		try {
			configFileInputStream = new FileInputStream(configFile);
			properties = new Properties();
			properties.load(configFileInputStream);
			
		} finally {
			if (configFileInputStream != null) {
				configFileInputStream.close();
			}
		}
	}

	private void loadConfigurationKeyValue(Properties properties) {
		StringBuilder stringBuilder = new StringBuilder(
				"Loaded Configuration Values:\n");
		Iterator<Object> iter = new TreeSet<Object>(properties.keySet())
				.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String value = properties.getProperty(key);

			// Mask sensetive data like "username" and "password"
			if (key.toLowerCase().contains("user")
					|| key.toLowerCase().contains("password"))
				value = "**********";

			stringBuilder.append("     " + key + "=" + value + "\n");

		}
		logger.info(stringBuilder.toString());
	}
}
