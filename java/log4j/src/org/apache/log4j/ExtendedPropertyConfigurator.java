/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.config.PropertySetter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 * An extended {@link org.apache.log4j.PropertyConfigurator
 * PropertyConfigurator} with support for {@link org.apache.log4j.spi.Filter
 * Filters}.
 *
 * <p>Configuration syntax for filters:
 * <pre>
 * log4j.appender.APPENDER.filter.ID=fully.qualified.name.of.filter.class
 * log4j.appender.APPENDER.filter.ID.option1=value1
 * log4j.appender.APPENDER.filter.ID.option2=value2
 * ...</pre>
 * The first line defines the class name of the filter identified by ID;
 * subsequent lines with the same ID specify filter option - value
 * paris. Multiple filters are added to the APPENDER in the lexicographic
 * order of IDs.
 * <p>Example usage:
 * <pre>
 * ExtendedPropertyConfigurator.configure("my.log4j.properties");</pre>
 * <p>Example configuration snippet for a filter chain that passes
 * only messages containing the "PATTERN" substring.
 * <pre>
 * my.StringToMatch=PATTERN
 * log4j.appender.CONSOLE.filter.01=org.apache.log4j.varia.StringMatchFilter
 * log4j.appender.CONSOLE.filter.01.StringToMatch=${my.StringToMatch}
 * log4j.appender.CONSOLE.filter.02=org.apache.log4j.varia.DenyAllFilter
 *
 * @author Lajos Pajtek
 */
public class ExtendedPropertyConfigurator extends PropertyConfigurator {

  /**
   *
   * @param configFilename config file name
   */
  static
  public void configure(String configFilename) {
    new ExtendedPropertyConfigurator().doConfigure(configFilename,
        LogManager.getLoggerRepository());
  }

  protected Appender parseAppender(Properties props, String appenderName) {
    Appender appender = super.parseAppender(props, appenderName);
    parseAppenderFilters(props, appenderName, appender);
    return appender;
  }

  protected void parseAppenderFilters(Properties props, String appenderName, Appender appender) {
    // extract filters and filter options from props into a hashtable mapping
    // the property name defining the filter class to a list of pre-parsed
    // name-value pairs associated to that filter
    final String filterPrefix = APPENDER_PREFIX + appenderName + ".filter.";
    int fIdx = filterPrefix.length();
    Hashtable filters = new Hashtable();
    Enumeration e = props.keys();
    String name = "";
    while (e.hasMoreElements()) {
      String key = (String) e.nextElement();
      if (key.startsWith(filterPrefix)) {
        int dotIdx = key.indexOf('.', fIdx);
        String filterKey = key;
        if (dotIdx != -1) {
          filterKey = key.substring(0, dotIdx);
          name = key.substring(dotIdx+1);
        }
        Vector filterOpts = (Vector) filters.get(filterKey);
        if (filterOpts == null) {
          filterOpts = new Vector();
          filters.put(filterKey, filterOpts);
        }
        if (dotIdx != -1) {
          String value = OptionConverter.findAndSubst(key, props);
          filterOpts.add(new NameValue(name, value));
        }
      }
    }

    // sort filters by IDs, insantiate filters, set filter options,
    // add filters to the appender
    Enumeration g = new SortedKeyEnumeration(filters);
    while (g.hasMoreElements()) {
      String key = (String) g.nextElement();
      String clazz = props.getProperty(key);
      if (clazz != null) {
        LogLog.debug("Filter key: ["+key+"] class: ["+props.getProperty(key) +"] props: "+filters.get(key));
        Filter filter = (Filter) OptionConverter.instantiateByClassName(clazz, Filter.class, null);
        if (filter != null) {
          PropertySetter propSetter = new PropertySetter(filter);          
          Vector v = (Vector)filters.get(key);
          Enumeration filterProps = v.elements();
          while (filterProps.hasMoreElements()) {
            NameValue kv = (NameValue)filterProps.nextElement();
            propSetter.setProperty(kv.key, kv.value);
          }
          propSetter.activate();
          LogLog.debug("Adding filter of type ["+filter.getClass()
           +"] to appender named ["+appender.getName()+"].");
          appender.addFilter(filter);
        }
      } else {
        LogLog.warn("Missing class definition for filter: ["+key+"]");
      }
    }
  }

  class NameValue {
    String key, value;
    public NameValue(String key, String value) {
      this.key = key;
      this.value = value;
    }
    public String toString() {
      return key + "=" + value;
    }
  }

  class SortedKeyEnumeration implements Enumeration {

    private Enumeration e;

    public SortedKeyEnumeration(Hashtable ht) {
      Enumeration f = ht.keys();
      Vector keys = new Vector(ht.size());
      for (int i, last = 0; f.hasMoreElements(); ++last) {
        String key = (String) f.nextElement();
        for (i = 0; i < last; ++i) {
          String s = (String) keys.get(i);
          if (key.compareTo(s) <= 0) break;
        }
        keys.add(i, key);
      }
      e = keys.elements();
    }

    public boolean hasMoreElements() {
      return e.hasMoreElements();
    }

    public Object nextElement() {
      return e.nextElement();
    }
  }
}
