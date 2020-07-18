package com.barbalho.rocha;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utils {

	public static String LOCAL_DATE_ZONE = "America/Sao_Paulo";

    public static User readUserFromJson(String file) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			int idade = Integer.valueOf(obj.get("idade").toString());
			int peso = Integer.valueOf(obj.get("peso").toString());
			int altura = Integer.valueOf( obj.get("altura").toString());
			String nome = obj.get("nome").toString();
			User user = new User(idade, peso, altura, nome);
            return user;
        }
    }
    
    public void testConversion() {
		final byte[] array = { 0x09, 0x01, 0x31, 0x32, 0x33, 0x34 };
		System.out.println(String.format("0x%02X", CRC8.calc(array, 6)));
	}
    
}