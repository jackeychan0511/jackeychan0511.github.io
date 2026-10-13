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

    buildFeatures { buildConfig = true }

    defaultConfig {
        applicationId = "com.jackeychankey.blobdrop"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1.0"

        // AdMob ID는 gradle.properties(admobAppId, admobInterstitialId)에서 읽는다. 기본값은 구글 공식 '테스트' ID.
        val appId = (project.findProperty("admobAppId") as String?) ?: "ca-app-pub-3940256099942544~3347511713"
        val interId = (project.findProperty("admobInterstitialId") as String?) ?: "ca-app-pub-3940256099942544/1033173712"
        manifestPlaceholders["admobAppId"] = appId
        buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"$interId\"")
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

// 출시용(release) 빌드에 테스트 광고 ID가 들어가는 실수를 막는다(검증용 빌드는 -PallowTestAds)
tasks.matching { it.name == "bundleRelease" || it.name == "assembleRelease" }.configureEach {
    doFirst {
        val ids = listOf(project.findProperty("admobAppId"), project.findProperty("admobInterstitialId"))
        if (ids.any { it == null || it.toString().contains("3940256099942544") } && !project.hasProperty("allowTestAds")) {
            throw GradleException("AdMob 테스트 ID로 출시 빌드를 만들 수 없습니다. gradle.properties에 실제 admobAppId/admobInterstitialId를 넣거나, 검증용이면 -PallowTestAds 를 붙이세요.")
        }
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-ads:24.5.0")
    implementation("androidx.webkit:webkit:1.14.0")
}
