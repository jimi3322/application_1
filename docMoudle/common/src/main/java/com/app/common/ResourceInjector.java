package com.app.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.app.common.utils.FileUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ResourceInjector {

    public static Resources injectResource(Context context, String pluginName) {
        File pluginFile;
        pluginFile = new File(FileUtil.getFileDir(context), pluginName);
        if (!pluginFile.exists()) {
            return context.getApplicationContext().getResources();
        }
        Resources orgResources = context.getApplicationContext().getResources();
        AssetManager assetManager;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            assetManager = getNewAssetManager(context, pluginFile.getAbsolutePath());
            return buildNewResource(orgResources, assetManager, context);
        } else {
            assetManager = orgResources.getAssets();
            addAssetPath(assetManager, pluginFile.getAbsolutePath());
            return orgResources;
        }
    }

    private static Resources buildNewResource(Resources hostResources, AssetManager assetManager, Context hostContext){
        Resources newResources;
        try{
            if (isMiUi(hostResources)) {
                newResources = MiUiResourcesCompat.createResources(hostResources, assetManager);
            } else if (isVivo(hostResources)) {
                newResources = VivoResourcesCompat.createResources(hostContext, hostResources, assetManager);
            } else if (isNubia(hostResources)) {
                newResources = NubiaResourcesCompat.createResources(hostResources, assetManager);
            } else if (isNotRawResources(hostResources)) {
                newResources = AdaptationResourcesCompat.createResources(hostResources, assetManager);
            } else {
                // is raw android resources
                newResources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
            }
        }catch (Exception e){
            e.printStackTrace();
            newResources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        }
        return newResources;
    }

    private static AssetManager getNewAssetManager(Context context, String pluginPath) {
        AssetManager mAsset = null;
        try {
            mAsset = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath",
                    String.class);
            addAssetPathMethod.setAccessible(true);
            if ((Integer) addAssetPathMethod.invoke(mAsset, pluginPath) == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
            if ((Integer) addAssetPathMethod.invoke(mAsset, context.getApplicationInfo().sourceDir) == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return mAsset;
    }

    private static void addAssetPath(AssetManager mAsset, String pluginPath) {
        try {
            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath",
                    String.class);
            addAssetPathMethod.setAccessible(true);
            if ((Integer) addAssetPathMethod.invoke(mAsset, pluginPath) == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static boolean isMiUi(Resources resources) {
        return resources.getClass().getName().equals("android.content.res.MiuiResources");
    }

    private static boolean isVivo(Resources resources) {
        return resources.getClass().getName().equals("android.content.res.VivoResources");
    }

    private static boolean isNubia(Resources resources) {
        return resources.getClass().getName().equals("android.content.res.NubiaResources");
    }

    private static boolean isNotRawResources(Resources resources) {
        return !resources.getClass().getName().equals("android.content.res.Resources");
    }

    private static final class MiUiResourcesCompat {
        private static Resources createResources(Resources hostResources, AssetManager assetManager) throws Exception {
            Class resourcesClazz = Class.forName("android.content.res.MiuiResources");
            Resources newResources = (Resources)  invokeConstructor(resourcesClazz,
                    new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class},
                    new Object[]{assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration()});
            return newResources;
        }
    }

    private static final class VivoResourcesCompat {
        private static Resources createResources(Context hostContext, Resources hostResources, AssetManager assetManager) throws Exception {
            Class resourcesClazz = Class.forName("android.content.res.VivoResources");
            Resources newResources = (Resources)  invokeConstructor(resourcesClazz,
                    new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class},
                    new Object[]{assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration()});
            invokeNoException(resourcesClazz, newResources, "init",
                    new Class[]{String.class}, hostContext.getPackageName());
            Object themeValues =  getFieldNoException(resourcesClazz, hostResources, "mThemeValues");
            setFieldNoException(resourcesClazz, newResources, "mThemeValues", themeValues);
            return newResources;
        }
    }

    private static final class NubiaResourcesCompat {
        private static Resources createResources(Resources hostResources, AssetManager assetManager) throws Exception {
            Class resourcesClazz = Class.forName("android.content.res.NubiaResources");
            Resources newResources = (Resources)  invokeConstructor(resourcesClazz,
                    new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class},
                    new Object[]{assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration()});
            return newResources;
        }
    }

    private static final class AdaptationResourcesCompat {
        private static Resources createResources(Resources hostResources, AssetManager assetManager) throws Exception {
            Resources newResources;
            try {
                Class resourcesClazz = hostResources.getClass();
                newResources = (Resources)  invokeConstructor(resourcesClazz,
                        new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class},
                        new Object[]{assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration()});
            } catch (Exception e) {
                newResources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
            }

            return newResources;
        }
    }

    /**
     * Locates a given field anywhere in the class inheritance hierarchy.
     *
     * @param instance an object to search the field into.
     * @param name     field name
     *
     * @return a field object
     *
     * @throws NoSuchFieldException if the field cannot be located
     */
    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                return field;
            } catch (NoSuchFieldException e) {
                // ignore and search next
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    public static Field findField(Class<?> originClazz, String name) throws NoSuchFieldException {
        for (Class<?> clazz = originClazz; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                return field;
            } catch (NoSuchFieldException e) {
                // ignore and search next
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + originClazz);
    }

    /**
     * Locates a given method anywhere in the class inheritance hierarchy.
     *
     * @param instance       an object to search the method into.
     * @param name           method name
     * @param parameterTypes method parameter types
     *
     * @return a method object
     *
     * @throws NoSuchMethodException if the method cannot be located
     */
    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);

                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                return method;
            } catch (NoSuchMethodException e) {
                // ignore and search next
            }
        }

        throw new NoSuchMethodException("Method "
                + name
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    /**
     * Locates a given method anywhere in the class inheritance hierarchy.
     *
     * @param clazz          a class to search the method into.
     * @param name           method name
     * @param parameterTypes method parameter types
     *
     * @return a method object
     *
     * @throws NoSuchMethodException if the method cannot be located
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);

                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                return method;
            } catch (NoSuchMethodException e) {
                // ignore and search next
            }
        }

        throw new NoSuchMethodException("Method "
                + name
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + clazz);
    }

    /**
     * Locates a given constructor anywhere in the class inheritance hierarchy.
     *
     * @param instance       an object to search the constructor into.
     * @param parameterTypes constructor parameter types
     *
     * @return a constructor object
     *
     * @throws NoSuchMethodException if the constructor cannot be located
     */
    public static Constructor<?> findConstructor(Object instance, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor(parameterTypes);

                if (!ctor.isAccessible()) {
                    ctor.setAccessible(true);
                }

                return ctor;
            } catch (NoSuchMethodException e) {
                // ignore and search next
            }
        }

        throw new NoSuchMethodException("Constructor"
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    /**
     * Replace the value of a field containing a non null array, by a new array containing the
     * elements of the original array plus the elements of extraElements.
     *
     * @param instance      the instance whose field is to be modified.
     * @param fieldName     the field to modify.
     * @param extraElements elements to append at the end of the array.
     */
    public static void expandFieldArray(Object instance, String fieldName, Object[] extraElements)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field jlrField = findField(instance, fieldName);

        Object[] original = (Object[]) jlrField.get(instance);
        Object[] combined = (Object[]) Array
                .newInstance(original.getClass().getComponentType(), original.length + extraElements.length);

        // NOTE: changed to copy extraElements first, for patch load first

        System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
        System.arraycopy(original, 0, combined, extraElements.length, original.length);

        jlrField.set(instance, combined);
    }

    /**
     * Replace the value of a field containing a non null array, by a new array containing the
     * elements of the original array plus the elements of extraElements.
     *
     * @param instance  the instance whose field is to be modified.
     * @param fieldName the field to modify.
     */
    public static void reduceFieldArray(Object instance, String fieldName, int reduceSize)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (reduceSize <= 0) {
            return;
        }

        Field jlrField = findField(instance, fieldName);

        Object[] original = (Object[]) jlrField.get(instance);
        int finalLength = original.length - reduceSize;

        if (finalLength <= 0) {
            return;
        }

        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), finalLength);

        System.arraycopy(original, reduceSize, combined, 0, finalLength);

        jlrField.set(instance, combined);
    }

    @SuppressWarnings("unchecked")
    public static Object invokeConstructor(Class clazz, Class[] parameterTypes, Object... args)
            throws Exception {
        Constructor constructor = clazz.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

    @SuppressWarnings("unchecked")
    public static Object invokeNoException(Class clazz, Object target, String name, Class[] parameterTypes, Object... args) {
        try {
            return invoke(clazz, target, name, parameterTypes, args);
        } catch (Exception e) {
        }

        return null;
    }

    public static Object getActivityThread(Context context,
                                           Class<?> activityThread) {
        try {
            if (activityThread == null) {
                activityThread = Class.forName("android.app.ActivityThread");
            }
            Method m = activityThread.getMethod("currentActivityThread");
            m.setAccessible(true);
            Object currentActivityThread = m.invoke(null);
            if (currentActivityThread == null && context != null) {
                // In older versions of Android (prior to frameworks/base 66a017b63461a22842)
                // the currentActivityThread was built on thread locals, so we'll need to try
                // even harder
                Field mLoadedApk = context.getClass().getField("mLoadedApk");
                mLoadedApk.setAccessible(true);
                Object apk = mLoadedApk.get(context);
                Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                mActivityThreadField.setAccessible(true);
                currentActivityThread = mActivityThreadField.get(apk);
            }
            return currentActivityThread;
        } catch (Throwable ignore) {
            return null;
        }
    }

    /**
     * Handy method for fetching hidden integer constant value in system classes.
     *
     * @param clazz
     * @param fieldName
     *
     * @return
     */
    public static int getValueOfStaticIntField(Class<?> clazz, String fieldName, int defVal) {
        try {
            final Field field = findField(clazz, fieldName);
            return field.getInt(null);
        } catch (Throwable thr) {
            return defVal;
        }
    }

    public static Object getField(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    public static Object getFieldNoException(Class clazz, Object target, String name) {
        try {
            return  getField(clazz, target, name);
        } catch (Exception e) {
            //ignored.
        }

        return null;
    }

    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    public static void setFieldNoException(Class clazz, Object target, String name, Object value) {
        try {
            setField(clazz, target, name, value);
        } catch (Exception e) {
            //ignored.
        }
    }

    public static Object getPackageInfo(Context base) {
        Object sLoadedApk = null;
        try {
            sLoadedApk =  getField(base.getClass(), base, "mPackageInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sLoadedApk;
    }

    @SuppressWarnings("unchecked")
    public static Object invoke(Class clazz, Object target, String name, Object... args)
            throws Exception {
        Class[] parameterTypes = null;
        if (args != null) {
            parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        Method method = clazz.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }
}
