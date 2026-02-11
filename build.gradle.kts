plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.6"
    war
}

group = "com.fawry"
version = "01.00"
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    val profileType = System.getenv("PROFILE_TYPE")
    val isDevProfile = profileType == null || profileType == "DEV"
    val nexusUsername = System.getenv("NEXUS_USERNAME")
    val nexusPassword = System.getenv("NEXUS_PASSWORD")

    if (!isDevProfile) {
        maven {
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
            url = uri("https://nexus.zone10.fawry.io/repository/maven-central")
        }
        maven {
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
            url = uri("https://nexus.zone10.fawry.io/repository/libs-release-local/")
        }
    }
    mavenCentral()
}

configurations {
    create("jaxb")
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    all {
//        exclude(group = "ch.qos.logback")
    }
}

sourceSets {
    named("main") {
        java {
            srcDir("src/main/java")
            srcDir("build/generated-sources/SOF")
        }
    }
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Web Services
    implementation("org.springframework.ws:spring-ws-core:4.0.3")
    implementation("wsdl4j:wsdl4j:1.6.3")
    // HTTP Client
    implementation("org.apache.httpcomponents.client5:httpclient5")


    // XML Processing
    implementation("org.apache.ws.xmlschema:xmlschema-core:2.3.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.1")
    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:2.16.1")
    implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:2.16.1")

    // JAXB (Jakarta EE)
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-xjc:4.0.2")

    // For JAXB code generation
    add("jaxb", "org.glassfish.jaxb:jaxb-xjc:4.0.2")
    add("jaxb", "org.glassfish.jaxb:jaxb-runtime:4.0.2")
    add("jaxb", "jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Servlet Container
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

fun registerJaxbTask(name: String, sourcesDir: String, schema: String, packageName: String) {
    tasks.register(name) {
        val outputDir = layout.buildDirectory.dir(sourcesDir)
        outputs.dir(outputDir)

        doLast {
            ant.withGroovyBuilder {
                "taskdef"(
                    "name" to "xjc",
                    "classname" to "com.sun.tools.xjc.XJCTask",
                    "classpath" to configurations["jaxb"].asPath
                )
                "mkdir"("dir" to outputDir.get().asFile)
                "xjc"(
                    "destdir" to outputDir.get().asFile,
                    "schema" to schema,
                    "package" to packageName,
                    "extension" to true
                ) {
                    "arg"("value" to "-wsdl")
                    "produces"("dir" to outputDir.get().asFile, "includes" to "**/*.java")
                }
            }
        }
    }
}

// Register JAXB generation tasks
//registerJaxbTask("genSOFJaxb", "generated-sources/SOF", "src/main/resources/XSD/SOF/SOFOnlineService_schema.xsd", "com.fawry.generated.sof")
// registerJaxbTask("genAdapterErrorCodeMappingJaxb", "generated-sources/adapter/errorCode", "src/main/resources/XSD/adapter/errorCodesMapping.xsd", "com.fawry.generated.adapter.errorCode")
// registerJaxbTask("genAdapterConfigJaxb", "generated-sources/adapter/config", "src/main/resources/XSD/adapter/configuration.xsd", "com.fawry.generated.adapter.config")

// Configure compile task dependencies
//tasks.named("compileJava") {
//    dependsOn(tasks.named("genSOFJaxb"))
//    // dependsOn(tasks.named("genAdapterErrorCodeMappingJaxb"))
//    // dependsOn(tasks.named("genAdapterConfigJaxb"))
//}

tasks.withType<Test> {
    useJUnitPlatform()
}