package com.dws.isobarfm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "bands.api.base-url=https://bands-api.vercel.app/api")
class IsobarFmApplicationTests {

    @Test
    void contextLoads() {
    }
}
