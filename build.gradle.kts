plugins {
    kotlin("jvm") version "2.4.10"
    id("com.gradleup.shadow") version "9.6.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.22"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
//    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
//    compileOnly("dev.folia:folia-api:26.1.2.build.+")
    paperweight.paperDevBundle("26.2.build.+")
    compileOnly("me.clip:placeholderapi:2.11.7")
    implementation("org.bstats:bstats-bukkit:3.2.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.jewelexx:craftcolours:1.0.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.22")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

kotlin {
    jvmToolchain(25)
}

tasks.shadowJar {
    relocate("org.bstats", "dev.cordor.tablocation.bstats")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION