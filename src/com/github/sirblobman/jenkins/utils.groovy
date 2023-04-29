package src.com.github.sirblobman.jenkins
// Copied from https://shenxianpeng.github.io/2022/10/jenkins-skip-ci/

def SkipCI(number = "all") {
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
