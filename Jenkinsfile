node {
    stage 'Checkout'
    git url: 'https://github.com/reasonthearchitect/AD_FilterCarList.git'

    stage 'Build'
    sh "./gradlew clean build"
    //step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/TEST-*.xml'])

    stage 'BuildRunDocker'
    sh 'docker kill filtercarlist'
    sh 'docker rm filtercarlist'
    sh 'docker build -t reasonthearchitect/filtercarlist .'
    sh 'docker run -d --name filtercarlist -p 8220:8220 reasonthearchitect/filtercarlist'
}