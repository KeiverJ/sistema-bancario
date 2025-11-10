package com.example.config;


import java.util.Properties;

public class DatabaseConfig {

  private final Properties properties;
  private String url;
  private String username;
  private String password;
  private int maxConnections;
  private int poolSize;
  private int timeout;

  public DatabaseConfig() {
    this.properties = new Properties();
    
    this.url = "jdbc:h2:mem:bankdb";
    this.username = "sa";
    this.password = "";
    this.maxConnections = 10;
    this.poolSize = 20;
    this.timeout = 30;

    loadDefaultProperties();
  }

  private void loadDefaultProperties() {
    properties.setProperty("database.driver", "org.h2.Driver");
    properties.setProperty("database.url", url);
    properties.setProperty("database.username", username);
    properties.setProperty("database.password", password);
    properties.setProperty("database.maxConnections", String.valueOf(maxConnections));
    properties.setProperty("database.autoCommit", "true");
    properties.setProperty("database.timeout", String.valueOf(timeout));
    properties.setProperty("database.poolSize", String.valueOf(poolSize));
  }

  

  public String getUrl() {
    return url;
  }


  public void setUrl(String url) {
    this.url = url;
    if (url != null) {
      properties.setProperty("database.url", url);
    } else {
      properties.remove("database.url");
    }
  }

  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
    if (username != null) {
      properties.setProperty("database.username", username);
    } else {
      properties.remove("database.username");
    }
  }

  public String getPassword() {
    return password;
  }


  public void setPassword(String password) {
    this.password = password;
    if (password != null) {
      properties.setProperty("database.password", password);
    } else {
      properties.remove("database.password");
    }
  }

  public int getMaxConnections() {
    return maxConnections;
  }


  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
    properties.setProperty("database.maxConnections", String.valueOf(maxConnections));
  }

  public int getPoolSize() {
    return poolSize;
  }


  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
    properties.setProperty("database.poolSize", String.valueOf(poolSize));
  }

  public int getTimeout() {
    return timeout;
  }


  public void setTimeout(int timeout) {
    this.timeout = timeout;
    properties.setProperty("database.timeout", String.valueOf(timeout));
  }

  public String getProperty(String key) {
    return key == null ? null : properties.getProperty(key);
  }

  public Properties getAllProperties() {
    return new Properties(properties);
  }

  public void setProperty(String key, String value) {
    if (key != null) {
      if (value != null) {
        properties.setProperty(key, value);
      } else {
        properties.remove(key);
      }
    }
  }

  public boolean isProductionEnvironment() {
    return !url.contains("h2:mem:");
  }

  public boolean isValidConfiguration() {
    return url != null && !url.isEmpty() &&
        username != null &&
        maxConnections > 0;
  }

  public String getConnectionString() {
    return String.format("%s;USER=%s;PASSWORD=%s;MAX_CONNECTIONS=%d",
        url, username, password, maxConnections);
  }

  @Override
  public String toString() {
    return String.format("DatabaseConfig{url='%s', username='%s', maxConnections=%d, poolSize=%d}",
        url, username, maxConnections, poolSize);
  }
}