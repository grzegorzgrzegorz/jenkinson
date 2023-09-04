import com.passfailerror.Utils
import groovy.test.GroovyTestCase

class UtilsTest extends GroovyTestCase {

    void test_utilThrowsException_whenMapContainsIllegalValueType() {
        //WHEN
        def map = ['someKey': 'someValue1']
        //THEN
        def exception = shouldFail(IllegalArgumentException.class) { Utils.instance.mapContainsValue(map, "someValue1") }
        assert exception.contains("illegal value")
    }

    void test_utilReturnsTrue_whenMapContainsValue() {
        //WHEN
        def map = ['someKey': ['someValue1', 'someValue2']]
        //THEN
        assert Utils.instance.mapContainsValue(map, "someValue1")
    }

    void test_utilReturnsFalse_whenMapNotContainsValue() {
        //WHEN
        def map = ['someKey': ['someValue1', 'someValue2']]
        //THEN
        assert Utils.instance.mapContainsValue(map, "unexisting") == false
    }

    void test_utilReturnsTrue_whenListContainsValue() {
        //WHEN
        def list = ['someValue1', 'someValue2']
        //THEN
        assert Utils.instance.listContainsValue(list, "someValue1")
    }

    void test_utilReturnsFalse_whenListNotContainValue() {
        //WHEN
        def list = ['someValue1', 'someValue2']
        //THEN
        assert Utils.instance.listContainsValue(list, "unexisting") == false
    }

}
