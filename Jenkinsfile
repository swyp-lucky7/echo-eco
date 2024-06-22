pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('Check Branch') {
            steps {
                script {
                    if (env.BRANCH_NAME != 'master') {
                        echo "Skipping build for non-master branch: ${env.BRANCH_NAME}"
                        currentBuild.result = 'SUCCESS'
                        return // 빌드를 중단하고 나머지 스테이지를 건너뜁니다.
                    }
                }
            }
        }
        stage('Clone Repository') {
            steps {
                git 'https://github.com/your-username/your-repo.git'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean build' // 또는 mvn clean install
            }
        }
        // 다른 stage들 추가 가능
    }
}
