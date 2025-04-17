import java.util.*;

// 定义成员类
class Member {
    private String id;
    private String name;
    private String role;
    private int activityParticipation;
    private int contributionDegree;
    private String permission;
    private String evaluationResult;

    public Member(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.activityParticipation = 0;
        this.contributionDegree = 0;
        this.permission = "";
        this.evaluationResult = "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getActivityParticipation() {
        return activityParticipation;
    }

    public void setActivityParticipation(int activityParticipation) {
        this.activityParticipation = activityParticipation;
    }

    public int getContributionDegree() {
        return contributionDegree;
    }

    public void setContributionDegree(int contributionDegree) {
        this.contributionDegree = contributionDegree;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(String evaluationResult) {
        this.evaluationResult = evaluationResult;
    }
}

// 定义社团成员管理系统类
class ClubMemberManagementSystem {
    private Map<String, Member> members;

    public ClubMemberManagementSystem() {
        this.members = new HashMap<>();
    }

    // 成员信息管理：录入、修改、删除成员信息
    public void manageMemberInfo(String operator, String id, String name, String role, String operation) {
        if ("社团负责人".equals(operator) || "管理员".equals(operator)) {
            if ("录入".equals(operation)) {
                Member member = new Member(id, name, role);
                members.put(id, member);
                System.out.println("成员信息录入成功");
            } else if ("修改".equals(operation)) {
                if (members.containsKey(id)) {
                    Member member = members.get(id);
                    member.setName(name);
                    member.setRole(role);
                    System.out.println("成员信息修改成功");
                } else {
                    System.out.println("未找到该成员信息");
                }
            } else if ("删除".equals(operation)) {
                if (members.containsKey(id)) {
                    members.remove(id);
                    System.out.println("成员信息删除成功");
                } else {
                    System.out.println("未找到该成员信息");
                }
            } else {
                System.out.println("不支持的操作");
            }
        } else {
            System.out.println("您没有权限进行此操作");
        }
    }

    // 成员权限管理：设置成员权限
    public void manageMemberPermission(String operator, String id, String permission) {
        if ("社团负责人".equals(operator)) {
            if (members.containsKey(id)) {
                Member member = members.get(id);
                member.setPermission(permission);
                System.out.println("权限设置成功");
            } else {
                System.out.println("未找到该成员信息");
            }
        } else {
            System.out.println("您没有权限进行此操作");
        }
    }

    // 成员考核管理：制定考核标准、进行考核、录入考核结果、反馈考核结果
    public void evaluateMembers(String operator, String[] criteria, Map<String, Integer> participationMap, Map<String, Integer> contributionMap) {
        if ("社团负责人".equals(operator)) {
            for (Map.Entry<String, Member> entry : members.entrySet()) {
                Member member = entry.getValue();
                int participation = participationMap.getOrDefault(member.getId(), 0);
                int contribution = contributionMap.getOrDefault(member.getId(), 0);
                member.setActivityParticipation(participation);
                member.setContributionDegree(contribution);

                // 简单的考核逻辑，这里可以根据实际考核标准进行调整
                String evaluation = "";
                if (participation >= 5 && contribution >= 5) {
                    evaluation = "优秀";
                } else if (participation >= 3 && contribution >= 3) {
                    evaluation = "良好";
                } else {
                    evaluation = "一般";
                }
                member.setEvaluationResult(evaluation);
                System.out.println("成员 " + member.getName() + " 的考核结果：" + evaluation);
            }
        } else {
            System.out.println("您没有权限进行此操作");
        }
    }

    // 获取成员信息
    public void getMemberInfo(String id) {
        if (members.containsKey(id)) {
            Member member = members.get(id);
            System.out.println("成员ID: " + member.getId());
            System.out.println("成员姓名: " + member.getName());
            System.out.println("成员角色: " + member.getRole());
            System.out.println("活动参与度: " + member.getActivityParticipation());
            System.out.println("贡献度: " + member.getContributionDegree());
            System.out.println("权限: " + member.getPermission());
            System.out.println("考核结果: " + member.getEvaluationResult());
        } else {
            System.out.println("未找到该成员信息");
        }
    }
}

// 主类
public class Main {
    public static void main(String[] args) {
        ClubMemberManagementSystem system = new ClubMemberManagementSystem();

        // 成员信息录入
        system.manageMemberInfo("社团负责人", "1", "张三", "普通成员", "录入");

        // 成员权限设置
        system.manageMemberPermission("社团负责人", "1", "查看社团资料");

        // 模拟活动参与情况和贡献度
        Map<String, Integer> participationMap = new HashMap<>();
        participationMap.put("1", 7);
        Map<String, Integer> contributionMap = new HashMap<>();
        contributionMap.put("1", 6);

        // 进行成员考核
        system.evaluateMembers("社团负责人", new String[]{"活动参与度", "贡献度"}, participationMap, contributionMap);

        // 获取成员信息
        system.getMemberInfo("1");
    }
}
