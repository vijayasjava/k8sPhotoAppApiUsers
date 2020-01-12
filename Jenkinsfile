node {

  try {
    def mvnHome
    def pom
    def artifactVersion
    def tagVersion
    def retrieveArtifact

    stage('Prepare') {
      mvnHome = tool 'MAVENHOME'
    }

    stage('Checkout') {
       checkout scm
    }

    stage('Build') {
       if (isUnix()) {
          sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
       } else {
          bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
       }
    }

    stage('Unit Test') {
       junit '**/target/surefire-reports/TEST-*.xml'
       archive 'target/*.jar'
    }

    stage('Integration Test') {
      if (isUnix()) {
         sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
      } else {
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean verify/)
      }
    }

 

    if(env.BRANCH_NAME == 'master'){
      stage('Validate Build Post Prod Release') {
        if (isUnix()) {
           sh "'${mvnHome}/bin/mvn' clean package"
        } else {
           bat(/"${mvnHome}\bin\mvn" clean package/)
        }
      }

    }

      if(env.BRANCH_NAME == 'develop'){
        stage('Snapshot Build And Upload Artifacts') {
          if (isUnix()) {
             sh "'${mvnHome}/bin/mvn' clean deploy"
          } else {
             bat(/"${mvnHome}\bin\mvn" clean deploy/)
          }
        }

        stage('Deploy') {
           sh 'curl -u deployer:deployer -T target/**.war "http://localhost:4040/manager/text/deploy?path=/usermanagement&update=true"'
        }

        stage("Smoke Test"){
           sh "curl --retry-delay 10 --retry 5 http://localhost:4040/usermanagement/users/status/check"
        }

      }

      if(env.BRANCH_NAME ==~ /feature.*/){
        stage('Deploy') {
           sh 'curl -u deployer:deployer -T target/**.war "http://localhost:4040/manager/text/deploy?path=/usermanagement&update=true"'
        }

        stage("Smoke Test"){
           sh "curl --retry-delay 10 --retry 5 http://localhost:4040/usermanagement/users/status/check"
        }

      }

     if(env.BRANCH_NAME ==~ /hotfix.*/){
        pom = readMavenPom file: 'pom.xml'
        artifactVersion = pom.version.replace("-SNAPSHOT", "")
        tagVersion = 'v'+artifactVersion

        stage('Release Build And Upload Artifacts') {
          if (isUnix()) {
             sh "'${mvnHome}/bin/mvn' clean release:clean release:prepare release:perform"
          } else {
             bat(/"${mvnHome}\bin\mvn" clean release:clean release:prepare release:perform/)
          }
        }
         stage('Deploy To Dev') {
            sh 'curl -u deployer:deployer -T target/**.war "http://localhost:4040/manager/text/deploy?path=/usermanagement&update=true"'
         }

         stage("Smoke Test Dev"){
             sh "curl --retry-delay 10 --retry 5 http://localhost:4040/usermanagement/users/status/check"
         }

         stage("QA Approval"){
             echo "Job '${env.JOB_NAME}' (${env.BUILD_NUMBER}) is waiting for input. Please go to ${env.BUILD_URL}."
             input 'Approval for QA Deploy?';
         }

         stage("Deploy from Artifactory to QA"){
           retrieveArtifact = 'http://localhost:7071/artifactory/libs-release-local/com/mars/photoapp/k8sPhotoAppApiUsers/' + artifactVersion + '/k8sPhotoAppApiUsers-' + artifactVersion + '.war'
           echo "${tagVersion} with artifact version ${artifactVersion}"
           echo "Deploying war from http://localhost:7071/artifactory/libs-release-local/com/mars/photoapp/k8sPhotoAppApiUsers/${artifactVersion}/k8sPhotoAppApiUsers-${artifactVersion}.war"
           sh 'curl -u admin:password -O ' + retrieveArtifact
           sh 'curl -u deployer:deployer -T *.war "http://localhost:2020/manager/text/deploy?path=/usermanagement&update=true"'
         }

         stage("Smoke Test Dev"){
             sh "curl --retry-delay 10 --retry 5 http://localhost:2020/usermanagement/users/status/check"
         }

      }


      if(env.BRANCH_NAME ==~ /release.*/){
        pom = readMavenPom file: 'pom.xml'
        artifactVersion = pom.version.replace("-SNAPSHOT", "")
        tagVersion = 'v'+artifactVersion

        stage('Release Build And Upload Artifacts') {
          if (isUnix()) {
             sh "'${mvnHome}/bin/mvn' clean release:clean release:prepare release:perform"
          } else {
             bat(/"${mvnHome}\bin\mvn" clean release:clean release:prepare release:perform/)
          }
        }
         stage('Deploy To Dev') {
            sh 'curl -u deployer:deployer -T target/**.war "http://localhost:4040/manager/text/deploy?path=/usermanagement&update=true"'
         }

         stage("Smoke Test Dev"){
             sh "curl --retry-delay 10 --retry 5 http://localhost:4040/usermanagement/users/status/check"
         }

         stage("QA Approval"){
             echo "Job '${env.JOB_NAME}' (${env.BUILD_NUMBER}) is waiting for input. Please go to ${env.BUILD_URL}."
             input 'Approval for QA Deploy?';
         }

         stage("Deploy from Artifactory to QA"){
           retrieveArtifact = 'http://localhost:7071/artifactory/libs-release-local/com/mars/photoapp/k8sPhotoAppApiUsers/' + artifactVersion + '/k8sPhotoAppApiUsers-' + artifactVersion + '.war'
           echo "${tagVersion} with artifact version ${artifactVersion}"
           echo "Deploying war from http://localhost:7071/artifactory/libs-release-local/com/mars/photoapp/k8sPhotoAppApiUsers/${artifactVersion}/k8sPhotoAppApiUsers-${artifactVersion}.war"
           sh 'curl -u admin:password -O ' + retrieveArtifact
           sh 'curl -u deployer:deployer -T *.war "http://localhost:2020/manager/text/deploy?path=/usermanagement&update=true"'
         }

         stage("Smoke Test Dev"){
             sh "curl --retry-delay 10 --retry 5 http://localhost:2020/usermanagement/users/status/check"
         }

      }
  } catch (exception) {
      committerEmail = sh (script: 'git --no-pager show -s --format=\'%ae\'', returnStdout: true).trim()
      emailext(body: '${DEFAULT_CONTENT}', mimeType: 'text/html', replyTo: '$DEFAULT_REPLYTO', subject: '${DEFAULT_SUBJECT}', to: committerEmail)
      throw exception
  }

}
