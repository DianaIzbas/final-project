package com.siit.finalproject;

import com.siit.finalproject.entity.AccountEntity;
import com.siit.finalproject.entity.EncryptMessageEntity;
import com.siit.finalproject.entity.PublicKeyEntity;
import com.siit.finalproject.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class FinalProjectApplicationTests {

	EncryptMessageEntity encryptMessage = new EncryptMessageEntity();
	PublicKeyEntity publicKey = new PublicKeyEntity();
	AccountService service = new AccountService();
	AccountEntity entity = new AccountEntity();





	@Test
	void contextLoads() throws IOException
	{
		//given
		String password = "a";
		int[][] key = publicKey.getPublicKey();
		int[] expected = {0,1,0,0,0,1,0};
		int[] message = new int[expected.length];

		//when
		entity.setUsername("Bogdan");
		entity.setPassword(password);

		//then
		service.addAccount(entity);
		for(int i=0; i<expected.length; i++)
		{
			message[i] = encryptMessage.getEncryptMessageElement(i);
		}
		boolean test = true;
		for(int i=0; i<expected.length; i++)
		{
			if(message[i]!=expected[i])
				test = false;
		}
		int aux = 0;
		if(test)
			aux = 1;
		Assertions.assertEquals(aux,1);

	}

}
