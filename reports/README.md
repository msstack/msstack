## About
Aggregated test coverage of all projects

## How to add project
Add project dependency
```xml
<dependency>
    <groupId>com.grydtech.msstack</groupId>
    <artifactId>core</artifactId>
    <version>${project.version}</version>
</dependency>
```

## How to generate test results
run `mvn clean verify jacoco:report` and it will generate aggregated test results in `reports/target/site/jacoco-aggregate`  
open `reports/target/site/jacoco-aggregate/index.html`