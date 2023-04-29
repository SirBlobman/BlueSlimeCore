def SkipCI(number = "all") {
    // Method copied from https://shenxianpeng.github.io/2022/10/jenkins-skip-ci/
    def statusCodeList = []

    String[] keyWords = ['ci skip', 'skip ci'] // add more keywords if need.
    keyWords.each { keyWord ->
        def statusCode = null
        if (number == "all") {
            statusCode = sh script: "git log --oneline --all | grep \'${keyWord}\'", returnStatus: true
        } else {
            statusCode = sh script: "git log --oneline -n ${number} | grep \'${keyWord}\'", returnStatus: true
        }
        statusCodeList.add(statusCode)
    }

    return statusCodeList.contains(0)
}

pipeline {
    agent any

    options {
        githubProjectProperty(projectUrlStr: "https://github.com/SirBlobman/BlueSlimeCore")
    }

    environment {
        DISCORD_URL = credentials('PUBLIC_DISCORD_WEBHOOK')
        MAVEN_DEPLOY = credentials('MAVEN_DEPLOY')
        HANGAR_API_KEY = credentials('HANGAR_API_KEY')
    }

    triggers {
        githubPush()
    }

    tools {
        jdk "JDK 17"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                skipCiCheck = this.SkipCI('1')
            }
        }

        stage("Gradle: Build") {
            when {
                beforeAgent = true
                expression {
                    return !skipCiCheck
                }
            }

            steps {
                withGradle {
                    script {
                        if (env.BRANCH_NAME == "main") {
                            sh("./gradlew --no-daemon --refresh-dependencies clean build publish publishAllPublicationsToHangar")
                        } else if (env.BRANCH_NAME == "dev") {
                            sh("./gradlew --no-daemon --refresh-dependencies clean build publish")
                        } else {
                            sh("./gradlew --no-daemon --refresh-dependencies clean build")
                        }
                    }
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
                        link: "${env.BUILD_URL}",
                        result: currentBuild.currentResult,
                        description: "**Build:** ${env.BUILD_NUMBER}\n**Status:** ${currentBuild.currentResult}",
                        enableArtifactsList: false,
                        showChangeset: true
            }
        }
    }
}
