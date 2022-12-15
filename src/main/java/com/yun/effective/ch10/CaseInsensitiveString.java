package com.yun.effective.ch10;

import java.util.Objects;

/**
 * Obey the general contract when overriding equals
 */
public final class CaseInsensitiveString {
    /**
     * Reflexive: For any non-null reference value x, x.equals(x) must return true.
     *
     * Symmetric: For any non-null reference values x and y, x.equals(y) must return
     *            true if and only if y.equals(x) returns true.
     *
     * Transitive: For any non-null reference values x, y, z, if x.equals(y) returns
     *             true and y.equals(z) returns true, then x.equals(z) must return true.
     *
     * Consistent: For any non-null reference values x and y, multiple invocations of x.equals(y)
     *             must consistently return true or consistently return false,
     *             provided no information used in equals comparisons is modified.
     *
     * For any non-null reference value x, x.equals(null) must return false.
     */

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // Broken - violates symmetry!

    public boolean equals2(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);

        if (o instanceof String)        // One-way interoperability!
            return s.equalsIgnoreCase((String) o);

        return false;
    }

    // Correct one
    @Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }
}
