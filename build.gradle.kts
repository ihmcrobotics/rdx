plugins {
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
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

    val lwjglVersion = "3.3.3"
    api("org.lwjgl:lwjgl-openvr:$lwjglVersion")
    api("org.lwjgl:lwjgl-openvr:$lwjglVersion:natives-linux")
    api("org.lwjgl:lwjgl-openvr:$lwjglVersion:natives-windows")
    api("org.lwjgl:lwjgl-openvr:$lwjglVersion:natives-windows-x86")
    api("org.lwjgl:lwjgl-openvr:$lwjglVersion:natives-macos")
    api("org.lwjgl:lwjgl-assimp:$lwjglVersion")
    api("org.lwjgl:lwjgl-assimp:$lwjglVersion:natives-linux")
    api("org.lwjgl:lwjgl-assimp:$lwjglVersion:natives-windows")
    api("org.lwjgl:lwjgl-assimp:$lwjglVersion:natives-windows-x86")
    api("org.lwjgl:lwjgl-assimp:$lwjglVersion:natives-macos")

    implementation("us.ihmc:euclid-geometry:0.21.0")
    implementation("us.ihmc:euclid-frame:0.21.0")

    implementation("us.ihmc:ihmc-commons:0.32.0") // TODO: Upgrade this, transitive vulnerable dependencies
    implementation("us.ihmc:ihmc-graphics-description:0.25.1")
    implementation("us.ihmc:ihmc-java-toolkit:0.14.0-240126") // TODO: Upgrade this, transitive vulnerable dependencies
    implementation("us.ihmc:ihmc-robotics-toolkit:0.14.0-240126") // TODO: Upgrade


    implementation("net.sf.trove4j:trove4j:3.0.3") // TODO: Get rid of this

    implementation("org.bytedeco:javacpp:1.5.9")
    implementation("org.bytedeco:opencv:4.7.0-1.5.9")
    implementation("org.bytedeco:opencv-platform:4.7.0-1.5.9") // TODO: replace with only a few platforms
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
