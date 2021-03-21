pipeline {
    agent any
    
    triggers {
    	pollSCM('* * * * *')
    }

    stages {
        stage('Build') {
            steps {
                git url: 'https://github.com/hoaftq/Lines-98.git', branch: 'main'

                withAnt(installation: 'localAnt') {
                    script {
                        if (isUnix()) {
                            sh 'ant clean package'
                        } else {
                            bat 'ant clean package'   
                        }
                    }
                }
            }

            post {
                success {
                    archiveArtifacts 'build/*.jar'
                }
            }
        }
    }
}
