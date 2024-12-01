package com.agent;

import com.sun.tools.attach.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AgentLoader {
    private static Logger LOGGER = LoggerFactory.getLogger(AgentLoader.class);

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        String agentFilePath = "Agent\\target\\Agent-jar-with-dependencies.jar";
        String applicationName = "Application\\target\\Application-jar-with-dependencies.jar";

        VirtualMachine vm = findVirtualMachine(applicationName);

        // load agent
        File agentFile = new File(agentFilePath);
        vm.loadAgent(agentFile.getAbsolutePath());
        LOGGER.info("Java agent loaded successfully");

        vm.detach();
        LOGGER.info("Process finished");
    }

    public static VirtualMachine findVirtualMachine(String name) throws IOException, AttachNotSupportedException {
        VirtualMachineDescriptor virtualMachineDescriptor = VirtualMachine.list()
                .stream()
                .filter(jvm -> jvm.displayName().contains(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find virtual machine with name: " + name));

        String pid = virtualMachineDescriptor.id();
        LOGGER.info("Attaching to JVM with PID: {}", pid);

        return VirtualMachine.attach(pid);
    }

}
