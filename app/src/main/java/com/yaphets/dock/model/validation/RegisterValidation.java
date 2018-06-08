package com.yaphets.dock.model.validation;

import android.util.Log;

import com.yaphets.dock.model.Accessible;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.storage.database.dao.MySqlDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterValidation implements Validator {
	private static final String TAG = "RegisterValidation";

	@Override
	public Result isValid(Accessible item) {
		if (! (item instanceof UserInfo)) {
			throw new IllegalArgumentException("accessible不是UserInfo实例对象");
		}
		UserInfo info = (UserInfo) item;

		
		Connection con = null;
		PreparedStatement ps = null;
		Result rst = new Result();

		try {
			con = MySqlDAO.getConnection();
			String sql = "INSERT INTO user(email, password, nickname) VALUES(?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, info.getEmail());
			ps.setString(2, info.getPassword());
			ps.setString(3, info.getNickname());
			rst.setCode(ps.executeUpdate());
			return rst;
		} catch (SQLException e) {
			Log.e(TAG, "isValid: ", e);
			rst.setCode(e.getErrorCode());
			rst.setMessage(e.getMessage());
			return rst;
		} finally {
			MySqlDAO.release(con, ps);
		}
	}

}
