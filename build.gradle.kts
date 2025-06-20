plugins {
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val jarName = "ADM"
group = "com.medvedev"
version = "1.0.3"

repositories {
    google()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.apache.directory.api:api-all:2.1.7")
    implementation("org.apache.mina:mina-core:2.2.4")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.medvedev.presentation.MainKt"
        )
    }
}

tasks {
    shadowJar {
        archiveBaseName.set(jarName)            // Укажите имя вашего JAR-файла
        archiveVersion.set(version.toString())  // Укажите версию вашего JAR-файла
        archiveClassifier.set("")               // Убираем классификатор, чтобы получить обычный JAR
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}