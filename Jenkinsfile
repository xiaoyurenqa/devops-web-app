pipeline {
    agent any
    
    triggers {
        pollSCM('H */4 * * 1-5')
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                echo "Workspace path ${WORKSPACE}"
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
