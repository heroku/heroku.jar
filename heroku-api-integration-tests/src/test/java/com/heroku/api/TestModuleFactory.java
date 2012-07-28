package com.heroku.api;

import com.google.inject.Module;
import com.heroku.api.parser.GsonParser;
import com.heroku.api.parser.JacksonParser;
import com.heroku.api.parser.JerseyClientJsonParser;
import org.testng.IModuleFactory;
import org.testng.ITestContext;


public class TestModuleFactory implements IModuleFactory {
    @Override
    public Module createModule(ITestContext iTestContext, Class<?> aClass) {
        if (iTestContext.getCurrentXmlTest().getName().contains("finagle")) {
            return new FinagleModule();
        } else if (iTestContext.getCurrentXmlTest().getName().contains("asynchttp")) {
            return new AsyncHttpClientModule();
        } else if (iTestContext.getCurrentXmlTest().getName().contains("jackson")) {
            return new HttpClientModule(new JacksonParser());
        } else if (iTestContext.getCurrentXmlTest().getName().contains("jersey-client")) {
            return new JerseyClientModule(new JerseyClientJsonParser());
        } else {
            return new HttpClientModule(new GsonParser());
        }

    }


}
