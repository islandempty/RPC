package com.zfoo.net.dispatcher.model.answer;

import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.net.task.model.SafeRunnable;
import com.zfoo.protocol.IPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public class AsyncAnswer<T extends IPacket> implements IAsyncAnswer<T> {

    private T futurePacket;
    private SignalPacketAttachment futureAttachment;

    private List<Consumer<T>> consumerList = new ArrayList<>(2);

    private Runnable askCallback;

    private SafeRunnable notCompleteCallback;


    @Override
    public IAsyncAnswer<T> thenAccept(Consumer<T> consumer) {
        consumerList.add(consumer);
        return this;
    }

    @Override
    public void whenComplete(Consumer<T> consumer) {
        thenAccept(consumer);
        askCallback.run();
    }

    @Override
    public IAsyncAnswer<T> notComplete(SafeRunnable notCompleteCallback) {
        this.notCompleteCallback = notCompleteCallback;
        return this;
    }

    public void consume() {
        consumerList.forEach(it -> it.accept(futurePacket));
    }

    public T getFuturePacket() {
        return futurePacket;
    }

    public void setFuturePacket(T futurePacket) {
        this.futurePacket = futurePacket;
    }

    public SignalPacketAttachment getFutureAttachment() {
        return futureAttachment;
    }

    public void setFutureAttachment(SignalPacketAttachment futureAttachment) {
        this.futureAttachment = futureAttachment;
    }

    public Runnable getAskCallback() {
        return askCallback;
    }

    public void setAskCallback(Runnable askCallback) {
        this.askCallback = askCallback;
    }

    public SafeRunnable getNotCompleteCallback() {
        return notCompleteCallback;
    }

}

