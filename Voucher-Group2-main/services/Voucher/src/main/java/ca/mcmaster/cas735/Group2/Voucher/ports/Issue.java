package ca.mcmaster.cas735.Group2.Voucher.ports;

public interface Issue {

    void register(String voucherID);
    void unregister(String voucherID);
}
