package com.ie.protocol.collection;

import com.ie.protocol.collection.model.NaturalComparator;
import com.ie.protocol.model.Pair;
import com.ie.protocol.util.AssertionUtils;

import java.util.*;

/**
 * @author islandempty
 * @since 2021/6/1
 **/
public class CollectionUtils {


    //判断对象是否为空
    public static boolean isEmpty(Collection<?> collection){
        return (collection==null || collection.isEmpty());
    }

    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    //获取集合长度
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }


    //获取迭代器对象
    public static <T> Iterator<T> iterator(Collection<T> collection) {
        return isEmpty(collection) ? Collections.emptyIterator() : collection.iterator();
    }

    public static <K, V> Iterator<Map.Entry<K, V>> iterator(Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyIterator() : map.entrySet().iterator();
    }


    /*
    * 固定集合大小，如果初始化为0，则后续无法继续增加集合容量
    * */

    public static List<?> newFixedList(int size) {
        return size <= 0 ? Collections.EMPTY_LIST : new ArrayList<>(size);
    }

    public static Set<?> newFixedSet(int size) {
        return size <= 0 ? Collections.EMPTY_SET : new HashSet<>(comfortableCapacity(size));
    }

    public static Map<?, ?> newFixedMap(int size) {
        return size <= 0 ? Collections.EMPTY_MAP : new HashMap<>(comfortableCapacity(size));
    }

    //集合的最大容量2^30
    public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    /**
     * 计算HashMap初始化合适的大小
     * from com.google.common.collect.Maps.capacity()
     */
    public static int comfortableCapacity(int expectedSize) {
        if (expectedSize < 3) {
            return expectedSize + 1;
        }

        if (expectedSize < MAX_POWER_OF_TWO) {
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }

        // any large value
        return Integer.MAX_VALUE;
    }

    //归并排序
    /**
     * Merges two sorted Collections, a and b, into a single, sorted List
     * such that the natural ordering of the elements is retained.
     * <p>
     * Uses the standard O(n) merge algorithm for combining two sorted lists.
     * </p>
     *
     * @param aList the first collection, must not be null
     * @param bList the second collection, must not be null
     * @return a new sorted List, containing the elements of Collection a and b
     */
    public static <T extends Comparable<? super T>> List<T> collate(List<? extends T> aList, List<? extends T> bList) {
        return collate(aList, bList, NaturalComparator.getInstance(), true);
    }

    public static <T extends Comparable<? super T>> List<T> collate(List<? extends T> aList, List<? extends T> bList, boolean includeDuplicates) {
        return collate(aList, bList, NaturalComparator.getInstance(), includeDuplicates);
    }

    public static <T> List<T> collate(List<T> aList, List<T> bList, Comparator<T> comparator) {
        return collate(aList, bList, comparator, true);
    }

    /**
     * Merges two sorted Collections, a and b, into a single, sorted List
     * such that the ordering of the elements according to Comparator c is retained.
     * <p>
     * Uses the standard O(n) merge algorithm for combining two sorted lists.
     * </p>
     *
     * @param <T>               the element type
     * @param aList             the first collection, must not be null
     * @param bList             the second collection, must not be null
     * @param comparator        the comparator to use for the merge.
     * @param includeDuplicates if {@code true} duplicate elements will be retained, otherwise
     *                          they will be removed in the output collection
     * @return a new sorted List, containing the elements of Collection a and b
     */
    public static <T> List<T> collate(List<? extends T> aList, List<? extends T> bList, Comparator<? super T> comparator, boolean includeDuplicates) {

        if (aList == null || bList == null) {
            throw new NullPointerException("The collections must not be null");
        }
        if (comparator == null) {
            throw new NullPointerException("The comparator must not be null");
        }

        var totalSize = aList.size() + bList.size();

        var mergedList = new ArrayList<T>(totalSize);

        var aIndex = 0;
        var bIndex = 0;

        T lastItem = null;
        while (aIndex < aList.size() && bIndex < bList.size()) {
            var a = aList.get(aIndex);
            var b = bList.get(bIndex);
            if (a == null) {
                aIndex++;
                continue;
            }
            if (b == null) {
                bIndex++;
                continue;
            }

            if (comparator.compare(a, b) >= 0) {
                bIndex++;
                if (!includeDuplicates && lastItem != null && lastItem.equals(b)) {
                    continue;
                }
                mergedList.add(b);
                lastItem = b;
            } else {
                aIndex++;
                if (!includeDuplicates && lastItem != null && lastItem.equals(a)) {
                    continue;
                }
                mergedList.add(a);
                lastItem = a;
            }

        }

        if (aIndex < aList.size()) {
            for (var i = aIndex; i < aList.size(); i++) {
                var value = aList.get(i);

                if (!includeDuplicates && lastItem != null && lastItem.equals(value)) {
                    continue;
                }

                mergedList.add(value);
                lastItem = value;
            }
        }

        if (bIndex < bList.size()) {
            for (var i = bIndex; i < bList.size(); i++) {
                var value = bList.get(i);

                if (!includeDuplicates && lastItem != null && lastItem.equals(value)) {
                    continue;
                }

                mergedList.add(value);
                lastItem = value;
            }
        }

        mergedList.trimToSize();
        return mergedList;
    }

    /**
     * list合并
     *
     * @param exclusive 元素是否是独占的，也就是说是否可以重复
     * @param pairs     需要被合并的pairs集合，第一个参数是步数，第二个参数是集合
     * @return 返回合并后的list
     */
    public static <T> List<T> listJoinList(boolean exclusive, Pair<Integer, List<T>>... pairs) {
        return listJoinList(exclusive, List.of(pairs));
    }

    public static <T> List<T> listJoinList(boolean exclusive, List<Pair<Integer, List<T>>> pairs) {
        var iteratorList = new ArrayList<List<T>>();
        var iteratorMap = new HashMap<List<T>, Iterator<T>>();
        var stepMap = new HashMap<List<T>, Integer>();
        for (var pair : pairs) {
            var step = pair.getKey();
            var list = pair.getValue();
            AssertionUtils.ge1(step);
            if (isNotEmpty(list)) {
                var iterator = list.iterator();
                iteratorList.add(list);
                iteratorMap.put(list, iterator);
                stepMap.put(list, step);
            }
        }

        var result = new ArrayList<T>();

        while (iteratorMap.values().stream().anyMatch(it -> it.hasNext())) {
            for (var list : iteratorList) {
                var iterator = iteratorMap.get(list);
                var step = stepMap.get(list);
                for (var i = 0; i < step && iterator.hasNext(); i++) {
                    var element = iterator.next();
                    if (exclusive && result.contains(element)) {
                        i--;
                        continue;
                    }
                    result.add(element);
                }
            }
        }

        return result;
    }

    /**
     * 获取集合的最后几个元素
     */
    public static <T> List<T> subListLast(List<T> list, int num) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }

        var startIndex = list.size() - num;
        if (startIndex <= 0) {
            return new ArrayList<>(list);
        }

        var result = new ArrayList<T>();


        for (T element : list) {
            startIndex--;
            if (startIndex < 0) {
                result.add(element);
            }
        }

        return result;
    }
}

