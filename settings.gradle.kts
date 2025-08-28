plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "StonksItems2"
include("common", "v8", "plugin")

include("plugin")
include("v21")