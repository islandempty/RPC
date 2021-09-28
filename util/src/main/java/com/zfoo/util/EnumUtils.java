package com.zfoo.util;

import com.zfoo.protocol.util.AssertionUtils;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author islandempty
 * @since 2021/7/17
 **/
public abstract class EnumUtils {

    public static <E extends Enum<E>> boolean isInEnums(String targetEnumName,E[] sourceEnums){
        AssertionUtils.notNull(targetEnumName, sourceEnums);
        return Arrays.stream(sourceEnums).anyMatch(sourceEnum -> sourceEnum.name().equals(targetEnumName));
    }

    public static <E> Set<E> enumerationToSet(Enumeration<E> eEnumeration){
        var set = new HashSet<E>();
        if (eEnumeration !=null){
            while (eEnumeration.hasMoreElements()){
                set.add(eEnumeration.nextElement());
            }
        }
        return set;
    }

}

