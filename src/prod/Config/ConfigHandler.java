/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2017, Aswa Paul
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package prod.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giantas
 */
public class ConfigHandler {
    
    private String propertiesFile = "prod.properties";
    private final Properties properties = new Properties();
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    
    public ConfigHandler() {}
    
    public ConfigHandler(String fileName) {
        this.propertiesFile = fileName;
    }
    
    public void create() {
        File f = new File(propertiesFile);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ConfigHandler.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    public String getProperty(String property) {
        String returnValue = null;
        loadInputStream();
        if (inputStream != null) {
            returnValue = properties.getProperty(property, null);
        }
        closeInputStream();
        return returnValue;
    }
    
    public String getProperty(String property, String defaultValue) {
        String returnValue = null;
        loadInputStream();
        if (inputStream != null) {
            returnValue = properties.getProperty(property, defaultValue);
        }
        closeInputStream();
        return returnValue;
    }
    
    public Enumeration getProperties() {
        Enumeration<?> e = null;
        loadInputStream();
        if (inputStream != null) {
            e = properties.propertyNames();
        }
        closeInputStream();
        return e;
    }
    
    public boolean setProperty(String key, String value) {
        boolean success = false;
        loadOutputStream();
        if (outputStream != null) {
            properties.setProperty(key, value);
            try {
                properties.store(outputStream, null);
                success = true;
            } catch (IOException ex) {
                Logger.getLogger(ConfigHandler.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        closeOutputStream();
        return success;
    }
    
    public boolean setProperties(Map map) {
        boolean success = false;
        loadOutputStream();
        if (outputStream != null) {
            properties.putAll(map);
            try {
                properties.store(outputStream, null);
                success = true;
            } catch (IOException ex) {
                Logger.getLogger(ConfigHandler.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        closeOutputStream();
        return success;
    }
    
    private void loadInputStream() {
        try {
            inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadOutputStream() {
        try {
            outputStream = new FileOutputStream(propertiesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigHandler.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private void closeInputStream() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigHandler.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void closeOutputStream() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigHandler.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
