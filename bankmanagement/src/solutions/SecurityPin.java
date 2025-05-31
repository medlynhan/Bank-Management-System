package solutions;
// untuk sementara pake class ini untuk menyimpan security pin sbgai objek
// kalo ud ad class account yang isinya kek account_number, full_name, email, balance, security_pin
// bisa dihapus class ini, jdnya pake class Account aja dalam account manager
public class SecurityPin {
    private final String pin;

    public SecurityPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }
}
