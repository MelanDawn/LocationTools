package com.zs.wcn;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        long a = 0x0000_0000_0000_0000L;
        long b = 0x1000_0000_0000_0000L;
        long c = 0x2000_0000_0000_0000L;
        long d = 0x3000_0000_0000_0000L;
        long e = 0x4000_0000_0000_0000L;
        long f = 0x5000_0000_0000_0000L;
        long g = 0x6000_0000_0000_0000L;
        long h = 0x7000_0000_0000_0000L;

        long i = 0x8000_0000_0000_0000L;
        long j = 0x9000_0000_0000_0000L;
        long k = 0xA000_0000_0000_0000L;
        long l = 0xB000_0000_0000_0000L;

        long m = 0xC000_0000_0000_0000L;
        long n = 0xD000_0000_0000_0000L;
        long o = 0xE000_0000_0000_0000L;
        long p = 0xF000_0000_0000_0000L;
        System.out.println("a = " + variant(a));
        System.out.println("b = " + variant(b));
        System.out.println("c = " + variant(c));
        System.out.println("d = " + variant(d));
        System.out.println("e = " + variant(e));
        System.out.println("f = " + variant(f));
        System.out.println("g = " + variant(g));
        System.out.println("h = " + variant(h));
        System.out.println("i = " + variant(i));
        System.out.println("j = " + variant(j));
        System.out.println("k = " + variant(k));
        System.out.println("l = " + variant(l));
        System.out.println("m = " + variant(m));
        System.out.println("n = " + variant(n));
        System.out.println("o = " + variant(o));
        System.out.println("p = " + variant(p));
    }

    public int variant(long leastSigBits) {
        // This field is composed of a varying number of bits.
        // 0    -    -    Reserved for NCS backward compatibility
        // 1    0    -    The IETF aka Leach-Salz variant (used by this class)
        // 1    1    0    Reserved, Microsoft backward compatibility
        // 1    1    1    Reserved for future definition.
        return (int) ((leastSigBits >>> (64 - (leastSigBits >>> 62)))
                & (leastSigBits >> 63));
    }
}