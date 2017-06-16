public class Fluent {

    private Fluent() {}

    public interface Map<K, V> extends java.util.Map<K, V> {

        default Optional<V> maybe(K key) {
            return Optional.ofNullable(get(key));
        }

        default Map<K, V> append(K key, V val) {
            put(key, val);
            return this;
        }

        default Map<K, V> appendAll(java.util.Map<? extends K, ? extends V> map) {
            putAll(map);
            return this;
        }

        default Map<K, V> append(Map.Entry<? extends K, ? extends V> entry) {
            return append(entry.getKey(), entry.getValue());
        }
    }

    public static class HashMap<K, V> extends java.util.HashMap<K, V> implements Fluent.Map<K, V> {
        public HashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public HashMap(int initialCapacity) {
            super(initialCapacity);
        }

        public HashMap(java.util.Map m) {
            super(m);
        }

        public HashMap() {
        }
    }

    public static class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements Fluent.Map<K, V> {
        public LinkedHashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public LinkedHashMap(int initialCapacity) {
            super(initialCapacity);
        }

        public LinkedHashMap(java.util.Map m) {
            super(m);
        }

        public LinkedHashMap() {}
    }
}
