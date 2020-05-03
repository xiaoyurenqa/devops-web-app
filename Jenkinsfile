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
                       adapters: [tomcat9(credentialsId: '5ade1677-464d-4c4e-90cd-db48ed114957', 
                                  path: '', 
                                  url: 'http://3.22.185.164:8080/')], 
                       contextPath: null, 
                       war: 'target/*.war' 
            }
        }
        stage('FunctionalTest') {
            steps {
                echo 'Launch functional test....'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
