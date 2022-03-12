plugins {
    java
}

group = "com.slava_110.kmapsolver"
version = "1.0-SNAPSHOT"

val logisim: Configuration by configurations.creating

configurations.compileOnly {
    extendsFrom(logisim)
}

repositories {
    mavenCentral()
    ivy {
        name = "SourceForge"
        url = uri("https://downloads.sourceforge.net/project")
        patternLayout {
            // Not sure what to do with 2.7.x in artifact pattern :I
            // Let's just leave it as constant because Logisim project is no longer maintained anyway
            artifact("[organization]/2.7.x/[revision]/[artifact]-[revision].[ext]")
        }
        metadataSources {
            artifact()
        }
    }
}

dependencies {
    implementation("org.jetbrains:annotations:16.0.2")

    logisim("circuit:logisim-generic:2.7.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.jar {
    manifest {
        attributes["Library-Class"] = "com.slava_110.kmapsolver.KMapSolverLib"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("launchLogisim") {
    group = "launch"
    dependsOn(tasks.build)

    mainClass.set("com.cburch.logisim.Main")
    classpath(logisim)

    args("-nosplash", "test-circuit.circ")

    workingDir = file("run")
}