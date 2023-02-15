pipeline {
    agent any

    options {
        githubProjectProperty(projectUrlStr: "https://github.com/SirBlobman/BlueSlimeCore")
    }

    environment {
        DISCORD_URL = credentials('PUBLIC_DISCORD_WEBHOOK')
    }

    triggers {
        githubPush()
    }

    tools {
        jdk "JDK 17"
    }

    stages {
        stage("Gradle: Build (No Daemon)") {
            steps {
                withGradle {
                    sh("ls $JAVA_HOME")
                    sh("./gradlew clean build --refresh-dependencies --no-daemon")
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'core/build/libs/BlueSlimeCore-*.jar, bungeecord/core/build/libs/BlueSlimeBungeeCore-*.jar', fingerprint: true
        }

        always {
            script {
                discordSend webhookURL: DISCORD_URL,
                        title: "${env.JOB_NAME}",
                        result: currentBuild.currentResult,
                        description: "**Build:** ${env.BUILD_NUMBER}",
                        enableArtifactsList: false,
                        showChangeset: true
            }
        }
    }
}
