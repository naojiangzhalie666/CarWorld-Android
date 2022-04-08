package com.liansheng.carworld.kit;

import cn.droidlover.xdroid.event.IBus;


public class Event {

    /**
     * 测试EventBus用到的实体
     */
    public static class TestEvent implements IBus.IEvent {

        public String name;

        public TestEvent(String name) {
            this.name = name;
        }

        @Override
        public int getTag() {
            return 10;
        }
    }

}
