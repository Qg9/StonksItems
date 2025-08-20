plugins {
    kotlin("jvm") version "2.1.21" apply false
}

allprojects {
    group = "fr.qg.uitems"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://repo.imanity.dev/imanity-libraries/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
    }
}

project(":common") {
    plugins.apply("org.jetbrains.kotlin.jvm")
    extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension::class.java) {
        jvmToolchain(21)
    }
    dependencies {
        add("compileOnly", "org.imanity.paperspigot:paper1.8.8:1.8.8")
        add("compileOnly", "de.tr7zw:item-nbt-api:2.15.1")
        add("compileOnly", "com.github.MilkBowl:VaultAPI:1.7")

        // SHOPS
        add("compileOnly", "com.github.brcdev-minecraft:shopgui-api:3.1.0")

        // JOBS
        add("compileOnly","com.github.Zrips:Jobs:v5.2.6.2")
    }
}

project(":v8") {
    plugins.apply("org.jetbrains.kotlin.jvm")
    extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension::class.java) {
        jvmToolchain(21)
    }
    dependencies {
        add("compileOnly", "org.imanity.paperspigot:paper1.8.8:1.8.8")
        add("compileOnly", "de.tr7zw:item-nbt-api:2.15.1")
        add("compileOnly", project(":common"))
        add("compileOnly", "com.github.MilkBowl:VaultAPI:1.7")
    }
}