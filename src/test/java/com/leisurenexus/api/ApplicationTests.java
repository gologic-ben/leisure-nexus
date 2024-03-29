package com.leisurenexus.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.leisurenexus.controller.UserApiController;

import lombok.extern.java.Log;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Log
class ApplicationTests {
	private @Autowired UserApiController ctrl;
//	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void contexLoads() throws Exception {
		log.info("contexLoads");
		assertThat(ctrl).isNotNull();
	}

}
