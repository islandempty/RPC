package com.ie.util.math;

import com.zfoo.protocol.model.Pair;

import java.util.*;

/**
 *
 *  带虚拟节点的一致性Hash算法，参考：http://www.zsythink.net/archives/1182
 *
 * @author islandempty
 * @since 2021/7/21
 **/
public class ConsistentHash<K,V> {

    // 真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
    private List<Pair<K,V>> realNodes = new LinkedList<>();

    // 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
    private TreeMap<Integer, Pair<K,V>> virtualNodeTreeMap = new TreeMap<>();

    //虚拟节点的数目，数量越大约均匀，经验值150
    private int virtualNodes = 0;

    public ConsistentHash(List<Pair<K,V>> realNodes,int virtualNodes){
        //先把原始的服务器添加到真是节点列表中
        this.realNodes.addAll(realNodes);
        this.virtualNodes = virtualNodes;

        //初始化
        //再添加虚拟节点，遍历LinkedList使用foreach循环效率会比较高
        for (var realNode : realNodes) {
            addNode(realNode);
        }
    }

    public void addNode(Pair<K,V> realNode){
        for (var i=0;i<this.virtualNodes;i++){
            var virtualNode = realNode.getKey().toString()+"&&VN" +i;
            var hash = HashUtils.fnvHash(virtualNode);
            virtualNodeTreeMap.put(hash, realNode);
        }
    }


    //得到应当路由到的节点
    public Pair<K,V> getRealNode(Object key){
        //得到该key的hash值
        var hash = HashUtils.fnvHash(key);
        //第一个Key就是顺时针过去离node最近的那个结点
        var entry = virtualNodeTreeMap.ceilingEntry(hash);
        if (Objects.isNull(entry)){
            // 如果没有比该key的hash值大的，则从第一个node开始
            return virtualNodeTreeMap.firstEntry().getValue();
        }
        return entry.getValue();
    }
}

