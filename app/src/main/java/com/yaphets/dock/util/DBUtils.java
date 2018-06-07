package com.yaphets.dock.util;

import com.yaphets.dock.model.annotation.ManyToOne;
import com.yaphets.dock.model.annotation.OneToMany;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;


public class DBUtils {
	private static HashMap<Class<?>, Object> typeDefault = new HashMap<>();
	static {
		typeDefault.put(int.class, 0);
		typeDefault.put(long.class, 0l);
		typeDefault.put(short.class, 0);
		typeDefault.put(float.class, 0.0f);
		typeDefault.put(double.class, 0.0);
		typeDefault.put(boolean.class, false);
	}
	
	/**
	 * 	获取clazz类型的默认值
	 * @param clazz
	 * 	类型
	 * @return
	 *  默认值
	 */
	public static Object getDefault(Class<?> clazz) {
		return typeDefault.get(clazz);
	}
	
	/**
	 * 从list里的数据来初始化PreparedStatement的参数
	 * @param ps
	 * 	PreparedStatement对象
	 * @param list
	 * 	存放Field对象的列表
	 * @param t
	 * 	Field的所属泛型对象
	 * @throws SQLException
	 * 	SQL异常
	 * @throws NoSuchFieldException 
	 * 	如果注解ManyToOne没有正确填写mappedBy，导致在外键引用的对象中找不到mappedBy所指的数据域，将引发异常
	 */
	public static <T> void initPreparedStatement(PreparedStatement ps, List<Field> list, T t) throws SQLException, NoSuchFieldException {
		try {
			for (int i = 1; i <= list.size(); i++) {
				Field field = list.get(i-1);

				ManyToOne ann;
				if ((ann=field.getAnnotation(ManyToOne.class)) != null) {
					System.out.println(ann.name());
					Object obj_t = field.get(t);
					Class<?> c = field.getType();
					Field field_t = c.getDeclaredField(ann.mappedBy());
					field_t.setAccessible(true);
					initPreStatementKit(ps, obj_t, i, field_t);
				} else {
					initPreStatementKit(ps, t, i, field);
				}				
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	从params里的数据来初始化PreparedStatement的参数
	 * @param ps
	 * 	PreparedStatement对象
	 * @param params
	 *  充填?的参数数组
	 * @throws SQLException
	 * 	SQL异常
	 */
	public static void initPreparedStatement(PreparedStatement ps, Object[] params) throws SQLException {
		for (int i = 1; i <= params.length; i++) {
			Object object = params[i-1];
			Class<?> clazz = object.getClass();
			
			if (clazz.equals(Integer.class)) {
				ps.setInt(i, (int) object);
			} else if (clazz.equals(String.class)) {
				ps.setString(i, (String) object);
			} else if (clazz.equals(Long.class)) {
				ps.setLong(i, (long) object);
			} else if (clazz.equals(Short.class)) {
				ps.setShort(i, (short) object);
			} else if (clazz.equals(Float.class)) {
				ps.setFloat(i, (float) object);
			} else if (clazz.equals(Double.class)) {
				ps.setDouble(i, (double) object);
			} else if (clazz.equals(Timestamp.class)) {
				ps.setTimestamp(i, (Timestamp) object);
			} else if (clazz.equals(Boolean.class)) {
				ps.setBoolean(i, (boolean) object);
			} else if (clazz.equals(Byte.class)) {
				ps.setByte(i, (byte) object);
			} else if (clazz.equals(Blob.class)) {
				ps.setBlob(i, (Blob) object);
			}
		}
	}
	
	/**
	 * 	从查询的ResultSet，构造一个clazz类型对象
	 * @param rs
	 * 	ResultSet对象
	 * @param clazz
	 * 	构造对象类型
	 * @return
	 * 	如果ResultSet不为空，则返回新对象，否则返回null
	 * @throws SQLException
	 * 	操纵ResultSet可能发生的SQL异常
	 */
	public static Object createInstance(ResultSet rs, Class<?> clazz) throws SQLException {
		try {
			Constructor<?> cst = clazz.getConstructor();
			Field[] fields = clazz.getDeclaredFields();
			Object oj = cst.newInstance();
			
			if (rs.next()) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					
					ManyToOne ann;
					if ((ann=field.getAnnotation(ManyToOne.class)) != null) {
						Object key = rs.getObject(ann.name());
						Class<?> cls = field.getType();
						Method mtd = cls.getMethod("createInstance", long.class);
						Object referOne = mtd.invoke(null, key);
						field.set(oj, referOne);
					} else if(field.getAnnotation(OneToMany.class) != null){
						PersistSet<?> set = new PersistSet<>(oj, field);
						field.set(oj, set);
					} else {
						if (passField(field))
							continue;
						field.set(oj, rs.getObject(field.getName()));
					}
				}
				return oj;
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object createInstance(ResultSet rs, Class<?> clazz, Object owner) throws SQLException {
		try {
			Constructor<?> cst = clazz.getConstructor();
			Field[] fields = clazz.getDeclaredFields();
			Object oj = cst.newInstance();
			
			if (rs.next()) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					
					if (field.getAnnotation(ManyToOne.class) != null) {
						field.set(oj, owner);
					} else {
						if (passField(field))
							continue;
						field.set(oj, rs.getObject(field.getName()));
					}
				}
				return oj;
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static <T> void initPreStatementKit(PreparedStatement ps, T obj, int idx, Field field) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Class<?> clazz = field.getType();
		
		if (clazz.equals(int.class)) {
			ps.setInt(idx, field.getInt(obj));
		} else if (clazz.equals(String.class)) {
			ps.setString(idx, (String) field.get(obj));
		} else if (clazz.equals(long.class)) {
			ps.setLong(idx, field.getLong(obj));
		} else if (clazz.equals(short.class)) {
			ps.setShort(idx, field.getShort(obj));
		} else if (clazz.equals(float.class)) {
			ps.setFloat(idx, field.getFloat(obj));
		} else if (clazz.equals(double.class)) {
			ps.setDouble(idx, field.getDouble(obj));
		} else if (clazz.equals(Timestamp.class)) {
			ps.setTimestamp(idx, (Timestamp) field.get(obj));
		} else if (clazz.equals(boolean.class)) {
			ps.setBoolean(idx, field.getBoolean(obj));
		} else if (clazz.equals(byte.class)) {
			ps.setByte(idx, field.getByte(obj));
		} else if (clazz.equals(Blob.class)) {
			ps.setBlob(idx, (Blob) field.get(obj));
		}
	}
	
	/**
	 * 	如果是静态数据域则不构建，返回true
	 * @param field
	 * 	数据域
	 * @return
	 */
	private static final boolean passField(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}
}
