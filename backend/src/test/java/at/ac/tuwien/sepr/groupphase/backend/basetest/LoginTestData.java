package at.ac.tuwien.sepr.groupphase.backend.basetest;

public interface LoginTestData {
    String TEST_NONCE = "ab2ecf87-580b-4188-9533-03e5fbbe1193";
    String TEST_SIGNATURE = "3fqN1W9p4koigsLpTuQHZ98DZQiWR5b9iu5nqYw9vpqpfiPV5bTf1jUEY2rz78YG37AqApKzrVc1JaaRA1hW1cSf";
    String BASE_URI = "/api/v1";
    String AUTH_BASE_URI = BASE_URI + "/authentication";
    String NONCE_BASE_URI = AUTH_BASE_URI + "/nonce";
}

