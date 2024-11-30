package ca.mcmaster.cas735.group2.voucher_service.ports;

public interface Issue {

    void register(String voucherID);
    void unregister(String voucherID);
}
