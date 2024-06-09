package objects;

public class BillingAddress {
    private String firstName;
    private String lastName;
    private String address;
    private String postcode;
    private String city;
    private String email;

    public BillingAddress(String firstName, String lastName, String address, String postcode, String city, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
    }

    public BillingAddress() {

    }

    public static BillingAddress getDefaultBillingAddress(User user){
        return new BillingAddress()
                .setFirstName("Adrian")
                .setLastName("Noglywski")
                .setAddress("32, Norawska")
                .setCity("Niecieczow")
                .setEmail(user.getEmail())
                .setPostcode("34-656");
    }
    public String getFirstName() {
        return firstName;
    }

    public BillingAddress setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public BillingAddress setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public BillingAddress setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public BillingAddress setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public BillingAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BillingAddress setEmail(String email) {
        this.email = email;
        return this;
    }
}