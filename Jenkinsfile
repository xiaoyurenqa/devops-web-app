pipeline {
    agent any
    
    triggers {
        pollSCM('H */4 * * 1-5')
    }

    environment {
        BUILD_PATH = env.WORKSPACE 
    }

    parameters {

    }
     
    stages {
        stage('MavenInstall') {
            steps {
                echo 'Generate WAR file...'
                echo "${BUILD_PATH}"
                sh "pwd"
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
