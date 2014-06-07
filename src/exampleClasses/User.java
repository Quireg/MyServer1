package exampleClasses;

@Table(name = "user")
public class User {
    @Column(name = "first_name")
    private String name;
    @Column(name = "last_name")
    private String lname;
    @Column(name = "age")
    private int age;
    @Column(name = "salary")
    private int salary;

    public User() {
    }

    public User(String name, String lname, int age, int salary) {
        this.name = name;
        this.lname = lname;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
