pipeline {
    agent any
    
    triggers {
        pollSCM('H */4 * * 1-5')
    }

    environment {
        BUILD_PATH = "${env.WORKSPACE}" 
    }
    
    stages {
        stage('MavenInstall') {
            steps {
                echo 'Generate WAR file...'
                echo "${BUILD_PATH}"
                sh "cd ${BUILD_PATH}"
                sh "mvn clean compile test install"

                jacoco( 
	            execPattern: 'target/*.exec',
                    classPattern: 'target/classes',
                    sourcePattern: 'src/main/java',
                    exclusionPattern: 'src/test*'
                )

            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy application to test environment..'
                deploy(
                       adapters: [tomcat9(credentialsId: '2e17c056-e8e0-4765-b563-23fad1681939', 
                                  path: '', 
                                  url: 'http://3.22.185.164:8080/')], 
                       contextPath: '/web-application', 
                       war: 'target/*.war'
                ) 
            }
        }
        stage('FunctionalTest') {
            steps {
                echo 'Launch functional test....'
                withPythonEnv('/home/ubuntu/selenium-test/'){
                   sh 'pytest --alluredir target/surefire-reports --driver chrome -vvvv functional_tests/web-test.py'
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/surefire-reports']]
         ])
            emailext(
                attachLog: true, 
                body: '''<p>Hi,</p>
                      <p>The test $PROJECT_NAME is finished with status of $BUILD_STATUS</p>
                      <p>
                      $PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:
                      Check console output at <a href="$BUILD_URL">$BUILD_URL</a> to view the results.</p>
                      <p>
                      For a comprehensive report, please see here <a href="$BUILD_URL/allure">$BUILD_URL/allure</a>.
                      </p>''', 
                subject: 'Test Report: $PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', 
                to: 'rxyjp2008@hotmail.com'
            )
        }
    }
}
