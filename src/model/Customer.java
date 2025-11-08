package model;

public class Customer {
    private long Customer_Id;
    private String name;
    private String email;
    private String mobile;
    private String Address;
    public Customer(String name,String email,String mobile,String Address)
    {
        this.Address=Address;
        this.email=email;
        this.name=name;
        this.mobile=mobile;
    }
    public Customer()
    {

    }
    public long getCustomer_Id() {
        return Customer_Id;
    }
    public void setCustomer_Id(long customer_Id) {
        Customer_Id = customer_Id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    @Override
    public String toString() {
        return "Customer [Customer_Id=" + Customer_Id + ", name=" + name + ", email=" + email + ", mobile=" + mobile
                + ", Address=" + Address + ", getCustomer_Id()=" + getCustomer_Id() + ", getName()=" + getName()
                + ", getEmail()=" + getEmail() + ", getMobile()=" + getMobile() + ", getAddress()=" + getAddress()
                + "]";
    }
    
    
}
