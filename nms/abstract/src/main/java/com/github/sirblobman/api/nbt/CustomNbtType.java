package com.github.sirblobman.api.nbt;

import org.jetbrains.annotations.NotNull;

public interface CustomNbtType<T, Z> {
    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @NotNull Class<T> getPrimitiveType();

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @NotNull Class<Z> getComplexType();

    /**
     * Returns the primitive data that resembles the complex object passed to
     * this method.
     *
     * @param complex the complex object instance
     * @param context the context this operation is running in
     * @return the primitive value
     */
    @NotNull T toPrimitive(@NotNull Z complex, @NotNull CustomNbtContext context);

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive the primitive value
     * @param context   the context this operation is running in
     * @return the complex object instance
     */
    @NotNull Z fromPrimitive(@NotNull T primitive, @NotNull CustomNbtContext context);
}
