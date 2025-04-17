import java.io.*;
import java.util.*;

// 用户角色枚举
enum UserRole {
    CLUB_LEADER,
    STUDENT,
    ADMIN,
    CLUB_TUTOR
}

// 用户类
class User {
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}

// 活动类
class Activity {
    private String theme;
    private String time;
    private String location;
    private String planFilePath;
    private boolean isApproved;
    private String summaryFilePath;

    public Activity(String theme, String time, String location, String planFilePath) {
        this.theme = theme;
        this.time = time;
        this.location = location;
        this.planFilePath = planFilePath;
        this.isApproved = false;
        this.summaryFilePath = null;
    }

    public String getTheme() {
        return theme;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getPlanFilePath() {
        return planFilePath;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void approve() {
        this.isApproved = true;
    }

    public String getSummaryFilePath() {
        return summaryFilePath;
    }

    public void setSummaryFilePath(String summaryFilePath) {
        this.summaryFilePath = summaryFilePath;
    }
}

// 活动管理系统类
class ActivityManagementSystem {
    private Map<String, User> users;
    private List<Activity> activities;
    private Map<Activity, List<String>> signIns;
    private Map<Activity, List<String>> evaluations;
    private static final String USERS_FILE = "users.dat";
    private static final String ACTIVITIES_FILE = "activities.dat";

    public ActivityManagementSystem() {
        this.users = loadUsers();
        this.activities = loadActivities();
        this.signIns = new HashMap<>();
        this.evaluations = new HashMap<>();
    }

    // 用户注册
    public void registerUser(String username, String password, UserRole role) {
        users.put(username, new User(username, password, role));
        saveUsers();
    }

    // 用户登录
    public boolean login(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    // 创建活动
    public Activity createActivity(String username, String theme, String time, String location, String planFilePath) {
        if (login(username, users.get(username).getPassword()) && users.get(username).getRole() == UserRole.CLUB_LEADER) {
            Activity activity = new Activity(theme, time, location, planFilePath);
            activities.add(activity);
            saveActivities();
            return activity;
        }
        return null;
    }

    // 活动审核
    public void approveActivity(Activity activity, String approverUsername) {
        User approver = users.get(approverUsername);
        if (approver != null && (approver.getRole() == UserRole.ADMIN || approver.getRole() == UserRole.CLUB_TUTOR)) {
            activity.approve();
            saveActivities();
        }
    }

    // 活动报名
    public boolean signUp(String username, Activity activity) {
        if (login(username, users.get(username).getPassword()) && users.get(username).getRole() == UserRole.STUDENT && activity.isApproved()) {
            // 模拟报名信息填写
            return true;
        }
        return false;
    }

    // 活动签到
    public void signIn(Activity activity, String username) {
        if (!signIns.containsKey(activity)) {
            signIns.put(activity, new ArrayList<>());
        }
        signIns.get(activity).add(username);
    }

    // 活动评价
    public void evaluateActivity(Activity activity, String username, String evaluation) {
        if (login(username, users.get(username).getPassword()) && users.get(username).getRole() == UserRole.STUDENT) {
            if (!evaluations.containsKey(activity)) {
                evaluations.put(activity, new ArrayList<>());
            }
            evaluations.get(activity).add(evaluation);
        }
    }

    // 活动总结
    public void summarizeActivity(Activity activity, String username, String summaryFilePath) {
        if (login(username, users.get(username).getPassword()) && users.get(username).getRole() == UserRole.CLUB_LEADER) {
            activity.setSummaryFilePath(summaryFilePath);
            saveActivities();
        }
    }

    // 获取活动列表
    public List<Activity> getActivities() {
        return activities;
    }

    // 获取活动签到信息
    public List<String> getSignIns(Activity activity) {
        return signIns.getOrDefault(activity, new ArrayList<>());
    }

    // 获取活动评价信息
    public List<String> getEvaluations(Activity activity) {
        return evaluations.getOrDefault(activity, new ArrayList<>());
    }

    // 保存用户数据
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载用户数据
    @SuppressWarnings("unchecked")
    private Map<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    // 保存活动数据
    private void saveActivities() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACTIVITIES_FILE))) {
            oos.writeObject(activities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载活动数据
    @SuppressWarnings("unchecked")
    private List<Activity> loadActivities() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACTIVITIES_FILE))) {
            return (List<Activity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}

// 主类，用于测试
public class Main {
    public static void main(String[] args) {
        ActivityManagementSystem system = new ActivityManagementSystem();

        // 注册用户
        system.registerUser("clubLeader", "123456", UserRole.CLUB_LEADER);
        system.registerUser("student1", "123456", UserRole.STUDENT);
        system.registerUser("admin", "123456", UserRole.ADMIN);
        system.registerUser("tutor", "123456", UserRole.CLUB_TUTOR);

        // 社团负责人创建活动
        Activity activity = system.createActivity("clubLeader", "社团活动", "2025-04-20", "学校礼堂", "activity_plan.txt");

        // 活动审核
        system.approveActivity(activity, "admin");

        // 学生报名活动
        boolean signedUp = system.signUp("student1", activity);
        System.out.println("学生报名结果: " + (signedUp ? "成功" : "失败"));

        // 活动签到
        system.signIn(activity, "student1");

        // 活动评价
        system.evaluateActivity(activity, "student1", "活动组织得很好");

        // 社团负责人撰写活动总结
        system.summarizeActivity(activity, "clubLeader", "activity_summary.txt");

        // 输出活动信息
        System.out.println("活动主题: " + activity.getTheme());
        System.out.println("活动时间: " + activity.getTime());
        System.out.println("活动地点: " + activity.getLocation());
        System.out.println("活动方案文件路径: " + activity.getPlanFilePath());
        System.out.println("活动是否审核通过: " + activity.isApproved());
        System.out.println("活动签到信息: " + system.getSignIns(activity));
        System.out.println("活动评价信息: " + system.getEvaluations(activity));
        System.out.println("活动总结文件路径: " + activity.getSummaryFilePath());
    }
}    
