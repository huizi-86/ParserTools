
package com.wind.parser;

/**
 * Created by sunhuihui on 2015/11/12.
 * 对应Apn或spn中的每个属性
 * <apn
 * mcc="210"  <----> {@link #tag}= {@link #value}  {@link #index}是在xml中定义的顺序
 * />
 */
public class PersistAttribute implements Comparable<PersistAttribute>, Cloneable {
    String value;
    String tag;
    int index;

    public PersistAttribute(int index, String tag, String value) {
        this.value = value;
        this.tag = tag;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersistAttribute that = (PersistAttribute) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    protected PersistAttribute clone() throws CloneNotSupportedException {
        return (PersistAttribute) super.clone();
    }

    @Override
    public int compareTo(PersistAttribute o) {
        return this.index - o.index;
    }
}
