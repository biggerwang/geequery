/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easyframe.fastjson.parser;

import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import jef.tools.Assert;

import org.easyframe.fastjson.JSONArray;
import org.easyframe.fastjson.JSONException;
import org.easyframe.fastjson.JSONObject;
import org.easyframe.fastjson.annotation.JSONType;
import org.easyframe.fastjson.parser.deserializer.ASMDeserializerFactory;
import org.easyframe.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import org.easyframe.fastjson.parser.deserializer.ArrayDeserializer;
import org.easyframe.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.BooleanFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.CharArrayDeserializer;
import org.easyframe.fastjson.parser.deserializer.ClassDerializer;
import org.easyframe.fastjson.parser.deserializer.CollectionDeserializer;
import org.easyframe.fastjson.parser.deserializer.DateDeserializer;
import org.easyframe.fastjson.parser.deserializer.DateFormatDeserializer;
import org.easyframe.fastjson.parser.deserializer.DefaultFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.EnumDeserializer;
import org.easyframe.fastjson.parser.deserializer.FieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.IntegerFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.JSONArrayDeserializer;
import org.easyframe.fastjson.parser.deserializer.JSONObjectDeserializer;
import org.easyframe.fastjson.parser.deserializer.JavaBeanDeserializer;
import org.easyframe.fastjson.parser.deserializer.JavaObjectDeserializer;
import org.easyframe.fastjson.parser.deserializer.LongFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.MapDeserializer;
import org.easyframe.fastjson.parser.deserializer.NumberDeserializer;
import org.easyframe.fastjson.parser.deserializer.ObjectDeserializer;
import org.easyframe.fastjson.parser.deserializer.SqlDateDeserializer;
import org.easyframe.fastjson.parser.deserializer.StackTraceElementDeserializer;
import org.easyframe.fastjson.parser.deserializer.StringFieldDeserializer;
import org.easyframe.fastjson.parser.deserializer.ThrowableDeserializer;
import org.easyframe.fastjson.parser.deserializer.TimeDeserializer;
import org.easyframe.fastjson.parser.deserializer.TimestampDeserializer;
import org.easyframe.fastjson.serializer.AtomicIntegerArrayCodec;
import org.easyframe.fastjson.serializer.AtomicLongArrayCodec;
import org.easyframe.fastjson.serializer.BigDecimalCodec;
import org.easyframe.fastjson.serializer.BigIntegerCodec;
import org.easyframe.fastjson.serializer.BooleanCodec;
import org.easyframe.fastjson.serializer.CalendarCodec;
import org.easyframe.fastjson.serializer.CharacterCodec;
import org.easyframe.fastjson.serializer.CharsetCodec;
import org.easyframe.fastjson.serializer.ColorCodec;
import org.easyframe.fastjson.serializer.FileCodec;
import org.easyframe.fastjson.serializer.FloatCodec;
import org.easyframe.fastjson.serializer.FontCodec;
import org.easyframe.fastjson.serializer.InetAddressCodec;
import org.easyframe.fastjson.serializer.InetSocketAddressCodec;
import org.easyframe.fastjson.serializer.IntegerCodec;
import org.easyframe.fastjson.serializer.LocaleCodec;
import org.easyframe.fastjson.serializer.LongCodec;
import org.easyframe.fastjson.serializer.PatternCodec;
import org.easyframe.fastjson.serializer.PointCodec;
import org.easyframe.fastjson.serializer.RectangleCodec;
import org.easyframe.fastjson.serializer.ReferenceCodec;
import org.easyframe.fastjson.serializer.StringCodec;
import org.easyframe.fastjson.serializer.TimeZoneCodec;
import org.easyframe.fastjson.serializer.URICodec;
import org.easyframe.fastjson.serializer.URLCodec;
import org.easyframe.fastjson.serializer.UUIDCodec;
import org.easyframe.fastjson.util.ASMUtils;
import org.easyframe.fastjson.util.DeserializeBeanInfo;
import org.easyframe.fastjson.util.FieldInfo;
import org.easyframe.fastjson.util.IdentityHashMap;
import org.easyframe.json.JsonTypeDeserializer;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class ParserConfig {

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    private final Set<Class<?>>                             primitiveClasses  = new HashSet<Class<?>>();

    private static ParserConfig                             global            = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> derializers       = new IdentityHashMap<Type, ObjectDeserializer>();
    private final IdentityHashMap<Type, JsonTypeDeserializer> structDerializer       = new IdentityHashMap<Type, JsonTypeDeserializer>();
    

    private boolean                                         asmEnable         = !ASMUtils.isAndroid();

    protected final SymbolTable                             symbolTable       = new SymbolTable();

    public ParserConfig(){
        primitiveClasses.add(boolean.class);
        primitiveClasses.add(Boolean.class);

        primitiveClasses.add(char.class);
        primitiveClasses.add(Character.class);

        primitiveClasses.add(byte.class);
        primitiveClasses.add(Byte.class);

        primitiveClasses.add(short.class);
        primitiveClasses.add(Short.class);

        primitiveClasses.add(int.class);
        primitiveClasses.add(Integer.class);

        primitiveClasses.add(long.class);
        primitiveClasses.add(Long.class);

        primitiveClasses.add(float.class);
        primitiveClasses.add(Float.class);

        primitiveClasses.add(double.class);
        primitiveClasses.add(Double.class);

        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);

        primitiveClasses.add(String.class);
        primitiveClasses.add(java.util.Date.class);
        primitiveClasses.add(java.sql.Date.class);
        primitiveClasses.add(java.sql.Time.class);
        primitiveClasses.add(java.sql.Timestamp.class);

        derializers.put(SimpleDateFormat.class, DateFormatDeserializer.instance);
        derializers.put(java.sql.Timestamp.class, TimestampDeserializer.instance);
        derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        derializers.put(java.sql.Time.class, TimeDeserializer.instance);
        derializers.put(java.util.Date.class, DateDeserializer.instance);
        derializers.put(Calendar.class, CalendarCodec.instance);

        derializers.put(JSONObject.class, JSONObjectDeserializer.instance);
        derializers.put(JSONArray.class, JSONArrayDeserializer.instance);

        derializers.put(Map.class, MapDeserializer.instance);
        derializers.put(HashMap.class, MapDeserializer.instance);
        derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        derializers.put(TreeMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

        derializers.put(Collection.class, CollectionDeserializer.instance);
        derializers.put(List.class, CollectionDeserializer.instance);
        derializers.put(ArrayList.class, CollectionDeserializer.instance);

        derializers.put(Object.class, JavaObjectDeserializer.instance);
        derializers.put(String.class, StringCodec.instance);
        derializers.put(char.class, CharacterCodec.instance);
        derializers.put(Character.class, CharacterCodec.instance);
        derializers.put(byte.class, NumberDeserializer.instance);
        derializers.put(Byte.class, NumberDeserializer.instance);
        derializers.put(short.class, NumberDeserializer.instance);
        derializers.put(Short.class, NumberDeserializer.instance);
        derializers.put(int.class, IntegerCodec.instance);
        derializers.put(Integer.class, IntegerCodec.instance);
        derializers.put(long.class, LongCodec.instance);
        derializers.put(Long.class, LongCodec.instance);
        derializers.put(BigInteger.class, BigIntegerCodec.instance);
        derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        derializers.put(float.class, FloatCodec.instance);
        derializers.put(Float.class, FloatCodec.instance);
        derializers.put(double.class, NumberDeserializer.instance);
        derializers.put(Double.class, NumberDeserializer.instance);
        derializers.put(boolean.class, BooleanCodec.instance);
        derializers.put(Boolean.class, BooleanCodec.instance);
        derializers.put(Class.class, ClassDerializer.instance);
        derializers.put(char[].class, CharArrayDeserializer.instance);

        derializers.put(AtomicBoolean.class, BooleanCodec.instance);
        derializers.put(AtomicInteger.class, IntegerCodec.instance);
        derializers.put(AtomicLong.class, LongCodec.instance);
        derializers.put(AtomicReference.class, ReferenceCodec.instance);

        derializers.put(WeakReference.class, ReferenceCodec.instance);
        derializers.put(SoftReference.class, ReferenceCodec.instance);

        derializers.put(UUID.class, UUIDCodec.instance);
        derializers.put(TimeZone.class, TimeZoneCodec.instance);
        derializers.put(Locale.class, LocaleCodec.instance);
        derializers.put(InetAddress.class, InetAddressCodec.instance);
        derializers.put(Inet4Address.class, InetAddressCodec.instance);
        derializers.put(Inet6Address.class, InetAddressCodec.instance);
        derializers.put(InetSocketAddress.class, InetSocketAddressCodec.instance);
        derializers.put(File.class, FileCodec.instance);
        derializers.put(URI.class, URICodec.instance);
        derializers.put(URL.class, URLCodec.instance);
        derializers.put(Pattern.class, PatternCodec.instance);
        derializers.put(Charset.class, CharsetCodec.instance);
        derializers.put(Number.class, NumberDeserializer.instance);
        derializers.put(AtomicIntegerArray.class, AtomicIntegerArrayCodec.instance);
        derializers.put(AtomicLongArray.class, AtomicLongArrayCodec.instance);
        derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);

        derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        derializers.put(Closeable.class, JavaObjectDeserializer.instance);

        try {
            derializers.put(Class.forName("java.awt.Point"), PointCodec.instance);
            derializers.put(Class.forName("java.awt.Font"), FontCodec.instance);
            derializers.put(Class.forName("java.awt.Rectangle"), RectangleCodec.instance);
            derializers.put(Class.forName("java.awt.Color"), ColorCodec.instance);
        } catch (Throwable e) {
            // skip
        }
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type instanceof Class<?>) {
            return getDeserializer((Class<?>) type, type);
        }

        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                return getDeserializer((Class<?>) rawType, type);
            } else {
                return getDeserializer(rawType);
            }
        }

        return JavaObjectDeserializer.instance;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type == null) {
            type = clazz;
        }

        derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        JSONType annotation = clazz.getAnnotation(JSONType.class);
		if(annotation!=null && annotation.deserializer()!=Void.class){
			derializer=getCustomDeserializer(annotation.deserializer());
			putDeserializer(type, derializer);
			return derializer;
		}
        {
            if (annotation != null) {
                Class<?> mappingTo = annotation.mappingTo();
                if (mappingTo != Void.class) {
                    return getDeserializer(mappingTo, mappingTo);
                }
            }
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            derializer = derializers.get(clazz);
        }

        if (derializer != null) {
            return derializer;
        }

        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            return ArrayDeserializer.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            derializer = CollectionDeserializer.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionDeserializer.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, derializer);

        return derializer;
    }
    
    private ObjectDeserializer getCustomDeserializer(Class<?> serializer) {
		Method m;
		try{
			m=serializer.getDeclaredMethod("getDeserializer"); //trt to get singleton instance.
			m.setAccessible(true);
		}catch(NoSuchMethodException e){
			try {
				return (ObjectDeserializer) serializer.newInstance();//call empty constructor
			} catch (Exception e1) {
				throw new IllegalStateException(e1);
			}
		}
		try{
			Object o=m.invoke(null);
			Assert.notNull(o);
			return (ObjectDeserializer)o;
		}catch(Exception e){
			throw new IllegalStateException(e);
		}
	}
    

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.asmEnable;
        if (asmEnable && !Modifier.isPublic(clazz.getModifiers())) {
            asmEnable = false;
        }

        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }

        if (ASMDeserializerFactory.getInstance().isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);
            if (beanInfo.getFieldList().size() > 200) {
                asmEnable = false;
            }

            Constructor<?> defaultConstructor = beanInfo.getDefaultConstructor();
            if (defaultConstructor == null && !clazz.isInterface()) {
                asmEnable = false;
            }

            for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
                if (fieldInfo.isGetOnly()) {
                    asmEnable = false;
                    break;
                }

                Class<?> fieldClass = fieldInfo.getFieldClass();
                if (!Modifier.isPublic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers())) {
                    asmEnable = false;
                }
            }
        }

        if (asmEnable) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                asmEnable = false;
            }
        }

        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }

        try {
            return ASMDeserializerFactory.getInstance().createJavaBeanDeserializer(this, clazz, type);
            // } catch (VerifyError e) {
            // e.printStackTrace();
            // return new JavaBeanDeserializer(this, clazz, type);
        } catch (JSONException asmError) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (Exception e) {
            throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        boolean asmEnable = this.asmEnable;

        if (!Modifier.isPublic(clazz.getModifiers())) {
            asmEnable = false;
        }

        if (fieldInfo.getFieldClass() == Class.class) {
            asmEnable = false;
        }

        if (ASMDeserializerFactory.getInstance().isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (!asmEnable) {
            return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
        }

        try {
            return ASMDeserializerFactory.getInstance().createFieldDeserializer(mapping, clazz, fieldInfo);
        } catch (Throwable e) {
            // skip
        }

        return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
    }

    public FieldDeserializer createFieldDeserializerWithoutASM(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.getFieldClass();

        if (fieldClass == boolean.class || fieldClass == Boolean.class) {
            return new BooleanFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == int.class || fieldClass == Integer.class) {
            return new IntegerFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == long.class || fieldClass == Long.class) {
            return new LongFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == String.class) {
            return new StringFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        derializers.put(type, deserializer);
        if(deserializer instanceof JsonTypeDeserializer){
        	structDerializer.put(type, (JsonTypeDeserializer)deserializer);
        }
    }
    
    public <T> JsonTypeDeserializer<T> getStructDeserializer(Type type){
    	return structDerializer.get(type);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.getFieldClass(), fieldInfo.getFieldType());
    }

    public boolean isPrimitive(Class<?> clazz) {
        return primitiveClasses.contains(clazz);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = getField0(clazz, fieldName);
        if (field == null) {
            field = getField0(clazz, "_" + fieldName);
        }
        if (field == null) {
            field = getField0(clazz, "m_" + fieldName);
        }
        return field;
    }

    private static Field getField0(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            return getField(clazz.getSuperclass(), fieldName);
        }

        return null;
    }

    public Map<String, FieldDeserializer> getFieldDeserializers(Class<?> clazz) {
        ObjectDeserializer deserizer = getDeserializer(clazz);

        if (deserizer instanceof JavaBeanDeserializer) {
            return ((JavaBeanDeserializer) deserizer).getFieldDeserializerMap();
        } else if (deserizer instanceof ASMJavaBeanDeserializer) {
            return ((ASMJavaBeanDeserializer) deserizer).getInnterSerializer().getFieldDeserializerMap();
        } else {
            return Collections.emptyMap();
        }
    }

}
