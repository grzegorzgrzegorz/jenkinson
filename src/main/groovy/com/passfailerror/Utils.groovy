package com.passfailerror

@Singleton
class Utils {

    boolean mapContainsValue(LinkedHashMap<String, List<String>> map, String valueParam) {
        validateMap(map)
        return map.entrySet().stream().filter(entry -> listContainsValue(entry.value, valueParam)).findAny().isPresent()
    }

    boolean listContainsValue(List<String> list, String value) {
        return list.stream().filter(item -> item.contains(value)).findAny().isPresent()
    }

    void validateMap(Map map) {
        if (map.entrySet().stream().filter(entry -> !(entry.value instanceof List)).findAny().isPresent()) {
            throw new IllegalArgumentException("This map is has illegal value: " + map.toString())
        }
    }
}