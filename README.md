
# Sample Java Instrumentation Agent

This project demonstrates how to create and use a Java instrumentation agent. It consists of two main components:

1. **Agent**: The instrumentation agent responsible for modifying the behavior of the target application.
2. **Application**: A simple vending machine application to be instrumented.

---

## Application

The **Application** module represents a straightforward implementation of a vending machine.

### Build the Application

To compile and package the application, navigate to the Application directory and execute the following command:

```bash
mvn clean package
```

This will generate the application JAR file in the `target` directory.

---

## Agent

The **Agent** module contains the code for the Java instrumentation agent.

### Build the Agent

To compile and package the agent, run the following command from the Agent directory:

```bash
mvn clean package
```

The resulting JAR file will be located in the `target` directory.

---

## Running the Instrumentation Agent

There are two ways to load a Java instrumentation agent: **Premain (static loading)** and **Agentmain (dynamic loading)**. Below are detailed instructions for both methods.

### Premain Loading (Static Loading)

In static loading, the agent is loaded at JVM startup using the `-javaagent` flag.

1. Navigate to the root directory of the project.
2. Run the following command:

```bash
java -javaagent:./Agent/target/Agent-jar-with-dependencies.jar -jar ./Application/target/Application-jar-with-dependencies.jar
```

This will start the application with the agent attached, allowing the agent to modify the application during startup.

---

### Agentmain Loading (Dynamic Loading)

In dynamic loading, the agent is attached to a running JVM process at runtime.

1. First, start the application using the following command:

```bash
java -jar ./Application/target/Application-jar-with-dependencies.jar
```

2. Then, load the agent dynamically into the running application:

```bash
java -jar ./Agent/target/Agent-jar-with-dependencies.jar
```

---

By following these steps, you can successfully experiment with Java instrumentation using the provided sample project.
