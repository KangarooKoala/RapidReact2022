package frc.team2412.robot.util.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import io.github.oblarg.oblog.annotations.Log;

public class ConstantLogger {
    @FunctionalInterface
    public static interface LogProcessor {
        void process(Supplier<Object> supplier, Annotation annotation, Log basicParams,
                ShuffleboardContainer container, String name);
    }

    @FunctionalInterface
    public static interface ConfigProcessor {
        void process();
    }

    private static final Map<Class<? extends Annotation>, LogProcessor> logProcessors = Map
            .ofEntries(Map.entry(Log.class, (supplier, annotation, basicParams, container, name) -> {
                Object obj = supplier.get();
                if (obj instanceof Sendable) {
                    configureComponent(container.add(name, (Sendable) obj), basicParams);
                } else {
                    configureAndRegisterSimpleWidget(container.add(name, obj), basicParams, supplier);
                }
            }), Map.entry(Log.NumberBar.class, (supplier, annotation, basicParams, container, name) -> {
                Log.NumberBar params = (Log.NumberBar) annotation;
                configureAndRegisterSimpleWidget(
                        container.add(name, supplier.get())
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "center", params.center())),
                        basicParams, supplier);
            }), Map.entry(Log.Dial.class, (supplier, annotation, basicParams, container, name) -> {
                Log.Dial params = (Log.Dial) annotation;
                configureAndRegisterSimpleWidget(
                        container.add(name, supplier.get())
                                .withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showValue", params.showValue())),
                        basicParams, supplier);
            }), Map.entry(Log.Graph.class, (supplier, annotation, basicParams, container, name) -> {
                Log.Graph params = (Log.Graph) annotation;
                configureAndRegisterSimpleWidget(
                        container.add(name, supplier.get())
                                .withWidget(BuiltInWidgets.kGraph)
                                .withProperties(Map.of(
                                        "Visible time", params.visibleTime())),
                        basicParams, supplier);
            }), Map.entry(Log.BooleanBox.class, (supplier, annotation, basicParams, container, name) -> {
                Log.BooleanBox params = (Log.BooleanBox) annotation;
                configureAndRegisterSimpleWidget(
                        container.add(name, supplier.get())
                                .withWidget(BuiltInWidgets.kBooleanBox)
                                .withProperties(Map.of(
                                        "colorWhenTrue", params.colorWhenTrue(),
                                        "colorWhenFalse", params.colorWhenFalse())),
                        basicParams, supplier);
            }), Map.entry(Log.VoltageView.class, (supplier, annotation, basicParams, container, name) -> {
                Log.VoltageView params = (Log.VoltageView) annotation;
                Map<String, Object> properties = Map.of(
                        "min", params.min(),
                        "max", params.max(),
                        "center", params.center(),
                        "orientation", params.orientation(),
                        "numberOfTickMarks", params.numTicks());
                Object obj = supplier.get();
                if (obj instanceof AnalogInput) {
                    configureComponent(
                            container.add(name, (Sendable) obj)
                                    .withWidget(BuiltInWidgets.kVoltageView)
                                    .withProperties(properties),
                            basicParams);
                } else {
                    configureAndRegisterSimpleWidget(
                            container.add(name, obj)
                                    .withWidget(BuiltInWidgets.kVoltageView)
                                    .withProperties(properties),
                            basicParams, supplier);
                }
            }), Map.entry(Log.PowerDistribution.class, (supplier, annotation, basicParams, container, name) -> {
                Log.PowerDistribution params = (Log.PowerDistribution) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kPowerDistribution)
                                .withProperties(Map.of(
                                        "showVoltageAndCurrentValues", params.showVoltageAndCurrent())),
                        basicParams);
            }), Map.entry(Log.Encoder.class, (supplier, annotation, basicParams, container, name) -> {
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kEncoder),
                        basicParams);
            }), Map.entry(Log.MotorController.class, (supplier, annotation, basicParams, container, name) -> {
                Log.MotorController params = (Log.MotorController) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kMotorController)
                                .withProperties(Map.of(
                                        "orientation", params.orientation())),
                        basicParams);
            }), Map.entry(Log.Accelerometer.class, (supplier, annotation, basicParams, container, name) -> {
                Log.Accelerometer params = (Log.Accelerometer) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kAccelerometer)
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showText", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks())),
                        basicParams);
            }), Map.entry(Log.ThreeAxisAccelerometer.class, (supplier, annotation, basicParams, container, name) -> {
                Log.ThreeAxisAccelerometer params = (Log.ThreeAxisAccelerometer) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.k3AxisAccelerometer)
                                .withProperties(Map.of(
                                        "range", params.range(),
                                        "showValue", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks())),
                        basicParams);
            }), Map.entry(Log.Gyro.class, (supplier, annotation, basicParams, container, name) -> {
                Log.Gyro params = (Log.Gyro) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kGyro)
                                .withProperties(Map.of(
                                        "majorTickSpacing", params.majorTickSpacing(),
                                        "startingAngle", params.startingAngle(),
                                        "showTickMarkRing", params.showTicks())),
                        basicParams);
            }), Map.entry(Log.DifferentialDrive.class, (supplier, annotation, basicParams, container, name) -> {
                Log.DifferentialDrive params = (Log.DifferentialDrive) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kDifferentialDrive)
                                .withProperties(Map.of(
                                        "numberOfWheels", params.numWheels(),
                                        "wheelDiameter", params.wheelDiameter(),
                                        "showVelocityVectors", params.showVel())),
                        basicParams);
            }), Map.entry(Log.MecanumDrive.class, (supplier, annotation, basicParams, container, name) -> {
                Log.MecanumDrive params = (Log.MecanumDrive) annotation;
                configureComponent(
                        container.add(name, (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kMecanumDrive)
                                .withProperties(Map.of(
                                        "showVelocityVectors", params.showVel())),
                        basicParams);
            }), Map.entry(Log.CameraStream.class, (supplier, annotation, basicParams, container, name) -> {
                Log.CameraStream params = (Log.CameraStream) annotation;
                configureComponent(
                        container.add(name, (VideoSource) supplier.get())
                                .withWidget(BuiltInWidgets.kCameraStream)
                                .withProperties(Map.of(
                                        "showCrosshair", params.showCrosshairs(),
                                        "crosshairColor", params.crosshairColor(),
                                        "showControls", params.showControls(),
                                        "rotation", params.rotation())),
                        basicParams);
            }), Map.entry(Log.ToString.class, (supplier, annotation, basicParams, container, name) -> {
                configureAndRegisterSimpleWidget(container.add(name, supplier.get().toString()), basicParams,
                        () -> supplier.get().toString());
            }));

    private static final Map<Class<? extends Annotation>, ConfigProcessor> configProcessors = Map.ofEntries();

    private static final Map<NetworkTableEntry, Supplier<Object>> entrySuppliers = new HashMap<>();

    private static final Map<Class<? extends Constants>, Map<String, NetworkTableEntry>> configEntries = new HashMap<>();

    /**
     * Registers an entry with a supplier of values.
     *
     * @param entry
     *            The entry to register.
     * @param supplier
     *            The supplier of values the entry should be set to.
     */
    public static void registerLogEntry(NetworkTableEntry entry, Supplier<Object> supplier) {
        entrySuppliers.put(entry, supplier);
    }

    private static void registerConfigEntry(NetworkTableEntry entry, Class<? extends Constants> constantsClass) {
        configEntries.get(constantsClass).put(entry.getName(), entry);
    }

    /**
     * Updates all entries registered with {@link ConstantLogger}.
     */
    public static void updateEntries() {
        entrySuppliers.forEach((entry, supplier) -> entry.setValue(supplier.get()));
    }

    public static NetworkTableValue getLoggedNTValue(Class<? extends Constants> c, String name) {
        return configEntries.get(c).get(name).getValue();
    }

    public static Object getLoggedValue(Class<? extends Constants> c, String name) {
        return getLoggedNTValue(c, name).getValue();
    }

    public static double getLoggedDouble(Class<? extends Constants> c, String name) {
        return getLoggedNTValue(c, name).getDouble();
    }

    public static void logConstants(Class<? extends Constants> constantsClass, ShuffleboardContainer container) {
        if (configEntries.containsKey(constantsClass)) {
            return;
        }
        logFieldsAndMethods(constantsClass, container);
    }

    private static void logFieldsAndMethods(Class<? extends Constants> constantsClass,
            ShuffleboardContainer container) {
        // Log every field
        for (Field field : constantsClass.getDeclaredFields()) {
            // Only look at static methods
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // Try to disable accessibility checking for the field, skip the field if not (We'll never)
            if (!field.trySetAccessible()) {
                continue;
            }
            field.setAccessible(true);

            // Supplier of values to update entry with
            Supplier<Object> supplier = () -> {
                try {
                    return field.get(null);
                } catch (IllegalAccessException e) {
                    return null;
                }
            };

            // Loop over every annotation of the field that we have a processor for
            for (Class<? extends Annotation> annotationClass : logProcessors.keySet()) {
                for (Annotation annotation : field.getAnnotationsByType(annotationClass)) {
                    processAnnotation(annotation, supplier, container, field.getName());
                }
            }
        }

        // Log every method
        for (Method method : constantsClass.getDeclaredMethods()) {
            // Only look at static methods
            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            // Only look at getters
            if (method.getReturnType() == Void.TYPE || method.getParameterCount() > 0) {
                continue;
            }

            // Supplier of values to update entry with
            Supplier<Object> supplier = () -> {
                try {
                    return method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return null;
                }
            };

            // For every annotation of the method that we have a processor for
            for (Class<? extends Annotation> annotationClass : logProcessors.keySet()) {
                for (Annotation annotation : method.getAnnotationsByType(annotationClass)) {
                    processAnnotation(annotation, supplier, container, method.getName());
                }
            }
        }
    }

    private static void configStaticFieldsAndMethods(Class<? extends Constants> constantsClass,
            ShuffleboardContainer container, Map<String, NetworkTableEntry> entries) {
        // Loop over every field
        for (Field field : constantsClass.getDeclaredFields()) {
            // Only process static fields
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            for (Class<? extends Annotation> annotationClass : configProcessors.keySet()) {
                for (Annotation annotation : field.getAnnotationsByType(annotationClass)) {
                    // TODO Process field
                }
            }
        }

        // TODO process methods
    }

    /**
     * Processes the given annotation.
     *
     * @param annotation
     *            The annotation to process.
     * @param supplier
     *            The supplier of objects to feed the entry.
     * @param container
     *            The container to put the entry into.
     * @param name
     *            The name of the field.
     */
    private static void processAnnotation(Annotation annotation, Supplier<Object> supplier,
            ShuffleboardContainer container, String name) {
        Log basicParams = LogUtil.convertToLog(annotation);
        LogProcessor processor = getFieldProcessor(annotation);
        if (processor != null) {
            processor.process(getFromMethod(supplier, basicParams.methodName()), annotation, basicParams,
                    basicParams.tabName().equals("DEFAULT") ? container : Shuffleboard.getTab(basicParams.tabName()),
                    basicParams.name().equals("NO_NAME") ? name : basicParams.name());
        }
    }

    /**
     * Returns a supplier that retrieves values from the given supplier. If the given name is
     * {@code "DEFAULT"}, returns the original supplier.
     *
     * @param supplier
     *            The original supplier of values.
     * @param methodName
     *            The name of the getter method to extract values with.
     * @return A supplier that calls the getter on the values supplied by the original supplier.
     */
    private static Supplier<Object> getFromMethod(Supplier<Object> supplier, String methodName) {
        if (methodName.equals("DEFAULT")) {
            return supplier;
        }

        final Object obj = supplier.get();
        final Class<?> cls = obj.getClass();
        Method method;
        try {
            method = cls.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "No appropriate method named " + methodName + " in class " + cls.getName() + "!");
        }

        method.setAccessible(true);
        return () -> {
            try {
                return method.invoke(obj);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    /**
     * Returns the most appropriate {@link LogProcessor} for the given annotation. Specifically, the
     * one for the most specific supertype of the annotation's type.
     *
     * @param annotation
     *            The annotation to get a {@link LogProcessor} for.
     * @return The most appropriate {@link LogProcessor}, or null if none are appropriate.
     */
    private static LogProcessor getFieldProcessor(Annotation annotation) {
        Class<?> annotationClass = annotation.annotationType();
        LogProcessor processor = logProcessors.get(annotationClass);
        while (processor == null) {
            Class<?> superClass = annotationClass.getSuperclass();
            if (superClass == null) {
                return null;
            }
            annotationClass = superClass;
            processor = logProcessors.get(annotationClass);
        }
        return processor;
    }

    /**
     * Configures a component's position and size.
     *
     * @param <C>
     *            The type of the {@link ShuffleboardComponent}.
     * @param component
     *            The component to configure.
     * @param params
     *            The instance of {@link Log} with the position and size parameters to use.
     * @return The configured component.
     */
    private static <C extends ShuffleboardComponent<C>> C configureComponent(C component, Log params) {
        return component.withPosition(params.columnIndex(), params.rowIndex())
                .withSize(params.width(), params.height());
    }

    /**
     * Configures a {@link SimpleWidget}'s position and size and registers its entry.
     *
     * @param widget
     *            The widget to configure and register.
     * @param params
     *            The instance of {@link Log} with the position and size parameters to use.
     * @param supplier
     *            The supplier of values the widget's entry should be set to.
     */
    private static void configureAndRegisterSimpleWidget(SimpleWidget widget, Log params, Supplier<Object> supplier) {
        registerLogEntry(configureComponent(widget, params).getEntry(), supplier);
    }
}
