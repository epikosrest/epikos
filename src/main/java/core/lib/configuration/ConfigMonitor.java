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

public class ConfigMonitor extends Thread {

	final static Logger logger = LoggerFactory.getLogger(ConfigMonitor.class);

	File appConfigFile;
	Properties properties = null;
	static List<IConfiguration> configConsumerList = new ArrayList<IConfiguration>();
	public void startMonitoring(File appConfigFile,Properties properties){
		this.appConfigFile = appConfigFile;
		this.properties = properties;
		this.start();
	}
	
	@Override
	public void run(){

		logger.info(String.format("[%s] Config file ", Utility.getTimeStamp(Calendar.getInstance().getTimeInMillis())) + appConfigFile.getName()
				+ " was last modified on "
				+ Utility.getTimeStamp(appConfigFile.lastModified()));
		long appConfigFileLastModifiedTime = appConfigFile
				.lastModified();
		while (true) {
			try {
				Thread.sleep(Integer.parseInt(getProperties().getProperty(("config.update.frequency")))*1000);
				// If the file has been changed then reload the
				// configuration and properties

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
					//updateFrequencyInMilliSeconds = Integer.parseInt(getProperties().getProperty(("config.update.frequency")));
					// notify to update the properties
					//threadIsAlive = true;
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
	
	public Properties getProperties(){
		return properties;
	}
	
}
