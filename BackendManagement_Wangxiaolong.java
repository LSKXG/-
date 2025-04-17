import java.util.*;
import java.io.*;

// 用户类
class User {
    private String username;
    private String password;
    private boolean isBanned;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isBanned = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}

// 用户管理类
class UserManagement {
    private Map<String, User> users;

    public UserManagement() {
        this.users = new HashMap<>();
    }

    // 注册用户
    public void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            User user = new User(username, password);
            users.put(username, user);
            logOperation("注册用户: " + username);
            System.out.println("用户注册成功");
        } else {
            System.out.println("用户名已存在");
        }
    }

    // 用户登录
    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password) &&!user.isBanned()) {
            logOperation("用户登录: " + username);
            System.out.println("用户登录成功");
            return true;
        } else {
            System.out.println("用户名或密码错误，或用户已被封禁");
            return false;
        }
    }

    // 处理密码修改请求
    public void changePassword(String username, String newPassword) {
        User user = users.get(username);
        if (user != null) {
            user = new User(username, newPassword);
            users.put(username, user);
            logOperation("修改用户密码: " + username);
            System.out.println("密码修改成功");
        } else {
            System.out.println("用户不存在");
        }
    }

    // 封禁用户
    public void banUser(String username) {
        User user = users.get(username);
        if (user != null) {
            user.setBanned(true);
            logOperation("封禁用户: " + username);
            System.out.println("用户已被封禁");
        } else {
            System.out.println("用户不存在");
        }
    }

    // 获取用户信息
    public User getUser(String username) {
        return users.get(username);
    }
}

// 数据备份与恢复类
class DataBackupRestore {
    private String backupPath;
    private String backupTime;
    private String backupStrategy;

    public DataBackupRestore(String backupPath, String backupTime, String backupStrategy) {
        this.backupPath = backupPath;
        this.backupTime = backupTime;
        this.backupStrategy = backupStrategy;
    }

    // 执行数据备份
    public void backupData(Map<String, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(backupPath))) {
            oos.writeObject(data);
            logOperation("数据备份成功");
            System.out.println("数据备份成功");
        } catch (IOException e) {
            logOperation("数据备份失败: " + e.getMessage());
            System.out.println("数据备份失败");
        }
    }

    // 执行数据恢复
    public Map<String, User> restoreData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(backupPath))) {
            @SuppressWarnings("unchecked")
            Map<String, User> data = (Map<String, User>) ois.readObject();
            logOperation("数据恢复成功");
            System.out.println("数据恢复成功");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            logOperation("数据恢复失败: " + e.getMessage());
            System.out.println("数据恢复失败");
            return null;
        }
    }
}

// 日志管理类
class LogManagement {
    private List<String> logs;

    public LogManagement() {
        this.logs = new ArrayList<>();
    }

    // 记录操作日志
    public void logOperation(String operation) {
        logs.add(operation);
    }

    // 查看日志
    public void viewLogs() {
        for (String log : logs) {
            System.out.println(log);
        }
    }

    // 筛选日志
    public void filterLogs(String keyword) {
        for (String log : logs) {
            if (log.contains(keyword)) {
                System.out.println(log);
            }
        }
    }

    // 清理过期日志
    public void clearLogs() {
        logs.clear();
        logOperation("日志清理成功");
        System.out.println("日志清理成功");
    }
}

// 主类
public class Main {
    public static void main(String[] args) {
        UserManagement userManagement = new UserManagement();
        DataBackupRestore dataBackupRestore = new DataBackupRestore("backup.data", "每天凌晨2点", "全量备份");
        LogManagement logManagement = new LogManagement();

        // 用户注册
        userManagement.registerUser("user1", "password1");

        // 用户登录
        userManagement.loginUser("user1", "password1");

        // 修改密码
        userManagement.changePassword("user1", "newPassword1");

        // 封禁用户
        userManagement.banUser("user1");

        // 数据备份
        dataBackupRestore.backupData(userManagement.users);

        // 模拟数据丢失，进行数据恢复
        userManagement.users.clear();
        userManagement.users = dataBackupRestore.restoreData();

        // 查看日志
        logManagement.viewLogs();

        // 筛选日志
        logManagement.filterLogs("用户");

        // 清理日志
        logManagement.clearLogs();
    }
}
