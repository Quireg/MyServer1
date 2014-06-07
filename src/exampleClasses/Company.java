package exampleClasses;
@Table(name = "companies")
public class Company {
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;

    public Company() {
    }

    public Company(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
