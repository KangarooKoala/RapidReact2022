package frc.team2412.robot.util.logging;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

/**
 * Utility class for logging.
 */
public class LogUtil {
    /**
     * Creates an instance of the {@link Log} annotation with the given parameters.
     *
     * <p>
     * When overriding the tab name, position, and size, be careful to avoid namespace (in the case of
     * tab name) and graphical (in the case of position and size) collisions. Because of this, all
     * positions and sizes should be specified if any are.
     *
     * @param name
     *            The name of the value on Shuffleboard; {@code "NO_NAME"} indicates field or method
     *            name.
     * @param tabName
     *            The name of the tab in which to place the widget; {@code "DEFAULT"} indicates the
     *            default inferred tab/layout.
     * @param methodName
     *            Optional name of a method ({@code "DEFAULT"} is the default) to call on the field (or
     *            return value of the logged method) to obtain the actual value to log.
     * @param columnIndex
     *            The column in which the widget should be placed.
     * @param rowIndex
     *            The row in which the widget should be placed.
     * @param width
     *            The width of the widget.
     * @param height
     *            The height of the widget.
     * @return An instance of the {@link Log} annotation with the given parameters.
     */
    private static Log createLog(String name, String tabName, String methodName, int columnIndex, int rowIndex,
            int width, int height) {
        return new Log() {
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Log)) {
                    return false;
                }
                Log log = (Log) obj;
                return log.name().equals(name) && log.tabName().equals(tabName) && log.methodName().equals(methodName)
                        && log.columnIndex() == columnIndex && log.rowIndex() == rowIndex && log.width() == width
                        && log.height() == height;
            }

            @Override
            public int hashCode() {
                return ((127 * "name".hashCode()) ^ name.hashCode())
                        + ((127 * "tabName".hashCode()) ^ tabName.hashCode())
                        + ((127 * "methodName".hashCode()) ^ methodName.hashCode())
                        + ((127 * "columnIndex".hashCode()) ^ Integer.valueOf(columnIndex).hashCode())
                        + ((127 * "rowIndex".hashCode()) ^ Integer.valueOf(rowIndex).hashCode())
                        + ((127 * "width".hashCode()) ^ Integer.valueOf(width).hashCode())
                        + ((127 * "height".hashCode()) ^ Integer.valueOf(height).hashCode());
            }

            @Override
            public String toString() {
                List<String> params = new ArrayList<>();
                if (!name.equals("NO_NAME")) {
                    params.add(name);
                }
                if (!tabName.equals("DEFAULT")) {
                    params.add(tabName);
                }
                if (!methodName.equals("DEFAULT")) {
                    params.add(methodName);
                }
                if (columnIndex != -1) {
                    params.add(Integer.toString(columnIndex));
                }
                if (rowIndex != -1) {
                    params.add(Integer.toString(rowIndex));
                }
                if (width != -1) {
                    params.add(Integer.toString(width));
                }
                if (height != -1) {
                    params.add(Integer.toString(height));
                }
                return "@Class<? extends Log>(" + params.stream().reduce("", String::concat) + ")";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Log.class;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String tabName() {
                return tabName;
            }

            @Override
            public String methodName() {
                return methodName;
            }

            @Override
            public int columnIndex() {
                return columnIndex;
            }

            @Override
            public int rowIndex() {
                return rowIndex;
            }

            @Override
            public int width() {
                return width;
            }

            @Override
            public int height() {
                return height;
            }
        };
    }

    /**
     * Creates an instance of the {@link Config} annotation with the given parameters.
     *
     * @param name
     *            The name of the value on Shuffleboard; {@code "NO_NAME"} indicates field or method
     *            name.
     * @param tabName
     *            The name of the tab in which to place the widget; {@code "DEFAULT"} indicates the
     *            default inferred tab/layout.
     * @param methodName
     *            Optional name of a method ({@code "DEFAULT"} is the default) to call on the field (or
     *            return value of the config method) to obtain the actual value to config.
     * @param methodTypes
     *            Parameter types of the named method, if {@code methodName} is provided.
     * @param defaultValueBoolean
     *            Default value for the setter if it is a single-argument boolean setter.
     * @param defaultValueNumeric
     *            Default value for the setter if it is a single-argument numeric setter.
     * @param rowIndex
     *            The row in which the widget should be placed.
     * @param columnIndex
     *            The column in which the widget should be placed.
     * @param width
     *            The width of the widget.
     * @param height
     *            The height of the widget.
     * @param multiArgLayoutType
     *            The type of layout to use if the annotation target is a multi-parameter setter. Must
     *            be either {@code "listLayout"} or {@code "gridLayout"}.
     * @param numGridColumns
     *            The number of grid columns if the annotation target is a multi-parameter setter and
     *            the layout type is set to grid.
     * @param numGridRows
     *            The number of grid rows if the annotation target is a multi-parameter setter and the
     *            layout type is set to grid.
     * @return An instance of the {@link Config} annotation with the given parameters.
     */
    private static Config createConfig(String name, String tabName, String methodName, Class<?>[] methodTypes,
            boolean defaultValueBoolean, double defaultValueNumeric, int rowIndex, int columnIndex, int width,
            int height, String multiArgLayoutType, int numGridColumns, int numGridRows) {
        return new Config() {
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Config)) {
                    return false;
                }
                Config config = (Config) obj;
                return config.name().equals(name) && config.tabName().equals(tabName)
                        && config.methodName().equals(methodName) && Arrays.equals(config.methodTypes(), methodTypes)
                        && config.defaultValueBoolean() == defaultValueBoolean
                        && Double.valueOf(config.defaultValueNumeric()).equals(Double.valueOf(defaultValueNumeric))
                        && config.rowIndex() == rowIndex && config.columnIndex() == columnIndex
                        && config.width() == width && config.height() == height
                        && config.multiArgLayoutType().equals(multiArgLayoutType)
                        && config.numGridColumns() == numGridColumns && config.numGridRows() == numGridRows;
            }

            @Override
            public int hashCode() {
                return ((127 * "name".hashCode()) ^ name.hashCode())
                       + ((127 * "tabName".hashCode()) ^ tabName.hashCode())
                       + ((127 * "methodName".hashCode()) ^ methodName.hashCode())
                       + ((127 * "methodTypes".hashCode()) ^ Arrays.hashCode(methodTypes))
                       + ((127 * "defaultValueBoolean".hashCode()) ^ Boolean.valueOf(defaultValueBoolean).hashCode())
                       + ((127 * "defaultValueNumeric".hashCode()) ^ Double.valueOf(defaultValueNumeric).hashCode())
                       + ((127 * "rowIndex".hashCode()) ^ Integer.valueOf(rowIndex).hashCode())
                       + ((127 * "columnIndex".hashCode()) ^ Integer.valueOf(columnIndex).hashCode())
                       + ((127 * "width".hashCode()) ^ Integer.valueOf(width).hashCode())
                       + ((127 * "multiArgLayoutType".hashCode()) ^ multiArgLayoutType.hashCode())
                       + ((127 * "numGridColumns".hashCode()) ^ Integer.valueOf(numGridColumns).hashCode())
                       + ((127 * "numGridRows".hashCode()) ^ Integer.valueOf(numGridRows).hashCode());
            }

            @Override
            public String toString() {
                // Optional
                // TODO Auto-generated method stub
                return super.toString();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Config.class;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String tabName() {
                return tabName;
            }

            @Override
            public String methodName() {
                return methodName;
            }

            @Override
            public Class<?>[] methodTypes() {
                return methodTypes;
            }

            @Override
            public boolean defaultValueBoolean() {
                return defaultValueBoolean;
            }

            @Override
            public double defaultValueNumeric() {
                return defaultValueNumeric;
            }

            @Override
            public int rowIndex() {
                return rowIndex;
            }

            @Override
            public int columnIndex() {
                return columnIndex;
            }

            @Override
            public int width() {
                return width;
            }

            @Override
            public int height() {
                return height;
            }

            @Override
            public String multiArgLayoutType() {
                return multiArgLayoutType;
            }

            @Override
            public int numGridColumns() {
                return numGridColumns;
            }

            @Override
            public int numGridRows() {
                return numGridColumns;
            }
        };
    }

    private static final Map<Class<? extends Annotation>, Function<Annotation, Log>> defaultToLogConverters = Map
            .ofEntries(Map.entry(Log.class, annotation -> {
                return (Log) annotation;
            }), Map.entry(Log.NumberBar.class, annotation -> {
                Log.NumberBar params = (Log.NumberBar) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.Dial.class, annotation -> {
                Log.Dial params = (Log.Dial) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.Graph.class, annotation -> {
                Log.Graph params = (Log.Graph) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.BooleanBox.class, annotation -> {
                Log.BooleanBox params = (Log.BooleanBox) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.VoltageView.class, annotation -> {
                Log.VoltageView params = (Log.VoltageView) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.PowerDistribution.class, annotation -> {
                Log.PowerDistribution params = (Log.PowerDistribution) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.Encoder.class, annotation -> {
                Log.Encoder params = (Log.Encoder) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.MotorController.class, annotation -> {
                Log.MotorController params = (Log.MotorController) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.Accelerometer.class, annotation -> {
                Log.Accelerometer params = (Log.Accelerometer) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.ThreeAxisAccelerometer.class, annotation -> {
                Log.ThreeAxisAccelerometer params = (Log.ThreeAxisAccelerometer) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.Gyro.class, annotation -> {
                Log.Gyro params = (Log.Gyro) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.DifferentialDrive.class, annotation -> {
                Log.DifferentialDrive params = (Log.DifferentialDrive) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.MecanumDrive.class, annotation -> {
                Log.MecanumDrive params = (Log.MecanumDrive) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.CameraStream.class, annotation -> {
                Log.CameraStream params = (Log.CameraStream) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }), Map.entry(Log.ToString.class, annotation -> {
                Log.ToString params = (Log.ToString) annotation;
                return createLog(params.name(), params.tabName(), params.methodName(), params.columnIndex(),
                        params.rowIndex(), params.width(), params.height());
            }));

    public static Map<Class<? extends Annotation>, Function<Annotation, Config>> toConfigConverters = Map
            .ofEntries(Map.entry(Config.class, annotation -> {
                return (Config) annotation;
            }), Map.entry(Config.NumberSlider.class, annotation -> {
                Config.NumberSlider params = (Config.NumberSlider) annotation;
                return createConfig(params.name(), params.tabName(), params.methodName(), params.methodTypes(),
                        false, params.defaultValue(), params.rowIndex(), params.columnIndex(), params.width(),
                        params.height(), "listLayout", 3, 3);
            }));

    public static final Map<Class<? extends Annotation>, Function<Annotation, Log>> toLogConverters = new HashMap<>();

    static {
        toLogConverters.putAll(defaultToLogConverters);
    }

    /**
     * Converts the given annotation to an instance of {@link Log}.
     *
     * @param annotation
     *            The annotation to convert.
     * @return The annotation converted to {@link Log}.
     */
    public static Log convertToLog(Annotation annotation) {
        Class<?> annotationClass = annotation.annotationType();
        Function<Annotation, Log> toLogConverter = toLogConverters.get(annotationClass);
        // Try getting converters from all annotation superclasses
        while (toLogConverter == null) {
            Class<?> superClass = annotationClass.getSuperclass();
            if (superClass == null || !superClass.isAnnotation()) {
                throw new ClassCastException("Cannot convert annotation class " + annotationClass + " to Log");
            }
            annotationClass = superClass;
            toLogConverter = toLogConverters.get(annotationClass);
        }
        return toLogConverter.apply(annotation);
    }
}
