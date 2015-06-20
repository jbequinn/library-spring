package com.dodecaedro.library.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EntityScan("com.dodecaedro.library.data.pojo")
@EnableJpaRepositories("com.dodecaedro.library.repository")
@ComponentScan("com.dodecaedro.library")
@EnableAutoConfiguration
@EnableTransactionManagement
public class LibraryDaoConfiguration extends JpaBaseConfiguration {
  @Override
  protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
    EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
    return adapter;
  }
  @Override
  protected Map<String, Object> getVendorProperties() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    return map;
  }
}
