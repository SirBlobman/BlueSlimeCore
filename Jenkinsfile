def skipCiCheck

def SkipCI(number = 'all') {
    // Method copied from https://shenxianpeng.github.io/2022/10/jenkins-skip-ci/
    def statusCodeList = []

    String[] keyWords = ['ci skip', 'skip ci'] // add more keywords if need.
    keyWords.each { keyWord ->
        def statusCode = null
        if (number == 'all') {
            statusCode = sh script: "git log --oneline --all | grep \'${keyWord}\'", returnStatus: true
        } else {
            statusCode = sh script: "git log --oneline -n ${number} | grep \'${keyWord}\'", returnStatus: true
        }
        statusCodeList.add(statusCode)
    }

    return statusCodeList.contains(0)
}

pipeline {
    agent {
        label 'multi-java'
    }

    options {
        githubProjectProperty(projectUrlStr: 'https://github.com/SirBlobman/BlueSlimeCore')
    }

    environment {
        DISCORD_URL = credentials('PUBLIC_DISCORD_WEBHOOK')
        MAVEN_DEPLOY = credentials('MAVEN_DEPLOY')
        HANGAR_API_KEY = credentials('HANGAR_API_KEY')
        JDK8 = '/opt/java/jdk1.8.0_503'
        JDK11 = '/opt/java/jdk-11.0.32'
        JDK16 = '/opt/java/jdk-16.0.2'
        JDK17 = '/opt/java/jdk-17.0.12'
        JDK21 = '/opt/java/jdk-21.0.12'
        JDK25 = '/opt/java/openjdk'
    }

    triggers {
        githubPush()
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
                        if (env.BRANCH_NAME == 'main') {
                            sh('./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build publish publishAllPublicationsToHangar')
                        } else if (env.BRANCH_NAME.contains('dev')) {
                            sh('./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build publish')
                        } else {
                            sh('./gradlew --no-daemon --no-configuration-cache --refresh-dependencies clean build')
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
                    def description = """
                        **Branch:** ${env.GIT_BRANCH}
                        **Build:** ${env.BUILD_NUMBER}
                        **Status:** ${currentBuild.currentResult}
                    """

                    discordSend(
                            webhookURL: DISCORD_URL,
                            title: 'BlueSlimeCore',
                            link: env.BUILD_URL,
                            result: currentBuild.currentResult,
                            description: description.stripIndent(),
                            enableArtifactsList: false,
                            showChangeset: true
                    )
                }
            }
        }
    }
}
