package com.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class MyInstrumentationAgent {

    private static Logger LOGGER = LoggerFactory.getLogger(MyInstrumentationAgent.class);

    private static String className = "com.application.VendingMachine";

    public static void premain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException {
        LOGGER.info("[Agent] Premain method");

        transformClass(inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException {
        LOGGER.info("[Agent] Agentmain method");

        transformClass(inst);
    }

    private static void transformClass(Instrumentation instrumentation) throws ClassNotFoundException, UnmodifiableClassException {
        Class<?> targetClass = Class.forName(className);
        ClassLoader targetClassLoader = targetClass.getClassLoader();

        ApplicationTransformer dt = new ApplicationTransformer(targetClass.getName(), targetClassLoader);
        instrumentation.addTransformer(dt, true);
        instrumentation.retransformClasses(targetClass);
    }

}
