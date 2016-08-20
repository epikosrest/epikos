/*
Copyright (c) [2016] [epikosrest@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package core.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

public final class Utility {

	final static String CONFIGURATION_FILE = "config.file.name";
	final static String CONFIGURATION_FOLDER = "confg.file.location";
	final static String DEFAULT_CONFIGURATION_FOLDER_NAME = "Config";
	static String configFileName;
	static String configFolder;

	public static void loadConfigurationKeyValue(Properties properties) {
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
		System.out.println(stringBuilder);
	}

	public static String getConfigFileFullPath() {
		final String fileSeparator = System.getProperty("file.separator");
		StringBuilder configFileNameAndFolderPath = new StringBuilder();
		// We will try to get name of configuration file if it's been supplied
		// as argument while starting service
		configFileName = System.getProperty(CONFIGURATION_FILE);
		if (configFileName == null) {
			// Assign default configuration name
			configFileName = "Application.configuration";
		}
		configFileNameAndFolderPath.append("Config file name : "
				+ configFileName);

		configFolder = System.getProperty(CONFIGURATION_FOLDER);
		if (configFolder == null) {
			configFolder = System.getProperty("user.dir") + fileSeparator
					+ DEFAULT_CONFIGURATION_FOLDER_NAME;
		} else {
			configFolder = System.getProperty("user.dir") + fileSeparator
					+ configFolder;
		}
		configFileNameAndFolderPath.append("\nConfig folder name : "
				+ configFolder);
		System.out.println(configFileNameAndFolderPath.toString());
		return configFolder + fileSeparator + configFileName;
	}

	public static String getConfigFileName() {
		if (configFileName == null) {
			configFileName = System.getProperty(CONFIGURATION_FILE);
			if (configFileName == null) {
				// Assign default configuration name
				configFileName = "Application.configuration";
			}
		}
		return configFileName;
	}

	public static String getConfigFolder() {
		if (configFolder == null) {
			final String fileSeparator = System.getProperty("file.separator");
			configFolder = System.getProperty(CONFIGURATION_FOLDER);
			if (configFolder == null) {
				configFolder = System.getProperty("user.dir") + fileSeparator
						+ DEFAULT_CONFIGURATION_FOLDER_NAME;
			} else {
				configFolder = System.getProperty("user.dir") + fileSeparator
						+ configFolder;
			}
		}
		return configFolder;
	}
	
	public static String getTimeStamp(long milliSeconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milliSeconds);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		return formatter.format(cal.getTime());
	}
}
