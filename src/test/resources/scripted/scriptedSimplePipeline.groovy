package scripted

node('master'){
    stage('First stage') {
        echo 'First'
        sh 'mvn --version'
        env.TEST_GLOBAL_VAR='global_value'
    }
    stage('Second stage') {
        echo 'Second'
        sh 'java --version"'
        env.SECOND_STAGE_VAR='value'
    }
}