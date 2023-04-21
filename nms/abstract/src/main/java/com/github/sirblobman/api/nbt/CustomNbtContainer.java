package com.github.sirblobman.api.nbt;

import java.util.Set;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomNbtContainer {
    /**
     * Stores a custom NBT value.
     *
     * @param key   the key this value will be stored under
     * @param type  the type this tag uses
     * @param value the value stored in the tag
     * @param <T>   the generic java type of the tag value
     * @param <Z>   the generic type of the object to store
     * @throws NullPointerException     if the key is null
     * @throws NullPointerException     if the type is null
     * @throws NullPointerException     if the value is null. Removing a tag should be done using {@link #remove(String)}
     * @throws IllegalArgumentException if no suitable adapter will be found for
     *                                  the {@link CustomNbtType#getPrimitiveType()}
     */
    <T, Z> void set(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @NotNull Z value);

    /**
     * Returns if the custom nbt provider has nbt registered matching the provided parameters.
     * <p>
     * This method will only return if the found value has the same primitive
     * data type as the provided key.
     * <p>
     * Storing a value using a custom {@link CustomNbtType} implementation
     * will not store the complex data type. Therefore storing a UUID (by
     * storing a byte[]) will match has("key" , {@link CustomNbtTypes#BYTE_ARRAY}).
     * Likewise a stored byte[] will always match your UUID {@link CustomNbtType} even if it is not 16
     * bytes long.
     * <p>
     * This method is only usable for custom object keys. Overwriting existing
     * tags, like the display name, will not work as the values are stored
     * using your namespace.
     *
     * @param key  the key the value is stored under
     * @param type the type which primitive storage type has to match the value
     * @param <T>  the generic type of the stored primitive
     * @param <Z>  the generic type of the eventually created complex object
     * @return if a value
     * @throws NullPointerException if the key to look up is null
     * @throws NullPointerException if the type to cast the found object to is
     *                              null
     */
    <T, Z> boolean has(@NotNull String key, @NotNull CustomNbtType<T, Z> type);

    /**
     * Returns the custom nbt value that is stored.
     *
     * @param key  the key to look up in the custom tag map
     * @param type the type the value must have.
     * @param <T>  the generic type of the stored primitive
     * @param <Z>  the generic type of the eventually created complex object
     * @return the value or {@code null} if no value was mapped under the given
     * value
     * @throws NullPointerException     if the key to look up is null
     * @throws NullPointerException     if the type to cast the found object to is
     *                                  null
     * @throws IllegalArgumentException if the value exists under the given key,
     *                                  but cannot be access using the given type
     * @throws IllegalArgumentException if no suitable adapter will be found for
     *                                  the {@link CustomNbtType#getPrimitiveType()}
     */
    @Nullable <T, Z> Z get(@NotNull String key, @NotNull CustomNbtType<T, Z> type);

    /**
     * Returns the custom nbt value that is stored.
     * If the value does not exist, the default value provided is returned.
     *
     * @param key          the key to look up in the custom tag map
     * @param type         the type the value must have.
     * @param defaultValue the default value to return if no value was found for
     *                     the provided key
     * @param <T>          the generic type of the stored primitive
     * @param <Z>          the generic type of the eventually created complex object
     * @return the value or the default value if no value was mapped under the
     * given value
     * @throws NullPointerException     if the key to look up is null
     * @throws NullPointerException     if the type to cast the found object to is
     *                                  null
     * @throws IllegalArgumentException if the value exists under the given key,
     *                                  but cannot be access using the given type
     * @throws IllegalArgumentException if no suitable adapter will be found for
     *                                  the {@link CustomNbtType#getPrimitiveType()}
     */

    @Contract("_, _, null -> null")
    <T, Z> @Nullable Z getOrDefault(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @Nullable Z defaultValue);

    /**
     * Get a set of keys present.
     * Any changes made to the returned set will not be reflected on the
     * instance.
     *
     * @return the key set
     */
    @NotNull
    Set<String> getKeys();

    /**
     * Removes a custom nbt by its key.
     *
     * @param key the key
     * @throws NullPointerException if the provided key is null
     */
    void remove(@NotNull String key);

    /**
     * Returns if the container instance is empty, therefore has no entries
     * inside it.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Returns the context this tag container uses.
     *
     * @return the tag context
     */
    @NotNull CustomNbtContext getContext();
}
