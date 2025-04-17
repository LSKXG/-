import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// 社团类
class Club {
    String name;
    String category;
    String establishmentDate;
    String constitutionFile;
    boolean isApproved;

    public Club(String name, String category, String establishmentDate, String constitutionFile) {
        this.name = name;
        this.category = category;
        this.establishmentDate = establishmentDate;
        this.constitutionFile = constitutionFile;
        this.isApproved = false;
    }
}

// 社团信息管理系统类
class ClubManagementSystem {
    private Map<String, Club> clubs;
    private List<String> approvalLogs;

    public ClubManagementSystem() {
        this.clubs = new HashMap<>();
        this.approvalLogs = new ArrayList<>();
    }

    // 社团信息录入
    public void addClub(String name, String category, String establishmentDate, String constitutionFile) {
        Club club = new Club(name, category, establishmentDate, constitutionFile);
        clubs.put(name, club);
        System.out.println("社团信息录入成功，等待审核。");
    }

    // 社团信息修改
    public void modifyClub(String name, String category, String establishmentDate, String constitutionFile) {
        if (clubs.containsKey(name)) {
            Club club = clubs.get(name);
            club.category = category;
            club.establishmentDate = establishmentDate;
            club.constitutionFile = constitutionFile;
            System.out.println("社团信息修改申请已提交。");
        } else {
            System.out.println("未找到该社团信息。");
        }
    }

    // 社团信息查询
    public List<Club> queryClubs(String condition) {
        List<Club> result = new ArrayList<>();
        for (Club club : clubs.values()) {
            if (club.name.contains(condition) || club.category.contains(condition)) {
                result.add(club);
            }
        }
        return result;
    }

    // 社团审核管理
    public void approveClub(String name, boolean isApproved) {
        if (clubs.containsKey(name)) {
            Club club = clubs.get(name);
            club.isApproved = isApproved;
            String log = "社团 " + name + (isApproved ? " 审核通过" : " 审核不通过");
            approvalLogs.add(log);
            System.out.println(log + "，已通知社团发起人。");
        } else {
            System.out.println("未找到该社团信息。");
        }
    }

    // 显示审核记录
    public void showApprovalLogs() {
        for (String log : approvalLogs) {
            System.out.println(log);
        }
    }
}

// 主类
public class Main {
    public static void main(String[] args) {
        ClubManagementSystem system = new ClubManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请选择操作：");
            System.out.println("1. 社团信息录入");
            System.out.println("2. 社团信息修改");
            System.out.println("3. 社团信息查询");
            System.out.println("4. 社团审核管理");
            System.out.println("5. 显示审核记录");
            System.out.println("6. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    System.out.println("请输入社团名称：");
                    String name = scanner.nextLine();
                    System.out.println("请输入社团类别：");
                    String category = scanner.nextLine();
                    System.out.println("请输入社团成立时间：");
                    String establishmentDate = scanner.nextLine();
                    System.out.println("请输入社团章程文件路径：");
                    String constitutionFile = scanner.nextLine();
                    system.addClub(name, category, establishmentDate, constitutionFile);
                    break;
                case 2:
                    System.out.println("请输入要修改的社团名称：");
                    name = scanner.nextLine();
                    System.out.println("请输入新的社团类别：");
                    category = scanner.nextLine();
                    System.out.println("请输入新的社团成立时间：");
                    establishmentDate = scanner.nextLine();
                    System.out.println("请输入新的社团章程文件路径：");
                    constitutionFile = scanner.nextLine();
                    system.modifyClub(name, category, establishmentDate, constitutionFile);
                    break;
                case 3:
                    System.out.println("请输入查询条件（社团名称、类别等）：");
                    String condition = scanner.nextLine();
                    List<Club> result = system.queryClubs(condition);
                    if (result.isEmpty()) {
                        System.out.println("未找到符合条件的社团信息。");
                    } else {
                        for (Club club : result) {
                            System.out.println("社团名称：" + club.name);
                            System.out.println("社团类别：" + club.category);
                            System.out.println("成立时间：" + club.establishmentDate);
                            System.out.println("章程文件：" + club.constitutionFile);
                            System.out.println("审核状态：" + (club.isApproved ? "通过" : "未通过"));
                        }
                    }
                    break;
                case 4:
                    System.out.println("请输入要审核的社团名称：");
                    name = scanner.nextLine();
                    System.out.println("请输入审核结果（1. 通过；2. 不通过）：");
                    int approvalChoice = scanner.nextInt();
                    scanner.nextLine(); // 消耗换行符
                    boolean isApproved = approvalChoice == 1;
                    system.approveClub(name, isApproved);
                    break;
                case 5:
                    system.showApprovalLogs();
                    break;
                case 6:
                    System.out.println("退出系统。");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }
}    
