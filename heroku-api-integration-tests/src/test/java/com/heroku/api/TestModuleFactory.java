package com.heroku.api;

import com.google.inject.Module;
import org.testng.IModuleFactory;
import org.testng.ITestContext;


public class TestModuleFactory implements IModuleFactory {
    @Override
    public Module createModule(ITestContext iTestContext, Class<?> aClass) {
        if (iTestContext.getCurrentXmlTest().getName().contains("finagle")) {
            return new FinagleModule();
        } else {
            return new HttpClientModule();
        }

    }


}
