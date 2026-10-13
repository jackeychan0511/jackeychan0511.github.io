// Maven Central 은 Google 운영 미러를 먼저 쓰고(속도·요청 제한 회피), 실패하면 원본으로 대체한다.
val centralMirror = "https://maven-central.storage-download.googleapis.com/maven2/"

pluginManagement {
    repositories {
        google()
        maven(url = "https://maven-central.storage-download.googleapis.com/maven2/")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = centralMirror)
        mavenCentral()
    }
}
rootProject.name = "BlobDrop"
include(":app")
