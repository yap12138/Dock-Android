package com.yaphets.storage.util;


import com.yaphets.storage.annotation.OneToMany;
import com.yaphets.storage.database.dao.GenericDAO;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class PersistSet<E> extends HashSet<E> {

	private static final long serialVersionUID = 1L;

	private boolean _fetch = false;
	private Object _owner;
	private Field _mapField;
	
	public PersistSet(Object owner, Field field) {
		super();
		this._owner = owner;
		this._mapField = field;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		if(!_fetch) {
			try {
				OneToMany otm = _mapField.getAnnotation(OneToMany.class);
				String where = otm.mappedBy() + "=?";
				Field id = _owner.getClass().getDeclaredField(otm.name());
				id.setAccessible(true);
				long arg = id.getLong(_owner);
				List<E> list = (List<E>) GenericDAO.findAll(otm.referedEntity(), _owner, where, arg);
				this.addAll(list);
				_fetch = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.iterator();
	}
}
