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
package core.lib.configuration;

import core.lib.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class AppConfiguration implements IAppConfiguration {

	final static Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

	protected static final AppConfiguration configuration = new AppConfiguration();
	
	Properties properties = null;
	StringBuilder errorMessage = new StringBuilder();

	static List<IConfiguration> configConsumerList = new ArrayList<IConfiguration>();
	
	private AppConfiguration() {
	try {
			loadConfiguration();
			keepConfigurationUptoDate();
			Utility.loadConfigurationKeyValue(properties);
		} catch (IOException ioExp) {
			errorMessage
					.append("Following exception occured while loading configuraiton file, please fix it !\n"
							+ ioExp.getMessage());
		}
	}

	public static AppConfiguration getConfiguration(IConfiguration config) {
		configConsumerList.add(config);
		return configuration;
	}

	public String getErrorMessage() {
		return errorMessage.toString();
	}

	public Properties getProperties() {
		return properties;
	}

	public String getConfigFileName() {
		return Utility.getConfigFileName();
	}

	public String getConfigFolder() {
		return Utility.getConfigFolder();
	}

	public void loadConfiguration() throws IOException {
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

	private void keepConfigurationUptoDate() {
		// If change detected notify config to reload configuration
		int updateFrequency = 0;
		String configUpdateFrequency = properties.getProperty("config.update.frequency");

		if (configUpdateFrequency != null) {
			updateFrequency = Integer.parseInt(configUpdateFrequency);
			logger.info(String.format(
					"Service will update properties every %s seconds",
					updateFrequency));
		} else {
			logger.info("Service will not update properties as config.update.frequency has not been configured/provided !");
			return;
		}

		// Get config file and keep last modify time stamp
		final File appConfigFile = new File(getConfigFolder()
				+ System.getProperty("file.separator") + getConfigFileName());
		if (appConfigFile.exists()) {
			logger.info("File exists");
		} else {
			logger.info("File doesn't exists");
			return;
		}
		final long updateFrequencyInMilliSeconds = updateFrequency * 1000;
		if (updateFrequencyInMilliSeconds > 0) {
			Thread configFileChangeDetectThread = new Thread() {
				public void run() {
					logger.info(String.format("[%s] Config file ",Utility.getTimeStamp(Calendar.getInstance().getTimeInMillis())) + appConfigFile.getName()
							+ " was last modified on "
							+ Utility.getTimeStamp(appConfigFile.lastModified()));
					long appConfigFileLastModifiedTime = appConfigFile
							.lastModified();
					while (true) {
						try {
							Thread.sleep(Integer.parseInt(getProperties().getProperty(("config.update.frequency")))*1000);
							// If the file has been changed then reload the configuration and properties
							if (appConfigFileLastModifiedTime != appConfigFile
									.lastModified()) {
								// update the time stamp
								logger.info(String.format("[%s] Updating Config file ",Utility.getTimeStamp(Calendar.getInstance().getTimeInMillis()))
												+ appConfigFile.getName()
												+ " recent modified time detected which is on "
												+ Utility.getTimeStamp(appConfigFile
														.lastModified()));

								appConfigFileLastModifiedTime = appConfigFile.lastModified();
								//We will reload/update the latest configuration
								loadConfiguration();
								for (IConfiguration config : configConsumerList) {
									config.notifyConfigChange();
								}
								

							} else {
								logger.info(String.format("[%s] Config file ",Utility.getTimeStamp(Calendar.getInstance().getTimeInMillis()))
												+ appConfigFile.getName()
												+ " has not been modified since last time "
												+ Utility.getTimeStamp(appConfigFile
														.lastModified()));
							}
						} catch (InterruptedException interupExp) {
							logger.info("Interrupt exception occured !");
						} catch (IOException ioExp) {
							logger.info("IOException occured !" + ioExp.getMessage());
							
						}
					}
				}
			};
			configFileChangeDetectThread.setName("ConfigFileChangeMonitor");
			configFileChangeDetectThread.setDaemon(true);
			configFileChangeDetectThread.start();
		}
	}
}
