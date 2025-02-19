package at.ac.tuwien.sepr.groupphase.backend.basetest;

public interface UserTestData {
    String TEST_PUBKEY = "7aHmx4DPLvw3pRUBMadkzU2oYqZqY7GiPPojtEemFWka";
    String TEST_PUBKEY_2 = "8bNpy5FQMnw4qSVBLbelzV3pYrArZ8HjQQpktUfnGXlb";
    String TEST_NONEXISTENT_PUBKEY = "NonExistentPublicKey";
    String BASE_URI = "/api/v1";
    String USER_BASE_URI = BASE_URI + "/user";
    String TXN_TEST_SIGNATURE = "3bDdedWEct1gArzeEGZcjnvRE8qpAS6xxm2DXJC14nAzwYCeccjRc8nBaks6Km5LvctyaRhPDi8MTqV1fuCggEX1";
    String TXN_TEST_SIGNATURE_INVALID = "InvalidSignature";
}
