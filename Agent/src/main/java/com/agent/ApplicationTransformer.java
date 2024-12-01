package com.agent;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ApplicationTransformer implements ClassFileTransformer {

    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationTransformer.class);

    private String targetClassName;
    private ClassLoader targetClassLoader;

    public ApplicationTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            LOGGER.info("[Agent] Transforming class VendingMachine");
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));

                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod("buy");

                // start time
                m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = java.lang.System.currentTimeMillis();");

                // end time
                StringBuilder endBlock = new StringBuilder();
                m.addLocalVariable("endTime", CtClass.longType);
                m.addLocalVariable("totalTime", CtClass.doubleType);
                endBlock.append("endTime = java.lang.System.currentTimeMillis();");
                endBlock.append("totalTime = ( endTime - startTime ) / 1000.0;");
                endBlock.append("LOGGER.info(\"[Agent] Operation completed in :\" + totalTime + \" seconds\");");
                m.insertAfter(endBlock.toString());

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return byteCode;
    }
}
