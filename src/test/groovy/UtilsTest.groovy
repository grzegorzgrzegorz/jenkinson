import com.passfailerror.Utils
import groovy.test.GroovyTestCase

class UtilsTest extends GroovyTestCase {

    void "test: util throws exception when Map contains illegal value type"() {
        when:
        def map = ['someKey': 'someValue1']
        then:
        def exception = shouldFail(IllegalArgumentException.class) { Utils.instance.mapContainsValue(map, "someValue1") }
        assert exception.contains("illegal value")
    }

    void "test: util returns True when Map contains value"() {
        when:
        def map = ['someKey': ['someValue1', 'someValue2']]
        then:
        assert Utils.instance.mapContainsValue(map, "someValue1")
    }

    void "test: util returns False when Map not contains value"() {
        when:
        def map = ['someKey': ['someValue1', 'someValue2']]
        then:
        assert Utils.instance.mapContainsValue(map, "unexisting") == false
    }

    void "test: util returns True when List contains value"() {
        when:
        def list = ['someValue1', 'someValue2']
        then:
        assert Utils.instance.listContainsValue(list, "someValue1")
    }

    void "test: util returns False when List not contain value"() {
        when:
        def list = ['someValue1', 'someValue2']
        then:
        assert Utils.instance.listContainsValue(list, "unexisting") == false
    }

}
