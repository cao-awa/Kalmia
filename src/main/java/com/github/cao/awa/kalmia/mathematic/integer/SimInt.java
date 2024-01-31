package com.github.cao.awa.kalmia.mathematic.integer;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.Receptacle;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimInt {
    private static final int DEFAULT_SIZE = 256;
    private static final BigInteger BIT_EXP_BASE = BigInteger.TWO;
    private final int size;
    private final boolean[] bits;

    public SimInt(int size, long init) {
        this.size = size;
        this.bits = new boolean[size];
        char[] charArray = Long.toBinaryString(init)
                               .toCharArray();
        int index = 0;
        for (int i = charArray.length - 1; i > - 1; i--) {
            char c = charArray[i];
            set(
                    index,
                    c == '1'
            );
            index++;
        }
    }

    public SimInt(String init, int radix) {
        BigInteger integer = new BigInteger(init,
                                            radix
        );
        int size = integer.toString(2)
                          .length();
        this.size = size;
        this.bits = new boolean[size];
        char[] charArray = integer.toString(2)
                                  .toCharArray();
        int index = 0;
        for (int i = charArray.length - 1; i > - 1; i--) {
            char c = charArray[i];
            set(
                    index,
                    c == '1'
            );
            index++;
        }
    }

    public int binLength() {
        return this.bits.length;
    }

    public void set(int index, boolean value) {
        if (index < this.bits.length) {
            this.bits[index] = value;
        }
    }

    public SimInt(SimInt integer) {
        this.size = integer.size;
        this.bits = integer.bits.clone();
    }

    public SimInt(int size, SimInt integer) {
        this.size = size;
        this.bits = new boolean[size];
        System.arraycopy(
                integer.bits,
                0,
                this.bits,
                0,
                this.bits.length
        );
    }

    public SimInt(int size, boolean fill) {
        this.size = size;
        this.bits = new boolean[size];
        this.fill(
                0,
                size,
                fill
        );
    }

    public SimInt fill(int from, int to, boolean fill) {
        Arrays.fill(
                this.bits,
                from,
                Math.min(
                        to,
                        this.size
                ),
                fill
        );
        return this;
    }

    public String integer() {
        BigInteger integer = BigInteger.valueOf(0);
        for (int i = 0; i < this.bits.length; i++) {
            if (get(i)) {
                integer = integer.add(BIT_EXP_BASE.pow(i));
            }
        }
        return integer.toString();
    }

    public boolean get(int index) {
        return index < this.bits.length && this.bits[index];
    }

    public static SimInt of(int size, long integer) {
        return new SimInt(
                size,
                integer
        );
    }

    public static SimInt of(int size, boolean fill) {
        return new SimInt(
                size,
                fill
        );
    }

    public int one(boolean target) {
        int result = - 1;
        for (int i = 0; i < bits.length; i++) {
            boolean bit = bits[i];
            if (bit == target) {
                if (result == - 1) {
                    result = i;
                } else {
                    return - 1;
                }
            }
        }
        return result;
    }

    public SimInt mut(long integer) {
        return mut(SimInt.of(integer));
    }

    public SimInt mut(SimInt mutInteger) {
        Receptacle<Integer> rs = Receptacle.of(- 1);
        SimInt result = SimInt.of(
                this.size,
                0
        );
        mutInteger.bitwise(bit -> {
            result.add(
                    other().bitwise(
                                   xBit -> xBit && bit
                           )
                           .rs(
                                   rs.set(
                                             rs.get() + 1
                                     )
                                     .get()
                           )
            );
        });
        return result;
    }

    public SimInt rs(int bits) {
        if (bits > this.size) {
            fill(
                    0,
                    this.size,
                    false
            );
            return this;
        }
        boolean[] arr = new boolean[this.size];

        System.arraycopy(
                this.bits,
                0,
                arr,
                bits,
                this.size - bits
        );
        System.arraycopy(
                arr,
                0,
                this.bits,
                0,
                arr.length
        );

        return this;
    }

    public SimInt add(SimInt integer) {
        for (int i = 0; i < this.size; i++) {
            if (integer.get(i)) {
                int n = i;
                while (get(n)) {
                    set(
                            n++,
                            false
                    );
                }
                set(
                        n,
                        true
                );
            }
        }
        return this;
    }

    public SimInt other() {
        return new SimInt(this);
    }

    public SimInt bitwise(Consumer<Boolean> action) {
        for (int i = 0; i < bits.length; i++) {
            action.accept(get(i));
        }
        return this;
    }

    public SimInt bitwise(Function<Boolean, Boolean> function) {
        for (int i = 0; i < bits.length; i++) {
            set(
                    i,
                    function.apply(get(i))
            );
        }
        return this;
    }

    public static SimInt of(long integer) {
        return new SimInt(
                DEFAULT_SIZE,
                integer
        );
    }

    public static SimInt of(String integer, int radix) {
        return new SimInt(
                integer,
                radix
        );
    }

    public SimInt subtract(long integer) {
        return subtract(SimInt.of(integer));
    }

    public SimInt subtract(SimInt integer) {
        for (int i = 0; i < this.size; i++) {
            if (integer.get(i)) {
                int n = i;
                while (! get(n)) {
                    set(
                            n++,
                            true
                    );
                }
                set(
                        n,
                        false
                );
            }
        }
        return this;
    }

    public SimInt add(long integer) {
        return add(SimInt.of(integer));
    }

    public SimInt ls(int bits) {
        if (bits > size) {
            fill(
                    0,
                    size,
                    false
            );
            return this;
        }
        boolean[] arr = new boolean[size];
        System.arraycopy(
                this.bits,
                bits,
                arr,
                0,
                size - bits
        );
        System.arraycopy(
                arr,
                0,
                this.bits,
                0,
                arr.length
        );
        return this;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@link
     *     #equals(Object) equals} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link #equals(Object) equals} method, then
     *     calling the {@code hashCode} method on each of the two objects
     *     must produce distinct integer results.  However, the programmer
     *     should be aware that producing distinct integer results for
     *     unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return this.toString()
                   .hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode() hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimInt integer) {
            return integer().equals(integer.integer());
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("]");
        this.bitwise(bit -> {
            builder.append(bit ? "1" : "0");
        });
        builder.append("[");
        builder.reverse();
        return builder.toString();
    }

    public boolean and(int index, boolean bit) {
        return get(index) && bit;
    }

    public SimInt bitwiseNot() {
        return bitwise(bit -> ! bit);
    }

    public SimInt bitwiseXor(SimInt integer) {
        return bitwise(
                integer,
                (bit, iBit) -> bit ^ iBit
        );
    }

    public SimInt bitwise(SimInt integer, BiFunction<Boolean, Boolean, Boolean> function) {
        AtomicInteger index = new AtomicInteger();
        return bitwise(bit -> {
            return function.apply(
                    bit,
                    integer.get(index.getAndIncrement())
            );
        });
    }

    public SimInt bitwiseAnd(SimInt integer) {
        return bitwise(
                integer,
                (bit, iBit) -> bit && iBit
        );
    }

    public SimInt bitwiseOr(SimInt integer) {
        return bitwise(
                integer,
                (bit, iBit) -> bit || iBit
        );
    }
}
