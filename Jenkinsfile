def skipCiCheck

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
        JDK8 = '/home/container/jdk/zulu8.78.0.19-ca-jdk8.0.412-linux_x64'
        JDK16 = '/home/container/jdk/zulu16.32.15-ca-jdk16.0.2-linux_x64'
        JDK17 = '/home/container/jdk/zulu17.50.19-ca-jdk17.0.11-linux_x64'
        JDK21 = '/home/container/jdk/zulu21.34.19-ca-jdk21.0.3-linux_x64'
    }

    triggers {
        githubPush()
    }

    tools {
        jdk "JDK 21"
    }

    stages {
        stage('Check CI Skip') {
            steps {
                script {
                    skipCiCheck = this.SkipCI('1')
                }
            }
        }

        stage("Gradle: Build") {
            when {
                expression { return !skipCiCheck }
            }

            steps {
                withGradle {
                    script {
                        if (env.BRANCH_NAME == "main") {
                            sh("./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build publish publishAllPublicationsToHangar")
                        } else if (env.BRANCH_NAME.contains("dev")) {
                            sh("./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build publish")
                        } else {
                            sh("./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build")
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                if (!skipCiCheck) {
                    archiveArtifacts artifacts: 'core/build/libs/BlueSlimeCore-*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'bungeecord/core/build/libs/BlueSlimeBungeeCore-*.jar', fingerprint: true
                }
            }
        }

        always {
            script {
                if (!skipCiCheck) {
                    discordSend webhookURL: DISCORD_URL, title: "BlueSlimeCore", link: "${env.BUILD_URL}",
                            result: currentBuild.currentResult,
                            description: """\
                                **Branch:** ${env.GIT_BRANCH}
                                **Build:** ${env.BUILD_NUMBER}
                                **Status:** ${currentBuild.currentResult}""".stripIndent(),
                            enableArtifactsList: false, showChangeset: true
                }
            }
        }
    }
}
