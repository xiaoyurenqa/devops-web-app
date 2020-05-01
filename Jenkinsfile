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
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
