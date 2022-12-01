package com.yun.effective.ch02_creatingAndDestryingObjects;

import java.math.BigInteger;
import java.sql.Connection;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

/**
 * A static method that returns an instance of the class.
 *
 * Advantages: Constructor can't have method name. but static method can have name.
 *             They are not required to create a new object each time they're invoked. (similar to Flyweight pattern)
 *             They can return an object of any subtype of their return type.
 *             The class of the returned object can vary from call to call.
 *             (calls same method but can be different object returned according to parameter.)
 *             The class of the returned object need not exist when the class containing the method is written.
 *
 *
 * Disadvantages: Classes without public or protected constructors cannot be subclassed.
 *                They are hard for programmers to find.
 *                (To reduce the difficulty, adhere to common naming conventions.)
 *
 */
public class StaticFactoryMethod {


    /**
     * examples of advantages
     * BigInteger(int, int, Random) -> BigInteger.probablePrime(int, int, Random)
     */

    /**
     * This method translates a boolean primitive value into a Boolean object reference.
     *
     * @param b a boolean primitive value
     * @return a Boolean object reference
     */
    public static Boolean valueOf(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * EnumSet returns object according to the size of parameters.
     * RegularEnumSet if p <= 64
     * JumboEnumSet if p > 64
     */

    // *********** naming conventions

    /**
     * from - A type-conversion method that takes a single parameter
     * Date d = Date.from(instance);
     */
    void fromExample() {
        A a = A.from(B.getInstance());
    }


    /**
     * of - An aggregation method that takes multiple parameters and
     *      returns an instance of this type that incorporates them.
     * Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
     */


    /**
     * valueOf - A more verbose alternative to from and of.
     * BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
     */

    /**
     * instance or getInstance - Returns an instance that is described by its parameters
     * StackWalker luke = StackWalker.getInstance(options);
     */

    /**
     * create or newInstance - Like instance or getInstance, except that
     *                         the method guarantees that each call returns a new instance.
     * Object newArray = Arrays.newInstance(classObject, arrayLen);
     */

    /**
     * getType - Like getInstance, but used if the factory method is in a different class.
     *           Type is the type of object returned by the factory method.
     * FileStore fs = Files.getFileStore(path);
     */

    /**
     * newType - Like newInstance, but used if the factory method is in a different class.
     *           Type is the type of object returned by the factory method.
     * BufferedReader br = Files.newBufferedReader(path);
     */

    /**
     * type - A concise alternative to get Type and new Type.
     * List<Complaint> litany = Collections.list(legacyLitany);
     */

}
