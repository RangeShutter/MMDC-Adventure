import java.util.ArrayList;
import java.util.List;

public class Permission {

    // Please note all details are only examples
    private int permissionID;
    private int roleID; 
    private String permissionDescription;
    private static List<Permission> permissions = new ArrayList<>();

    public Permission(int permissionID, int roleID, String permissionDescription) {
        this.permissionID = permissionID;
        this.roleID = roleID;
        this.permissionDescription = permissionDescription;
    }

    public String searchPermission(int searchID) {
        return permissions.stream()
                .filter(p -> p.permissionID == searchID)
                .findFirst()
                .map(p -> "Permission ID: " + p.permissionID +
                        "\nRole ID: " + p.roleID +
                        "\nDescription: " + p.permissionDescription)
                .orElse("Permission not found");
    }

    public void addPermission() {
        if (permissions.stream().noneMatch(p -> p.permissionID == this.permissionID)) {
            permissions.add(this);
            System.out.println("Permission added successfully. ID: " + this.permissionID);
        } else {
            System.out.println("Error: Permission ID " + this.permissionID + " already exists.");
        }
    }

    public void editPermission(String newDescription) {
        this.permissionDescription = newDescription;
        System.out.println("Permission ID " + this.permissionID + " updated successfully.");
    }

    public void deletePermission() {
        if (permissions.removeIf(p -> p.permissionID == this.permissionID)) {
            System.out.println("Permission ID " + this.permissionID + " deleted successfully.");
        } else {
            System.out.println("Error: Permission not found.");
        }
    }

    // Getters
    public int getPermissionID() {
        return permissionID;
    }

    public int getRoleID() {
        return roleID;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public static List<Permission> getPermissionsByRole(int roleID) {
        return permissions.stream()
                .filter(p -> p.roleID == roleID)
                .toList();
    }
}
