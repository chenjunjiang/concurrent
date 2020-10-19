package com.chenjj.concurrent.atomic;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class VMSupportLongCAS {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field vm_supports_long_cas = AtomicLong.class.getDeclaredField("VM_SUPPORTS_LONG_CAS");
        vm_supports_long_cas.setAccessible(true);
        boolean isSupport = (boolean) vm_supports_long_cas.get(null);
        System.out.println(isSupport);
    }
}
