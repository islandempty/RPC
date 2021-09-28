/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.protocol.generate;

import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.collection.tree.GeneralTree;
import com.zfoo.protocol.collection.tree.TreeNode;
import com.zfoo.protocol.registration.IProtocolRegistration;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成协议的时候，协议的最终生成路径会使用这个类
 *
 /**
 * @author islandempty
 */
public abstract class GenerateProtocolPath {

    // 临时变量，启动完成就会销毁，协议生成的路径
    private static Map<Short, String> tempProtocolPathMap = new HashMap<>();


    public static void clear() {
        tempProtocolPathMap.clear();
        tempProtocolPathMap = null;
    }

    /**
     * 获取协议生成的路径
     */
    public static String getProtocolPath(short protocolId) {
        AssertionUtils.notNull(tempProtocolPathMap, "[{}]已经初始完成，初始化完成过后不能调用getProtocolPath", GenerateProtocolPath.class.getSimpleName());

        var protocolPath = tempProtocolPathMap.get(protocolId);
        if (StringUtils.isBlank(protocolPath)) {
            return StringUtils.EMPTY;
        }

        return protocolPath.replaceAll(StringUtils.PERIOD_REGEX, StringUtils.SLASH);
    }

    /**
     * 获取协议生成的首字母大写的路径
     */
    public static String getCapitalizeProtocolPath(short protocolId) {
        return StringUtils.joinWith(StringUtils.SLASH, Arrays.stream(getProtocolPath(protocolId).split(StringUtils.SLASH)).map(it -> StringUtils.capitalize(it)).toArray());
    }

    /**
     * 解析协议的路径
     *
     * @param protocolRegistrations 需要解析的路径
     */
    public static void initProtocolPath(List<IProtocolRegistration> protocolRegistrations) {
        AssertionUtils.notNull(tempProtocolPathMap, "[{}]已经初始完成，初始化完成过后不能调用initProtocolPath", GenerateProtocolPath.class.getSimpleName());

        // 将需要生成的协议的路径添加到多叉树中
        var protocolPathTree = new GeneralTree<IProtocolRegistration>();
        protocolRegistrations.forEach(it -> protocolPathTree.addNode(it.protocolConstructor().getDeclaringClass().getCanonicalName(), it));

        var rootTreeNode = protocolPathTree.getRootNode();

        if (CollectionUtils.isEmpty(rootTreeNode.getChildren())) {
            return;
        }

        var queue = new LinkedList<>(rootTreeNode.getChildren());
        while (!queue.isEmpty()) {
            var childTreeNode = queue.poll();
            var childChildren = childTreeNode.getChildren();
            // 如果子节点为空，则以当前节点为路径
            if (CollectionUtils.isEmpty(childChildren)) {
                toProtocolPath(childTreeNode);
                continue;
            }

            // 如果子节点的协议数据有一个不为空的，则以当前节点为路径
            if (childChildren.stream().anyMatch(it -> it.getData() != null)) {
                toProtocolPath(childTreeNode);
                continue;
            }

            // 继续深度便利子节点的路径
            for (var subClassId : childTreeNode.getChildren()) {
                queue.offer(subClassId);
            }
        }
    }

    private static void toProtocolPath(TreeNode<IProtocolRegistration> protocolTreeNode) {
        var allChildren = protocolTreeNode.flatTreeNodes()
                .stream()
                .filter(it -> it.getData() != null)
                .collect(Collectors.toList());
        var pathBefore = StringUtils.substringBeforeLast(protocolTreeNode.fullName(), StringUtils.PERIOD);
        for (var child : allChildren) {
            var protocolSimpleName = child.getData().protocolConstructor().getDeclaringClass().getSimpleName();
            var splits = Arrays.stream(StringUtils.substringBeforeLast(StringUtils.substringAfterFirst(child.fullName(), pathBefore), protocolSimpleName)
                    .split(StringUtils.PERIOD_REGEX))
                    .filter(it -> !StringUtils.isBlank(it))
                    .toArray();
            tempProtocolPathMap.put(child.getData().protocolId(), StringUtils.joinWith(StringUtils.PERIOD, splits));
        }
    }

}
