pipeline {
    agent any
    parameters {
        choice(name: 'ENV_PROFILE', choices: ['dev', 'staging', 'prod'], description: 'config/*.properties profile')
        string(name: 'SUITE', defaultValue: 'src/test/resources/suites/smoke.xml', description: 'TestNG suite path')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Local/Grid browser')
        string(name: 'GRID_URL', defaultValue: '', description: 'Optional Selenium Grid URL (or set SELENIUM_GRID_URL on the agent)')
    }
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        SELENIUM_GRID_URL = "${params.GRID_URL}"
    }
    stages {
        stage('UI tests') {
            steps {
                sh """
                    mvn -B clean test \\
                      -Denv=${params.ENV_PROFILE} \\
                      -Dbrowser=${params.BROWSER} \\
                      -Dheadless=true \\
                      -DsuiteXmlFile=${params.SUITE}
                """
            }
            post {
                always {
                    publishHTML([
                        reportDir: 'target/extent-report',
                        reportFiles: 'index.html',
                        reportName: 'Extent Report',
                        keepAll: true,
                        allowMissing: true
                    ])
                    archiveArtifacts artifacts: 'screenshots/**/*.png', allowEmptyArchive: true
                }
            }
        }
    }
}
