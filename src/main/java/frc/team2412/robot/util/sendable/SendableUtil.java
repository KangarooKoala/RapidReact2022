package frc.team2412.robot.util.sendable;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class SendableUtil {
    /**
     * This method does nothing. Useful for creating no-op consumers.
     *
     * @param args
     *            The ignored arguments.
     */
    public static void doNothing(Object... args) {
        // Does nothing
    }

    /**
     * Creates a double supplier that applies a mapping function to a supplier. Null values are mapped
     * to Double.NaN.
     *
     * @param source
     *            The source of values.
     * @param map
     *            The function to map the values to a double.
     * @return A supplier that returns the result of applying the mapping function to the supplier, or
     *         NaN if the source value is null.
     */
    public static <T> DoubleSupplier mapNullableToDouble(Supplier<T> source, ToDoubleFunction<T> map) {
        return mapNullableToDouble(source, map, Double.NaN);
    }

    /**
     * Creates a double supplier that applies a mapping function to a supplier.
     *
     * @param source
     *            The source of values.
     * @param map
     *            The function to map the values to a double.
     * @param defaultValue
     *            The default value to return when the source value is null.
     * @return A supplier that returns the result of applying the mapping function to the supplier.
     */
    public static <T> DoubleSupplier mapNullableToDouble(Supplier<T> source, ToDoubleFunction<T> map,
            double defaultValue) {
        return () -> {
            T value = source.get();
            if (value == null) {
                return defaultValue;
            }
            return map.applyAsDouble(value);
        };
    }
}
