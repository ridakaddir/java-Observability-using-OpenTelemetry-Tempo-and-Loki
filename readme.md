## Observability with OpenTelemetry, OpenTelemetry Collector, Grafana Tempo, Loki

This repository contains an examples to demonstrate how to use OpenTelemetry, OpenTelemetry Collector, Grafana Tempo, and Loki to achieve observability in your Spring boot applications.

### Prerequisites

- Docker
- Docker Compose
- Java 11 or higher
- Maven

### How to run

1. Clone the repository
2. Run the following command to start the services

```shell
docker-compose up
```

This will start the following services:

- loki
- tempo
- grafana
- OpenTelemetry Collector

3. Run the following command to start the Spring boot application

```shell
mvn spring-boot:run
```

4. Access the application at http://localhost:8081/hello
5. Access the Grafana dashboard at http://localhost:3000

### How the spring boot application works

To automatically instrument the Spring Boot application with OpenTelemetry, we require OpenTelemetry dependencies in our classpath. To achieve this, we can include the OpenTelemetry Spring Boot starter in the `pom.xml` file

```xml
<dependency>
      <groupId>io.opentelemetry.instrumentation</groupId>
      <artifactId>opentelemetry-spring-boot-starter</artifactId>
</dependency>

<dependencyManagement>
      <dependencies>
            <dependency>
                  <groupId>io.opentelemetry</groupId>
                  <artifactId>opentelemetry-bom</artifactId>
                  <version>1.37.0</version>
                  <type>pom</type>
                  <scope>import</scope>
            </dependency>
            <dependency>
                  <groupId>io.opentelemetry.instrumentation</groupId>
                  <artifactId>opentelemetry-instrumentation-bom-alpha</artifactId>
                  <version>2.3.0-alpha</version>
                  <type>pom</type>
                  <scope>import</scope>
            </dependency>
      </dependencies>
</dependencyManagement>
```

## How the application sends traces to the OpenTelemetry Collector

The OpenTelemetry spring boot starter will automatically instrument the application with OpenTelemetry. The application will start sending traces to the OpenTelemetry Collector.

## How the application sends logs to Loki

The application logback configuration is updated to use the OpenTelemetry logback appender to send ERROR logs to the CONSOLE and OpenTelemetry Collector.

```xml
<appender name="OpenTelemetry" class="io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender">
```

## How the application sends logs to Grafana Loki and traces to Grafana Tempo

The OpenTelemetry Collector is configured to send traces to Grafana Tempo and logs to Grafana Loki.

```yaml
receivers:
  otlp:
    protocols:
      http:
      # grpc:

processors:
  # batch metrics before sending to reduce API usage
  batch:

exporters:
  loki:
    endpoint: "http://loki:3100/loki/api/v1/push"

  otlp:
    endpoint: tempo:4317 # tempo container host & port
    tls:
      insecure: true

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
    logs:
      receivers: [otlp]
      exporters: [loki]
```

GitHub repository:
https://github.com/ridakaddir/java-Observability-using-OpenTelemetry-Tempo-and-Loki
