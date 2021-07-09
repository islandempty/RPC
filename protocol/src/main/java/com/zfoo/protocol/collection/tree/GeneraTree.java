package com.zfoo.protocol.collection.tree;

import com.zfoo.protocol.util.StringUtils;

/**
 * 多叉树
 *
 * @author islandempty
 * @since 2021/7/8
 **/
public class GeneraTree<T> {

    private TreeNode<T> rootNode = new TreeNode<>(null,null);

    public TreeNode<T> getRootNode(){
        return rootNode;
    }

    public TreeNode<T> getNodeByPath(String path){
        var current = rootNode;
        var splitPath = splitPath(path);
        for (var nodeName : splitPath){
            current = current.childByName(nodeName);
            if (current == null){
                return null;
            }
        }
        return current;
    }

    public void addNode(String path , T data){
        var current = rootNode;

        var splitPath = splitPath(path);

        for (var nodeName : splitPath){
            current = current.getOrAddChild(nodeName);
        }
        current.setData(data);
    }


    /**
     * 移除所有数据结点
     */
    public void clear() {
        rootNode.clear();
    }

    private String[] splitPath(String path){
        if (StringUtils.isBlank(path)){
            return StringUtils.EMPTY_ARRAY;
        }

        if (!path.contains(StringUtils.PERIOD)){
            return new String[]{path};
        }

        //“.”和“|”都是转义字符，必须得加"\\"
        return path.split(StringUtils.PERIOD_REGEX);
    }
}

