pipeline {
    agent {
        label 'webserver'
    }
    stages {
        stage('pull') {
            steps {
                echo 'this is pull stage'
                git 'https://github.com/AnupDudhe/studentapp-ui.git'
            }
        }
        stage('build') {
            steps {
                echo 'this is pull stage'
               sh '''mvn clean package
                sudo mv /home/ubuntu/workspace/webserver/target/studentapp-2.2-SNAPSHOT.war /home/ubuntu/workspace/webserver/target/student.war
                sudo aws s3 cp /home/ubuntu/workspace/webserver/target/student.war s3://cbzappvarad'''
            }
        }
        stage('test') {
            steps {
                echo 'this is pull stage'
               sh '''mvn clean verify sonar:sonar \\
                  -Dsonar.projectKey=studentapp \\
                  -Dsonar.host.url=http://13.61.35.190:9000 \\
                  -Dsonar.login=sqp_7927590b10914bc04cc8fe857200f4b292596d99'''
            }
        }
            stage('deploy') {
            steps {
                echo 'this is pull stage'
                sh '''
                sudo curl -L -o /home/ubuntu/apache-tomcat-9.0.116.zip https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.116/bin/apache-tomcat-9.0.116.zip
                sudo unzip /home/ubuntu/apache-tomcat-9.0.116.zip -d /opt/
                sudo aws s3 cp  s3://cbzappvarad/student.war  /opt/apache-tomcat-9.0.116/webapps/
                
                sudo bash /opt/apache-tomcat-9.0.116/bin/catalina.sh start'''
            }
        }
    }
}
