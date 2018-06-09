package com.yaphets.storage.database.dao;

import com.yaphets.storage.annotation.Column;
import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.annotation.ManyToOne;
import com.yaphets.storage.annotation.OneToMany;
import com.yaphets.storage.util.DBUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GenericDAO {
	/**
	 * 	往MySql数据库插入一条记录，table为T类型的表
	 * @param obj
	 * 	T类型的泛型对象 将持久化的对象
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问MySql数据库可能抛出的异常
	 */
	public static <T> int insert(T obj) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			ArrayList<Field> fieldList = new ArrayList<>(); // 用来标记使用了的field
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(getInsertSql(obj, fieldList));
			DBUtils.initPreparedStatement(ps, fieldList, obj);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}
	
	/**
	 * 	删除持久化对象t，删除数据库的记录
	 * @param obj
	 * 	T类型的泛型对象 准备瞬态化的对象
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问MySql数据库可能抛出的异常
	 */
	public static <T> int delete(T obj) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			ArrayList<Field> fieldList = new ArrayList<>(); // 用来标记使用了的field
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(getDeleteSql(obj, fieldList));
			DBUtils.initPreparedStatement(ps, fieldList, obj);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}
	
	/**
	 * 	查找由t对象包含的条件的持久化对象
	 * @param obj
	 * 	T类型的泛型对象 保存了查找的条件
	 * @return
	 * 	查询返回的对象
	 * @throws SQLException
	 * 	访问MySql数据库可能抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T find(T obj) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ArrayList<Field> fieldList = new ArrayList<>(); // 用来标记使用了的field
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(getFindSql(obj, fieldList));
			DBUtils.initPreparedStatement(ps, fieldList, obj);
			rs = ps.executeQuery();
			T newOj = (T) DBUtils.createInstance(rs, obj.getClass());
			return newOj;
		} catch (SQLException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}
	
	/**
	 * 	更新持久化对象的信息，修改远端数据库
	 * @param obj
	 * 	T类型的泛型对象 保存了更改信息的对象
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问MySql数据库可能抛出的异常
	 */
	public static <T> int update(T obj) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			ArrayList<Field> fieldList = new ArrayList<>(); // 用来标记使用了的field
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(getUpdateSql(obj, fieldList));
			DBUtils.initPreparedStatement(ps, fieldList, obj);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}
	
	/**
	 * 	构造insert语句
	 * @param obj
	 * 	T类型的泛型对象 将持久化的对象
	 * @param fieldList
	 * 	保存可用的Field对象，用于充填values()子句
	 * @return
	 * 	SQL语句
	 */
	private static <T> String getInsertSql(T obj, List<Field> fieldList){
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder builder1 = new StringBuilder("INSERT INTO " + clazz.getSimpleName().toLowerCase() + " (");
		StringBuilder builder2 = new StringBuilder(" VALUES(");
		ManyToOne ann;
		try {
			for (Field field : fields) {
				field.setAccessible(true);

				if (passField(obj, field)) {
					continue;
				}
				if ((ann=field.getAnnotation(ManyToOne.class)) != null) {
					builder1.append(ann.name() + ", ");
				} else {
					builder1.append(field.getName() + ", ");
				}
				builder2.append("?, ");
				fieldList.add(field);

			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		builder1.replace(builder1.length() - 2, builder1.length(), ")");
		builder2.replace(builder2.length() - 2, builder2.length(), ")");
		builder1.append(builder2.toString());

		return builder1.toString();
	}
	
	/**
	 * 	构造delete语句
	 * @param obj
	 * 	T类型的泛型对象 保存了删除的条件
	 * @param fieldList
	 * 	保存可用的Field对象，用于充填where从句
	 * @return
	 * 	SQL语句
	 */
	private static <T> String getDeleteSql(T obj, List<Field> fieldList) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		StringBuilder builder = new StringBuilder("DELETE FROM " + clazz.getSimpleName().toLowerCase() + " WHERE ");
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				
				if (passField(obj, field)) {
					continue;
				}
				builder.append(field.getName() + "=? and ");
				fieldList.add(field);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		builder.replace(builder.length() - 5, builder.length(), "");
		
		return builder.toString();
	}
	
	/**
	 * 	构造select语句
	 * @param obj
	 * 	T类型的泛型对象 保存了查找的条件
	 * @param fieldList
	 * 	保存可用的Field对象，用于充填where从句
	 * @return
	 * 	SQL语句
	 */
	private static <T> String getFindSql(T obj, List<Field> fieldList) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();

		StringBuilder builder = new StringBuilder("SELECT * FROM " + clazz.getSimpleName().toLowerCase() + " WHERE ");
		try {
			for (Field field : fields) {
				field.setAccessible(true);

				if (passField(obj, field)) {
					continue;
				}
				builder.append(field.getName() + "=? and ");
				fieldList.add(field);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		builder.replace(builder.length() - 5, builder.length(), "");
		
		return builder.toString();
	}
	
	/**
	 * 	以id（主键）来找到数据并跟新
	 * @param obj
	 * 	obj对象需要包含只有一个@Id的注解用来标注主键字段
	 * @param fieldList
	 * 	保存可用的Field对象，用于充填set，where从句
	 * @return
	 * 	SQL语句
	 */
	private static <T> String getUpdateSql(T obj, List<Field> fieldList) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		StringBuilder builder = new StringBuilder("UPDATE " + clazz.getSimpleName().toLowerCase() + " SET ");
		try {
			int[] mark = {-1,-1,-1,-1};
			int j = 0;
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				//判断是否为@Id字段，即primary key字段
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					mark[j++] = i;	//记录Id字段
					continue;
				}
				if (passField(obj, field)) {
					continue;
				}
				builder.append(field.getName() + "=?, ");
				fieldList.add(field);
			}
			builder.deleteCharAt(builder.lastIndexOf(","));
			//设置where从句 , 如果mark为-1就让他index out of range奔溃吧...懒得处理了
			j = 0;
			builder.append("WHERE ");
			Field idCol = fields[mark[j++]];
			builder.append(idCol.getName() + "=?");
			while(mark[j++] != -1) {
				builder.append(" and ");
				idCol = fields[mark[j++]];
				builder.append(idCol.getName() + "=?");
			}
			fieldList.add(idCol);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	/**
	 * 	判断是否字段未赋值,字段为静态字段 或者是字段为一对多的set, 以及是ignore为true，是则返回true
	 * @param obj
	 * @param field
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static <T> boolean passField(T obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		Object d = DBUtils.getDefault(field.getType());
		Object f = field.get(obj);
		OneToMany otm = field.getAnnotation(OneToMany.class);
		Column col = field.getAnnotation(Column.class);
		return d == null && f == null || f.equals(d) 
				|| Modifier.isStatic(field.getModifiers()) || otm != null
				|| (col != null && col.ignore());
	}

	/**
	 * 	从数据库中查找T对应表的所有记录，返回一个List存放查询的对象
	 * @param clazz
	 * 	表的实体类类型
	 * @return
	 * 	返回查询的实体类T的对象列表List
	 * @throws SQLException
	 * 	访问数据库异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz) throws SQLException {
		List<T> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase();
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			T newOj;
			while ((newOj = (T) DBUtils.createInstance(rs, clazz)) != null) {
				list.add(newOj);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
		return list;
	}

	/**
	 * 	从数据库中批量查找数据，表名为T的类名，where为筛选条件，如："name=?"; params为充填?的参数列表,长度应为?的个数
	 * @param clazz
	 * 	表的实体类类型
	 * @param where
	 * 	where条件从句
	 * @param params
	 * 	充填?的参数列表，参数顺序必须为?对应的值的顺序
	 * @return
	 * 	返回查询的实体类T的对象列表List
	 * @throws SQLException
	 * 	访问数据库异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz, String where, Object...params) throws SQLException {
		List<T> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() 
					+ " WHERE " + where;
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			DBUtils.initPreparedStatement(ps, params);
			rs = ps.executeQuery();
			T newOj;
			while ((newOj = (T) DBUtils.createInstance(rs, clazz)) != null) {
				list.add(newOj);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
		return list;
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz, Object owner, String where, Object...params) throws SQLException {
		List<T> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() 
					+ " WHERE " + where;
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			DBUtils.initPreparedStatement(ps, params);
			rs = ps.executeQuery();
			T newOj;
			while ((newOj = (T) DBUtils.createInstance(rs, clazz, owner)) != null) {
				list.add(newOj);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
		return list;
	}

	/**
	 * 	从数据库中批量删除数据，表名为clazz的类名，where为筛选条件，如："name=?"; params为充填?的参数列表,长度应为?的个数
	 * @param clazz
	 * 	表的实体类类型
	 * @param where
	 * 	where条件从句
	 * @param params
	 * 	充填?的参数列表，参数顺序必须为?对应的值的顺序
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问数据库异常
	 */
	public static int deleteAll(Class<?> clazz, String where, Object...params) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "DELETE FROM " + clazz.getSimpleName().toLowerCase() + " WHERE " + where;
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			DBUtils.initPreparedStatement(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}

	/**
	 * 	从数据库中删除clazz对应表的所有数据行
	 * @param clazz
	 * 	表的实体类类型
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问数据库异常
	 */
	public static int deleteAll(Class<?> clazz) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "DELETE FROM " + clazz.getSimpleName().toLowerCase();
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}

	/**
	 * 	从数据库中批量更新数据，表名为clazz的类名，set为要更新的列，where为筛选条件，如："name=?"; 
	 * 	params为充填?的参数列表,长度应为?的个数
	 * @param clazz
	 * 	表的实体类类型
	 * @param set
	 * 	set条件从句
	 * @param where
	 * 	where条件从句
	 * @param params
	 * 	充填?的参数列表，参数顺序必须为?对应的值的顺序
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 *  访问数据库异常
	 */
	public static int updateAll(Class<?> clazz, String set, String where, Object...params) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "UPDATE " + clazz.getSimpleName().toLowerCase() + " SET " + set 
					+ " WHERE " + where;
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			DBUtils.initPreparedStatement(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}

	/**
	 * 	从数据库中更新所有数据，表名为clazz的类名，set为要更新的列，如："name=?"; 
	 * @param clazz
	 * 	表的实体类类型
	 * @param set
	 * 	set条件从句
	 * @param params
	 * 	充填?的参数列表，参数顺序必须为?对应的值的顺序
	 * @return
	 * 	受到影响的记录条数
	 * @throws SQLException
	 * 	访问数据库异常
	 */
	public static int updateAll(Class<?> clazz, String set, Object...params) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "UPDATE " + clazz.getSimpleName().toLowerCase() + " SET " + set;
			con = MySqlDAO.getConnection();
			ps = con.prepareStatement(sql);
			DBUtils.initPreparedStatement(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}
}








