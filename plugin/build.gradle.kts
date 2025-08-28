plugins {
    kotlin("jvm") version "2.1.21"
    id("com.gradleup.shadow") version "8.3.9"
    id("io.github.revxrsal.zapper") version "1.0.3"
}

dependencies {
    compileOnly("org.imanity.paperspigot:paper1.8.8:1.8.8")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    implementation(project(":v8"))
    implementation(project(":common"))

    zap("de.tr7zw:item-nbt-api:2.15.1")
    zap("com.github.Qg9:Drink:1")
    zap("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
}

tasks.named<Jar>("jar") { enabled = false }

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    mergeServiceFiles()

    archiveFileName.set("StonksItems-paper-${project.version}.jar")
    destinationDirectory.set(rootProject.layout.projectDirectory.dir("target"))
    archiveClassifier.set("") // pas de "-all" dans le nom
}

tasks.named("assemble") { dependsOn(tasks.named("shadowJar")) }

zapper {
    libsFolder = "libraries"
    relocationPrefix = "fr.qg.items.libs"

    repositories {
        includeProjectRepositories()
    }

    relocate("de.tr7zw.changeme.nbtapi", "nbtapi")
    relocate("org.jetbrains.kotlin", "kotlin")
    relocate("com.jonahseguin.drink", "drink")
}