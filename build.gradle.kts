plugins {
    id("us.ihmc.ihmc-build")
    id("us.ihmc.ihmc-ci") version "8.3"
    id("us.ihmc.ihmc-cd") version "1.26"
}

ihmc {
    group = "us.ihmc"
    version = "1.0.0"
    vcsUrl = "https://github.com/ihmcrobotics/rdx"
    openSource = false

    configureDependencyResolution()
    configurePublications()
}

mainDependencies {
    val gdxVersion = "1.12.1"
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    api("com.github.mgsx-dev.gdx-gltf:core:2.2.1")

    val imguiVersion = "1.86.11"
    api("io.github.spair:imgui-java-binding:$imguiVersion")
    api("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    api("io.github.spair:imgui-java-natives-linux-ft:$imguiVersion")
    api("io.github.spair:imgui-java-natives-macos-ft:$imguiVersion")
    api("io.github.spair:imgui-java-natives-windows-ft:$imguiVersion")

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

    api("us.ihmc:euclid-geometry:0.21.0")
    api("us.ihmc:euclid-frame:0.21.0")

    api("us.ihmc:ihmc-commons:0.32.0") // TODO: Upgrade this, transitive vulnerable dependencies
    api("us.ihmc:ihmc-graphics-description:0.25.1")
    api("us.ihmc:ihmc-java-toolkit:0.14.0-240126") // TODO: Upgrade this, transitive vulnerable dependencies
    api("us.ihmc:ihmc-robotics-toolkit:0.14.0-240126") // TODO: Upgrade

    api("net.sf.trove4j:trove4j:3.0.3") // TODO: Get rid of this

    api("org.bytedeco:javacpp:1.5.9")
    api("org.bytedeco:opencv:4.7.0-1.5.9")
    api("org.bytedeco:opencv-platform:4.7.0-1.5.9") // TODO: replace with only a few platforms
}
