package com.zfoo.protocol.serializer;

import com.zfoo.protocol.buffer.ByteBufUtils;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.registration.field.ListField;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class ListSerializer implements ISerializer{
    private static final ListSerializer SERIALIZER = new ListSerializer();

    public ListSerializer() {
    }
    public static ListSerializer getInstance(){
        return SERIALIZER;
    }
    @Override
    public void writeObject(ByteBuf buffer, Object object, IFieldRegistration fieldRegistration) {
        if (object == null){
            ByteBufUtils.writeInt(buffer , 0);
            return;
        }
        List<?> list = (List<?>) object;
        ListField listField = (ListField) fieldRegistration;

        int size = list.size();
        if (size == 0){
            ByteBufUtils.writeInt(buffer,0);
            return;
        }
        ByteBufUtils.writeInt(buffer,size);
        for (Object element : list) {
            listField.getListElementRegistration().serializer().writeObject(buffer,element,listField.getListElementRegistration());
        }
    }

    @Override
    public Object readObject(ByteBuf buffer, IFieldRegistration fieldRegistration) {
        int size = ByteBufUtils.readInt(buffer);
        if (size<=0){
            return Collections.EMPTY_LIST;
        }
        ListField listField = (ListField) fieldRegistration;
        List<Object> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Object object = listField.getListElementRegistration().serializer().readObject(buffer, listField.getListElementRegistration());
            list.add(object);
        }
        return list;
    }
}

