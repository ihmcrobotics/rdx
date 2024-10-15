plugins {
    id("java-library")
    id("maven-publish")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    val gdxVersion = "1.12.1"
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.github.mgsx-dev.gdx-gltf:core:2.2.1")

    val imguiVersion = "1.86.11"
    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-linux-ft:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-macos-ft:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-windows-ft:$imguiVersion")

    implementation("us.ihmc:ihmc-commons:0.32.0") // TODO: Upgrade this, transitive vulnerable dependencies

    implementation("net.sf.trove4j:trove4j:3.0.3") // TODO: Get rid of this
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
