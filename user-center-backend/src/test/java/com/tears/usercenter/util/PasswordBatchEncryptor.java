package com.tears.usercenter.util;

import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * 批量加密数据库中所有用户的密码，并更新回数据库。
 * 运行前请务必备份数据库！
 */
public class PasswordBatchEncryptor {
    public static final String SALT = "tears";

    public static void main(String[] args) throws Exception {
        // 1. 连接数据库（请根据实际情况修改）
        String url = "jdbc:mysql://localhost:3306/user_center?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url, user, password);

        // 2. 查询所有用户
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, userPassword FROM user");

        // 3. 批量加密并更新
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE user SET userPassword=? WHERE id=?");
        int count = 0;
        while (rs.next()) {
            long id = rs.getLong("id");
            String plainPwd = rs.getString("userPassword");
            // 用和后端登录完全一致的加密方式（加盐+MD5+UTF-8编码）
            String encryptedPwd = DigestUtils.md5DigestAsHex((SALT + plainPwd).getBytes(StandardCharsets.UTF_8));
            updateStmt.setString(1, encryptedPwd);
            updateStmt.setLong(2, id);
            updateStmt.addBatch();
            count++;
        }
        updateStmt.executeBatch();

        System.out.println("批量加密并更新完成！共处理 " + count + " 条记录。");
        conn.close();
    }
}