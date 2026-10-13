import java.util.Properties

plugins {
    id("com.android.application")
}

// 서명 정보는 keystore.properties(저장소에 올리지 않음)에서 읽는다. 없으면 서명 없는 번들이 만들어진다.
val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "com.jackeychankey.blobdrop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jackeychankey.blobdrop"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        if (keystoreProps.containsKey("storeFile")) {
            create("release") {
                storeFile = rootProject.file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (keystoreProps.containsKey("storeFile")) signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // 게임 소스(games/blob-drop)를 앱 assets/www 로 복사해 넣는다 → 게임 코드는 저장소 한 곳에서만 관리
    sourceSets["main"].assets.srcDir(layout.buildDirectory.dir("generated/web"))
}

val syncWebAssets = tasks.register<Sync>("syncWebAssets") {
    from(rootProject.layout.projectDirectory.dir("../../games/blob-drop")) {
        include("index.html", "matter.min.js", "team-jackeychankey.png")
    }
    into(layout.buildDirectory.dir("generated/web/www"))
}
// 웹 파일 복사가 항상 먼저 끝나도록 순서를 고정(lint·assets 병합 등이 복사 결과를 읽기 때문)
tasks.named("preBuild") { dependsOn(syncWebAssets) }
tasks.matching { it.name != "syncWebAssets" && it.name != "clean" }.configureEach { mustRunAfter(syncWebAssets) }

dependencies {
    implementation("androidx.webkit:webkit:1.14.0")
}
